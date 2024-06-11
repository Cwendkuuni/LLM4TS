package com.fasterxml.jackson.core.io; // package import for testing

import org.junit.Assert;
import org.junit.Test;

public class IOContextTest {

    

    @Test
    public void testIOContextConstructor() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        Assert.assertNotNull(ioContext);
    }

    @Test
    public void testSetEncodingMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding jsonEncoding = JsonEncoding.UTF8;
        ioContext.setEncoding(jsonEncoding);
        Assert.assertEquals(jsonEncoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncodingMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding jsonEncoding = JsonEncoding.UTF8;
        IOContext ioContextWithEncoding = ioContext.withEncoding(jsonEncoding);
        Assert.assertEquals(jsonEncoding, ioContextWithEncoding.getEncoding());
    }

    @Test
    public void testGetSourceReferenceMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", false);
        Object sourceReference = ioContext.getSourceReference();
        Assert.assertEquals("sourceRef", sourceReference);
    }

    @Test
    public void testGetEncodingMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        Assert.assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManagedMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, true);
        Assert.assertTrue(ioContext.isResourceManaged());

        ioContext = new IOContext(bufferRecycler, null, false);
        Assert.assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64BufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBufferMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        Assert.assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseReadIOBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseBase64Buffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseTokenBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseConcatBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithNullBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        ioContext.releaseNameCopyBuffer(null);
    }

    
}