
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {
    
    // Public methods in the IOContext class
    
    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        IOContext ioContext2 = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
        assertEquals(ioContext, ioContext2);
    }
    
    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(null, sourceRef, false);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }
    
    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(null, null, true);
        assertTrue(ioContext.isResourceManaged());
    }
    
    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocReadIOBufferWithSize() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBufferWithSize() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(buffer);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}
