package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

/**
 * Test class for IOContext
 */
public class IOContextTest {

 private BufferRecycler bufferRecycler;
 private IOContext ioContext;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, this, true);
 }

 @After
 public void tearDown() {
 ioContext = null;
 bufferRecycler = null;
 }

 @Test
 public void testIOContextConstruction() {
 // Test constructor
 IOContext ioContext1 = new IOContext(bufferRecycler, this, true);
 IOContext ioContext2 = new IOContext(bufferRecycler, this, true);

 // Test equals and hashCode
 assertSame(ioContext1, ioContext1);
 assertSame(ioContext1, ioContext1);
 assertNotSame(ioContext1, ioContext2);
 assertNotSame(ioContext1, new Object());
 }

 @Test
 public void testGetSourceReference() {
 assertSame(this, ioContext.getSourceReference());
 }

 @Test
 public void testSetAndGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 assertSame(true, ioContext.isResourceManaged());
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
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertArrayEquals(new byte[10], readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
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
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertArrayEquals(new byte[10], writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
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
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[10], tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 char[] tokenBuffer2 = ioContext.allocTokenBuffer();
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
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertArrayEquals(new char[10], nameCopyBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocNameCopyBufferTwice() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer();
 char[] nameCopyBuffer2 = ioContext.allocNameCopyBuffer();
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext._concatCBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext._nameCopyBuffer);
 }
}