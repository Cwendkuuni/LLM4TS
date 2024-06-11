package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOContextTest {

 IOContext context;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextConstruction() {
 context = new IOContext(bufferRecycler, null, false);
 assertEquals(bufferRecycler, context.getBufferRecycler());
 assertNull(context.getEncoding());
 assertFalse(context.isResourceManaged());
 }

 @Test
 public void testIOContextAllocation() {
 context = new IOContext(bufferRecycler, null, false);

 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertNull(context.getReadIOBuffer());

 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertNull(context.getWriteEncodingBuffer());

 byte[] base64Buffer = context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertNull(context.getBase64Buffer());

 char[] tokenBuffer = context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertNull(context.getTokenBuffer());

 char[] concatBuffer = context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertNull(context.getConcatBuffer());

 char[] nameCopyBuffer = context.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertNotNull(context.getNameCopyBuffer());
 assertArrayEquals(new char[10], context.getNameCopyBuffer());
 }

 @Test
 public void testIOContextAllocationWithMinSize() {
 context = new IOContext(bufferRecycler, null, false);

 byte[] readIOBuffer = context.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertNull(context.getReadIOBuffer());
 assertArrayEquals(new byte[10], readIOBuffer);

 byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertNull(context.getWriteEncodingBuffer());
 assertArrayEquals(new byte[10], writeEncodingBuffer);
 }

 @Test
 public void testIOContextRelease() {
 context = new IOContext(bufferRecycler, null, false);

 byte[] readIOBuffer = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertNull(context.getReadIOBuffer());

 context.releaseReadIOBuffer(readIOBuffer);
 assertNull(context.getReadIOBuffer());

 byte[] readIOBuffer2 = context.allocReadIOBuffer();
 assertNotNull(readIOBuffer2);
 assertNull(context.getReadIOBuffer());
 assertNotSame(readIOBuffer, readIOBuffer2);
 }

 @Test(expected = IllegalStateException.class)
 public void testIOContextDoubleRelease() {
 context = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = context.allocReadIOBuffer();
 context.releaseReadIOBuffer(readIOBuffer);
 context.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testSetEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 context.setEncoding(JsonEncoding.UTF8);
 assertEquals(JsonEncoding.UTF8, context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 context = new IOContext(bufferRecycler, null, false);
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext newContext = context.withEncoding(encoding);
 assertEquals(encoding, newContext.getEncoding());
 assertEquals(context, newContext.getSourceRef());
 assertEquals(context.getBufferRecycler(), newContext.getBufferRecycler());
 assertEquals(context.isResourceManaged(), newContext.isResourceManaged());
 }

 @Test
 public void testGetSourceReference() {
 context = new IOContext(bufferRecycler, null, false);
 context.setSourceRef(new Object());
 assertEquals(context.getSourceRef(), context.getSourceReference());
 }

 @Test
 public void testIsResourceManaged() {
 context = new IOContext(bufferRecycler, null, false);
 context.setResourceManaged(true);
 assertTrue(context.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 context = new IOContext(bufferRecycler, null, false);TextBuffer textBuffer = context.constructTextBuffer(); assertEquals(context, textBuffer.getIOContext()); }

}