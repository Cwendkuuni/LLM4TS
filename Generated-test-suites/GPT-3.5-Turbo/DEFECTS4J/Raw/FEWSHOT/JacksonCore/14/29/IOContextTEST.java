package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {
    @Test
    public void testConstructor() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        
        assertSame(br, ioContext._bufferRecycler);
        assertSame(sourceRef, ioContext._sourceRef);
        assertTrue(ioContext._managedResource);
    }
    
    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        assertNull(ioContext._encoding);
        
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);
        
        assertSame(enc, ioContext._encoding);
    }
    
    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        assertNull(ioContext._encoding);
        
        JsonEncoding enc = JsonEncoding.UTF8;
        IOContext newContext = ioContext.withEncoding(enc);
        
        assertSame(enc, newContext._encoding);
    }
    
    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
        
        assertSame(sourceRef, ioContext.getSourceReference());
    }
    
    @Test
    public void testGetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        ioContext.setEncoding(enc);
        
        assertSame(enc, ioContext.getEncoding());
    }
    
    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        assertTrue(ioContext.isResourceManaged());
    }
    
    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        
        assertNotNull(textBuffer);
        assertNotNull(textBuffer._charBuffer);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        byte[] buf1 = ioContext.allocReadIOBuffer();
        assertNotNull(buf1);
        assertEquals(BufferRecycler.DEFAULT_READ_IO_BUFFER_SIZE, buf1.length);
        
        byte[] buf2 = ioContext.allocReadIOBuffer(1024);
        assertNotNull(buf2);
        assertEquals(1024, buf2.length);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        byte[] buf1 = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buf1);
        assertEquals(BufferRecycler.DEFAULT_WRITE_ENCODING_BUFFER_SIZE, buf1.length);
        
        byte[] buf2 = ioContext.allocWriteEncodingBuffer(1024);
        assertNotNull(buf2);
        assertEquals(1024, buf2.length);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        byte[] buf1 = ioContext.allocBase64Buffer();
        assertNotNull(buf1);
        assertEquals(BufferRecycler.DEFAULT_BASE64_CODEC_BUFFER_SIZE, buf1.length);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        char[] buf1 = ioContext.allocTokenBuffer();
        assertNotNull(buf1);
        assertEquals(BufferRecycler.DEFAULT_TOKEN_BUFFER_SIZE, buf1.length);
        
        char[] buf2 = ioContext.allocTokenBuffer(1024);
        assertNotNull(buf2);
        assertEquals(1024, buf2.length);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        char[] buf1 = ioContext.allocConcatBuffer();
        assertNotNull(buf1);
        assertEquals(BufferRecycler.DEFAULT_CONCAT_BUFFER_SIZE, buf1.length);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        char[] buf1 = ioContext.allocNameCopyBuffer(100);
        assertNotNull(buf1);
        assertEquals(100, buf1.length);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        byte[] buf = ioContext.allocReadIOBuffer();
        assertNotNull(ioContext._readIOBuffer);
        
        ioContext.releaseReadIOBuffer(buf);
        
        assertNull(ioContext._readIOBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        assertNotNull(ioContext._writeEncodingBuffer);
        
        ioContext.releaseWriteEncodingBuffer(buf);
        
        assertNull(ioContext._writeEncodingBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        byte[] buf = ioContext.allocBase64Buffer();
        assertNotNull(ioContext._base64Buffer);
        
        ioContext.releaseBase64Buffer(buf);
        
        assertNull(ioContext._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        char[] buf = ioContext.allocTokenBuffer();
        assertNotNull(ioContext._tokenCBuffer);
        
        ioContext.releaseTokenBuffer(buf);
        
        assertNull(ioContext._tokenCBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        char[] buf = ioContext.allocConcatBuffer();
        assertNotNull(ioContext._concatCBuffer);
        
        ioContext.releaseConcatBuffer(buf);
        
        assertNull(ioContext._concatCBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        
        char[] buf = ioContext.allocNameCopyBuffer(100);
        assertNotNull(ioContext._nameCopyBuffer);
        
        ioContext.releaseNameCopyBuffer(buf);
        
        assertNull(ioContext._nameCopyBuffer);
    }
}