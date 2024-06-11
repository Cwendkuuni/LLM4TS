
package com.fasterxml.jackson.core.io;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {
    
    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assert(ioContext.getEncoding() == JsonEncoding.UTF8);
    }
    
    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        ioContext = ioContext.withEncoding(JsonEncoding.UTF16_BE);
        assert(ioContext.getEncoding() == JsonEncoding.UTF16_BE);
    }
    
    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assert(textBuffer != null);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assert(buffer != null);
    }
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assert(buffer != null);
        assert(buffer.length >= 1024);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assert(buffer != null);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);
        assert(buffer != null);
        assert(buffer.length >= 1024);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocBase64Buffer();
        assert(buffer != null);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocTokenBuffer();
        assert(buffer != null);
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocTokenBuffer(1024);
        assert(buffer != null);
        assert(buffer.length >= 1024);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocConcatBuffer();
        assert(buffer != null);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assert(buffer != null);
        assert(buffer.length >= 1024);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buffer);
    }
}
