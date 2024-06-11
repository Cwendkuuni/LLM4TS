package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void test_newInstance() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
        assertSame(bufferRecycler, ioContext._bufferRecycler);
        assertNull(ioContext._sourceRef);
    }

    @Test
    public void test_setEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertSame(encoding, ioContext.getEncoding());
    }

    @Test
    public void test_withEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);

        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
        assertSame(encoding, ioContextWithEncoding.getEncoding());
        assertSame(ioContext, ioContextWithEncoding);
    }

    @Test
    public void test_allocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void test_allocReadIOBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void test_releaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void test_releaseReadIOBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = new byte[10];
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void test_allocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void test_allocWriteEncodingBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
        assertEquals(minSize, writeEncodingBuffer.length);
    }

    @Test
    public void test_releaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void test_releaseWriteEncodingBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = new byte[10];
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void test_allocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void test_releaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void test_releaseBase64BufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = new byte[10];
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void test_allocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void test_allocTokenBufferMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
        assertEquals(minSize, tokenBuffer.length);
    }

    @Test
    public void test_releaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void test_releaseTokenBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = new char[10];
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void test_allocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void test_releaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void test_releaseConcatBufferSameBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        ioContext.allocConcatBuffer();
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void test_allocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        assertNotNull(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test
    public void test_releaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test
    public void test_releaseNameCopyBufferSameBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        ioContext.allocNameCopyBuffer(5);
        assertNull(ioContext._nameCopyBuffer);
    }
}