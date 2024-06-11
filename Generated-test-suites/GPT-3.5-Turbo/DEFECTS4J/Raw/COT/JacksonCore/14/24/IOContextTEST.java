
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, true);
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);
        assertEquals(IOContextBufferRecycler.BYTE_READ_IO_BUFFER_LENGTH, buffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(null, null, true);
        int minSize = 1024;
        byte[] buffer = context.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        assertEquals(IOContextBufferRecycler.BYTE_WRITE_ENCODING_BUFFER_LENGTH, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(null, null, true);
        int minSize = 1024;
        byte[] buffer = context.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);
        assertEquals(IOContextBufferRecycler.BYTE_BASE64_CODEC_BUFFER_LENGTH, buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocTokenBuffer();
        assertNotNull(buffer);
        assertEquals(IOContextBufferRecycler.CHAR_TOKEN_BUFFER_LENGTH, buffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(null, null, true);
        int minSize = 1024;
        char[] buffer = context.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);
        assertEquals(IOContextBufferRecycler.CHAR_CONCAT_BUFFER_LENGTH, buffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, null, true);
        int minSize = 1024;
        char[] buffer = context.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);
        context.releaseReadIOBuffer(buffer);
        byte[] bufferAfterRelease = context.allocReadIOBuffer();
        assertSame(buffer, bufferAfterRelease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = new byte[IOContextBufferRecycler.BYTE_READ_IO_BUFFER_LENGTH];
        context.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        context.releaseWriteEncodingBuffer(buffer);
        byte[] bufferAfterRelease = context.allocWriteEncodingBuffer();
        assertSame(buffer, bufferAfterRelease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = new byte[IOContextBufferRecycler.BYTE_WRITE_ENCODING_BUFFER_LENGTH];
        context.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);
        context.releaseBase64Buffer(buffer);
        byte[] bufferAfterRelease = context.allocBase64Buffer();
        assertSame(buffer, bufferAfterRelease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = new byte[IOContextBufferRecycler.BYTE_BASE64_CODEC_BUFFER_LENGTH];
        context.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocTokenBuffer();
        assertNotNull(buffer);
        context.releaseTokenBuffer(buffer);
        char[] bufferAfterRelease = context.allocTokenBuffer();
        assertSame(buffer, bufferAfterRelease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = new char[IOContextBufferRecycler.CHAR_TOKEN_BUFFER_LENGTH];
        context.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);
        context.releaseConcatBuffer(buffer);
        char[] bufferAfterRelease = context.allocConcatBuffer();
        assertSame(buffer, bufferAfterRelease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = new char[IOContextBufferRecycler.CHAR_CONCAT_BUFFER_LENGTH];
        context.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, true);
        int minSize = 1024;
        char[] buffer = context.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        context.releaseNameCopyBuffer(buffer);
        char[] bufferAfterRelease = context.allocNameCopyBuffer(minSize);
        assertSame(buffer, bufferAfterRelease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithWrongBuffer() {
        IOContext context = new IOContext(null, null, true);
        int minSize = 1024;
        char[] buffer = new char[minSize];
        context.releaseNameCopyBuffer(buffer);
    }

}
