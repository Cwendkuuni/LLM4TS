package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextInitialization() {
 context = new IOContext(bufferRecycler, null, false);
 assertEquals(null, context.getSourceReference());
 assertNull(context.getEncoding());
 assertFalse(context.isResourceManaged());
 }

 @Test
 public void testIOContextWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 context = new IOContext(bufferRecycler, null, false).withEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = context.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertArrayEquals(new byte[10], readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertArrayEquals(new byte[10], writeEncodingBuffer);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 context.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = context.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[10], tokenBuffer);
 context.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 context.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertArrayEquals(new char[10], nameCopyBuffer);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 }
}