package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void test_constructor() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        assertNotNull(context);
        assertEquals(this, context.getSourceReference());
        assertTrue(context.isResourceManaged());
    }

    @Test
    public void test_setEncoding() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);
        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void test_withEncoding() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext contextWithEncoding = context.withEncoding(encoding);
        assertEquals(encoding, contextWithEncoding.getEncoding());
        assertEquals(context, contextWithEncoding); // Ensure this is the same instance
    }

    @Test
    public void test_allocReadIOBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        context.releaseReadIOBuffer(readIOBuffer);
        readIOBuffer = context.allocReadIOBuffer();
        assertNotSame(readIOBuffer, readIOBuffer);
    }

    @Test
    public void test_allocReadIOBufferWithMinSize() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        int minSize = 10;
        byte[] readIOBuffer = context.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertEquals(minSize, readIOBuffer.length);
        context.releaseReadIOBuffer(readIOBuffer);
        readIOBuffer = context.allocReadIOBuffer(minSize);
        assertNotSame(readIOBuffer, readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void test_allocReadIOBufferTwice() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        context.allocReadIOBuffer();
    }

    @Test
    public void test_allocWriteEncodingBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        context.releaseWriteEncodingBuffer(writeEncodingBuffer);
        writeEncodingBuffer = context.allocWriteEncodingBuffer();
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer);
    }

    @Test
    public void test_allocWriteEncodingBufferWithMinSize() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        int minSize = 10;
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertEquals(minSize, writeEncodingBuffer.length);
        context.releaseWriteEncodingBuffer(writeEncodingBuffer);
        writeEncodingBuffer = context.allocWriteEncodingBuffer(minSize);
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer);
    }

    @Test
    public void test_allocBase64Buffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] base64Buffer = context.allocBase64Buffer();
        assertNotNull(base64Buffer);
        context.releaseBase64Buffer(base64Buffer);
        base64Buffer = context.allocBase64Buffer();
        assertNotSame(base64Buffer, base64Buffer);
    }

    @Test
    public void test_allocTokenBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        char[] tokenBuffer = context.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        context.releaseTokenBuffer(tokenBuffer);
        tokenBuffer = context.allocTokenBuffer();
        assertNotSame(tokenBuffer, tokenBuffer);
    }

    @Test
    public void test_allocTokenBufferWithMinSize() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        int minSize = 10;
        char[] tokenBuffer = context.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        assertEquals(minSize, tokenBuffer.length);
        context.releaseTokenBuffer(tokenBuffer);
        tokenBuffer = context.allocTokenBuffer(minSize);
        assertNotSame(tokenBuffer, tokenBuffer);
    }

    @Test
    public void test_allocConcatBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        char[] concatBuffer = context.allocConcatBuffer();
        assertNotNull(concatBuffer);
        context.releaseConcatBuffer(concatBuffer);
        concatBuffer = context.allocConcatBuffer();
        assertNotSame(concatBuffer, concatBuffer);
    }

    @Test
    public void test_allocNameCopyBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        int minSize = 10;
        char[] nameCopyBuffer = context.allocNameCopyBuffer(minSize);
        assertNotNull(nameCopyBuffer);
        assertEquals(minSize, nameCopyBuffer.length);
        context.releaseNameCopyBuffer(nameCopyBuffer);
        nameCopyBuffer = context.allocNameCopyBuffer(minSize);
        assertNotSame(nameCopyBuffer, nameCopyBuffer);
    }

    @Test
    public void test_releaseReadIOBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(readIOBuffer);
        assertNull(context.getReadIOBuffer());
    }

    @Test
    public void test_releaseWriteEncodingBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(context.getWriteEncodingBuffer());
    }

    @Test
    public void test_releaseBase64Buffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] base64Buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(base64Buffer);
        assertNull(context.getBase64Buffer());
    }

    @Test
    public void test_releaseTokenBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        char[] tokenBuffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(tokenBuffer);
        assertNull(context.getTokenBuffer());
    }

    @Test
    public void test_releaseConcatBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        char[] concatBuffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(concatBuffer);
        assertNull(context.getConcatBuffer());
    }

    @Test
    public void test_releaseNameCopyBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        char[] nameCopyBuffer = context.allocNameCopyBuffer(5);
        context.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(context.getNameCopyBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_releaseWrongBuffer() {
        BufferRecycler recycler = new BufferRecycler();
        IOContext context = new IOContext(recycler, this, true);
        byte[] readIOBuffer = context.allocReadIOBuffer();
        context.releaseBase64Buffer(readIOBuffer);
    }
}