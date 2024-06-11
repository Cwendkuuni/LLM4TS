package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private IOContext context;
 private BufferRecycler recycler;

 @Before
 public void setUp() {
 recycler = new BufferRecycler();
 context = new IOContext(recycler, "Test Source", true);
 }

 @Test
 public void testConstructor() {
 assertNotNull(context);
 assertEquals("Test Source", context.getSourceReference());
 assertTrue(context.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 context.setEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext contextWithEncoding = context.withEncoding(encoding);
 assertEquals(encoding, contextWithEncoding.getEncoding());
 assertEquals(context, contextWithEncoding);
 }

 @Test(expected = IllegalStateException.class)
 public void testConstructTextBufferTwice() {
 context.constructTextBuffer();
 context.constructTextBuffer();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] buffer = context.allocReadIOBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
 context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] buffer = context.allocReadIOBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] buffer = context.allocWriteEncodingBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
 context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] buffer = context.allocWriteEncodingBuffer(20);
 assertNotNull(buffer);
 assertEquals(20, buffer.length);
 context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] buffer = context.allocBase64Buffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
 context.releaseBase64Buffer(buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] buffer = context.allocTokenBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
 context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] buffer = context.allocTokenBuffer(15);
 assertNotNull(buffer);
 assertEquals(15, buffer.length);
 context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] buffer = context.allocConcatBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
 context.releaseConcatBuffer(buffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] buffer = context.allocNameCopyBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 context.releaseNameCopyBuffer(buffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] buffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(buffer);
 assertNull(context.getReadIOBuffer());
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] buffer = context.allocWriteEncodingBuffer();
 context.releaseWriteEncodingBuffer(buffer);
 assertNull(context.getWriteEncodingBuffer());
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] buffer = context.allocBase64Buffer();
 context.releaseBase64Buffer(buffer);
 assertNull(context.getBase64Buffer());
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] buffer = context.allocTokenBuffer();
 context.releaseTokenBuffer(buffer);
 assertNull(context.getTokenBuffer());
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] buffer = context.allocConcatBuffer();
 context.releaseConcatBuffer(buffer);
 assertNull(context.getConcatBuffer());
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] buffer = context.allocNameCopyBuffer(10);
 context.releaseNameCopyBuffer(buffer);
 assertNull(context.getNameCopyBuffer());
 }
}