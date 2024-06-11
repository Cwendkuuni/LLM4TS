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
 IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContextWithEncoding.getEncoding());
 assertEquals(ioContext, ioContextWithEncoding);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithWrongBuffer() {
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseReadIOBuffer(wrongBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithWrongBuffer() {
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseWriteEncodingBuffer(wrongBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext.getBase64Buffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64BufferWithWrongBuffer() {
 byte[] wrongBuffer = new byte[10];
 ioContext.releaseBase64Buffer(wrongBuffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.allocTokenBuffer();
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWithWrongBuffer() {
 char[] wrongBuffer = new char[10];
 ioContext.releaseTokenBuffer(wrongBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBufferWithWrongBuffer() {
 char[] wrongBuffer = new char[10];
 ioContext.releaseConcatBuffer(wrongBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertEquals(10, nameCopyBuffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBufferWithWrongBuffer() {
 char[] wrongBuffer = new char[10];
 ioContext.releaseNameCopyBuffer(wrongBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }

 @After
 public void tearDown() {
 ioContext = null;
 }
}