package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

 private IOContext ioContext;

 @Before
 public void setUp() {
 BufferRecycler bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, this, true);
 }

 @After
 public void tearDown() {
 ioContext = null;
 }

 @Test
 public void testIOContextConstruction() {
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
 IOContext context = ioContext.withEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 assertEquals(ioContext, context);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] buffer = ioContext.allocReadIOBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 fail("Should not be able to allocate read buffer twice");
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] buffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(buffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] buffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] buffer = ioContext.allocTokenBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] buffer = ioContext.allocConcatBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] buffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 }

 @Test
 public void testReleaseBuffers() {
 byte[] readBuffer = ioContext.allocReadIOBuffer();
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseReadIOBuffer(readBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getReadIOBuffer());
 assertNull(ioContext.getTokenBuffer());
 }
}