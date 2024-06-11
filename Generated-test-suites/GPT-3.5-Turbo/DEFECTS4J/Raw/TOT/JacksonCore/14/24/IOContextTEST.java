package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;
    private Object sourceRef;
    private boolean managedResource;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
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
    }

    @Test
    public void testGetSourceReference() {
        Assert.assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        Assert.assertEquals(managedResource, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 100;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 100;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 100;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 100;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(100);
        ioContext.releaseNameCopyBuffer(buffer);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAlloc() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext._verifyAlloc(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseWithIncorrectBuffer() {
        byte[] buffer = new byte[10];
        ioContext._verifyRelease(buffer, ioContext._base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseWithShrinkingBuffer() {
        byte[] buffer = new byte[10];
        byte[] src = new byte[20];
        ioContext._verifyRelease(buffer, src);
    }

    @Test
    public void testVerifyRelease() {
        byte[] buffer = new byte[20];
        byte[] src = new byte[10];
        ioContext._verifyRelease(buffer, src);
    }
}