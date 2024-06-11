package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContextConfiguration() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());

        IOContext ioContextManaged = new IOContext(bufferRecycler, null, true);
        assertTrue(ioContextManaged.isResourceManaged());

        JsonEncoding jsonEncoding = JsonEncoding.UTF8;
        ioContext.setEncoding(jsonEncoding);
        assertEquals(jsonEncoding, ioContext.getEncoding());
        ioContextManaged.withEncoding(JsonEncoding.UTF8);
        assertEquals(jsonEncoding, ioContextManaged.getEncoding());
    }

    @Test
    public void testIOContextBufferManagement() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        // Test read IO buffer
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        assertNotSame(readIOBuffer, readIOBuffer2);
        assertArrayEquals(readIOBuffer, readIOBuffer2);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        // Test write encoding buffer
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer2);
        assertArrayEquals(writeEncodingBuffer, writeEncodingBuffer2);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        // Test base64 buffer
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);

        ioContext.releaseBase64Buffer(base64Buffer);

        // Test token buffer
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
        assertNotSame(tokenBuffer, tokenBuffer2);
        assertArrayEquals(tokenBuffer, tokenBuffer2);
        ioContext.releaseTokenBuffer(tokenBuffer);

        // Test concat buffer
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);

        ioContext.releaseConcatBuffer(concatBuffer);

        // Test name copy buffer
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        assertNotNull(nameCopyBuffer);
        assertEquals(5, nameCopyBuffer.length);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test
    public void testIOContextBufferManagementErrorHandling() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);

        try {
            ioContext.allocReadIOBuffer();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Trying to call same allocXxx() method second time", e.getMessage());
        }

        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);

        try {
            ioContext.releaseWriteEncodingBuffer(readIOBuffer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }
}