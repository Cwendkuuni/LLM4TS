package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

public class IOContextTest {

    private BufferRecycler bufferRecycler = new BufferRecycler();
    private IOContext ioContext;

    @Test
    public void testIOContextConstruction() {
        ioContext = new IOContext(bufferRecycler, null, false);
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        ioContext = new IOContext(bufferRecycler, null, false);
        ioContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.setSourceReference(new Object());
        assertEquals(ioContext.getSourceReference(), new Object());
    }

    @Test
    public void testAllocReadIOBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferMinSize() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer1 = ioContext.allocReadIOBuffer();
        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
    }

    @Test
    public void testReleaseReadIOBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = new byte[10];
        ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferMinSize() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferMinSize() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer(10);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

}