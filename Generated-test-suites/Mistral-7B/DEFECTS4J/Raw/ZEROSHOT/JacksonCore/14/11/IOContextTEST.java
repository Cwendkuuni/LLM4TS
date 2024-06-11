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
 ioContext = new IOContext(bufferRecycler, null, false);
 }

 @Test
 public void testIOContextCreation() {
 assertNotNull(ioContext);
 assertEquals(bufferRecycler, ioContext.getBufferRecycler());
 assertNull(ioContext.getEncoding());
 assertFalse(ioContext.isResourceManaged());
 }

 @Test(expected = IllegalStateException.class)
 public void testIOContextAllocateReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocateAndReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocateAndReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
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
}