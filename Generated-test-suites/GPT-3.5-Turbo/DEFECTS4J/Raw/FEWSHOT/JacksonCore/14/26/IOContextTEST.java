
package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {
    private IOContext ioContext;
    private BufferRecycler bufferRecycler;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, "source", true);
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("source", ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(bufferRecycler, textBuffer.getBufferRecycler());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] buffer = ioContext.allocTokenBuffer(1024);
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assertEquals(bufferRecycler, bufferRecycler());
    }

    @Test
    public void testWithEncoding() {
        IOContext newIOContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newIOContext.getEncoding());
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

    private BufferRecycler bufferRecycler() {
        return ioContext._bufferRecycler;
    }
}
