package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, this, true);
 }

 @Test
 public void testIOContextConstruction() {
 assertSame(bufferRecycler, ioContext._bufferRecycler);
 assertSame(this, ioContext._sourceRef);
 assertTrue(ioContext._managedResource);
 assertNull(ioContext._encoding);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext._encoding);
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext = ioContext.withEncoding(encoding);
 assertSame(encoding, ioContext._encoding);
 }

 @Test
 public void testGetSourceReference() {
 assertSame(this, ioContext._sourceRef);
 }

 @Test
 public void testGetEncoding() {
 assertNull(ioContext._encoding);
 }

 @Test
 public void testIsResourceManaged() {
 assertTrue(ioContext._managedResource);
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
 assertNotNull(readIOBuffer[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext._readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongReadIOBuffer() {
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseReadIOBuffer(wrongBuffer);
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
 assertNotNull(writeEncodingBuffer[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongWriteEncodingBuffer() {
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseWriteEncodingBuffer(wrongBuffer);
 }

 @Test
 public void testConstructByteBuffer() {
 byte[] byteBuffer = bufferRecycler.allocByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER);
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 textBuffer.writeByteBuffer(byteBuffer);
 byte[] result = textBuffer.getTextBytes();
 assertArrayEquals(byteBuffer, result);
 bufferRecycler.releaseByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, byteBuffer);
 }

 @After
 public void tearDown() {
 bufferRecycler.clear();
 }
}