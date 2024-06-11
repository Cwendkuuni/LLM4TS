package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;
    private Object sourceRef;
    private boolean managedResource;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        managedResource = true;
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
        JsonEncoding encoding = JsonEncoding.UTF16_LE;
        IOContext result = ioContext.withEncoding(encoding);
        assertEquals(encoding, result.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertEquals(managedResource, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] result = ioContext.allocReadIOBuffer();
        assertNotNull(result);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, result.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 10;
        byte[] result = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(result);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] result = ioContext.allocWriteEncodingBuffer();
        assertNotNull(result);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, result.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 20;
        byte[] result = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(result);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] result = ioContext.allocBase64Buffer();
        assertNotNull(result);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, result.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] result = ioContext.allocTokenBuffer();
        assertNotNull(result);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, result.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 30;
        char[] result = ioContext.allocTokenBuffer(minSize);
        assertNotNull(result);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] result = ioContext.allocConcatBuffer();
        assertNotNull(result);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, result.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 40;
        char[] result = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(result);
        assertEquals(minSize, result.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buf);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buf);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buf);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buf);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buf);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buf = ioContext.allocNameCopyBuffer(50);
        ioContext.releaseNameCopyBuffer(buf);
        assertNull(ioContext._nameCopyBuffer);
    }
}