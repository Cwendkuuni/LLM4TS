package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContextConfiguration() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());

        IOContext ioContextEncoded = new IOContext(null, null, false).withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContextEncoded.getEncoding());

        IOContext ioContextManaged = new IOContext(null, null, true);
        assertTrue(ioContextManaged.isResourceManaged());
    }

    @Test
    public void testIOContextBufferManagement() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer2);
        assertNotSame(readIOBuffer, readIOBuffer2);

        ioContext.releaseReadIOBuffer(readIOBuffer2);

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer2);
        assertNotSame(tokenBuffer, tokenBuffer2);

        ioContext.releaseTokenBuffer(tokenBuffer2);
    }

    @Test(expected = IllegalStateException.class)
    public void testIOContextBufferManagement_releaseTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }
}