package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext _context;

    @Before
    public void setUp() {
        _context = new IOContext(new BufferRecycler(), new Object(), true);
    }

    @Test
    public void testGetSourceReference() {
        assertNotNull(_context.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(_context.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(_context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = _context.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = _context.allocBase64Buffer();
        assertNotNull(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = _context.allocTokenBuffer();
        assertNotNull(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = _context.allocConcatBuffer();
        assertNotNull(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        assertEquals(10, nameCopyBuffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        _context.releaseReadIOBuffer(readIOBuffer);
        assertNull(_context.getReadIOBuffer());
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
        _context.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(_context.getWriteEncodingBuffer());
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = _context.allocBase64Buffer();
        _context.releaseBase64Buffer(base64Buffer);
        assertNull(_context.getBase64Buffer());
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = _context.allocTokenBuffer();
        _context.releaseTokenBuffer(tokenBuffer);
        assertNull(_context.getTokenBuffer());
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = _context.allocConcatBuffer();
        _context.releaseConcatBuffer(concatBuffer);
        assertNull(_context.getConcatBuffer());
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
        _context.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(_context.getNameCopyBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongBufferRelease() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        _context.releaseWriteEncodingBuffer(readIOBuffer);
    }
}