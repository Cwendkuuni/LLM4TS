package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    private BufferRecycler bufferRecycler;
    private IOContext ioContext;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, null, false);
    }

    @After
    public void tearDown() {
        ioContext.releaseAllBuffers();
        bufferRecycler.clear();
    }

    @Test
    public void testIOContextConfiguration() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContextWithEncoding.getEncoding());
    }

    @Test
    public void testIOContextBufferManagement() throws JsonProcessingException {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertTrue(readIOBuffer != null);
        int length = 100;
        ioContext.readFrom(readIOBuffer, 0, length);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertTrue(tokenBuffer != null);
        ioContext.readFrom(tokenBuffer, 0, length);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(length);
        assertTrue(nameCopyBuffer != null);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertTrue(writeEncodingBuffer != null);
        ioContext.writeTo(writeEncodingBuffer, 0, length);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertTrue(base64Buffer != null);
        ioContext.releaseBase64Buffer(base64Buffer);

        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertTrue(concatBuffer != null);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIOContextReleaseWrongBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseBase64Buffer(readIOBuffer);
    }

    @Test
    public void testIOContextIsResourceManaged() {
        boolean managedResource = true;
        IOContext ioContextManaged = new IOContext(bufferRecycler, null, managedResource);
        assertTrue(ioContextManaged.isResourceManaged());

        boolean managedResourceFalse = false;
        IOContext ioContextNotManaged = new IOContext(bufferRecycler, null, managedResourceFalse);
        assertFalse(ioContextNotManaged.isResourceManaged());
    }
}