package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Set encoding
        context.setEncoding(JsonEncoding.UTF8);
        
        // Verify encoding is set correctly
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Set encoding using withEncoding method
        context.withEncoding(JsonEncoding.UTF8);
        
        // Verify encoding is set correctly
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }
    
    @Test
    public void testConstructTextBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Construct a text buffer
        TextBuffer textBuffer = context.constructTextBuffer();
        
        // Verify text buffer is constructed
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a read IO buffer
        byte[] buffer = context.allocReadIOBuffer();
        
        // Verify buffer is allocated
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocReadIOBufferWithSize() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a read IO buffer with size
        byte[] buffer = context.allocReadIOBuffer(1024);
        
        // Verify buffer is allocated
        assertNotNull(buffer);
        // Verify buffer size matches the requested size
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a write encoding buffer
        byte[] buffer = context.allocWriteEncodingBuffer();
        
        // Verify buffer is allocated
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a write encoding buffer with size
        byte[] buffer = context.allocWriteEncodingBuffer(1024);
        
        // Verify buffer is allocated
        assertNotNull(buffer);
        // Verify buffer size matches the requested size
        assertEquals(1024, buffer.length);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a base64 buffer
        byte[] buffer = context.allocBase64Buffer();
        
        // Verify buffer is allocated
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a token buffer
        char[] buffer = context.allocTokenBuffer();
        
        // Verify buffer is allocated
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBufferWithSize() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a token buffer with size
        char[] buffer = context.allocTokenBuffer(1024);
        
        // Verify buffer is allocated
        assertNotNull(buffer);
        // Verify buffer size matches the requested size
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a concat buffer
        char[] buffer = context.allocConcatBuffer();
        
        // Verify buffer is allocated
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate a name copy buffer
        char[] buffer = context.allocNameCopyBuffer(1024);
        
        // Verify buffer is allocated
        assertNotNull(buffer);
        // Verify buffer size matches the requested size
        assertEquals(1024, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate and release a read IO buffer
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
        
        // Verify buffer is released
        assertNull(context._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate and release a write encoding buffer
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
        
        // Verify buffer is released
        assertNull(context._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate and release a base64 buffer
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
        
        // Verify buffer is released
        assertNull(context._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate and release a token buffer
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
        
        // Verify buffer is released
        assertNull(context._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate and release a concat buffer
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
        
        // Verify buffer is released
        assertNull(context._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        // Create IOContext instance
        IOContext context = new IOContext(null, null, false);

        // Allocate and release a name copy buffer
        char[] buffer = context.allocNameCopyBuffer(1024);
        context.releaseNameCopyBuffer(buffer);
        
        // Verify buffer is released
        assertNull(context._nameCopyBuffer);
    }
}