package com.example.junit.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    IOContext ioContext;

    @Before
    public void setup() {
        ioContext = new IOContext(new BufferRecycler(), null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(ioContext);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer(); //should throw an exception
    }

    @Test
    public void testReleaseReadIOBufferDifferentBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseReadIOBuffer(buffer); //should throw an exception
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferDifferentBuffer() {
        byte[] buffer = new byte[10];
        ioContext.releaseWriteEncodingBuffer(buffer); //should throw an exception
    }

    @Test(expected = NullPointerException.class)
    public void testReleaseTokenBufferNull() {
        ioContext.releaseTokenBuffer(null);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocTokenBufferTwice() {
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.allocTokenBuffer(); //should throw an exception
    }
}