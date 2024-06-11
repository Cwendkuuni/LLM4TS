package com.fasterxml.jackson.core.io; // make sure to import all necessary packages

import org.junit.Assert; // for Assert.assertEquals, Assert.assertNull, etc.
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testConstructor() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        Assert.assertNotNull(ioContext);
        Assert.assertEquals(new Object(), ioContext.getSourceReference());
        Assert.assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        Assert.assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer(); // should throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNonOwnedBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = new byte[10];
        ioContext.releaseReadIOBuffer(buffer); // should throw exception
    }

    @Test
    public void testReleaseOwnedBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        Assert.assertNull(ioContext._readIOBuffer);
    }
}