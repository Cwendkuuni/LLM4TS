
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    // Test getSourceReference()
    @Test
    public void testGetSourceReference() {
        IOContext context = new IOContext(null, "source", false);
        assertEquals("source", context.getSourceReference());
    }

    // Test getEncoding() and setEncoding()
    @Test
    public void testGetSetEncoding() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.getEncoding());
        
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    // Test isResourceManaged()
    @Test
    public void testIsResourceManaged() {
        IOContext context1 = new IOContext(null, null, false);
        assertFalse(context1.isResourceManaged());
        
        IOContext context2 = new IOContext(null, null, true);
        assertTrue(context2.isResourceManaged());
    }

    // Test constructTextBuffer()
    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNotNull(context.constructTextBuffer());
        assertTrue(context.constructTextBuffer() instanceof TextBuffer);
    }

    // Test allocReadIOBuffer() and releaseReadIOBuffer()
    @Test
    public void testAllocReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocReadIOBuffer());
        
        byte[] buffer = new byte[1024];
        context.releaseReadIOBuffer(buffer);
    }

    // Test allocReadIOBuffer(int) and releaseReadIOBuffer()
    @Test
    public void testAllocReleaseReadIOBufferWithSize() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocReadIOBuffer(512));
        
        byte[] buffer = new byte[512];
        context.releaseReadIOBuffer(buffer);
    }

    // Test allocWriteEncodingBuffer() and releaseWriteEncodingBuffer()
    @Test
    public void testAllocReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocWriteEncodingBuffer());
        
        byte[] buffer = new byte[1024];
        context.releaseWriteEncodingBuffer(buffer);
    }

    // Test allocWriteEncodingBuffer(int) and releaseWriteEncodingBuffer()
    @Test
    public void testAllocReleaseWriteEncodingBufferWithSize() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocWriteEncodingBuffer(512));
        
        byte[] buffer = new byte[512];
        context.releaseWriteEncodingBuffer(buffer);
    }

    // Test allocBase64Buffer() and releaseBase64Buffer()
    @Test
    public void testAllocReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocBase64Buffer());
        
        byte[] buffer = new byte[1024];
        context.releaseBase64Buffer(buffer);
    }

    // Test allocTokenBuffer() and releaseTokenBuffer()
    @Test
    public void testAllocReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocTokenBuffer());
        
        char[] buffer = new char[1024];
        context.releaseTokenBuffer(buffer);
    }

    // Test allocTokenBuffer(int) and releaseTokenBuffer()
    @Test
    public void testAllocReleaseTokenBufferWithSize() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocTokenBuffer(512));
        
        char[] buffer = new char[512];
        context.releaseTokenBuffer(buffer);
    }

    // Test allocConcatBuffer() and releaseConcatBuffer()
    @Test
    public void testAllocReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocConcatBuffer());
        
        char[] buffer = new char[1024];
        context.releaseConcatBuffer(buffer);
    }

    // Test allocNameCopyBuffer() and releaseNameCopyBuffer()
    @Test
    public void testAllocReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNull(context.allocNameCopyBuffer(512));
        
        char[] buffer = new char[512];
        context.releaseNameCopyBuffer(buffer);
    }

}

