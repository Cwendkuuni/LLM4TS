package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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
    public void setup() {
        bufferRecycler = mock(BufferRecycler.class);
        sourceRef = mock(Object.class);
        managedResource = true;

        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);
        assertEquals(enc, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        IOContext newContext = ioContext.withEncoding(enc);
        assertEquals(enc, newContext.getEncoding());
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
        // Verify that the text buffer is not null
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        // Verify that the read IO buffer is not null
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        // Verify that the read IO buffer is not null and has the minimum size
        assertNotNull(readIOBuffer);
        assertTrue(readIOBuffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        // Verify that the write encoding buffer is not null
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        // Verify that the write encoding buffer is not null and has the minimum size
        assertNotNull(writeEncodingBuffer);
        assertTrue(writeEncodingBuffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        // Verify that the base64 buffer is not null
        assertNotNull(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        // Verify that the token buffer is not null
        assertNotNull(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        // Verify that the token buffer is not null and has the minimum size
        assertNotNull(tokenBuffer);
        assertTrue(tokenBuffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        // Verify that the concat buffer is not null
        assertNotNull(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        // Verify that the name copy buffer is not null and has the minimum size
        assertNotNull(nameCopyBuffer);
        assertTrue(nameCopyBuffer.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        // Verify that the read IO buffer is released and set to null
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        // Verify that the write encoding buffer is released and set to null
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        // Verify that the base64 buffer is released and set to null
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        // Verify that the token buffer is released and set to null
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        // Verify that the concat buffer is released and set to null
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        int minSize = 1024;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        // Verify that the name copy buffer is released and set to null
        assertNull(ioContext._nameCopyBuffer);
    }
}