package com.googlecode.mp4parser.boxes.apple;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Tobias Bley / UltraMixer on 04/25/2014.
 */
public class AppleCoverBox extends AppleDataBox
{
    private Logger logger = Logger.getLogger(getClass().getName());
    private Image cover;
    private Image origCover;
    private byte[] data;

    public AppleCoverBox()
    {
        super("covr", 1);
    }

    public Image getCover()
    {
        return cover;
    }

    @Override
    protected byte[] writeData()
    {
        if (origCover == cover) {
            return data;
        } else {
            throw new RuntimeException("At the moment you can't change the cover");
        }
    }

    @Override
    protected void parseData(ByteBuffer data)
    {
        InputStream is = null;
        try
        {
            this.data = data.array();
            is = new ByteBufferBackedInputStream(data);
            this.cover = this.origCover =  ImageIO.read(is);

        }
        catch (Exception e)
        {
            logger.log(Level.WARNING, "", e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    logger.log(Level.WARNING, "", e);
                }
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int getDataLength()
    {
        logger.info("not yet implemented");
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public class ByteBufferBackedInputStream extends InputStream
    {

        private final ByteBuffer buf;

        public ByteBufferBackedInputStream(ByteBuffer buf)
        {
            // make a coy of the buffer
            this.buf = buf.duplicate();
        }

        public int read() throws IOException
        {
            if (!buf.hasRemaining())
            {
                return -1;
            }
            return buf.get() & 0xFF;
        }

        public int read(byte[] bytes, int off, int len)
                throws IOException
        {
            if (!buf.hasRemaining())
            {
                return -1;
            }

            len = Math.min(len, buf.remaining());
            buf.get(bytes, off, len);
            return len;
        }
    }
}