package com.fasterxml.jackson.core.io; // import statements go here

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;
    private Object sourceRef;
    private JsonEncoding encoding;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        ioContext = new IOContext(bufferRecycler, sourceRef, true);
        encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
    }

    

    @Test
    public void testIOContextConstructor() {
        assertNotNull(ioContext);
        assertEquals(sourceRef, ioContext.getSourceReference());
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding newEncoding = JsonEncoding.UTF16;
        ioContext.setEncoding(newEncoding);
        assertEquals(newEncoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding newEncoding = JsonEncoding.UTF16;
        IOContext ioContextWithEncoding = ioContext.withEncoding(newEncoding);
        assertEquals(newEncoding, ioContextWithEncoding.getEncoding());
        assertEquals(ioContext, ioContextWithEncoding); // tests that it returns self
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAlloc_byteArray_twice() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer(); // should throw an exception
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBuffer_minSize() {
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer_minSize() {
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertEquals(minSize, writeEncodingBuffer.length);
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

    @Test
    public void testAllocTokenBuffer_minSize() {
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertEquals(minSize, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBuffer_releaseWrongBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseWriteEncodingBuffer(readIOBuffer); // should throw an exception
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    
}