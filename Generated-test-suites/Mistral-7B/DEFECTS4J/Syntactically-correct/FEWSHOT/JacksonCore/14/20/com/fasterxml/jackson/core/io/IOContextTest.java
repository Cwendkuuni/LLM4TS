package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context = null;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextCreation() {
 context = new IOContext(bufferRecycler, null, false);
 assertNull(context.getEncoding());
 assertFalse(context.isResourceManaged());
 }

 @Test
 public void testIOContextWithEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 context.setEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 context = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, context.getSourceReference());
 }

 @Test
 public void testIsResourceManaged() {
 context = new IOContext(bufferRecycler, null, true);
 assertTrue(context.isResourceManaged());
 }

 @Test
 public void testAllocReadIOBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer1 = context.allocReadIOBuffer();
 byte[] readIOBuffer2 = context.allocReadIOBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readIOBuffer);
 assertNull(context.getReadIOBuffer());
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer1 = context.allocWriteEncodingBuffer();
 byte[] writeEncodingBuffer2 = context.allocWriteEncodingBuffer();
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(context.getWriteEncodingBuffer());
 }

 @Test
 public void testAllocBase64Buffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer1 = context.allocTokenBuffer();
 char[] tokenBuffer2 = context.allocTokenBuffer();
 }

 @Test
 public void testReleaseTokenBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(tokenBuffer);
 assertNull(context.getTokenBuffer());
 }

 @Test
 public void testAllocConcatBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] concatBuffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(concatBuffer);
 assertNull(context.getConcatBuffer());
 }

 @Test
 public void testAllocNameCopyBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertEquals(10, nameCopyBuffer.length);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context.getNameCopyBuffer());
 }

 @Test
 public void testBufferRecycling() {
 char[] tokenBuffer = context.allocTokenBuffer();
 // Use tokenBuffer here...
 context.releaseTokenBuffer(tokenBuffer);
 char[] newTokenBuffer = context.allocTokenBuffer();
 assertSame(tokenBuffer, newTokenBuffer);
 }

 @Test
 public void testBufferRecyclingWithSameSize() {
 char[] tokenBuffer = context.allocTokenBuffer(10);
 context.releaseTokenBuffer(tokenBuffer);
 char[] newTokenBuffer = context.allocTokenBuffer(10);
 assertSame(tokenBuffer, newTokenBuffer);
 }

 @Test
 public void testBufferRecyclingWithDifferentSize() {
 char[] tokenBuffer = context.allocTokenBuffer(10);
 context.releaseTokenBuffer(tokenBuffer);
 char[] newTokenBuffer = context.allocTokenBuffer(5);
 assertNotSame(tokenBuffer, newTokenBuffer);
 }

 @Test
 public void testBufferRecyclingWithSameBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer(10);
 context.releaseTokenBuffer(tokenBuffer);
 char[] newTokenBuffer = tokenBuffer;
 context.allocTokenBuffer(); // Call this to reset the buffer to default state
 assertNotSame(tokenBuffer, newTokenBuffer);
 }

 @Test
 public void testBufferRecyclingWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 // Use writeEncodingBuffer here...
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 byte[] newWriteEncodingBuffer = context.allocWriteEncodingBuffer();
 assertSame(writeEncodingBuffer, newWriteEncodingBuffer);
 }

 @Test
 public void testBufferRecyclingReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 // Use readIOBuffer here...
 context.releaseReadIOBuffer(readIOBuffer);
 byte[] newReadIOBuffer = context.allocReadIOBuffer();
 assertSame(readIOBuffer, newReadIOBuffer);
 }

}