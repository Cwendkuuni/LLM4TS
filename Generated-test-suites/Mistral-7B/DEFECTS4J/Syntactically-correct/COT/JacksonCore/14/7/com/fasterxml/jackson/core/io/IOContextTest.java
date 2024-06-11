package com.fasterxml.jackson.core.io; // adjust the package name if needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {
    // A test double of BufferRecycler for test purposes
    static class TestBufferRecycler implements BufferRecycler {
        private byte[] byteBuffers = new byte[100][];
        private char[] charBuffers = new char[100][];

        @Override
        public byte[] allocByteBuffer(int size) {
            byte[] buffer = new byte[size];
            byteBuffers[byteBuffers.length] = buffer;
            return buffer;
        }

        @Override
        public byte[] allocByteBuffer(int size, int minSize) {
            byte[] buffer = new byte[size];
            byteBuffers[byteBuffers.length] = buffer;
            return buffer;
        }

        @Override
        public char[] allocCharBuffer(int size) {
            char[] buffer = new char[size];
            charBuffers[charBuffers.length] = buffer;
            return buffer;
        }

        @Override
        public char[] allocCharBuffer(int size, int minSize) {
            char[] buffer = new char[size];
            charBuffers[charBuffers.length] = buffer;
            return buffer;
        }

        @Override
        public void releaseByteBuffer(int type, byte[] buffer) {
            int index = findIndex(byteBuffers, buffer);
            if (index >= 0) {
                byteBuffers[index] = null;
            }
        }

        @Override
        public void releaseCharBuffer(int type, char[] buffer) {
            int index = findIndex(charBuffers, buffer);
            if (index >= 0) {
                charBuffers[index] = null;
            }
        }

        private int findIndex(Object[] buffers, Object buffer) {
            for (int i = 0; i < buffers.length; i++) {
                if (buffers[i] == buffer) {
                    return i;
                }
            }
            return -1;
        }
    }

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;

    @Before
    public void setUp() {
        bufferRecycler = new TestBufferRecycler();
        ioContext = new IOContext(bufferRecycler, new Object(), false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(ioContext);
        assertNotNull(ioContext._bufferRecycler);
        assertNotNull(ioContext._sourceRef);
        assertFalse(ioContext._managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertTrue(ioContext._encoding == encoding);
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext = ioContext.withEncoding(encoding);
        assertTrue(ioContext._encoding == encoding);
    }

    @Test
    public void testGetSourceReference() {
        assertNotNull(ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertTrue(ioContext.getEncoding() == encoding);
    }

    @Test
    public void testIsResourceManaged() {
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(128);
        assertNotNull(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer();
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(128);
        assertNotNull(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocTokenBufferTwice() {
        ioContext.allocTokenBuffer();
        ioContext.allocTokenBuffer();
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] tokenBuffer = ioContext.allocTokenBuffer(128);
        assertNotNull(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(128);
        assertNotNull(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(128);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test
    public void testReleaseBuffers() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        char[] tokenBuffer = ioContext.allocTokenBuffer();

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        assertNull(ioContext._readIOBuffer);
        assertNull(ioContext._writeEncodingBuffer);
        assertNull(ioContext._tokenCBuffer);
    }
}