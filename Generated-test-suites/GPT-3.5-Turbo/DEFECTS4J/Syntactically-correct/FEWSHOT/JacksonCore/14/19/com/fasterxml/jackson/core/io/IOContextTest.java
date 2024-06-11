
package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.withEncoding(JsonEncoding.UTF16);
        assertEquals(JsonEncoding.UTF16, context.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, false);
        assertNotNull(context.constructTextBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocReadIOBuffer(100);
        assertNotNull(buffer);
        assertEquals(100, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocWriteEncodingBuffer(200);
        assertNotNull(buffer);
        assertEquals(200, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocTokenBuffer(150);
        assertNotNull(buffer);
        assertEquals(150, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocNameCopyBuffer(250);
        assertNotNull(buffer);
        assertEquals(250, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocNameCopyBuffer(300);
        context.releaseNameCopyBuffer(buffer);
    }
}
