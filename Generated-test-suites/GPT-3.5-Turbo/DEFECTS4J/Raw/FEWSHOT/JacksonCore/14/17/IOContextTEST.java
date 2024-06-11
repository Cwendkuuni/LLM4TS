package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    //set up
    private IOContext ioContext;
    private BufferRecycler bufferRecycler;
    private Object sourceReference;
    private boolean managedResource;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceReference = new Object();
        managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceReference, managedResource);
    }

    @Test
    public void testSetEncoding() {
        assertNull(ioContext.getEncoding());
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        assertNull(ioContext.getEncoding());
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceReference, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertFalse(textBuffer.isEmpty());
    }

    @Test
    public void testAllocReadIOBuffer() {
        assertNull(ioContext.allocReadIOBuffer());
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertEquals(BufferRecycler.DEFAULT_READ_IO_BUFFER_LENGTH, readIOBuffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 512;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        assertNull(ioContext.allocWriteEncodingBuffer());
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(BufferRecycler.DEFAULT_WRITE_ENCODING_BUFFER_LENGTH, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 512;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertEquals(minSize, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        assertNull(ioContext.allocBase64Buffer());
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertEquals(BufferRecycler.DEFAULT_BASE64_CODEC_BUFFER_LENGTH, base64Buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        assertNull(ioContext.allocTokenBuffer());
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertEquals(BufferRecycler.DEFAULT_TOKEN_BUFFER_LENGTH, tokenBuffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 512;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertEquals(minSize, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        assertNull(ioContext.allocConcatBuffer());
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertEquals(BufferRecycler.DEFAULT_CONCAT_BUFFER_LENGTH, concatBuffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 512;
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
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(BufferRecycler.DEFAULT_NAME_COPY_BUFFER_LENGTH);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}