package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
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
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF16_BE;
        IOContext withEncodingContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, withEncodingContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertEquals(managedResource, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(TextBuffer.class, textBuffer.getClass());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertEquals(byte[].class, readIOBuffer.getClass());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertEquals(byte[].class, readIOBuffer.getClass());
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(byte[].class, writeEncodingBuffer.getClass());
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 10;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertEquals(byte[].class, writeEncodingBuffer.getClass());
        assertEquals(minSize, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertEquals(byte[].class, base64Buffer.getClass());
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertEquals(char[].class, tokenBuffer.getClass());
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 10;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertEquals(char[].class, tokenBuffer.getClass());
        assertEquals(minSize, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertEquals(char[].class, concatBuffer.getClass());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 10;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertEquals(char[].class, nameCopyBuffer.getClass());
        assertEquals(minSize, nameCopyBuffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = new char[10];
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = new char[10];
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = new char[10];
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}