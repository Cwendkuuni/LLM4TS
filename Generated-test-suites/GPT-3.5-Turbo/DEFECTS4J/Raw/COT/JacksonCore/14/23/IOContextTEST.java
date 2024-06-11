package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOTestContext {

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(1024);
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] tokenBuffer = ioContext.allocTokenBuffer(1024);
        assertNotNull(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(nameCopyBuffer);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}