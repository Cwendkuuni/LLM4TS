package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextConstruction() {
 IOContext ioContext = new IOContext(bufferRecycler, null, false);
 assertNull(ioContext.getEncoding());
 assertFalse(ioContext.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 context.setEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 context = context.withEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 context = new IOContext(bufferRecycler, new Object(), false);
 assertEquals(context._sourceRef, context.getSourceReference());
 }

 @Test
 public void testAllocReadIOBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferMinSize() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = context.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferMinSize() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 context.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferMinSize() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer = context.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 context.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readIOBuffer);
 assertNull(context._readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(context._writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] base64Buffer = context.allocBase64Buffer();
 context.releaseBase64Buffer(base64Buffer);
 assertNull(context._base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(tokenBuffer);
 assertNull(context._tokenCBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] concatBuffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(concatBuffer);
 assertNull(context._concatCBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 context = new IOContext(bufferRecycler, null, false);
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context._nameCopyBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 context = new IOContext(bufferRecycler, null, false);
 context.allocReadIOBuffer();
 context.allocReadIOBuffer();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferReadIO() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = new byte[10];
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferWriteEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] writeEncodingBuffer = new byte[10];
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferBase64() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] base64Buffer = new byte[10];
 context.releaseBase64Buffer(base64Buffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferToken() {
 context = new IOContext(bufferRecycler, null, false);
 char[] tokenBuffer = new char[10];
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferConcat() {
 context = new IOContext(bufferRecycler, null, false);
 char[] concatBuffer = new char[10];
 context.releaseConcatBuffer(concatBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBufferNameCopy() {
 context = new IOContext(bufferRecycler, null, false);
 char[] nameCopyBuffer = new char[10];
 context.releaseNameCopyBuffer(nameCopyBuffer);
 }

}