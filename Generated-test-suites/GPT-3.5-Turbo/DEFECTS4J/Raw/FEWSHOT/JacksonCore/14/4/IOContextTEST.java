package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

import org.junit.Test;

public class IOContextTest {
    private final BufferRecycler bufferRecycler = new BufferRecycler();

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(0, textBuffer.contentsAsArray().length);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertEquals(8192, readIOBuffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(2048);
        assertEquals(2048, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(8192, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(2048);
        assertEquals(2048, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertEquals(8192, base64Buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertEquals(4096, tokenBuffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        char[] tokenBuffer = ioContext.allocTokenBuffer(1024);
        assertEquals(1024, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertEquals(8192, concatBuffer.length);
    }

    @Test
    public void testAllocNameCopyBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(2048);
        assertEquals(2048, nameCopyBuffer.length);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertSame(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext withEncodingContext = ioContext.withEncoding(encoding);
        assertSame(encoding, withEncodingContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, false);
        assertSame(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), true);
        assertEquals(true, ioContext.isResourceManaged());
    }

    @Test(expected=IllegalStateException.class)
    public void testVerifyAlloc() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        ioContext.allocBase64Buffer();
        try {
            ioContext.allocBase64Buffer();
            fail("Should throw an exception");
        } catch (IllegalStateException ex) {
            assertEquals("Trying to call same allocXxx() method second time", ex.getMessage());
            throw ex;
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testVerifyReleaseForByteBuffers() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        try {
            ioContext.releaseReadIOBuffer(readIOBuffer);
            fail("Should throw an exception");
        } catch (IllegalArgumentException ex) {
            assertEquals("Trying to release buffer not owned by the context", ex.getMessage());
            throw ex;
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testVerifyReleaseForCharBuffers() {
        IOContext ioContext = new IOContext(bufferRecycler, new Object(), false);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        try {
            ioContext.releaseTokenBuffer(tokenBuffer);
            fail("Should throw an exception");
        } catch (IllegalArgumentException ex) {
            assertEquals("Trying to release buffer not owned by the context", ex.getMessage());
            throw ex;
        }
    }
}