package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler = Mockito.mock(BufferRecycler.class);

    @Before
    public void setUp() {
        ioContext = new IOContext(bufferRecycler, this, true);
    }

    

    // Test for the constructor
    @Test
    public void testIOContextConstruction() {
        assertNotNull(ioContext);
        assertEquals(bufferRecycler, ioContext._bufferRecycler);
        assertTrue(ioContext._managedResource);
    }

    // Test for setEncoding and withEncoding methods
    @Test
    public void testSetEncodingAndWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());

        ioContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    // Test for getSourceReference method
    @Test
    public void testGetSourceReference() {
        assertNull(ioContext.getSourceReference());
    }

    // Test for getEncoding method
    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    // Test for isResourceManaged method
    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    // Test for constructTextBuffer method
    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    // Test for allocReadIOBuffer method
    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    // Test for allocReadIOBuffer with minimum size
    @Test
    public void testAllocReadIOBufferMinSize() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    // Test for allocWriteEncodingBuffer method
    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    // Test for allocWriteEncodingBuffer with minimum size
    @Test
    public void testAllocWriteEncodingBufferMinSize() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    // Test for allocBase64Buffer method
    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    // Test for allocTokenBuffer method
    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    // Test for allocTokenBuffer with minimum size
    @Test
    public void testAllocTokenBufferMinSize() {
        char[] tokenBuffer = ioContext.allocTokenBuffer(10);
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    // Test for allocConcatBuffer method
    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    // Test for allocNameCopyBuffer method
    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    // Test for releaseReadIOBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    // Test for releaseWriteEncodingBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithNullBuffer() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    // Test for releaseBase64Buffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithNullBuffer() {
        ioContext.releaseBase64Buffer(null);
    }

    // Test for releaseTokenBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithNullBuffer() {
        ioContext.releaseTokenBuffer(null);
    }

    // Test for releaseConcatBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithNullBuffer() {
        ioContext.releaseConcatBuffer(null);
    }

    // Test for releaseNameCopyBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithNullBuffer() {
        ioContext.releaseNameCopyBuffer(null);
    }

    
}