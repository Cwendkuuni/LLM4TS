package com.fasterxml.jackson.core.io; // or the package where IOContext is located

import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testConstructor() {
        assertNotNull(ioContext);
        assertEquals(sourceRef, ioContext.getSourceReference());
        assertTrue(managedResource == ioContext.isResourceManaged());
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
        ioContext.withEncoding(JsonEncoding.UTF8); // same as setEncoding
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertSame(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());

        ioContext.allocReadIOBuffer(); // can be called multiple times
        byte[] readIOBuffer2 = ioContext.getReadIOBuffer();
        assertNotNull(readIOBuffer2);
        assertEquals(readIOBuffer, readIOBuffer2);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext.getWriteEncodingBuffer());

        ioContext.allocWriteEncodingBuffer(); // can be called multiple times
        byte[] writeEncodingBuffer2 = ioContext.getWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer2);
        assertEquals(writeEncodingBuffer, writeEncodingBuffer2);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext.getBase64Buffer());

        ioContext.allocBase64Buffer(); // can be called multiple times
        byte[] base64Buffer2 = ioContext.getBase64Buffer();
        assertNotNull(base64Buffer2);
        assertEquals(base64Buffer, base64Buffer2);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());

        ioContext.allocTokenBuffer(); // can be called multiple times
        char[] tokenBuffer2 = ioContext.getTokenBuffer();
        assertNotNull(tokenBuffer2);
        assertEquals(tokenBuffer, tokenBuffer2);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());

        ioContext.allocConcatBuffer(); // can be called multiple times
        char[] concatBuffer2 = ioContext.getConcatBuffer();
        assertNotNull(concatBuffer2);
        assertEquals(concatBuffer, concatBuffer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBufferNull() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext.getWriteEncodingBuffer());
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext.getBase64Buffer());
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext.getNameCopyBuffer());
    }
}