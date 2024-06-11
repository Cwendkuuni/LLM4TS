package com.fasterxml.jackson.core.io; // import necessary packages here

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    

    @Test
    public void testIOContextConstructor() {
        Assert.assertNotNull(ioContext);
    }

    @Test
    public void testGetSourceReference() {
        Assert.assertEquals(ioContext.getSourceReference(), sourceRef);
    }

    @Test
    public void testGetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(ioContext.getEncoding(), encoding);
    }

    @Test
    public void testIsResourceManaged() {
        Assert.assertEquals(ioContext.isResourceManaged(), managedResource);
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
        Assert.assertEquals(readIOBuffer.length, minSize);
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
        Assert.assertEquals(writeEncodingBuffer.length, minSize);
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
        Assert.assertEquals(tokenBuffer.length, minSize);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        Assert.assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocateSameBufferTwice() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer(); // throws an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseDifferentBuffer() {
        byte[] anotherBuffer = new byte[10];
        ioContext.releaseReadIOBuffer(anotherBuffer);
    }

    
}