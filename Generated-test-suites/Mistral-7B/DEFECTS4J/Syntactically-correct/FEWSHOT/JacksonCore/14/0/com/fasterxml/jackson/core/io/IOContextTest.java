package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testInitialization() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        assertNull(context.getEncoding());
        assertTrue(context.isResourceManaged());
    }

    @Test
    public void testTextBufferConstruction() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNull(textBuffer.getBuffer());
    }

    @Test
    public void testAllocateAndReleaseReadIOBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        assertArrayEquals(new byte[BufferRecycler.BYTE_READ_IO_BUFFER.length], readIOBuffer);
        context.releaseReadIOBuffer(readIOBuffer);
        assertNull(context.getReadIOBuffer());
    }

    @Test
    public void testAllocateAndReleaseWriteEncodingBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        assertArrayEquals(new byte[BufferRecycler.BYTE_WRITE_ENCODING_BUFFER.length], writeEncodingBuffer);
        context.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(context.getWriteEncodingBuffer());
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocateSameBufferTwice() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        context.allocReadIOBuffer();
    }

    @Test
    public void testAllocateAndReleaseTokenBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        char[] tokenBuffer = context.allocTokenBuffer();
        assertArrayEquals(new char[BufferRecycler.CHAR_TOKEN_BUFFER.length], tokenBuffer);
        context.releaseTokenBuffer(tokenBuffer);
        assertNull(context.getTokenBuffer());
    }

    @Test
    public void testAllocateAndReleaseConcatBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        char[] concatBuffer = context.allocConcatBuffer();
        assertArrayEquals(new char[BufferRecycler.CHAR_CONCAT_BUFFER.length], concatBuffer);
        context.releaseConcatBuffer(concatBuffer);
        assertNull(context.getConcatBuffer());
    }

    @Test
    public void testAllocateAndReleaseNameCopyBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, "sourceRef", true);
        char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
        assertArrayEquals(new char[10], nameCopyBuffer);
        context.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(context.getNameCopyBuffer());
    }
}