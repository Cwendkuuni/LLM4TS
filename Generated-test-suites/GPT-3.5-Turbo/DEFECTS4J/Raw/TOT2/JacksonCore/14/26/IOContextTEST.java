package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {
    
    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        IOContext newContext = context.withEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, newContext.getEncoding());
    }
    
    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocReadIOBuffer_MinSize() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = context.allocReadIOBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
        assertNull(context._readIOBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
        assertNull(context._writeEncodingBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
        assertNull(context._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
        assertNull(context._tokenCBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
        assertNull(context._concatCBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = context.allocNameCopyBuffer(512);
        context.releaseNameCopyBuffer(buffer);
        assertNull(context._nameCopyBuffer);
    }
}