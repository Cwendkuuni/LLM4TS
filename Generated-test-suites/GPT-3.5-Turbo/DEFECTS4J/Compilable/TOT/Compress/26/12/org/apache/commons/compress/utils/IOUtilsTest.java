package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;
import org.junit.Before;
import org.junit.After;

public class IOUtilsTest {
  
  private InputStream inputStream;
  private OutputStream outputStream;
  
  @Before
  public void setUp() {
    inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
    outputStream = new ByteArrayOutputStream();
  }
  
  @After
  public void tearDown() {
    try {
      inputStream.close();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testCopy_DefaultBufferSize() {
    try {
      long bytesCopied = IOUtils.copy(inputStream, outputStream);
      assertEquals("Bytes copied should be equal to the input stream length.", "Hello, World!".getBytes().length, bytesCopied);
      assertEquals("Content of the output stream should be equal to the input stream.", "Hello, World!", outputStream.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testCopy_CustomBufferSize() {
    try {
      long bytesCopied = IOUtils.copy(inputStream, outputStream, 16);
      assertEquals("Bytes copied should be equal to the input stream length.", "Hello, World!".getBytes().length, bytesCopied);
      assertEquals("Content of the output stream should be equal to the input stream.", "Hello, World!", outputStream.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testSkip_PositiveNumberToSkip() {
    try {
      long bytesSkipped = IOUtils.skip(inputStream, 7);
      assertEquals("Bytes skipped should be equal to the specified value.", 7, bytesSkipped);
      assertEquals("Content of the input stream should be correctly skipped.", "World!", inputStream.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testSkip_NegativeNumberToSkip() {
    try {
      long bytesSkipped = IOUtils.skip(inputStream, -5);
      assertEquals("Bytes skipped should be equal to the specified value.", 0, bytesSkipped);
      assertEquals("Content of the input stream should not be skipped.", "Hello, World!", inputStream.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testReadFully_BufferSizedEqualToArrayLength() {
    byte[] buffer = new byte[13];
    try {
      int bytesread = IOUtils.readFully(inputStream, buffer);
      assertEquals("Bytes read should be equal to the buffer size.", 13, bytesread);
      assertEquals("Content of the input stream should be completely read.", "Hello, World!", new String(buffer));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testReadFully_BufferSizedSmallerThanArrayLength() {
    byte[] buffer = new byte[10];
    try {
      int bytesread = IOUtils.readFully(inputStream, buffer);
      assertEquals("Bytes read should be equal to the specified buffer size.", 10, bytesread);
      assertEquals("Content of the input stream should be partially read.", "Hello, Wor", new String(buffer));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testToByteArray() {
    byte[] byteArray;
    try {
      byteArray = IOUtils.toByteArray(inputStream);
      assertArrayEquals("Content of the input stream should be correctly converted to a byte array.", "Hello, World!".getBytes(), byteArray);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testCloseQuietly_CloseableNotNull() {
    Closeable closeable = new Closeable() {
      @Override
      public void close() throws IOException {
        // Do nothing here
      }
    };
    try {
      IOUtils.closeQuietly(closeable);
    } catch (Exception e) {
      fail("Exception should not be thrown when closing a valid Closeable object.");
    }
  }
  
  @Test
  public void testCloseQuietly_CloseableNull() {
    Closeable closeable = null;
    try {
      IOUtils.closeQuietly(closeable);
    } catch (Exception e) {
      fail("Exception should not be thrown when closing a null Closeable object.");
    }
  }
}

