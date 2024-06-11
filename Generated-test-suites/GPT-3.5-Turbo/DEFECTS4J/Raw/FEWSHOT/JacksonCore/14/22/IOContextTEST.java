package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
        assertNull(textBuffer.getBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        int minSize = 10;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        int minSize = 10;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        int minSize = 10;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        int minSize = 10;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }

}