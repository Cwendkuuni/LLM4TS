package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, "sourceRef", true);
 }

 @Test
 public void testIOContextConstruction() {
 assertEquals("sourceRef", ioContext.getSourceReference());
 assertTrue(ioContext.isResourceManaged());
 assertNull(ioContext.getEncoding());
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
 IOContext newContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, newContext.getEncoding());
 assertNotSame(ioContext, newContext);
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 }
}