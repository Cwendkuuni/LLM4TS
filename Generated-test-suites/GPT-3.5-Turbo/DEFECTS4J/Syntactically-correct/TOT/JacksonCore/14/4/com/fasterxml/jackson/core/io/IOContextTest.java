
package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        IOContext newIoContext = ioContext.withEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, newIoContext.getEncoding());
        assertNotSame(ioContext, newIoContext);
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = "source";
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
        assertNull(ioContext.allocReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(buffer);
        assertNull(ioContext.allocReadIOBuffer());
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        assertNull(ioContext.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);
        assertNotNull(buffer);
        assertNull(ioContext.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
        assertNull(ioContext.allocBase64Buffer());
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
        assertNull(ioContext.allocTokenBuffer());
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer(1024);
        assertNotNull(buffer);
        assertNull(ioContext.allocTokenBuffer());
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
        assertNull(ioContext.allocConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        assertNotNull(buffer);
        assertNull(ioContext.allocNameCopyBuffer(1024));
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext.allocReadIOBuffer());
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext.allocWriteEncodingBuffer());
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext.allocBase64Buffer());
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext.allocTokenBuffer());
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext.allocConcatBuffer());
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext.allocNameCopyBuffer(1024));
    }
}

