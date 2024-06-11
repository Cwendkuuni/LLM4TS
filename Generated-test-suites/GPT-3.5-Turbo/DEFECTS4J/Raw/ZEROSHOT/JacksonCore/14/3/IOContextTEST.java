package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testGetSourceReference() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        // Verify
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        JsonEncoding enc = JsonEncoding.UTF8;

        // Execute
        ioContext.setEncoding(enc);

        // Verify
        assertEquals(enc, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        // Verify
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        // Execute
        TextBuffer textBuffer = ioContext.constructTextBuffer();

        // Verify
        assertNotNull(textBuffer);
        assertEquals(br, textBuffer.getBufferRecycler());
    }

    @Test
    public void testAllocReadIOBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        // Execute
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);

        // Verify
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        // Execute
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);

        // Verify
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        // Execute
        byte[] buffer = ioContext.allocBase64Buffer();

        // Verify
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        // Execute
        char[] buffer = ioContext.allocTokenBuffer(minSize);

        // Verify
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        // Execute
        char[] buffer = ioContext.allocConcatBuffer();

        // Verify
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        // Execute
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);

        // Verify
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        byte[] buf = ioContext.allocReadIOBuffer();

        // Execute
        ioContext.releaseReadIOBuffer(buf);

        // Verify
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        byte[] buf = ioContext.allocWriteEncodingBuffer();

        // Execute
        ioContext.releaseWriteEncodingBuffer(buf);

        // Verify
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        char[] buf = ioContext.allocTokenBuffer();

        // Execute
        ioContext.releaseTokenBuffer(buf);

        // Verify
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        char[] buf = ioContext.allocConcatBuffer();

        // Execute
        ioContext.releaseConcatBuffer(buf);

        // Verify
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        // Setup
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        char[] buf = ioContext.allocNameCopyBuffer(10);

        // Execute
        ioContext.releaseNameCopyBuffer(buf);

        // Verify
        assertNull(ioContext._nameCopyBuffer);
    }
}