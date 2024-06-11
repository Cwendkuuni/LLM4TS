package com.fasterxml.jackson.core.io; // package import for IOContext class

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContextConstruction() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        assertNotNull(ioContext);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), false);
        assertFalse(ioContext.isResourceManaged());

        ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    // Test other methods for allocWriteEncodingBuffer, allocBase64Buffer, allocTokenBuffer,
    // allocConcatBuffer, allocNameCopyBuffer, releaseWriteEncodingBuffer, releaseBase64Buffer,
    // releaseTokenBuffer, releaseConcatBuffer, releaseNameCopyBuffer, _verifyAlloc, _verifyRelease,
    // and _verifyRelease with different types of buffers

    @Test(expected = IllegalStateException.class)
    public void testReleaseUnownedBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] unownedBuffer = new byte[10];
        ioContext.releaseReadIOBuffer(unownedBuffer);
    }
}