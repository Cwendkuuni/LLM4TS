package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IOContextTest {
    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testConstructor() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        
        assertEquals(bufferRecycler, ioContext._bufferRecycler);
        assertEquals(sourceRef, ioContext._sourceRef);
        assertEquals(managedResource, ioContext._managedResource);
    }

    @Test
    public void testConstructTextBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        
        assertEquals(bufferRecycler, textBuffer._allocator._recycler);
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        
        assertEquals(readIOBuffer, ioContext._readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 512;
        
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        
        assertEquals(readIOBuffer, ioContext._readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        
        assertEquals(writeEncodingBuffer, ioContext._writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 512;
        
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        
        assertEquals(writeEncodingBuffer, ioContext._writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        
        assertEquals(base64Buffer, ioContext._base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        
        assertEquals(tokenBuffer, ioContext._tokenCBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 512;
        
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        
        assertEquals(tokenBuffer, ioContext._tokenCBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        
        char[] concatBuffer = ioContext.allocConcatBuffer();
        
        assertEquals(concatBuffer, ioContext._concatCBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 512;
        
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        
        assertEquals(nameCopyBuffer, ioContext._nameCopyBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = new byte[1024];
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.allocReadIOBuffer();
        
        ioContext.releaseReadIOBuffer(buf);
        
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = new byte[1024];
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.allocWriteEncodingBuffer();
        
        ioContext.releaseWriteEncodingBuffer(buf);
        
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = new byte[1024];
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.allocBase64Buffer();
        
        ioContext.releaseBase64Buffer(buf);
        
        assertNull(ioContext._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = new char[1024];
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.allocTokenBuffer();
        
        ioContext.releaseTokenBuffer(buf);
        
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = new char[1024];
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.allocConcatBuffer();
        
        ioContext.releaseConcatBuffer(buf);
        
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buf = new char[1024];
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.allocNameCopyBuffer(512);
        
        ioContext.releaseNameCopyBuffer(buf);
        
        assertNull(ioContext._nameCopyBuffer);
    }
}