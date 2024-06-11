package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {
    @Test
    public void test_IOContext_Constructor() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        assertEquals(bufferRecycler, ioContext._bufferRecycler);
        assertNull(ioContext._sourceRef);
        assertFalse(ioContext._managedResource);
    }

    @Test
    public void test_setEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void test_withEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext expectedContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, expectedContext.getEncoding());
        assertEquals(ioContext, ioContext); // same instance
    }

    @Test
    public void test_getSourceReference() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", false);
        Object sourceRef = ioContext.getSourceReference();
        assertEquals("sourceRef", sourceRef);
    }

    @Test
    public void test_isResourceManaged() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, true);
        assertTrue(ioContext.isResourceManaged());

        IOContext ioContext2 = new IOContext(bufferRecycler, null, false);
        assertFalse(ioContext2.isResourceManaged());
    }

    @Test
    public void test_allocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        assertNotSame(readIOBuffer, readIOBuffer2);
    }

    @Test
    public void test_allocReadIOBuffer_minSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer(minSize + 1);
        assertNotSame(readIOBuffer, readIOBuffer2);
    }

    @Test
    public void test_allocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer2);
    }

    @Test
    public void test_allocWriteEncodingBuffer_minSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer(minSize + 1);
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer2);
    }

    @Test
    public void test_allocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);

        base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);

        byte[] base64Buffer2 = ioContext.allocBase64Buffer();
        assertNotSame(base64Buffer, base64Buffer2);
    }

    @Test
    public void test_allocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
        assertNotSame(tokenBuffer, tokenBuffer2);
    }

    @Test
    public void test_allocTokenBuffer_minSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer(minSize + 1);
        assertNotSame(tokenBuffer, tokenBuffer2);
    }

    @Test
    public void test_allocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);

        concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);

        char[] concatBuffer2 = ioContext.allocConcatBuffer();
        assertNotSame(concatBuffer, concatBuffer2);
    }

    @Test
    public void test_allocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        char[] nameCopyBuffer2 = ioContext.allocNameCopyBuffer(11);
        assertNotSame(nameCopyBuffer, nameCopyBuffer2);
    }

    @Test
    public void test_releaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);

        readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] releasedReadIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(releasedReadIOBuffer);

        assertArrayEquals(Util.EMPTY_STRING_ARRAY, ioContext._bufferRecycler.getAvailableByteBuffers());
    }

    @Test
    public void test_releaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] releasedWriteEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(releasedWriteEncodingBuffer);

        assertArrayEquals(Util.EMPTY_STRING_ARRAY, ioContext._bufferRecycler.getAvailableByteBuffers());
    }

    @Test
    public void test_releaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);

        base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);

        byte[] releasedBase64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(releasedBase64Buffer);

        assertArrayEquals(Util.EMPTY_STRING_ARRAY, ioContext._bufferRecycler.getAvailableByteBuffers());
    }

    @Test
    public void test_releaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);

        tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] releasedTokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(releasedTokenBuffer);

        assertArrayEquals(Util.EMPTY_STRING_ARRAY, ioContext._bufferRecycler.getAvailableCharBuffers());
    }

    @Test
    public void test_releaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);

        concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);

        char[] releasedConcatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(releasedConcatBuffer);

        assertArrayEquals(Util.EMPTY_STRING_ARRAY, ioContext._bufferRecycler.getAvailableCharBuffers());
    }

    @Test
    public void test_releaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        char[] releasedNameCopyBuffer = ioContext.allocNameCopyBuffer(11);
        ioContext.releaseNameCopyBuffer(releasedNameCopyBuffer);

        assertArrayEquals(Util.EMPTY_STRING_ARRAY, ioContext._bufferRecycler.getAvailableCharBuffers());
    }
}