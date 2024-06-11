package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

 private IOContext ioContext;

 @Before
 public void setUp() {
 ioContext = new IOContext(new BufferRecycler(), this, true);
 }

 @Test
 public void testConstructor() {
 assertNotNull(ioContext);
 assertEquals(this, ioContext.getSourceReference());
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBuffersTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 fail("Expected IllegalStateException: Trying to call same allocXxx() method second time");
 }

 @Test(expected = NullPointerException.class)
 public void testReleaseNullReadIOBuffer() {
 ioContext.releaseReadIOBuffer(null);
 fail("Expected NullPointerException: Trying to release buffer not owned by the context");
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = NullPointerException.class)
 public void testReleaseNullWriteEncodingBuffer() {
 ioContext.releaseWriteEncodingBuffer(null);
 fail("Expected NullPointerException: Trying to release buffer not owned by the context");
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test(expected = NullPointerException.class)
 public void testReleaseNullBase64Buffer() {
 ioContext.releaseBase64Buffer(null);
 fail("Expected NullPointerException: Trying to release buffer not owned by the context");
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = NullPointerException.class)
 public void testReleaseNullTokenBuffer() {
 ioContext.releaseTokenBuffer(null);
 fail("Expected NullPointerException: Trying to release buffer not owned by the context");
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test(expected = NullPointerException.class)
 public void testReleaseNullConcatBuffer() {
 ioContext.releaseConcatBuffer(null);
 fail("Expected NullPointerException: Trying to release buffer not owned by the context");
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test(expected = NullPointerException.class)
 public void testReleaseNullNameCopyBuffer() {
 ioContext.releaseNameCopyBuffer(null);
 fail("Expected NullPointerException: Trying to release buffer not owned by the context");
 }

 @After
 public void tearDown() {
 // release all allocated buffers to test recycling
 ioContext.releaseReadIOBuffer(_readIOBuffer);
 ioContext.releaseWriteEncodingBuffer(_writeEncodingBuffer);
 ioContext.releaseBase64Buffer(_base64Buffer);
 ioContext.releaseTokenBuffer(_tokenCBuffer);
 ioContext.releaseConcatBuffer(_concatCBuffer);
 ioContext.releaseNameCopyBuffer(_nameCopyBuffer);
 }
}