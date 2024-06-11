package com.fasterxml.jackson.core.io; // adjust the package name if required

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, null, false);
    }

    

    // Test constructor
    @Test
    public void testIOContextConstructor() {
        assertNotNull(ioContext);
    }

    // Test getters

    @Test
    public void testGetSourceReference() {
        assertNull(ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertFalse(ioContext.isResourceManaged());
    }

    // Test buffer management methods

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer();
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocTokenBufferTwice() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.allocTokenBuffer();
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testReleaseOwnedBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseNullWritableBuffer() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseNullReadableBuffer() {
        ioContext.releaseBase64Buffer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseNullTokenBuffer() {
        ioContext.releaseTokenBuffer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseNullConcatBuffer() {
        ioContext.releaseConcatBuffer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseNullNameCopyBuffer() {
        ioContext.releaseNameCopyBuffer(null);
    }

    // Test internal helpers

    @Test(expected = IllegalStateException.class)
    public void testVerifyAllocTwice() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testVerifyReleaseSameBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseDifferentBuffer() {
        byte[] readIOBuffer1 = ioContext.allocReadIOBuffer();
        byte[] readIOBuffer2 = new byte[10];
        ioContext.releaseReadIOBuffer(readIOBuffer1);
        ioContext.releaseReadIOBuffer(readIOBuffer2);
    }

    
}