package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    // Mocked objects
    private BufferRecycler bufferRecycler;
    private Object sourceRef;
    private IOContext ioContext;

    @Before
    public void setup() {
        bufferRecycler = mock(BufferRecycler.class);
        sourceRef = new Object() { /* empty */ };
        ioContext = new IOContext(bufferRecycler, sourceRef, false);
    }

    

    // Test constructor
    @Test
    public void testIOContextConstruction() {
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
    }

    // Test setEncoding and isResourceManaged methods
    @Test
    public void testSettingEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertSame(encoding, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertFalse(ioContext.isResourceManaged());
    }

    // Test getSourceReference, getEncoding and isResourceManaged accessors
    @Test
    public void testGetters() {
        assertSame(sourceRef, ioContext.getSourceReference());
        assertSame(ioContext.getEncoding(), ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
    }

    // Test constructTextBuffer method
    @Test
    public void testConstructTextBuffer() throws IOException {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    // Test allocReadIOBuffer method
    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        assertNotSame(readIOBuffer, readIOBuffer2);

        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    // Test allocReadIOBuffer with minSize
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readIOBuffer);
        assertArrayEquals(new byte[10], readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer(5);
        assertNotSame(readIOBuffer, readIOBuffer2);
        assertArrayEquals(new byte[5], readIOBuffer2);

        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    // Test allocWriteEncodingBuffer method
    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer2);

        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    // Test allocWriteEncodingBuffer with minSize
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
        assertNotNull(writeEncodingBuffer);
        assertArrayEquals(new byte[10], writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer(5);
        assertNotSame(writeEncodingBuffer, writeEncodingBuffer2);
        assertArrayEquals(new byte[5], writeEncodingBuffer2);

        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    // Test allocBase64Buffer method
    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);

        byte[] base64Buffer2 = ioContext.allocBase64Buffer();
        assertNotSame(base64Buffer, base64Buffer2);

        ioContext.releaseBase64Buffer(base64Buffer);
    }

    // Test allocTokenBuffer method
    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
        assertNotSame(tokenBuffer, tokenBuffer2);

        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    // Test allocTokenBuffer with minSize
    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] tokenBuffer = ioContext.allocTokenBuffer(10);
        assertNotNull(tokenBuffer);
        assertArrayEquals(new char[10], tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer(5);
        assertNotSame(tokenBuffer, tokenBuffer2);
        assertArrayEquals(new char[5], tokenBuffer2);

        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    // Test allocConcatBuffer method
    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);

        char[] concatBuffer2 = ioContext.allocConcatBuffer();
        assertNotSame(concatBuffer, concatBuffer2);

        ioContext.releaseConcatBuffer(concatBuffer);
    }

    // Test allocNameCopyBuffer method
    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        assertArrayEquals(new char[10], nameCopyBuffer);

        char[] nameCopyBuffer2 = ioContext.allocNameCopyBuffer(5);
        assertNotSame(nameCopyBuffer, nameCopyBuffer2);
        assertArrayEquals(new char[5], nameCopyBuffer2);

        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    // Test releaseReadIOBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithNullBuffer() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    // Test releaseWriteEncodingBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithNullBuffer() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext.getWriteEncodingBuffer());
    }

    // Test releaseBase64Buffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithNullBuffer() {
        ioContext.releaseBase64Buffer(null);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext.getBase64Buffer());
    }

    // Test releaseTokenBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithNullBuffer() {
        ioContext.releaseTokenBuffer(null);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext.getTokenBuffer());
    }

    // Test releaseConcatBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithNullBuffer() {
        ioContext.releaseConcatBuffer(null);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext.getConcatBuffer());
    }

    // Test releaseNameCopyBuffer method
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithNullBuffer() {
        ioContext.releaseNameCopyBuffer(null);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext.getNameCopyBuffer());
    }

    
}