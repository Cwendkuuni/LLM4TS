package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testInitialization() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, false);
        assertEquals(null, ioContext.getSourceReference());
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testBufferManagement() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, false);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        tokenBuffer = ioContext.allocTokenBuffer(5);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);

        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test
    public void testBufferManagementWithEncodedData() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, false);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] encodedData = new byte[10];
        System.arraycopy(readIOBuffer, 0, encodedData, 0, readIOBuffer.length);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] decodedReadIOBuffer = ioContext.allocReadIOBuffer();
        System.arraycopy(encodedData, 0, decodedReadIOBuffer, 0, encodedData.length);
        ioContext.releaseReadIOBuffer(decodedReadIOBuffer);

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        char[] encodedTokenData = new char[5];
        for (int i = 0; i < encodedTokenData.length; i++) {
            encodedTokenData[i] = (char) (i + 128);
        }
        System.arraycopy(tokenBuffer, 0, encodedTokenData, 0, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] decodedTokenBuffer = ioContext.allocTokenBuffer();
        System.arraycopy(encodedTokenData, 0, decodedTokenBuffer, 0, encodedTokenData.length);
        ioContext.releaseTokenBuffer(decodedTokenBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBufferManagementReleaseTwice() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, false);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testBufferManagementReleaseDifferentBuffer() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, false);

        byte[] readIOBuffer1 = ioContext.allocReadIOBuffer();
        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer1);
        try {
            ioContext.releaseReadIOBuffer(readIOBuffer2);
        } finally {
            ioContext.releaseReadIOBuffer(readIOBuffer2);
        }
    }

    @Test
    public void testBufferManagementReleaseNull() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, false);
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testBufferManagementReleaseManaged() {
        BufferRecycler br = new BufferRecycler();
        IOContext ioContext = new IOContext(br, null, true);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] managedBuffer = new byte[10];
        ioContext.setEncoding(JsonEncoding.UTF8);
        ioContext.allocWriteEncodingBuffer();
        System.arraycopy(readIOBuffer, 0, managedBuffer, 0, readIOBuffer.length);
        ioContext.releaseWriteEncodingBuffer();

        ioContext.releaseReadIOBuffer(readIOBuffer);
    }
}