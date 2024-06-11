package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IOContextTest {

 private IOContext _context;
 private BufferRecycler _bufferRecycler;

 @Before
 public void setUp() {
 _bufferRecycler = new BufferRecycler();
 _context = new IOContext(_bufferRecycler, this, true);
 }

 @Test
 public void testInitialization() {
 assertEquals(this, _context.getSourceReference());
 assertNull(_context.getEncoding());
 assertEquals(true, _context.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context.setEncoding(encoding);
 assertEquals(encoding, _context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context = _context.withEncoding(encoding);
 assertEquals(encoding, _context.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] buffer = _context.allocReadIOBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
 _context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] buffer = _context.allocReadIOBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 _context.releaseReadIOBuffer(buffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] buffer = _context.allocWriteEncodingBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
 _context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] buffer = _context.allocWriteEncodingBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 _context.releaseWriteEncodingBuffer(buffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] buffer = _context.allocBase64Buffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
 _context.releaseBase64Buffer(buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] buffer = _context.allocTokenBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
 _context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] buffer = _context.allocTokenBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 _context.releaseTokenBuffer(buffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] buffer = _context.allocConcatBuffer();
 assertNotNull(buffer);
 assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
 _context.releaseConcatBuffer(buffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] buffer = _context.allocNameCopyBuffer(10);
 assertNotNull(buffer);
 assertEquals(10, buffer.length);
 _context.releaseNameCopyBuffer(buffer);
 }

 @After
 public void tearDown() {
 _context = null;
 _bufferRecycler = null;
 }
}