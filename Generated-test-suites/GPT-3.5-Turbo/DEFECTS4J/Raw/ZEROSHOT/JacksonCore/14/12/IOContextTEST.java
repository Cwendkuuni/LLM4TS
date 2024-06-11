package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

  private IOContext ioContext;

  @Before
  public void setUp() {
    ioContext = new IOContext(null, null, false);
  }

  @Test
  public void testSetEncoding() {
    ioContext.setEncoding(JsonEncoding.UTF8);
    assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
  }

  @Test
  public void testWithEncoding() {
    ioContext.setEncoding(JsonEncoding.UTF8);
    IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF16_BE);
    assertEquals(JsonEncoding.UTF16_BE, newContext.getEncoding());
  }

  @Test
  public void testGetSourceReference() {
    Object sourceRef = new Object();
    IOContext context = new IOContext(null, sourceRef, false);
    assertEquals(sourceRef, context.getSourceReference());
  }

  @Test
  public void testIsResourceManaged() {
    assertTrue(ioContext.isResourceManaged());
  }

  @Test
  public void testConstructTextBuffer() {
    TextBuffer textBuffer = ioContext.constructTextBuffer();
    assertNotNull(textBuffer);
  }

  @Test
  public void testAllocReadIOBuffer() {
    byte[] buffer = ioContext.allocReadIOBuffer();
    assertNotNull(buffer);
  }

  @Test
  public void testAllocReadIOBufferWithMinSize() {
    int minSize = 100;
    byte[] buffer = ioContext.allocReadIOBuffer(minSize);
    assertNotNull(buffer);
    assertTrue(buffer.length >= minSize);
  }

  @Test
  public void testAllocWriteEncodingBuffer() {
    byte[] buffer = ioContext.allocWriteEncodingBuffer();
    assertNotNull(buffer);
  }

  @Test
  public void testAllocWriteEncodingBufferWithMinSize() {
    int minSize = 100;
    byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
    assertNotNull(buffer);
    assertTrue(buffer.length >= minSize);
  }

  @Test
  public void testAllocBase64Buffer() {
    byte[] buffer = ioContext.allocBase64Buffer();
    assertNotNull(buffer);
  }

  @Test
  public void testAllocTokenBuffer() {
    char[] buffer = ioContext.allocTokenBuffer();
    assertNotNull(buffer);
  }

  @Test
  public void testAllocTokenBufferWithMinSize() {
    int minSize = 100;
    char[] buffer = ioContext.allocTokenBuffer(minSize);
    assertNotNull(buffer);
    assertTrue(buffer.length >= minSize);
  }

  @Test
  public void testAllocConcatBuffer() {
    char[] buffer = ioContext.allocConcatBuffer();
    assertNotNull(buffer);
  }

  @Test
  public void testAllocNameCopyBuffer() {
    int minSize = 100;
    char[] buffer = ioContext.allocNameCopyBuffer(minSize);
    assertNotNull(buffer);
    assertTrue(buffer.length >= minSize);
  }

  @Test
  public void testReleaseReadIOBuffer() {
    byte[] buffer = ioContext.allocReadIOBuffer();
    ioContext.releaseReadIOBuffer(buffer);
    assertNull(ioContext._readIOBuffer);
  }

  @Test
  public void testReleaseWriteEncodingBuffer() {
    byte[] buffer = ioContext.allocWriteEncodingBuffer();
    ioContext.releaseWriteEncodingBuffer(buffer);
    assertNull(ioContext._writeEncodingBuffer);
  }

  @Test
  public void testReleaseBase64Buffer() {
    byte[] buffer = ioContext.allocBase64Buffer();
    ioContext.releaseBase64Buffer(buffer);
    assertNull(ioContext._base64Buffer);
  }

  @Test
  public void testReleaseTokenBuffer() {
    char[] buffer = ioContext.allocTokenBuffer();
    ioContext.releaseTokenBuffer(buffer);
    assertNull(ioContext._tokenCBuffer);
  }

  @Test
  public void testReleaseConcatBuffer() {
    char[] buffer = ioContext.allocConcatBuffer();
    ioContext.releaseConcatBuffer(buffer);
    assertNull(ioContext._concatCBuffer);
  }

  @Test
  public void testReleaseNameCopyBuffer() {
    int minSize = 100;
    char[] buffer = ioContext.allocNameCopyBuffer(minSize);
    ioContext.releaseNameCopyBuffer(buffer);
    assertNull(ioContext._nameCopyBuffer);
  }
}