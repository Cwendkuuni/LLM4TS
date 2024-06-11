package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context = null;
 BufferRecycler bufferRecycler = new BufferRecycler();
 Object sourceRef = new Object();

 @Test
 public void testIOContext() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertEquals(sourceRef, context.getSourceReference());
 assertTrue(context.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.getEncoding());
 context.setEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.getEncoding());
 context = context.withEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocReadIOBuffer());
 byte[] buffer = context.allocReadIOBuffer();
 assertNotNull(buffer);
 context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testAllocReadIOBufferMinSize() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocReadIOBuffer(5));
 byte[] buffer = context.allocReadIOBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocWriteEncodingBuffer());
 byte[] buffer = context.allocWriteEncodingBuffer();
 assertNotNull(buffer);
 context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testAllocWriteEncodingBufferMinSize() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocWriteEncodingBuffer(5));
 byte[] buffer = context.allocWriteEncodingBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocBase64Buffer());
 byte[] buffer = context.allocBase64Buffer();
 assertNotNull(buffer);
 context.releaseBase64Buffer(buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocTokenBuffer());
 char[] buffer = context.allocTokenBuffer();
 assertNotNull(buffer);
 context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testAllocTokenBufferMinSize() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocTokenBuffer(5));
 char[] buffer = context.allocTokenBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocConcatBuffer());
 char[] buffer = context.allocConcatBuffer();
 assertNotNull(buffer);
 context.releaseConcatBuffer(buffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 assertNull(context.allocNameCopyBuffer());
 char[] buffer = context.allocNameCopyBuffer(5);
 assertNotNull(buffer);
 context.releaseNameCopyBuffer(buffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] buffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(buffer);
 assertNull(context.getReadIOBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testReleaseReadIOBufferTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] buffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(buffer);
 context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] buffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(buffer);
 assertNull(context.getWriteEncodingBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testReleaseWriteEncodingBufferTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] buffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(buffer);
 context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] buffer = context.allocBase64Buffer();
 context.releaseBase64Buffer(buffer);
 assertNull(context.getBase64Buffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testReleaseBase64BufferTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] buffer = context.allocBase64Buffer();
 context.releaseBase64Buffer(buffer);
 context.releaseBase64Buffer(buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 char[] buffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(buffer);
 assertNull(context.getTokenBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testReleaseTokenBufferTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 char[] buffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(buffer);
 context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 char[] buffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(buffer);
 assertNull(context.getConcatBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testReleaseConcatBufferTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 char[] buffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(buffer);
 context.releaseConcatBuffer(buffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 char[] buffer = context.allocNameCopyBuffer();
 context.releaseNameCopyBuffer(buffer);
 assertNull(context.getNameCopyBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testReleaseNameCopyBufferTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 char[] buffer = context.allocNameCopyBuffer();
 context.releaseNameCopyBuffer(buffer);
 context.releaseNameCopyBuffer(buffer);
 }

 @Test
 public void testBuffersNotReleasedTwice() {
 context = new IOContext(bufferRecycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer();
 byte[] writeBuffer = context.allocWriteEncodingBuffer();
 char[] tokenBuffer = context.allocTokenBuffer();
 char[] concatBuffer = context.allocConcatBuffer();
 char[] nameCopyBuffer = context.allocNameCopyBuffer();

 // Release read and write buffers
 context.releaseReadIOBuffer(readBuffer);
 context.releaseWriteEncodingBuffer(writeBuffer);

 // Attempt to release token and concat buffers, which should fail
 try {
 context.releaseTokenBuffer(tokenBuffer);
 context.releaseConcatBuffer(concatBuffer);
 } catch (IllegalStateException e) {
 // Pass
 }

 // Release name copy buffer, which should succeed
 context.releaseNameCopyBuffer(nameCopyBuffer);
 }
}