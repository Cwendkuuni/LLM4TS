package com.fasterxml.jackson.core.io; // package name should be imported if using this test class

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    // Given
    private IOContext ioContext;

    @Before
    public void setUp() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, new Object(), true);
    }

    

    @Test
    public void testConstructor() {
        // Check that constructor is called with correct arguments
        Assert.assertNotNull(ioContext._bufferRecycler);
        Assert.assertNotNull(ioContext._sourceRef);
        Assert.assertTrue(ioContext._managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newIOContext = ioContext.withEncoding(encoding);
        Assert.assertEquals(encoding, newIOContext.getEncoding());
        Assert.assertNotSame(ioContext, newIOContext);
    }

    @Test
    public void testGetSourceReference() {
        Assert.assertNotNull(ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        Assert.assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(readIOBuffer);
        Assert.assertEquals(minSize, readIOBuffer.length);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(writeEncodingBuffer);
        Assert.assertEquals(minSize, writeEncodingBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        Assert.assertNotNull(tokenBuffer);
        Assert.assertEquals(minSize, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocConcatBufferTwice() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.allocConcatBuffer();
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        Assert.assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithNullBuffer() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithNullBuffer() {
        ioContext.releaseBase64Buffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithNullBuffer() {
        ioContext.releaseTokenBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithNullBuffer() {
        ioContext.releaseConcatBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithNullBuffer() {
        ioContext.releaseNameCopyBuffer(null);
    }

    
}