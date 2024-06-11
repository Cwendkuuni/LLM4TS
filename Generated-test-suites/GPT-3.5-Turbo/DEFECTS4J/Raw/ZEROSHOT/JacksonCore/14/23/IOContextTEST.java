package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {
    
    private IOContext ioContext;
    private BufferRecycler bufferRecycler;
    
    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
    }
    
    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        
        assertEquals(encoding, ioContext.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.withEncoding(encoding);
        
        assertEquals(encoding, ioContext.getEncoding());
    }
    
    @Test
    public void testGetSourceReference() {
        Object sourceRef = ioContext.getSourceReference();
        
        assertEquals("sourceRef", sourceRef);
    }
    
    @Test
    public void testIsResourceManaged() {
        boolean isResourceManaged = ioContext.isResourceManaged();
        
        assertTrue(isResourceManaged);
    }
    
    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        
        assertNotNull(textBuffer);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        
        assertNotNull(buffer);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = new byte[1024];
        
        ioContext.releaseReadIOBuffer(buffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = new byte[1024];
        
        ioContext.releaseWriteEncodingBuffer(buffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = new byte[1024];
        
        ioContext.releaseBase64Buffer(buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = new char[1024];
        
        ioContext.releaseTokenBuffer(buffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = new char[1024];
        
        ioContext.releaseConcatBuffer(buffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = new char[1024];
        
        ioContext.releaseNameCopyBuffer(buffer);
    }
}