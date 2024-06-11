package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextCreation() {
 context = new IOContext(bufferRecycler, null, false);
 assertNull(context.getEncoding());
 assertFalse(context.isResourceManaged());
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
 context = context.withEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 context = new IOContext(bufferRecycler, "sourceRef", false);
 assertEquals("sourceRef", context.getSourceReference());
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
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 readIOBuffer = context.allocReadIOBuffer();
 assertSame(readIOBuffer, context.allocReadIOBuffer());
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertSame(writeEncodingBuffer, context.allocWriteEncodingBuffer());
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 context.releaseBase64Buffer(base64Buffer);
 base64Buffer = context.allocBase64Buffer();
 assertSame(base64Buffer, context.allocBase64Buffer());
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 context.releaseTokenBuffer(tokenBuffer);
 tokenBuffer = context.allocTokenBuffer();
 assertSame(tokenBuffer, context.allocTokenBuffer());
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 context.releaseConcatBuffer(concatBuffer);
 concatBuffer = context.allocConcatBuffer();
 assertSame(concatBuffer, context.allocConcatBuffer());
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertSame(nameCopyBuffer, context.allocNameCopyBuffer(10));
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
 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context.getNameCopyBuffer());
 }

 @Test
 public void testVerifyAlloc() {
 context = new IOContext(bufferRecycler, null, false);
 context.allocBase64Buffer();
 context.allocBase64Buffer(); // should fail
 }

 @Test
 public void testVerifyRelease() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(new byte[1]); // should fail
 }

 @Test
 public void testWrongBuf() {
 context = new IOContext(bufferRecycler, null, false);
 context.releaseBase64Buffer(context.getReadIOBuffer()); // should fail
 }
}