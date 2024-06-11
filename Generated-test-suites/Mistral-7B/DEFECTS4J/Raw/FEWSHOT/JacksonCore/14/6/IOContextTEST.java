package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", true);
        assertNull(ioContext.getEncoding());
        assertTrue(ioContext.isResourceManaged());

        IOContext ioContext2 = new IOContext(bufferRecycler, "sourceRef2", false);
        assertNotNull(ioContext2.getEncoding());
        assertFalse(ioContext2.isResourceManaged());
    }

    @Test
    public void testAllocateAndReleaseBuffers() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", true);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNull(ioContext.getReadIOBuffer());
        assertArrayEquals(new byte[BufferRecycler.BYTE_READ_IO_BUFFER], readIOBuffer);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());

        readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNull(ioContext.getReadIOBuffer());
        assertArrayEquals(new byte[10], readIOBuffer);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNull(ioContext.getTokenBuffer());
        assertArrayEquals(new char[BufferRecycler.CHAR_TOKEN_BUFFER], tokenBuffer);

        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());

        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNull(ioContext.getConcatBuffer());
        assertArrayEquals(new char[BufferRecycler.CHAR_CONCAT_BUFFER], concatBuffer);

        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocateBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", true);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testVerifyAllocationAndRelease() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", true);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer2);

        assertNull(ioContext.getReadIOBuffer());
    }
}