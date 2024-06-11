package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    private BufferRecycler bufferRecycler = new BufferRecycler();
    private Object sourceRef = new Object();
    private boolean managedResource = true;
    private IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

    @Test
    public void testSetEncoding() {
        assertNull(ioContext.getEncoding());

        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        assertNull(ioContext.getEncoding());

        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertNotSame(ioContext, newContext);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertEquals(managedResource, ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
        assertSame(bufferRecycler, textBuffer.getBufferRecycler());
    }

    @Test
    public void testAllocReadIOBuffer() {
        assertNull(ioContext.allocReadIOBuffer());

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertSame(readIOBuffer, ioContext.allocReadIOBuffer());
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 100;
        assertNull(ioContext.allocReadIOBuffer(minSize));

        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertSame(readIOBuffer, ioContext.allocReadIOBuffer(minSize));
        assertTrue(readIOBuffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        assertNull(ioContext.allocWriteEncodingBuffer());

        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertSame(writeEncodingBuffer, ioContext.allocWriteEncodingBuffer());
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 100;
        assertNull(ioContext.allocWriteEncodingBuffer(minSize));

        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertSame(writeEncodingBuffer, ioContext.allocWriteEncodingBuffer(minSize));
        assertTrue(writeEncodingBuffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        assertNull(ioContext.allocBase64Buffer());

        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertSame(base64Buffer, ioContext.allocBase64Buffer());
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        assertNull(ioContext.allocTokenBuffer());

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertSame(tokenBuffer, ioContext.allocTokenBuffer());
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 100;
        assertNull(ioContext.allocTokenBuffer(minSize));

        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        assertSame(tokenBuffer, ioContext.allocTokenBuffer(minSize));
        assertTrue(tokenBuffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        assertNull(ioContext.allocConcatBuffer());

        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertSame(concatBuffer, ioContext.allocConcatBuffer());
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        assertNull(ioContext.allocNameCopyBuffer(10));

        int minSize = 100;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(nameCopyBuffer);
        assertSame(nameCopyBuffer, ioContext.allocNameCopyBuffer(minSize));
        assertTrue(nameCopyBuffer.length >= minSize);
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAlloc() {
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithWrongBuffer() {
        byte[] buf = new byte[BufferRecycler.BYTE_READ_IO_BUFFER - 1];
        ioContext.releaseReadIOBuffer(buf);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buf);
        assertNull(ioContext.allocReadIOBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithWrongBuffer() {
        byte[] buf = new byte[BufferRecycler.BYTE_WRITE_ENCODING_BUFFER - 1];
        ioContext.releaseWriteEncodingBuffer(buf);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buf);
        assertNull(ioContext.allocWriteEncodingBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithWrongBuffer() {
        byte[] buf = new byte[BufferRecycler.BYTE_BASE64_CODEC_BUFFER - 1];
        ioContext.releaseBase64Buffer(buf);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buf);
        assertNull(ioContext.allocBase64Buffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithWrongBuffer() {
        char[] buf = new char[BufferRecycler.CHAR_TOKEN_BUFFER - 1];
        ioContext.releaseTokenBuffer(buf);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buf);
        assertNull(ioContext.allocTokenBuffer());
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buf);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buf = ioContext.allocNameCopyBuffer(10);
        ioContext.releaseNameCopyBuffer(buf);
    }

    @Test
    public void testCompleteCoverage() {
        // Invoke all methods for complete coverage
        IOContext context = new IOContext(bufferRecycler, sourceRef, false);
        context.setEncoding(JsonEncoding.UTF8);

        IOContext newContext = context.withEncoding(JsonEncoding.UTF8);
        assertNotSame(context, newContext);

        Object sourceReference = context.getSourceReference();
        assertNotNull(sourceReference);

        boolean resourceManaged = context.isResourceManaged();
        assertFalse(resourceManaged);

        TextBuffer textBuffer = context.constructTextBuffer();
        assertNotNull(textBuffer);

        byte[] readIOBuffer = context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        context.releaseReadIOBuffer(readIOBuffer);

        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        context.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] base64Buffer = context.allocBase64Buffer();
        assertNotNull(base64Buffer);
        context.releaseBase64Buffer(base64Buffer);

        char[] tokenBuffer = context.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        context.releaseTokenBuffer(tokenBuffer);

        char[] concatBuffer = context.allocConcatBuffer();
        assertNotNull(concatBuffer);
        context.releaseConcatBuffer(concatBuffer);

        char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        context.releaseNameCopyBuffer(nameCopyBuffer);
    }

}