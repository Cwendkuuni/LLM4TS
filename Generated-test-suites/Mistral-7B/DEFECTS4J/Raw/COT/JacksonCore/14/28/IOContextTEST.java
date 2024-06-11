package com.fasterxml.jackson.core.io; // Make sure the test class is in the same package as the tested class

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    IOContext ioContext;
    BufferRecycler bufferRecycler;

    @Before
    public void setup() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, null, false);
    }

    

    // Testing constructor
    @Test
    public void testIOContextConstructor() {
        Assert.assertNotNull(ioContext);
        Assert.assertNotNull(ioContext._bufferRecycler);
        Assert.assertNull(ioContext._sourceRef);
        Assert.assertNull(ioContext._encoding);
        Assert.assertFalse(ioContext._managedResource);
    }

    // Testing getter methods
    @Test
    public void testGetSourceReference() {
        ioContext._sourceRef = new Object();
        Assert.assertEquals(ioContext._sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        Assert.assertFalse(ioContext.isResourceManaged());
    }

    // Testing buffer management methods
    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        Assert.assertNull(ioContext._readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        Assert.assertNull(ioContext._writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        Assert.assertNull(ioContext._base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        Assert.assertNull(ioContext._tokenCBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        Assert.assertNull(ioContext._concatCBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        Assert.assertNotNull(nameCopyBuffer);
        Assert.assertNull(ioContext._nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    // Testing release methods
    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseReadIOBufferWithNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseWriteEncodingBufferWithNullBuffer() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseBase64BufferWithNullBuffer() {
        ioContext.releaseBase64Buffer(null);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseTokenBufferWithNullBuffer() {
        ioContext.releaseTokenBuffer(null);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseConcatBufferWithNullBuffer() {
        ioContext.releaseConcatBuffer(null);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseNameCopyBufferWithNullBuffer() {
        ioContext.releaseNameCopyBuffer(null);
    }

    
}