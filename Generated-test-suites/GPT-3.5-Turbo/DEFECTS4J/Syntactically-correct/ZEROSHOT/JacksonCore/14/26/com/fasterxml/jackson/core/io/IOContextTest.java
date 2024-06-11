package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testConstruction() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        assertNotNull(ioContext);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertSame(ioContext, newContext);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceReference = new Object();
        IOContext ioContext = new IOContext(null, sourceReference, true);
        assertSame(sourceReference, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        char[] buffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(buffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, new Object(), true);
        char[] buffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(buffer);
    }
}