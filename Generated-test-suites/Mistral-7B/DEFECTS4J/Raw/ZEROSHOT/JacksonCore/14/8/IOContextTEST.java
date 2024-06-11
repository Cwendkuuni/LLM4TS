package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.After;
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

 @After
 public void tearDown() {
 // Release all buffers to ensure tests start with clean state
 ioContext.releaseReadIOBuffer(ioContext.allocReadIOBuffer());
 ioContext.releaseWriteEncodingBuffer(ioContext.allocWriteEncodingBuffer());
 ioContext.releaseBase64Buffer(ioContext.allocBase64Buffer());
 ioContext.releaseTokenBuffer(ioContext.allocTokenBuffer());
 ioContext.releaseConcatBuffer(ioContext.allocConcatBuffer());
 ioContext.releaseNameCopyBuffer(ioContext.allocNameCopyBuffer());
 }

 @Test
 public void testIOContextConstruction() {
 // Test IOContext construction
 ioContext = new IOContext(bufferRecycler, "sourceRef", true);
 assertNotNull(ioContext);
 assertSame(bufferRecycler, ioContext._bufferRecycler);
 assertSame("sourceRef", ioContext._sourceRef);
 assertFalse(ioContext._managedResource);
 }

 @Test
 public void testSetEncoding() {
 // Test setting encoding
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 // Test withEncoding method
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 // Test getting source reference
 assertEquals("sourceRef", ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 // Test getting encoding
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 // Test isResourceManaged method
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 // Test constructTextBuffer method
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 // Test allocReadIOBuffer method
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 // Test allocReadIOBuffer method with minSize
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertEquals(10, readIOBuffer.length);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 // Test allocWriteEncodingBuffer method
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 // Test allocWriteEncodingBuffer method with minSize
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertEquals(10, writeEncodingBuffer.length);
 }

 @Test
 public void testAllocBase64Buffer() {
 // Test allocBase64Buffer method
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 // Test allocTokenBuffer method
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 // Test allocTokenBuffer method with minSize
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertEquals(10, tokenBuffer.length);
 }

 @Test
 public void testAllocConcatBuffer() {
 // Test allocConcatBuffer method
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBufferWithMinSize() {
 // Test allocNameCopyBuffer method with minSize
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertEquals(10, nameCopyBuffer.length);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 // Test releaseReadIOBuffer method
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithWrongBuffer() {
 byte[] readIOBuffer = new byte[10];
 ioContext.releaseReadIOBuffer(readIOBuffer);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 // Test releaseWriteEncodingBuffer method
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(writeEncodingBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithWrongBuffer() {
 byte[] writeEncodingBuffer = new byte[10];
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testReleaseBase64Buffer() {
 // Test releaseBase64Buffer method
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(base64Buffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64BufferWithWrongBuffer() {
 byte[] base64Buffer = new byte[10];
 ioContext.releaseBase64Buffer(base64Buffer);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testReleaseTokenBuffer() {
 // Test releaseTokenBuffer method
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(tokenBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWithWrongBuffer() {
 char[] tokenBuffer = new char[10];
 ioContext.releaseTokenBuffer(tokenBuffer);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testReleaseConcatBuffer() {
 // Test releaseConcatBuffer method
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(concatBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBufferWithWrongBuffer() {
 char[] concatBuffer = new char[10];
 ioContext.releaseConcatBuffer(concatBuffer);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 // Test releaseNameCopyBuffer method
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(nameCopyBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBufferWithWrongBuffer() {
 char[] nameCopyBuffer = new char[10];
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 fail("Expected IllegalArgumentException");
 }
}