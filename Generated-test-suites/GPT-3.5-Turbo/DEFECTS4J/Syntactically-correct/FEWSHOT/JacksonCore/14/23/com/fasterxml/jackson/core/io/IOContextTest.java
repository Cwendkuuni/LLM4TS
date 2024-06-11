package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(BufferRecycler.instance(), "Test", true);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext result = ioContext.withEncoding(JsonEncoding.UTF16_LE);
        assertEquals(JsonEncoding.UTF16_LE, result.getEncoding());
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("Test", ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertEquals(true, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] result = ioContext.allocReadIOBuffer();
        assertNotNull(result);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] result = ioContext.allocReadIOBuffer(1024);
        assertNotNull(result);
        assertEquals(1024, result.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] result = ioContext.allocWriteEncodingBuffer();
        assertNotNull(result);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] result = ioContext.allocWriteEncodingBuffer(1024);
        assertNotNull(result);
        assertEquals(1024, result.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] result = ioContext.allocBase64Buffer();
        assertNotNull(result);
    }

    @Test
    public void testAlocTokenBuffer() {
        char[] result = ioContext.allocTokenBuffer();
        assertNotNull(result);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] result = ioContext.allocTokenBuffer(1024);
        assertNotNull(result);
        assertEquals(1024, result.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] result = ioContext.allocConcatBuffer();
        assertNotNull(result);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] result = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(result);
        assertEquals(1024, result.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}