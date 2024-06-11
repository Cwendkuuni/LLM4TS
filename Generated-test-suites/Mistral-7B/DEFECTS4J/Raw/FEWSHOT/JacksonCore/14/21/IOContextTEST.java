package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

 private BufferRecycler _bufferRecycler;
 private IOContext _context;

 @Before
 public void setUp() {
 _bufferRecycler = mock(BufferRecycler.class);
 _context = new IOContext(_bufferRecycler, new Object(), true);
 }

 @After
 public void tearDown() {
 _context = null;
 _bufferRecycler = null;
 }

 @Test
 public void testGetSourceReference() {
 assertSame("sourceRef should be the same", new Object(), _context.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 assertNull("encoding should be null initially", _context.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 assertTrue("managedResource should be true", _context.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = _context.constructTextBuffer();
 assertNotNull("textBuffer should not be null", textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = _context.allocReadIOBuffer();
 assertNotNull("readIOBuffer should not be null", readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = _context.allocReadIOBuffer(10);
 assertNotNull("readIOBuffer should not be null", readIOBuffer);
 assertArrayEquals("readIOBuffer size should be 10", new byte[10], readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = _context.allocReadIOBuffer();
 byte[] readIOBuffer2 = _context.allocReadIOBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = _context.allocReadIOBuffer();
 _context.releaseReadIOBuffer(readIOBuffer);
 assertNull("readIOBuffer should be null after release", readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBuffer() {
 byte[] wrongBuffer = new byte[10];
 _context.releaseReadIOBuffer(wrongBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
 assertNotNull("writeEncodingBuffer should not be null", writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer(10);
 assertNotNull("writeEncodingBuffer should not be null", writeEncodingBuffer);
 assertArrayEquals("writeEncodingBuffer size should be 10", new byte[10], writeEncodingBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
 _context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull("writeEncodingBuffer should be null after release", writeEncodingBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferWriteEncoding() {
 byte[] wrongBuffer = new byte[10];
 _context.releaseWriteEncodingBuffer(wrongBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = _context.allocBase64Buffer();
 assertNotNull("base64Buffer should not be null", base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 assertNotNull("tokenBuffer should not be null", tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = _context.allocTokenBuffer(10);
 assertNotNull("tokenBuffer should not be null", tokenBuffer);
 assertArrayEquals("tokenBuffer size should be 10", new char[10], tokenBuffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 _context.releaseTokenBuffer(tokenBuffer);
 assertNull("tokenBuffer should be null after release", tokenBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferToken() {
 char[] wrongBuffer = new char[10];
 _context.releaseTokenBuffer(wrongBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = _context.allocConcatBuffer();
 assertNotNull("concatBuffer should not be null", concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
 assertNotNull("nameCopyBuffer should not be null", nameCopyBuffer);
 assertArrayEquals("nameCopyBuffer size should be 10", new char[10], nameCopyBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = _context.allocConcatBuffer();
 _context.releaseConcatBuffer(concatBuffer);
 assertNull("concatBuffer should be null after release", concatBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferConcat() {
 char[] wrongBuffer = new char[10];
 _context.releaseConcatBuffer(wrongBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
 _context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull("nameCopyBuffer should be null after release", nameCopyBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferNameCopy() {
 char[] wrongBuffer = new char[10];
 _context.releaseNameCopyBuffer(wrongBuffer);
 }
}