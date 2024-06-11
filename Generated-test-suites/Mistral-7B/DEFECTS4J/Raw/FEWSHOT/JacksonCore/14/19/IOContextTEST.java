package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler recycler = new BufferRecycler();
 Object sourceRef = new Object();

 @Test
 public void testIOContext() {
 context = new IOContext(recycler, sourceRef, true);
 assertEquals(sourceRef, context.getSourceReference());
 assertEquals(true, context.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 context = new IOContext(recycler, sourceRef, true);
 context.setEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 context = new IOContext(recycler, sourceRef, true);
 context = context.withEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer();
 assertNull(context.getReadIOBuffer());
 assertNotNull(readBuffer);
 }

 @Test
 public void testAllocReadIOBufferMinSize() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer(10);
 assertNull(context.getReadIOBuffer());
 assertNotNull(readBuffer);
 assertEquals(10, readBuffer.length);
 }

 @Test
 public void testGetReadIOBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readBuffer);
 byte[] readBuffer2 = context.getReadIOBuffer();
 assertSame(readBuffer, readBuffer2);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readBuffer);
 assertNull(context.getReadIOBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer();
 context.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] writeBuffer = context.allocWriteEncodingBuffer();
 assertNull(context.getWriteEncodingBuffer());
 assertNotNull(writeBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferMinSize() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] writeBuffer = context.allocWriteEncodingBuffer(10);
 assertNull(context.getWriteEncodingBuffer());
 assertNotNull(writeBuffer);
 assertEquals(10, writeBuffer.length);
 }

 @Test
 public void testGetWriteEncodingBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] writeBuffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(writeBuffer);
 byte[] writeBuffer2 = context.getWriteEncodingBuffer();
 assertSame(writeBuffer, writeBuffer2);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] writeBuffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(writeBuffer);
 assertNull(context.getWriteEncodingBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] writeBuffer = context.allocWriteEncodingBuffer();
 context.allocWriteEncodingBuffer();
 }

 @Test
 public void testAllocBase64Buffer() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNull(context.getBase64Buffer());
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNull(context.getTokenBuffer());
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferMinSize() {
 context = new IOContext(recycler, sourceRef, true);
 char[] tokenBuffer = context.allocTokenBuffer(10);
 assertNull(context.getTokenBuffer());
 assertNotNull(tokenBuffer);
 assertEquals(10, tokenBuffer.length);
 }

 @Test
 public void testGetTokenBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] tokenBuffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(tokenBuffer);
 char[] tokenBuffer2 = context.getTokenBuffer();
 assertSame(tokenBuffer, tokenBuffer2);
 }

 @Test
 public void testReleaseTokenBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] tokenBuffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(tokenBuffer);
 assertNull(context.getTokenBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 context = new IOContext(recycler, sourceRef, true);
 char[] tokenBuffer = context.allocTokenBuffer();
 context.allocTokenBuffer();
 }

 @Test
 public void testAllocConcatBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] concatBuffer = context.allocConcatBuffer();
 assertNull(context.getConcatBuffer());
 assertNotNull(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 assertNull(context.getNameCopyBuffer());
 assertNotNull(nameCopyBuffer);
 }

 @Test
 public void testAllocNameCopyBufferMinSize() {
 context = new IOContext(recycler, sourceRef, true);
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNull(context.getNameCopyBuffer());
 assertNotNull(nameCopyBuffer);
 assertEquals(10, nameCopyBuffer.length);
 }

 @Test
 public void testGetNameCopyBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 context.releaseNameCopyBuffer(nameCopyBuffer);
 char[] nameCopyBuffer2 = context.getNameCopyBuffer();
 assertSame(nameCopyBuffer, nameCopyBuffer2);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 context = new IOContext(recycler, sourceRef, true);
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context.getNameCopyBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocNameCopyBufferTwice() {
 context = new IOContext(recycler, sourceRef, true);
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 context.allocNameCopyBuffer();
 }

 @Test
 public void testReleaseBuffers() {
 context = new IOContext(recycler, sourceRef, true);
 byte[] readBuffer = context.allocReadIOBuffer();
 byte[] writeBuffer = context.allocWriteEncodingBuffer();
 char[] tokenBuffer = context.allocTokenBuffer();
 char[] concatBuffer = context.allocConcatBuffer();
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 context.releaseReadIOBuffer(readBuffer);
 context.releaseWriteEncodingBuffer(writeBuffer);
 context.releaseTokenBuffer(tokenBuffer);
 context.releaseConcatBuffer(concatBuffer);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context.getReadIOBuffer());
 assertNull(context.getWriteEncodingBuffer());
 assertNull(context.getTokenBuffer());
 assertNull(context.getConcatBuffer());
 assertNull(context.getNameCopyBuffer());
 }
}