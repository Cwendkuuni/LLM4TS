package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {
    private IOContext context;
    private byte[] byteBuffer;
    private char[] charBuffer;
    
    @Before
    public void setUp() {
        context = new IOContext(null, null, true);
    }
    
    @Test
    public void testConstructTextBuffer() {
        assertNotNull(context.constructTextBuffer());
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        assertNull(context.allocReadIOBuffer());
    }
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        assertNull(context.allocReadIOBuffer(1024));
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        assertNull(context.allocWriteEncodingBuffer());
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        assertNull(context.allocWriteEncodingBuffer(1024));
    }
    
    @Test
    public void testAllocBase64Buffer() {
        assertNull(context.allocBase64Buffer());
    }
    
    @Test
    public void testAllocTokenBuffer() {
        assertNull(context.allocTokenBuffer());
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        assertNull(context.allocTokenBuffer(1024));
    }
    
    @Test
    public void testAllocConcatBuffer() {
        assertNull(context.allocConcatBuffer());
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        assertNull(context.allocNameCopyBuffer(1024));
    }
    
    @Test
    public void testSetEncoding() {
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        IOContext newContext = context.withEncoding(JsonEncoding.UTF16_LE);
        assertEquals(JsonEncoding.UTF16_LE, newContext.getEncoding());
    }
    
    @Test
    public void testGetSourceReference() {
        assertNull(context.getSourceReference());
    }
    
    @Test
    public void testIsResourceManaged() {
        assertTrue(context.isResourceManaged());
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        byteBuffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(byteBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        byteBuffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(byteBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        byteBuffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(byteBuffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        charBuffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(charBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        charBuffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(charBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        charBuffer = context.allocNameCopyBuffer(1024);
        context.releaseNameCopyBuffer(charBuffer);
    }
}