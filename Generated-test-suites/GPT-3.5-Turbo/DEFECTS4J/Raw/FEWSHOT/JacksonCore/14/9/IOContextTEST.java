package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IOContextTest {
    private static final BufferRecycler RECYCLER = new BufferRecycler();
    private IOContext context = new IOContext(RECYCLER, "source", true);
    private byte[] byteBuffer;
    private char[] charBuffer;

    @Test
    public void testWithEncoding() {
        assertSame(context, context.withEncoding(null));
        assertSame(context, context.withEncoding(JsonEncoding.UTF8));

        JsonEncoding enc = JsonEncoding.UTF8;
        context.setEncoding(enc);
        assertSame(context, context.withEncoding(enc));

        assertEquals(enc, context.getEncoding());
    }

    @Test
    public void testReadIOBuffer() {
        assertNull(context._readIOBuffer);
        byteBuffer = context.allocReadIOBuffer();
        assertArrayEquals(RECYCLER.allocByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER), byteBuffer);

        byte[] smallBuffer = context.allocReadIOBuffer(8);
        assertArrayEquals(RECYCLER.allocByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, 8), smallBuffer);

        context.releaseReadIOBuffer(byteBuffer);
        assertFalse(context._readIOBuffer == byteBuffer);
    }

    @Test
    public void testWriteEncodingBuffer() {
        assertNull(context._writeEncodingBuffer);
        byteBuffer = context.allocWriteEncodingBuffer();
        assertArrayEquals(RECYCLER.allocByteBuffer(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER), byteBuffer);

        byte[] smallBuffer = context.allocWriteEncodingBuffer(8);
        assertArrayEquals(RECYCLER.allocByteBuffer(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, 8), smallBuffer);

        context.releaseWriteEncodingBuffer(byteBuffer);
        assertFalse(context._writeEncodingBuffer == byteBuffer);
    }

    @Test
    public void testBase64Buffer() {
        assertNull(context._base64Buffer);
        byteBuffer = context.allocBase64Buffer();
        assertArrayEquals(RECYCLER.allocByteBuffer(BufferRecycler.BYTE_BASE64_CODEC_BUFFER), byteBuffer);

        context.releaseBase64Buffer(byteBuffer);
        assertFalse(context._base64Buffer == byteBuffer);
    }

    @Test
    public void testTokenBuffer() {
        assertNull(context._tokenCBuffer);
        charBuffer = context.allocTokenBuffer();
        assertArrayEquals(RECYCLER.allocCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER), charBuffer);

        char[] smallBuffer = context.allocTokenBuffer(8);
        assertArrayEquals(RECYCLER.allocCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER, 8), smallBuffer);

        context.releaseTokenBuffer(charBuffer);
        assertFalse(context._tokenCBuffer == charBuffer);
    }

    @Test
    public void testConcatBuffer() {
        assertNull(context._concatCBuffer);
        charBuffer = context.allocConcatBuffer();
        assertArrayEquals(RECYCLER.allocCharBuffer(BufferRecycler.CHAR_CONCAT_BUFFER), charBuffer);

        context.releaseConcatBuffer(charBuffer);
        assertFalse(context._concatCBuffer == charBuffer);
    }

    @Test
    public void testNameCopyBuffer() {
        assertNull(context._nameCopyBuffer);
        charBuffer = context.allocNameCopyBuffer(8);
        assertArrayEquals(RECYCLER.allocCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER, 8), charBuffer);

        context.releaseNameCopyBuffer(charBuffer);
        assertFalse(context._nameCopyBuffer == charBuffer);
    }

    @Test
    public void testPublicAccessors() {
        Object sourceReference = context.getSourceReference();
        assertEquals("source", sourceReference);

        assertFalse(context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = context.constructTextBuffer();

        assertTrue(textBuffer.hasCapacity());
        assertEquals(0, textBuffer.currentSegmentLength());
    }
}