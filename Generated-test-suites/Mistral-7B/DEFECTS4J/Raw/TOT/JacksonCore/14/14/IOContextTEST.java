package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    private BufferRecycler bufferRecycler;
    private IOContext ioContext;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(ioContext);
        assertEquals(bufferRecycler, ioContext._bufferRecycler);
        assertNull(ioContext._sourceRef);
        assertFalse(ioContext._managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());

        ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetters() {
        Object sourceRef = new Object();
        ioContext.setEncoding(JsonEncoding.UTF8);
        ioContext = new IOContext(bufferRecycler, sourceRef, true);

        assertEquals(sourceRef, ioContext.getSourceReference());
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer();
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocTokenBufferTwice() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.allocTokenBuffer();
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer.length);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        assertEquals(10, nameCopyBuffer.length);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocNameCopyBufferTwice() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.allocNameCopyBuffer(10);
    }

    @Test
    public void testReleaseBuffers() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        assertNull(ioContext._readIOBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @After
    public void tearDown() {
        bufferRecycler.clear();
        ioContext = null;
    }

}