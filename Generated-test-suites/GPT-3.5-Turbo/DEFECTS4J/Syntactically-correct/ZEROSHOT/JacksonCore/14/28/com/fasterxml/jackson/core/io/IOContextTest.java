package com.fasterxml.jackson.core.io;

import org.junit.*;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.setEncoding(JsonEncoding.UTF16_BE);

        assertEquals(JsonEncoding.UTF16_BE, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, null, false);
        context = context.withEncoding(JsonEncoding.UTF8);

        assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, false);
        TextBuffer textBuffer = context.constructTextBuffer();

        assertNotNull(textBuffer);
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
        byte[] buffer = context.allocReadIOBuffer(1024);

        assertNotNull(buffer);
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
        byte[] buffer = context.allocWriteEncodingBuffer(1024);

        assertNotNull(buffer);
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
        char[] buffer = context.allocTokenBuffer(1024);

        assertNotNull(buffer);
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
        char[] buffer = context.allocNameCopyBuffer(1024);

        assertNotNull(buffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);

        assertNull(context._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);

        assertNull(context._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, false);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);

        assertNull(context._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);

        assertNull(context._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);

        assertNull(context._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, false);
        char[] buffer = context.allocNameCopyBuffer(1024);
        context.releaseNameCopyBuffer(buffer);

        assertNull(context._nameCopyBuffer);
    }
}