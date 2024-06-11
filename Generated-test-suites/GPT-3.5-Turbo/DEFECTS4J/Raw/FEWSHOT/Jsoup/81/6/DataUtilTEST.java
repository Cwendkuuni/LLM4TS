package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
  private static final String CHARSET_UTF_8 = "UTF-8";
  private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
  private static final String BASE_URI = "http://example.com";

  @Test
  public void testLoadFile() throws IOException {
    // Prepare
    File file = new File("example.html");

    // Act
    Document document = DataUtil.load(file, CHARSET_UTF_8, BASE_URI);

    // Assert
    assertNotNull(document);
    assertEquals(CHARSET_UTF_8, document.outputSettings().charset().name());
  }

  @Test
  public void testLoadInputStream() throws IOException {
    // Prepare
    InputStream inputStream = new FileInputStream("example.html");

    // Act
    Document document = DataUtil.load(inputStream, CHARSET_UTF_8, BASE_URI);

    // Assert
    assertNotNull(document);
    assertEquals(CHARSET_UTF_8, document.outputSettings().charset().name());
  }

  @Test
  public void testLoadInputStreamWithParser() throws IOException {
    // Prepare
    InputStream inputStream = new FileInputStream("example.xml");

    // Act
    Document document = DataUtil.load(inputStream, CHARSET_UTF_8, BASE_URI, Parser.xmlParser());

    // Assert
    assertNotNull(document);
    assertEquals(CHARSET_UTF_8, document.outputSettings().charset().name());
  }

  @Test
  public void testCrossStreams() throws IOException {
    // Prepare
    byte[] inputBytes = "This is a test".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

    // Act
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataUtil.crossStreams(inputStream, outputStream);

    // Assert
    byte[] outputBytes = outputStream.toByteArray();
    assertNotNull(outputBytes);
    assertEquals(inputBytes.length, outputBytes.length);
    for (int i = 0; i < inputBytes.length; i++) {
      assertEquals(inputBytes[i], outputBytes[i]);
    }
  }

  @Test
  public void testParseInputStream() throws IOException {
    // Prepare
    InputStream inputStream = new FileInputStream("example.html");

    // Act
    Document document = DataUtil.parseInputStream(inputStream, CHARSET_UTF_8, BASE_URI, Parser.htmlParser());

    // Assert
    assertNotNull(document);
    assertEquals(CHARSET_UTF_8, document.outputSettings().charset().name());
  }

  @Test
  public void testReadToByteBuffer() throws IOException {
    // Prepare
    InputStream inputStream = new FileInputStream("example.html");

    // Act
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

    // Assert
    assertNotNull(byteBuffer);
    assertEquals(1024 * 32, byteBuffer.capacity());
  }

  @Test
  public void testReadToByteBufferWithMaxSize() throws IOException {
    // Prepare
    InputStream inputStream = new FileInputStream("example.html");
    int maxSize = 1024;

    // Act
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

    // Assert
    assertNotNull(byteBuffer);
    assertTrue(byteBuffer.capacity() <= maxSize);
  }

  @Test
  public void testReadFileToByteBuffer() throws IOException {
    // Prepare
    File file = new File("example.html");

    // Act
    ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

    // Assert
    assertNotNull(byteBuffer);
    assertEquals(file.length(), byteBuffer.capacity());
  }

  @Test
  public void testEmptyByteBuffer() {
    // Act
    ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

    // Assert
    assertNotNull(byteBuffer);
    assertEquals(0, byteBuffer.capacity());
  }

  @Test
  public void testGetCharsetFromContentType() {
    // Prepare
    String contentTypeWithCharset = "text/html; charset=UTF-8";
    String contentTypeWithoutCharset = "text/html";

    // Act
    String charsetWithCharset = DataUtil.getCharsetFromContentType(contentTypeWithCharset);
    String charsetWithoutCharset = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);

    // Assert
    assertEquals(CHARSET_UTF_8, charsetWithCharset);
    assertNull(charsetWithoutCharset);
  }
}