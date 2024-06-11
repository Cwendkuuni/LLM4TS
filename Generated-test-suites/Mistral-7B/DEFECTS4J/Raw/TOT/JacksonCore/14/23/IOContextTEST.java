package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

 private BufferRecycler _bufferRecycler;
 private IOContext _context;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 _bufferRecycler = new BufferRecycler();
 _context = new IOContext(_bufferRecycler, "testSource", true);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readBuffer = _context.allocReadIOBuffer();
 assertNotNull(readBuffer);
 _context.releaseReadIOBuffer(readBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readBuffer = _context.allocReadIOBuffer();
 _context.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeBuffer = _context.allocWriteEncodingBuffer();
 assertNotNull(writeBuffer);
 _context.releaseWriteEncodingBuffer(writeBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 byte[] writeBuffer = _context.allocWriteEncodingBuffer();
 _context.allocWriteEncodingBuffer();
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context.setEncoding(encoding);
 assertEquals(encoding, _context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context = _context.withEncoding(encoding);
 assertEquals(encoding, _context.getEncoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetEncodingNull() {
 _context.setEncoding(null);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 _context.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 _context.allocTokenBuffer();
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = _context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 _context.releaseConcatBuffer(concatBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocConcatBufferTwice() {
 char[] concatBuffer = _context.allocConcatBuffer();
 _context.allocConcatBuffer();
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 _context.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocNameCopyBufferNegativeSize() {
 _context.allocNameCopyBuffer(-1);
 }

 @Test
 public void testAllocNameCopyBufferZeroSize() {
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(0);
 assertNotNull(nameCopyBuffer);
 _context.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBufferWithNull() {
 byte[] writeBuffer = null;
 _context.releaseWriteEncodingBuffer(writeBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithNullAndOwned() {
 byte[] writeBuffer = _context.allocWriteEncodingBuffer();
 _context.releaseWriteEncodingBuffer(null);
 }

}