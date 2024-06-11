package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOContextTest {

 private BufferRecycler bufferRecycler = new BufferRecycler();
 private IOContext ioContext;

 @Test
 public void testIOContextCreation() {
 ioContext = new IOContext(bufferRecycler, "sourceRef", true);
 assertNotNull(ioContext);
 assertEquals("sourceRef", ioContext.getSourceReference());
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testAllocAndReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocAndReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 }

 @Test
 public void testAllocAndReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocAndReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocAndReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testAllocAndReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 }
}