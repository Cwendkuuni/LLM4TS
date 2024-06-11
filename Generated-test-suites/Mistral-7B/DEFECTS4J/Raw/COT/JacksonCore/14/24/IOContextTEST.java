package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * IOContext test cases.
 */
public class IOContextTest {

    @Test
    public void testIOContextConstructor() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        assertNotNull(ioContext.getSourceReference());
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContextWithEncoding.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 5;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertArrayEquals(new byte[minSize], readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 5;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertArrayEquals(new byte[minSize], writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 5;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        assertArrayEquals(new char[minSize], tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test
    public void testAllocNameCopyBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 5;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(nameCopyBuffer);
        assertArrayEquals(new char[minSize], nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test
    public void testReleaseReadIOBufferNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseReadIOBuffer(null);
        fail("Expected IllegalStateException but got null");
    }

    @Test
    public void testReleaseWriteEncodingBufferNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseWriteEncodingBuffer(null);
        fail("Expected IllegalStateException but got null");
    }

    @Test
    public void testReleaseBase64BufferNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseBase64Buffer(null);
        fail("Expected IllegalStateException but got null");
    }

    @Test
    public void testReleaseTokenBufferNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseTokenBuffer(null);
        fail("Expected IllegalStateException but got null");
    }

    @Test
    public void testReleaseConcatBufferNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseConcatBuffer(null);
        fail("Expected IllegalStateException but got null");
    }

    @Test
    public void testReleaseNameCopyBufferNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseNameCopyBuffer(null);
        fail("Expected IllegalStateException but got null");
    }

    private void assertEquals(JsonEncoding expected, JsonEncoding actual) {
        if (expected != actual) {
            fail("Expected: " + expected + ", but was: " + actual);
        }
    }
}