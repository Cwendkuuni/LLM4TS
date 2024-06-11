package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testGetSourceReference() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNull(ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(null, null, false);
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.constructTextBuffer());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocReadIOBuffer(10));
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocWriteEncodingBuffer());
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocWriteEncodingBuffer(10));
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocBase64Buffer());
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocTokenBuffer());
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocTokenBuffer(10));
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocConcatBuffer());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNotNull(ioContext.allocNameCopyBuffer(10));
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buf = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buf);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buf);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buf = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buf);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buf = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buf);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buf = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buf);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buf = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(buf);
        assertNull(ioContext._nameCopyBuffer);
    }
}