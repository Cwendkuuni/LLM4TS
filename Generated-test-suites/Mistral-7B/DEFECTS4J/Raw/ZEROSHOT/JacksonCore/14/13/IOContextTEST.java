package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setup() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, this, true);
 }

 @After
 public void teardown() {
 ioContext = null;
 bufferRecycler = null;
 }

 @Test
 public void testIOContextConstruction() {
 Assert.assertNotNull(ioContext);
 Assert.assertEquals(this, ioContext.getSourceReference());
 Assert.assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 Assert.assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext = ioContext.withEncoding(encoding);
 Assert.assertEquals(encoding, ioContext.getEncoding());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBuffertwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 Assert.assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferNegativeSize() {
 ioContext.allocReadIOBuffer(-1);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 Assert.assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferNegativeSize() {
 ioContext.allocWriteEncodingBuffer(-1);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 Assert.assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBuffertwice() {
 ioContext.allocTokenBuffer();
 ioContext.allocTokenBuffer();
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 Assert.assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferNegativeSize() {
 ioContext.allocTokenBuffer(-1);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 Assert.assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 Assert.assertNotNull(nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocNameCopyBufferNegativeSize() {
 ioContext.allocNameCopyBuffer(-1);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }
}