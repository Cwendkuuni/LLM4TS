package com.fasterxml.jackson.core.io;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IOContextTest {

    private IOContext ioContext;

    @Mock
    private BufferRecycler bufferRecycler;

    @Mock
    private Object sourceRef;

    private boolean managedResource;

    @Before
    public void setUp() {
        managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);
        assertEquals(enc, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding enc = JsonEncoding.UTF16_BE;
        IOContext newContext = ioContext.withEncoding(enc);
        assertEquals(enc, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertEquals(null, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertEquals(true, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertEquals(TextBuffer.class, textBuffer.getClass());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertEquals(byte[].class, buffer.getClass());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertEquals(byte[].class, buffer.getClass());
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertEquals(byte[].class, buffer.getClass());
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertEquals(byte[].class, buffer.getClass());
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertEquals(byte[].class, buffer.getClass());
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertEquals(char[].class, buffer.getClass());
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertEquals(char[].class, buffer.getClass());
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertEquals(char[].class, buffer.getClass());
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertEquals(char[].class, buffer.getClass());
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = new byte[1024];
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = new byte[1024];
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = new byte[1024];
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = new char[1024];
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = new char[1024];
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = new char[1024];
        ioContext.releaseNameCopyBuffer(buffer);
    }
}