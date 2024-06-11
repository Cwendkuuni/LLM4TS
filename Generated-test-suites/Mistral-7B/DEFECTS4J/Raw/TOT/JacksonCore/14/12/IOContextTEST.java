package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

    private IOContext ioContext;
    private BufferRecycler bufferRecycler;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, this, true);
    }

    @After
    public void tearDown() {
        if (ioContext != null) {
            ioContext.releaseAllBuffers();
            ioContext = null;
        }
        bufferRecycler = null;
    }

    @Test
    public void testInitialization() {
        assertNotNull(ioContext);
        assertEquals(this, ioContext.getSourceReference());
        assertTrue(ioContext.isResourceManaged());
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext context = ioContext.withEncoding(encoding);
        assertEquals(encoding, context.getEncoding());
        assertNotSame(ioContext, context);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readBuffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readBuffer.length);
        ioContext.releaseReadIOBuffer(readBuffer);
    }

    @Test
    public void testAllocReadIOBufferMinSize() {
        byte[] readBuffer = ioContext.allocReadIOBuffer(10);
        assertNotNull(readBuffer);
        assertEquals(10, readBuffer.length);
        ioContext.releaseReadIOBuffer(readBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        byte[] readBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeBuffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferMinSize() {
        byte[] writeBuffer = ioContext.allocWriteEncodingBuffer(10);
        assertNotNull(writeBuffer);
        assertEquals(10, writeBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        byte[] writeBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer();
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
        ioContext.releaseBase64Buffer(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferMinSize() {
        char[] tokenBuffer = ioContext.allocTokenBuffer(10);
        assertNotNull(tokenBuffer);
        assertEquals(10, tokenBuffer.length);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer.length);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        assertEquals(10, nameCopyBuffer.length);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseReadIOBufferNull() {
        ioContext.releaseReadIOBuffer(null);
    }

    @Test
    public void testReleaseWriteEncodingBufferNull() {
        ioContext.releaseWriteEncodingBuffer(null);
    }

    @Test
    public void testReleaseBase64BufferNull() {
        ioContext.releaseBase64Buffer(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testReleaseTokenBufferNull() {
        ioContext.releaseTokenBuffer(null);
    }

    @Test
    public void testReleaseConcatBufferNull() {
        ioContext.releaseConcatBuffer(null);
    }

    @Test
    public void testReleaseNameCopyBufferNull() {
        ioContext.releaseNameCopyBuffer(null);
    }

    @Test
    public void testReleaseAllBuffers() {
        byte[] readBuffer = ioContext.allocReadIOBuffer();
        byte[] writeBuffer = ioContext.allocWriteEncodingBuffer();
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        char[] concatBuffer = ioContext.allocConcatBuffer();
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
        ioContext.releaseAllBuffers();
        assertNull(readBuffer);
        assertNull(writeBuffer);
        assertNull(base64Buffer);
        assertNull(tokenBuffer);
        assertNull(concatBuffer);
        assertNull(nameCopyBuffer);
    }

    @Test
    public void testJsonFactoryUsage() throws IOException {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(new byte[]{}));
        ioContext = parser.getCodec().getIOContext();
        ioContext.setEncoding(JsonEncoding.UTF8);
        byte[] writeBuffer = ioContext.allocWriteEncodingBuffer();
        parser.writeValueAsBytes(writeBuffer);
        assertArrayEquals(new byte[]{}, writeBuffer);
        parser.close();
        factory.close();
        ioContext.releaseWriteEncodingBuffer(writeBuffer);
    }
}