package com.fasterxml.jackson.core.io; //make sure the package name is correct

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

    IOContext ioContext;
    BufferRecycler bufferRecycler;
    
    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, null, false);
    }

    

    // Testing constructor method
    @Test
    public void testIOContextConstructor() {
        Assert.assertNotNull(ioContext);
        Assert.assertNull(ioContext.getEncoding());
        Assert.assertFalse(ioContext.isResourceManaged());
    }

    // Testing setEncoding method
    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    // Testing withEncoding method
    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newIOContext = ioContext.withEncoding(encoding);
        Assert.assertEquals(encoding, newIOContext.getEncoding());
        Assert.assertNotSame(ioContext, newIOContext);
    }

    // Testing getSourceReference method
    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(bufferRecycler, sourceRef, false);
        Assert.assertEquals(sourceRef, ioContext.getSourceReference());
    }

    // Testing getEncoding method
    @Test
    public void testGetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    // Testing isResourceManaged method
    @Test
    public void testIsResourceManaged() {
        Assert.assertFalse(ioContext.isResourceManaged());
    }

    // Testing constructTextBuffer method
    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    // Testing allocReadIOBuffer method with default size
    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    // Testing allocReadIOBuffer method with minSize
    @Test
    public void testAllocReadIOBufferMinSize() {
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(readIOBuffer);
        Assert.assertEquals(minSize, readIOBuffer.length);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    // Testing allocWriteEncodingBuffer method with default size
    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    // Testing allocWriteEncodingBuffer method with minSize
    @Test
    public void testAllocWriteEncodingBufferMinSize() {
        int minSize = 20;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(writeEncodingBuffer);
        Assert.assertEquals(minSize, writeEncodingBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    // Testing allocBase64Buffer method
    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    // Testing allocTokenBuffer method with default size
    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    // Testing allocTokenBuffer method with minSize
    @Test
    public void testAllocTokenBufferMinSize() {
        int minSize = 20;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        Assert.assertNotNull(tokenBuffer);
        Assert.assertEquals(minSize, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    // Testing allocConcatBuffer method
    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    // Testing allocNameCopyBuffer method with default size
    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
        Assert.assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    // Testing allocNameCopyBuffer method with minSize
    @Test
    public void testAllocNameCopyBufferMinSize() {
        int minSize = 30;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        Assert.assertNotNull(nameCopyBuffer);
        Assert.assertEquals(minSize, nameCopyBuffer.length);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    // Testing releaseReadIOBuffer method with null buffer
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    // Testing releaseWriteEncodingBuffer method with null buffer
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferNullBuffer() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    // Testing releaseBase64Buffer method with null buffer
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferNullBuffer() {
        ioContext.releaseBase64Buffer(null);
    }

    // Testing releaseTokenBuffer method with null buffer
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferNullBuffer() {
        ioContext.releaseTokenBuffer(null);
    }

    // Testing releaseConcatBuffer method with null buffer
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferNullBuffer() {
        ioContext.releaseConcatBuffer(null);
    }

    // Testing releaseNameCopyBuffer method with null buffer
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferNullBuffer() {
        ioContext.releaseNameCopyBuffer(null);
    }

    
}