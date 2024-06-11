package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler = new BufferRecycler();

    @Before
    public void setUp() {
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
    }

    

    // Testing the constructor
    @Test
    public void testIOContextConstructor() {
        assertNotNull(ioContext);
    }

    // Testing getters

    @Test
    public void testGetSourceReference() {
        assertEquals("sourceRef", ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    // Testing setEncoding and withEncoding

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    // Testing buffer management methods

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
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

    @Test
    public void testReleaseBuffers() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();

        // Release readIOBuffer
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);

        // Release writeEncodingBuffer
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    // Testing edge cases and exceptions

    @Test(expected = IllegalStateException.class)
    public void testAllocBufferTwice() {
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBufferDifferentInstance() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBufferNull() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocBufferNegativeSize() {
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
        ioContext.allocTokenBuffer(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBufferNullBuffer() {
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
        ioContext.releaseTokenBuffer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBufferSmallerBuffer() {
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
        char[] tokenBuffer = ioContext.allocTokenBuffer(10);
        ioContext.releaseTokenBuffer(new char[5]);
    }

    
}