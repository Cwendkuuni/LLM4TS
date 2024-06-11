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
 ioContext = new IOContext(bufferRecycler, new Object(), true);
 }

 @After
 public void tearDown() {
 if (ioContext != null) {
 ioContext.releaseAllBuffers();
 bufferRecycler.releaseAllBuffers();
 }
 }

 @Test
 public void testIOContextConstruction() {
 Object sourceRef = new Object();
 IOContext ioContext1 = new IOContext(bufferRecycler, sourceRef, true);
 IOContext ioContext2 = new IOContext(bufferRecycler, sourceRef, false);

 assertNotSame(ioContext1, ioContext2);
 assertSame(sourceRef, ioContext1.getSourceReference());
 assertSame(sourceRef, ioContext2.getSourceReference());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext = ioContext.withEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotSame(readIOBuffer, ioContext.allocReadIOBuffer());
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertArrayEquals(new byte[10], readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotSame(readIOBuffer, ioContext.allocReadIOBuffer());
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotSame(writeEncodingBuffer, ioContext.allocWriteEncodingBuffer());
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertArrayEquals(new byte[10], writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotSame(writeEncodingBuffer, ioContext.allocWriteEncodingBuffer());
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 base64Buffer = ioContext.allocBase64Buffer();
 assertNotSame(base64Buffer, ioContext.allocBase64Buffer());
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 tokenBuffer = ioContext.allocTokenBuffer();
 assertNotSame(tokenBuffer, ioContext.allocTokenBuffer());
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[10], tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 tokenBuffer = ioContext.allocTokenBuffer();
 assertNotSame(tokenBuffer, ioContext.allocTokenBuffer());
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 concatBuffer = ioContext.allocConcatBuffer();
 assertNotSame(concatBuffer, ioContext.allocConcatBuffer());
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertArrayEquals(new char[10], nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 nameCopyBuffer = ioContext.allocNameCopyBuffer();
 assertNotSame(nameCopyBuffer, ioContext.allocNameCopyBuffer());
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithNullBuffer() {
 ioContext.releaseReadIOBuffer(null);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithNullBuffer() {
 ioContext.releaseWriteEncodingBuffer(null);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext.getBase64Buffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64BufferWithNullBuffer() {
 ioContext.releaseBase64Buffer(null);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWithNullBuffer() {
 ioContext.releaseTokenBuffer(null);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBufferWithNullBuffer() {
 ioContext.releaseConcatBuffer(null);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBufferWithNullBuffer() {
 ioContext.releaseNameCopyBuffer(null);
 }

}