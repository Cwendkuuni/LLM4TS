package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private BufferRecycler _bufferRecycler;
 private IOContext _ioContext;

 @Before
 public void setUp() {
 _bufferRecycler = new BufferRecycler();
 _ioContext = new IOContext(_bufferRecycler, this, true);
 }

 @Test
 public void testConstructor() {
 Object sourceRef = new Object();
 IOContext ioContext = new IOContext(_bufferRecycler, sourceRef, true);
 assertEquals(sourceRef, ioContext.getSourceReference());
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _ioContext.setEncoding(encoding);
 assertEquals(encoding, _ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContext1 = _ioContext.withEncoding(encoding);
 JsonEncoding encoding1 = ioContext1.getEncoding();
 assertEquals(encoding, encoding1);
 assertEquals(ioContext1, _ioContext);
 JsonEncoding encoding2 = _ioContext.withEncoding(JsonEncoding.UTF8);
 JsonEncoding encoding3 = ioContext1.getEncoding();
 assertNotSame(encoding2, encoding3);
 assertNotSame(ioContext2, ioContext1);
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 _ioContext = new IOContext(_bufferRecycler, sourceRef, true);
 assertEquals(sourceRef, _ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _ioContext.setEncoding(encoding);
 assertEquals(encoding, _ioContext.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 _ioContext = new IOContext(_bufferRecycler, this, false);
 assertFalse(_ioContext.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = _ioContext.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = _ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferMinSize() {
 int minSize = 10;
 byte[] readIOBuffer = _ioContext.allocReadIOBuffer(minSize);
 assertNotNull(readIOBuffer);
 assertEquals(minSize, readIOBuffer.length);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferMinSize() {
 int minSize = 10;
 byte[] writeEncodingBuffer = _ioContext.allocWriteEncodingBuffer(minSize);
 assertNotNull(writeEncodingBuffer);
 assertEquals(minSize, writeEncodingBuffer.length);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = _ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = _ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferMinSize() {
 int minSize = 10;
 char[] tokenBuffer = _ioContext.allocTokenBuffer(minSize);
 assertNotNull(tokenBuffer);
 assertEquals(minSize, tokenBuffer.length);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = _ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = _ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertEquals(10, nameCopyBuffer.length);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = _ioContext.allocReadIOBuffer();
 _ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(_ioContext._readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _ioContext.allocWriteEncodingBuffer();
 _ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(_ioContext._writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = _ioContext.allocBase64Buffer();
 _ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(_ioContext._base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = _ioContext.allocTokenBuffer();
 _ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(_ioContext._tokenCBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = _ioContext.allocConcatBuffer();
 _ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(_ioContext._concatCBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = _ioContext.allocNameCopyBuffer(10);
 _ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(_ioContext._nameCopyBuffer);
 }

 @After
 public void tearDown() {
 if (_bufferRecycler != null) {
 _bufferRecycler.clear();
 }
 }

}