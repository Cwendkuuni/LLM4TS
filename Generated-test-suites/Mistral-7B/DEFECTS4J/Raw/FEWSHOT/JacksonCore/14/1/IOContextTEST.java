package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

import org.junit.Test;

public class IOContextTest {

    private BufferRecycler bufferRecycler = new BufferRecycler();
    private IOContext ioContext;

    @Test
    public void testIOContextCreation() {
        ioContext = new IOContext(bufferRecycler, null, false);
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        ioContext = new IOContext(bufferRecycler, null, false);
        ioContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(bufferRecycler, sourceRef, false);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        ioContext = new IOContext(bufferRecycler, null, false);
        assertFalse(ioContext.isResourceManaged());

        ioContext = new IOContext(bufferRecycler, null, true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        assertArrayEquals(readIOBuffer, readIOBuffer2);
        ioContext.releaseReadIOBuffer(readIOBuffer2);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer(1024);
        assertArrayEquals(readIOBuffer, readIOBuffer2);
        ioContext.releaseReadIOBuffer(readIOBuffer2);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
        assertArrayEquals(writeEncodingBuffer, writeEncodingBuffer2);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer2);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(1024);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer(1024);
        assertArrayEquals(writeEncodingBuffer, writeEncodingBuffer2);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer2);
    }

    @Test
    public void testAllocBase64Buffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);

        byte[] base64Buffer2 = ioContext.allocBase64Buffer();
        assertArrayEquals(base64Buffer, base64Buffer2);
        ioContext.releaseBase64Buffer(base64Buffer2);
    }

    @Test
    public void testAllocTokenBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
        assertArrayEquals(tokenBuffer, tokenBuffer2);
        ioContext.releaseTokenBuffer(tokenBuffer2);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer(1024);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer(1024);
        assertArrayEquals(tokenBuffer, tokenBuffer2);
        ioContext.releaseTokenBuffer(tokenBuffer2);
    }

    @Test
    public void testAllocConcatBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);

        char[] concatBuffer2 = ioContext.allocConcatBuffer();
        assertArrayEquals(concatBuffer, concatBuffer2);
        ioContext.releaseConcatBuffer(concatBuffer2);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        char[] nameCopyBuffer2 = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(nameCopyBuffer2);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer2);

        char[] nameCopyBuffer3 = ioContext.allocNameCopyBuffer(1024);
        assertSame(nameCopyBuffer2, nameCopyBuffer3);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext.getWriteEncodingBuffer());
    }

    @Test
    public void testReleaseBase64Buffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext.getBase64Buffer());
    }

    @Test
    public void testReleaseTokenBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());
    }

    @Test
    public void testReleaseConcatBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext.getNameCopyBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWrongBuffer() {
        ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseWriteEncodingBuffer(readIOBuffer);
    }
}