package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(null, "source", true);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("source", ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(0, textBuffer.size());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);

        byte[] buffer2 = ioContext.allocReadIOBuffer();
        assertNull(buffer2);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] buffer = ioContext.allocReadIOBuffer(100);
        assertEquals(100, buffer.length);

        byte[] buffer2 = ioContext.allocReadIOBuffer(50);
        assertNull(buffer2);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);

        byte[] buffer2 = ioContext.allocWriteEncodingBuffer();
        assertNull(buffer2);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer(100);
        assertEquals(100, buffer.length);

        byte[] buffer2 = ioContext.allocWriteEncodingBuffer(50);
        assertNull(buffer2);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);

        byte[] buffer2 = ioContext.allocBase64Buffer();
        assertNull(buffer2);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);

        char[] buffer2 = ioContext.allocTokenBuffer();
        assertNull(buffer2);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] buffer = ioContext.allocTokenBuffer(100);
        assertEquals(100, buffer.length);

        char[] buffer2 = ioContext.allocTokenBuffer(50);
        assertNull(buffer2);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);

        char[] buffer2 = ioContext.allocConcatBuffer();
        assertNull(buffer2);
    }

    @Test
    public void testAllocNameCopyBufferWithMinSize() {
        char[] buffer = ioContext.allocNameCopyBuffer(100);
        assertEquals(100, buffer.length);

        char[] buffer2 = ioContext.allocNameCopyBuffer(50);
        assertNull(buffer2);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buf);
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithWrongBuf() {
        byte[] buf = new byte[BufferRecycler.BYTE_READ_IO_BUFFER - 1];
        ioContext.releaseReadIOBuffer(buf);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buf);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithWrongBuf() {
        byte[] buf = new byte[BufferRecycler.BYTE_WRITE_ENCODING_BUFFER - 1];
        ioContext.releaseWriteEncodingBuffer(buf);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buf);
        assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithWrongBuf() {
        byte[] buf = new byte[BufferRecycler.BYTE_BASE64_CODEC_BUFFER - 1];
        ioContext.releaseBase64Buffer(buf);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buf);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithWrongBuf() {
        char[] buf = new char[BufferRecycler.CHAR_TOKEN_BUFFER - 1];
        ioContext.releaseTokenBuffer(buf);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buf);
        assertNull(ioContext._concatCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithWrongBuf() {
        char[] buf = new char[BufferRecycler.CHAR_CONCAT_BUFFER - 1];
        ioContext.releaseConcatBuffer(buf);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buf = ioContext.allocNameCopyBuffer(100);
        ioContext.releaseNameCopyBuffer(buf);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithWrongBuf() {
        char[] buf = new char[BufferRecycler.CHAR_NAME_COPY_BUFFER - 1];
        ioContext.releaseNameCopyBuffer(buf);
    }
}