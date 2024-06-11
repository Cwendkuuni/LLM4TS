package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextConstruction() {
 context = new IOContext(bufferRecycler, null, false);
 assertEquals(bufferRecycler, context._bufferRecycler);
 assertNull(context._sourceRef);
 assertFalse(context._managedResource);
 }

 @Test
 public void testSetEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 context.setEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 context = new IOContext(bufferRecycler, new Object(), false);
 assertEquals(new Object(), context.getSourceReference());
 }

 @Test
 public void testIsResourceManaged() {
 context = new IOContext(bufferRecycler, null, true);
 assertTrue(context.isResourceManaged());
 context = new IOContext(bufferRecycler, null, false);
 assertFalse(context.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = context.constructTextBuffer();
 assertSame(bufferRecycler, textBuffer._bufferRecycler);
 assertSame(context, textBuffer._ioContext);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertSame(bufferRecycler, readIOBuffer._recycler);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = context.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertSame(bufferRecycler, readIOBuffer._recycler);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertSame(bufferRecycler, writeEncodingBuffer._recycler);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertSame(bufferRecycler, writeEncodingBuffer._recycler);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertSame(bufferRecycler, base64Buffer._recycler);
 context.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertSame(bufferRecycler, tokenBuffer._recycler);
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = context.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertSame(bufferRecycler, tokenBuffer._recycler);
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertSame(bufferRecycler, concatBuffer._recycler);
 context.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertSame(bufferRecycler, nameCopyBuffer._recycler);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readIOBuffer);
 assertNull(context._readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(context._writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = context.allocBase64Buffer();
 context.releaseBase64Buffer(base64Buffer);
 assertNull(context._base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(tokenBuffer);
 assertNull(context._tokenCBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(concatBuffer);
 assertNull(context._concatCBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context._nameCopyBuffer);
 }

}