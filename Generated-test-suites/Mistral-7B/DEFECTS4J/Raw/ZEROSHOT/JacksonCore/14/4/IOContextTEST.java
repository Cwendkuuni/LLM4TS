package com.fasterxml.jackson.core.io;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, this, true);
 }

 @Test
 public void testIOContextConstruction() {
 // Check that IOContext is constructed correctly
 Assert.assertNotNull(ioContext);
 Assert.assertNotNull(ioContext.getSourceReference());
 Assert.assertNull(ioContext.getEncoding());
 Assert.assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 // Test setting encoding
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 Assert.assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 // Test withEncoding method
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
 Assert.assertEquals(encoding, ioContextWithEncoding.getEncoding());
 Assert.assertNotSame(ioContext, ioContextWithEncoding);
 }

 @Test
 public void testAllocReadIOBuffer() {
 // Test allocating read I/O buffer
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 Assert.assertNotNull(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 // Test allocating read I/O buffer twice
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 // Test releasing read I/O buffer
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 Assert.assertNull(ioContext.getReadIOBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWrongBuffer() {
 // Test releasing wrong buffer
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseReadIOBuffer(wrongBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 // Test allocating write encoding buffer
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 Assert.assertNotNull(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 // Test allocating write encoding buffer twice
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 // Test releasing write encoding buffer
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 Assert.assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWrongBuffer() {
 // Test releasing wrong buffer
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseWriteEncodingBuffer(wrongBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 // Test allocating base64 buffer
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 Assert.assertNotNull(base64Buffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocBase64BufferTwice() {
 // Test allocating base64 buffer twice
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.allocBase64Buffer();
 }

 @Test
 public void testReleaseBase64Buffer() {
 // Test releasing base64 buffer
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 Assert.assertNull(ioContext.getBase64Buffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64BufferWrongBuffer() {
 // Test releasing wrong buffer
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseBase64Buffer(wrongBuffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 // Test allocating token buffer
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 Assert.assertNotNull(tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 // Test allocating token buffer twice
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.allocTokenBuffer();
 }

 @Test
 public void testReleaseTokenBuffer() {
 // Test releasing token buffer
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 Assert.assertNull(ioContext.getTokenBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWrongBuffer() {
 // Test releasing wrong buffer
 char[] wrongBuffer = new char[10];
 ioContext.releaseTokenBuffer(wrongBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 // Test allocating concat buffer
 char[] concatBuffer = ioContext.allocConcatBuffer();
 Assert.assertNotNull(concatBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocConcatBufferTwice() {
 // Test allocating concat buffer twice
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.allocConcatBuffer();
 }

 @Test
 public void testReleaseConcatBuffer() {
 // Test releasing concat buffer
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 Assert.assertNull(ioContext.getConcatBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBufferWrongBuffer() {
 // Test releasing wrong buffer
 char[] wrongBuffer = new char[10];
 ioContext.releaseConcatBuffer(wrongBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 // Test allocating name copy buffer
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 Assert.assertNotNull(nameCopyBuffer);
 Assert.assertEquals(5, nameCopyBuffer.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocNameCopyBufferTwice() {
 // Test allocating name copy buffer twice
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 ioContext.allocNameCopyBuffer(5);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 // Test releasing name copy buffer
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 Assert.assertNull(ioContext.getNameCopyBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBufferWrongBuffer() {
 // Test releasing wrong buffer
 char[] wrongBuffer = new char[10];
 ioContext.releaseNameCopyBuffer(wrongBuffer);
 }
}