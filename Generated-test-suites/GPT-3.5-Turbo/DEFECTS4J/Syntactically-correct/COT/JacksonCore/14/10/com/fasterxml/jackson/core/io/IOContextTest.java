
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testGetSourceReference() {
        IOContext context = new IOContext(null, "source", true);
        assertEquals("source", context.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        IOContext context = new IOContext(null, null, true);
        assertNull(context.getEncoding());

        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);
        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext context = new IOContext(null, null, true);
        assertTrue(context.isResourceManaged());

        context = new IOContext(null, null, false);
        assertFalse(context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.constructTextBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocReadIOBuffer(1024));
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocWriteEncodingBuffer(1024));
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocBase64Buffer());
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocTokenBuffer());
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocTokenBuffer(1024));
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        assertNotNull(context.allocNameCopyBuffer(1024));
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
        assertNull(context._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
        assertNull(context._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
        assertNull(context._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
        assertNull(context._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
        assertNull(context._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = context.allocNameCopyBuffer(1024);
        context.releaseNameCopyBuffer(buffer);
        assertNull(context._nameCopyBuffer);
    }
}
