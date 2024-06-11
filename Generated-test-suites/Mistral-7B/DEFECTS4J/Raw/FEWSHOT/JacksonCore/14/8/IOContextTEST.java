package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testConstruct() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
        assertNull(ioContext._readIOBuffer);
        assertNull(ioContext._writeEncodingBuffer);
        assertNull(ioContext._base64Buffer);
        assertNull(ioContext._tokenCBuffer);
        assertNull(ioContext._concatCBuffer);
        assertNull(ioContext._nameCopyBuffer);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertSame(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertSame(bufferRecycler, readIOBuffer.recycler());
        assertNull(ioContext._readIOBuffer);
        assertArrayEquals(new byte[BufferRecycler.BYTE_READ_IO_BUFFER], readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertSame(bufferRecycler, writeEncodingBuffer.recycler());
        assertNull(ioContext._writeEncodingBuffer);
        assertArrayEquals(new byte[BufferRecycler.BYTE_WRITE_ENCODING_BUFFER], writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertSame(bufferRecycler, base64Buffer.recycler());
        assertNull(ioContext._base64Buffer);
        assertArrayEquals(new byte[BufferRecycler.BYTE_BASE64_CODEC_BUFFER], base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertSame(bufferRecycler, tokenBuffer.recycler());
        assertNull(ioContext._tokenCBuffer);
        assertArrayEquals(new char[BufferRecycler.CHAR_TOKEN_BUFFER], tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertSame(bufferRecycler, concatBuffer.recycler());
        assertNull(ioContext._concatCBuffer);
        assertArrayEquals(new char[BufferRecycler.CHAR_CONCAT_BUFFER], concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(32);
        assertSame(bufferRecycler, nameCopyBuffer.recycler());
        assertNull(ioContext._nameCopyBuffer);
        assertArrayEquals(new char[32], nameCopyBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = new byte[10];
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = new byte[10];
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = new byte[10];
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.releaseBase64Buffer(null);
    }

    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = new char[10];
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.releaseTokenBuffer(null);
    }

    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = new char[10];
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.releaseConcatBuffer(null);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(32);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferDifferentBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = new char[10];
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        ioContext.releaseNameCopyBuffer(null);
    }
}