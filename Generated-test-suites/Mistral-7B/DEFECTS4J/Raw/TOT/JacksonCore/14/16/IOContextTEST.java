package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private BufferRecycler bufferRecycler;
 private IOContext ioContext;
 private Object sourceRef;

 @Before
 public void setUp() throws IOException {
 bufferRecycler = new BufferRecycler();
 sourceRef = new Object();
 ioContext = new IOContext(bufferRecycler, sourceRef, true);
 }

 @Test
 public void testAllocAndReleaseReadIOBuffer() throws IOException {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertTrue(readIOBuffer != null);
 assertArrayEquals(new byte[0], readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 readIOBuffer = ioContext.allocReadIOBuffer();
 assertArrayEquals(new byte[0], readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocAndReleaseReadIOBufferTwice() throws IOException {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 ioContext.allocReadIOBuffer(); // should throw exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithNullBuffer() throws IOException {
 ioContext.releaseReadIOBuffer(null);
 }

 @Test
 public void testAllocWriteEncodingBuffer() throws IOException {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertTrue(writeEncodingBuffer != null);
 assertArrayEquals(new byte[0], writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertArrayEquals(new byte[0], writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocAndReleaseWriteEncodingBufferTwice() throws IOException {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 ioContext.allocWriteEncodingBuffer(); // should throw exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithNullBuffer() throws IOException {
 ioContext.releaseWriteEncodingBuffer(null);
 }

 @Test
 public void testAllocBase64Buffer() throws IOException {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertTrue(base64Buffer != null);
 assertArrayEquals(new byte[0], base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 base64Buffer = ioContext.allocBase64Buffer();
 assertArrayEquals(new byte[0], base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() throws IOException {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertTrue(tokenBuffer != null);
 assertArrayEquals(new char[0], tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 tokenBuffer = ioContext.allocTokenBuffer();
 assertArrayEquals(new char[0], tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocAndReleaseTokenBufferTwice() throws IOException {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 ioContext.allocTokenBuffer(); // should throw exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWithNullBuffer() throws IOException {
 ioContext.releaseTokenBuffer(null);
 }

 @Test
 public void testAllocConcatBuffer() throws IOException {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertTrue(concatBuffer != null);
 assertArrayEquals(new char[0], concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 concatBuffer = ioContext.allocConcatBuffer();
 assertArrayEquals(new char[0], concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() throws IOException {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertTrue(nameCopyBuffer != null);
 assertArrayEquals(new char[10], nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertArrayEquals(new char[10], nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testIsResourceManaged() {
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testGetEncoding() {
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
 ioContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @After
 public void tearDown() throws IOException {
 ioContext.releaseAllBuffers();
 bufferRecycler = null;
 }
}