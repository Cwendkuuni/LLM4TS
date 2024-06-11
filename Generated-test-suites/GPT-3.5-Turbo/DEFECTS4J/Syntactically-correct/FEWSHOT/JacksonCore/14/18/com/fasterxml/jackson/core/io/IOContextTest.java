package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Test;

public class IOContextTest {
    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        assertEquals(true, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(0, textBuffer.size());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertEquals(BufferRecycler.DEFAULT_READ_IO_BUFFER_SIZE, buffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(BufferRecycler.DEFAULT_WRITE_ENCODING_BUFFER_SIZE, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertEquals(BufferRecycler.DEFAULT_BASE64_CODEC_BUFFER_SIZE, buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer();
        assertEquals(BufferRecycler.DEFAULT_TOKEN_BUFFER_SIZE, buffer.length);
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer(1024);
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocConcatBuffer();
        assertEquals(BufferRecycler.DEFAULT_CONCAT_BUFFER_SIZE, buffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}