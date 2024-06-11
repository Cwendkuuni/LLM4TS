package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private BufferRecycler bufferRecycler;
 private IOContext ioContext;

 @Before
 public void setup() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, null, false);
 }

 @Test
 public void testIOContextConstruction() {
 IOContext context = new IOContext(bufferRecycler, null, false);
 assertNotNull(context);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext context = new IOContext(bufferRecycler, null, false);
 context.setEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext context = new IOContext(bufferRecycler, null, false);
 IOContext contextWithEncoding = context.withEncoding(encoding);
 assertEquals(encoding, contextWithEncoding.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 IOContext context = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, context.getSourceReference());
 }

 @Test
 public void testIsResourceManaged() {
 IOContext context = new IOContext(bufferRecycler, null, false);
 assertFalse(context.isResourceManaged());
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
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 int minSize = 10;
 char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
 assertEquals(minSize, tokenBuffer.length);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(20);
 assertEquals(20, nameCopyBuffer.length);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext.getBase64Buffer());
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(20);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }
}