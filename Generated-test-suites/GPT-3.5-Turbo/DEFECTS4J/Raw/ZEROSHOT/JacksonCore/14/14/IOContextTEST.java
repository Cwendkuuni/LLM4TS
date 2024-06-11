package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    private IOContext ioContext;
    private Object sourceRef;
    private boolean managedResource;
    private BufferRecycler bufferRecycler;

    @Before
    public void setUp() throws Exception {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @After
    public void tearDown() throws Exception {
        ioContext = null;
        sourceRef = null;
        bufferRecycler = null;
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);
        Assert.assertEquals(enc, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding enc = JsonEncoding.UTF16_BE;
        IOContext context = ioContext.withEncoding(enc);
        Assert.assertEquals(enc, context.getEncoding());
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
        byte[] buf = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] buf = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        byte[] buf = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        char[] buf = ioContext.allocTokenBuffer(minSize);
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buf = ioContext.allocNameCopyBuffer(minSize);
        Assert.assertNotNull(buf);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buf);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buf);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buf);
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buf);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buf);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buf = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buf);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }
}