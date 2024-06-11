package com.fasterxml.jackson.core.io;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IOContextTest {
    private IOContext ioContext;

    @Before
    public void setUp() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF16_BE;
        IOContext context = ioContext.withEncoding(encoding);
        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        boolean managedResource = ioContext.isResourceManaged();
        assertEquals(true, managedResource);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer buffer = ioContext.constructTextBuffer();
        assertEquals(0, buffer.size());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 20;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertEquals(minSize, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 30;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertEquals(minSize, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 40;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertEquals(minSize, nameCopyBuffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        int minSize = 40;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}