package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

 private IOContext ioContext;

 @Before
 public void setup() {
 ioContext = new IOContext(new BufferRecycler(), new Object(), true);
 }

 @Test
 public void testConstructor() {
 // Testing the constructor with valid arguments
 IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
 assertNotNull(ioContext);
 assertEquals(new Object(), ioContext.getSourceReference());
 assertTrue(ioContext.isResourceManaged());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullBufferRecycler() {
 IOContext ioContext = new IOContext(null, new Object(), true);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullSourceRef() {
 IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
 fail("Should throw IllegalArgumentException");
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
 IOContext ioContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test(expected = NullPointerException.class)
 public void testSetEncodingWithNullEncoding() {
 ioContext.setEncoding(null);
 fail("Should throw NullPointerException");
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
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertArrayEquals(new byte[10], readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAllocReadIOBufferWithNegativeMinSize() {
 ioContext.allocReadIOBuffer(-1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertArrayEquals(new byte[10], writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAllocWriteEncodingBufferWithNegativeMinSize() {
 ioContext.allocWriteEncodingBuffer(-1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[10], tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAllocTokenBufferWithNegativeMinSize() {
 ioContext.allocTokenBuffer(-1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertArrayEquals(new char[10], nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAllocNameCopyBufferWithNegativeMinSize() {
 ioContext.allocNameCopyBuffer(-1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext.getBase64Buffer());
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }
}