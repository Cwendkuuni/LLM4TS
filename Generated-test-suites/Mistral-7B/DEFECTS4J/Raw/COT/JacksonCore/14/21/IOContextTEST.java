package com.fasterxml.jackson.core.io; // Import necessary packages

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

    

    @Test
    public void testIOContextConstructor() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertNotNull(ioContext);
    }

    @Test
    public void testSetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContextWithEncoding.getEncoding());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetEncodingTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        ioContext.setEncoding(JsonEncoding.UTF8);
    }

    @Test
    public void testGetSourceReference() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        Object sourceReference = ioContext.getSourceReference();
        assertNotNull(sourceReference);
    }

    @Test
    public void testGetEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        JsonEncoding returnedEncoding = ioContext.getEncoding();
        assertEquals(encoding, returnedEncoding);
    }

    @Test
    public void testIsResourceManaged() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocTokenBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocConcatBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        char[] concatBuffer2 = ioContext.allocConcatBuffer();
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocNameCopyBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        char[] nameCopyBuffer2 = ioContext.allocNameCopyBuffer(5);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    
}