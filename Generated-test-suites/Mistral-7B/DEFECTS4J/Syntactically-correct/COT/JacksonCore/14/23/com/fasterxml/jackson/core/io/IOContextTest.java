package com.fasterxml.jackson.core.io; // package import for testing class

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
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncodingMethod() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
        Assert.assertEquals(encoding, ioContextWithEncoding.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, false);
        Assert.assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext ioContext = new IOContext(bufferRecycler, null, false).withEncoding(encoding);
        Assert.assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, true);
        Assert.assertTrue(ioContext.isResourceManaged());

        IOContext ioContext2 = new IOContext(bufferRecycler, null, false);
        Assert.assertFalse(ioContext2.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(readIOBuffer);
        Assert.assertEquals(minSize, readIOBuffer.length);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(writeEncodingBuffer);
        Assert.assertEquals(minSize, writeEncodingBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        Assert.assertNotNull(tokenBuffer);
        Assert.assertEquals(minSize, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        int minSize = 10;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        Assert.assertNotNull(nameCopyBuffer);
        Assert.assertEquals(minSize, nameCopyBuffer.length);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, null, false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }

    
}