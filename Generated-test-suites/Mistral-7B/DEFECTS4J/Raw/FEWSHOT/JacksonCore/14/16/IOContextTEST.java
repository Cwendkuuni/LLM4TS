package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContextConstruction() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertNull(ioContext.getEncoding());
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext withEncoding = ioContext.withEncoding(encoding);
        assertEquals(encoding, withEncoding.getEncoding());
        assertEquals(ioContext, withEncoding);
    }

    @Test
    public void testGetSourceReference() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertEquals(new Object(), ioContext.getSourceReference());
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext.getWriteEncodingBuffer());
    }

    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext.getBase64Buffer());
    }

    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());
    }

    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext.getNameCopyBuffer());
    }

    @Test
    public void testGetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertTrue(ioContext.isResourceManaged());

        IOContext ioContext2 = new IOContext(bufferRecycler, new Object(), false);
        assertFalse(ioContext2.isResourceManaged());
    }

    @Test
    public void testBufferRecyclerRelease() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        byte[] readIOBuffer = bufferRecycler.allocByteBuffer(1024);
        char[] tokenBuffer = bufferRecycler.allocCharBuffer(1024);
        char[] concatBuffer = bufferRecycler.allocCharBuffer(1024);
        char[] nameCopyBuffer = bufferRecycler.allocCharBuffer(1024);

        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(readIOBuffer);
        ioContext.releaseBase64Buffer(readIOBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        ioContext.releaseBase64Buffer(readIOBuffer); // should fail
        ioContext.releaseTokenBuffer(readIOBuffer); // should fail
        ioContext.releaseConcatBuffer(readIOBuffer); // should fail
        ioContext.releaseNameCopyBuffer(readIOBuffer); // should fail

        bufferRecycler.releaseByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer);
        bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer);
        bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer);
        bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER, nameCopyBuffer);

        byte[] readIOBuffer2 = bufferRecycler.allocByteBuffer(1024);
        char[] tokenBuffer2 = bufferRecycler.allocCharBuffer(1024);
        char[] concatBuffer2 = bufferRecycler.allocCharBuffer(1024);
        char[] nameCopyBuffer2 = bufferRecycler.allocCharBuffer(1024);

        ioContext.allocReadIOBuffer();
        ioContext.allocWriteEncodingBuffer();
        ioContext.allocBase64Buffer();
        ioContext.allocTokenBuffer();
        ioContext.allocConcatBuffer();
        ioContext.allocNameCopyBuffer(1024);

        assertArrayEquals(readIOBuffer2, ioContext.getReadIOBuffer());
        assertArrayEquals(tokenBuffer2, ioContext.getTokenBuffer());
        assertArrayEquals(concatBuffer2, ioContext.getConcatBuffer());
        assertArrayEquals(nameCopyBuffer2, ioContext.getNameCopyBuffer());

        ioContext.releaseReadIOBuffer(readIOBuffer2);
        ioContext.releaseWriteEncodingBuffer(readIOBuffer2);
        ioContext.releaseBase64Buffer(readIOBuffer2);
        ioContext.releaseTokenBuffer(tokenBuffer2);
        ioContext.releaseConcatBuffer(concatBuffer2);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer2);

        bufferRecycler.releaseByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer2);
        bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer2);
        bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer2);
        bufferRecycler.releaseCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER, nameCopyBuffer2);
    }

    @Test
    public void testBufferRecyclerReleaseNull() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        byte[] readIOBuffer = null;
        char[] tokenBuffer = null;
        char[] concatBuffer = null;
        char[] nameCopyBuffer = null;

        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(readIOBuffer);
        ioContext.releaseBase64Buffer(readIOBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        assertNull(ioContext.getReadIOBuffer());
        assertNull(ioContext.getWriteEncodingBuffer());
        assertNull(ioContext.getBase64Buffer());
        assertNull(ioContext.getTokenBuffer());
        assertNull(ioContext.getConcatBuffer());
        assertNull(ioContext.getNameCopyBuffer());

        ioContext.releaseReadIOBuffer(readIOBuffer); // should do nothing
        ioContext.releaseWriteEncodingBuffer(readIOBuffer); // should do nothing
        ioContext.releaseBase64Buffer(readIOBuffer); // should do nothing
        ioContext.releaseTokenBuffer(readIOBuffer); // should do nothing
        ioContext.releaseConcatBuffer(readIOBuffer); // should do nothing
        ioContext.releaseNameCopyBuffer(readIOBuffer); // should do nothing
    }
}