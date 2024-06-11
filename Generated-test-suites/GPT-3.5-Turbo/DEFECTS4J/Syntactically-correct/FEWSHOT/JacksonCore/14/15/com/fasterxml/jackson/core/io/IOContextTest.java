package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class IOContextTest {

    @Mock
    BufferRecycler bufferRecyclerMock;

    private IOContext ioContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ioContext = new IOContext(bufferRecyclerMock, "sourceRef", true);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext newIoContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertSame(ioContext, newIoContext);
        assertEquals(JsonEncoding.UTF8, newIoContext.getEncoding());
    }

    @Test
    public void testGetters() {
        assertEquals("sourceRef", ioContext.getSourceReference());
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        byte[] buffer2 = ioContext.allocReadIOBuffer();
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] buffer = ioContext.allocReadIOBuffer(100);
        byte[] buffer2 = ioContext.allocReadIOBuffer(200);
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        byte[] buffer2 = ioContext.allocWriteEncodingBuffer();
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer(100);
        byte[] buffer2 = ioContext.allocWriteEncodingBuffer(200);
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        byte[] buffer2 = ioContext.allocBase64Buffer();
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        char[] buffer2 = ioContext.allocTokenBuffer();
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] buffer = ioContext.allocTokenBuffer(100);
        char[] buffer2 = ioContext.allocTokenBuffer(200);
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        char[] buffer2 = ioContext.allocConcatBuffer();
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(100);
        char[] buffer2 = ioContext.allocNameCopyBuffer(200);
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        byte[] buffer2 = ioContext.allocReadIOBuffer();
        assertSame(buffer, buffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWrongBuffer() {
        byte[] buffer1 = ioContext.allocReadIOBuffer();
        byte[] buffer2 = new byte[buffer1.length];
        ioContext.releaseReadIOBuffer(buffer2);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        byte[] buffer2 = ioContext.allocWriteEncodingBuffer();
        assertSame(buffer, buffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWrongBuffer() {
        byte[] buffer1 = ioContext.allocWriteEncodingBuffer();
        byte[] buffer2 = new byte[buffer1.length];
        ioContext.releaseWriteEncodingBuffer(buffer2);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        byte[] buffer2 = ioContext.allocBase64Buffer();
        assertSame(buffer, buffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWrongBuffer() {
        byte[] buffer1 = ioContext.allocBase64Buffer();
        byte[] buffer2 = new byte[buffer1.length];
        ioContext.releaseBase64Buffer(buffer2);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        char[] buffer2 = ioContext.allocTokenBuffer();
        assertSame(buffer, buffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWrongBuffer() {
        char[] buffer1 = ioContext.allocTokenBuffer();
        char[] buffer2 = new char[buffer1.length];
        ioContext.releaseTokenBuffer(buffer2);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        char[] buffer2 = ioContext.allocConcatBuffer();
        assertSame(buffer, buffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWrongBuffer() {
        char[] buffer1 = ioContext.allocConcatBuffer();
        char[] buffer2 = new char[buffer1.length];
        ioContext.releaseConcatBuffer(buffer2);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(100);
        ioContext.releaseNameCopyBuffer(buffer);
        char[] buffer2 = ioContext.allocNameCopyBuffer(200);
        assertSame(buffer, buffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWrongBuffer() {
        char[] buffer1 = ioContext.allocNameCopyBuffer(100);
        char[] buffer2 = new char[buffer1.length];
        ioContext.releaseNameCopyBuffer(buffer2);
    }
}