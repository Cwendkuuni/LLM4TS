package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextConstruction() {
 IOContext ioContext = new IOContext(bufferRecycler, null, false);
 assertNull(ioContext.getSourceReference());
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
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContext = context.withEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 assertEquals(context, ioContext); // testing that withEncoding returns this instance
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = context.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertNull(context.getReadIOBuffer());
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertNull(context.getWriteEncodingBuffer());
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertNull(context.getBase64Buffer());
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertNull(context.getTokenBuffer());
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertNull(context.getConcatBuffer());
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(3);
 assertNotNull(nameCopyBuffer);
 assertNull(context.getNameCopyBuffer());
 assertEquals(3, nameCopyBuffer.length);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readIOBuffer);
 assertNull(context.getReadIOBuffer());
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(context.getWriteEncodingBuffer());
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = context.allocBase64Buffer();
 context.releaseBase64Buffer(base64Buffer);
 assertNull(context.getBase64Buffer());
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(tokenBuffer);
 assertNull(context.getTokenBuffer());
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(concatBuffer);
 assertNull(context.getConcatBuffer());
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(3);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context.getNameCopyBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer1 = context.allocReadIOBuffer();
 byte[] readIOBuffer2 = context.allocReadIOBuffer();
 }

 @Test
 public void testVerifyAlloc() {
 context.allocNameCopyBuffer(3);
 context._verifyAlloc(context.getNameCopyBuffer());
 assertNull(context.getNameCopyBuffer());
 }

 @Test
 public void testVerifyRelease() {
 byte[] buffer1 = context.allocReadIOBuffer();
 byte[] buffer2 = context.allocReadIOBuffer();
 context._verifyRelease(buffer1, buffer2);
 assertNull(context.getReadIOBuffer());
 }
}