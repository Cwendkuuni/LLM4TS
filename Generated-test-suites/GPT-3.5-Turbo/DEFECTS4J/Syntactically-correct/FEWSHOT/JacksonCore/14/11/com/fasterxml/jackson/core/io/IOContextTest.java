package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext context;
    private BufferRecycler bufferRecycler;
 
    @Before
    public void setup() {
        bufferRecycler = new BufferRecycler();
        context = new IOContext(bufferRecycler, new Object(), true);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);

        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF16_BE;
        IOContext newContext = context.withEncoding(encoding);

        assertEquals(encoding, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        context = new IOContext(bufferRecycler, sourceRef, true);

        assertEquals(sourceRef, context.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = context.constructTextBuffer();

        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = context.allocReadIOBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] buffer = context.allocReadIOBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = context.allocWriteEncodingBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        byte[] buffer = context.allocWriteEncodingBuffer(minSize);
        
        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = context.allocBase64Buffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = context.allocTokenBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        char[] buffer = context.allocTokenBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = context.allocConcatBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buffer = context.allocNameCopyBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        int minSize = 1024;
        char[] buffer = context.allocNameCopyBuffer(minSize);
        context.releaseNameCopyBuffer(buffer);
    }
}