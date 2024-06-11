package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    // Test IOContext constructor
    @Test
    public void testIOContextConstructor() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;

        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        assertEquals(br, ioContext._bufferRecycler);
        assertEquals(sourceRef, ioContext._sourceRef);
        assertEquals(managedResource, ioContext._managedResource);
    }

    // Test setEncoding method
    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;

        ioContext.setEncoding(encoding);

        assertEquals(encoding, ioContext._encoding);
    }

    // Test withEncoding method
    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;

        IOContext newContext = ioContext.withEncoding(encoding);

        assertEquals(encoding, newContext._encoding);
        assertNotEquals(ioContext, newContext);
    }

    // Test getSourceReference method
    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);

        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    // Test getEncoding method
    @Test
    public void testGetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext._encoding = encoding;

        assertEquals(encoding, ioContext.getEncoding());
    }

    // Test isResourceManaged method
    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        assertTrue(ioContext.isResourceManaged());

        ioContext = new IOContext(new BufferRecycler(), new Object(), false);

        assertFalse(ioContext.isResourceManaged());
    }

    // Test constructTextBuffer method
    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        TextBuffer textBuffer = ioContext.constructTextBuffer();

        assertNotNull(textBuffer);
    }

    // Test allocReadIOBuffer method
    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        byte[] buffer = ioContext.allocReadIOBuffer();

        assertNotNull(buffer);
    }

    // Test allocReadIOBuffer with specified minSize method
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        int minSize = 8;

        byte[] buffer = ioContext.allocReadIOBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test allocWriteEncodingBuffer method
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        assertNotNull(buffer);
    }

    // Test allocWriteEncodingBuffer with specified minSize method
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        int minSize = 8;

        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test allocBase64Buffer method
    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        byte[] buffer = ioContext.allocBase64Buffer();

        assertNotNull(buffer);
    }

    // Test allocTokenBuffer method
    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        char[] buffer = ioContext.allocTokenBuffer();

        assertNotNull(buffer);
    }

    // Test allocTokenBuffer with specified minSize method
    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        int minSize = 8;

        char[] buffer = ioContext.allocTokenBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test allocConcatBuffer method
    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        char[] buffer = ioContext.allocConcatBuffer();

        assertNotNull(buffer);
    }

    // Test allocNameCopyBuffer method
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        int minSize = 8;

        char[] buffer = ioContext.allocNameCopyBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test releaseReadIOBuffer method
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        byte[] newBuffer = ioContext.allocReadIOBuffer();

        assertNotSame(buffer, newBuffer);
    }

    // Test releaseWriteEncodingBuffer method
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        byte[] newBuffer = ioContext.allocWriteEncodingBuffer();

        assertNotSame(buffer, newBuffer);
    }

    // Test releaseBase64Buffer method
    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        byte[] newBuffer = ioContext.allocBase64Buffer();

        assertNotSame(buffer, newBuffer);
    }

    // Test releaseTokenBuffer method
    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        char[] newBuffer = ioContext.allocTokenBuffer();

        assertNotSame(buffer, newBuffer);
    }

    // Test releaseConcatBuffer method
    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        char[] newBuffer = ioContext.allocConcatBuffer();

        assertNotSame(buffer, newBuffer);
    }

    // Test releaseNameCopyBuffer method
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);

        char[] buffer = ioContext.allocNameCopyBuffer(8);
        ioContext.releaseNameCopyBuffer(buffer);
        char[] newBuffer = ioContext.allocNameCopyBuffer(8);

        assertNotSame(buffer, newBuffer);
    }
    
}