package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

 private IOContext context;
 private BufferRecycler bufferRecycler;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 context = new IOContext(bufferRecycler, this, false);
 }

 @Test
 public void testIOContextCreation() {
 assertNotNull(context);
 assertEquals(bufferRecycler, context._bufferRecycler);
 assertNull(context._sourceRef);
 assertNull(context._encoding);
 assertFalse(context._managedResource);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 context.setEncoding(encoding);
 assertEquals(encoding, context._encoding);
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext contextWithEncoding = context.withEncoding(encoding);
 assertEquals(encoding, contextWithEncoding._encoding);
 assertEquals(context, contextWithEncoding);
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 context = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, context.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 context.setEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 assertFalse(context._managedResource);
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
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = context.allocReadIOBuffer(5);
 assertNotNull(readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testCallAllocReadIOBufferTwice() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testCallAllocWriteEncodingBufferTwice() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 context.allocWriteEncodingBuffer();
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

 @Test(expected = IllegalStateException.class)
 public void testCallAllocTokenBufferTwice() {
 char[] tokenBuffer = context.allocTokenBuffer();
 context.allocTokenBuffer();
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 context.releaseConcatBuffer(concatBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testCallAllocConcatBufferTwice() {
 char[] concatBuffer = context.allocConcatBuffer();
 context.allocConcatBuffer();
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 context.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testCallAllocNameCopyBufferTwice() {
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 context.allocNameCopyBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readIOBuffer);
 assertNull(context._readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();context.releaseWriteEncodingBuffer(writeEncodingBuffer);
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
 char[] nameCopyBuffer = context.allocNameCopyBuffer();
 context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(context._nameCopyBuffer);
 }
}