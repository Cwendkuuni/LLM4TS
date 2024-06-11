package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
  @Test
  public void testLoadFromInputStreamWithCharsetNameAndBaseUri() throws IOException {
    String htmlData = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));

    Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");

    assertNotNull(document);
    assertEquals("Test", document.title());
  }

  @Test
  public void testLoadFromFileWithCharsetNameAndBaseUri() throws IOException {
    File file = new File("test.html");

    Document document = DataUtil.load(file, "UTF-8", "https://example.com");

    assertNotNull(document);
    assertEquals("Test", document.title());
  }

  @Test
  public void testLoadFromInputStreamWithCharsetNameAndBaseUriAndParser() throws IOException {
    String htmlData = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));

    Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.htmlParser());

    assertNotNull(document);
    assertEquals("Test", document.title());
  }

  @Test
  public void testParseByteDataWithCharsetNameAndBaseUri() {
    ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
    String charsetName = "UTF-8";
    String baseUri = "https://example.com";
    Parser parser = Parser.htmlParser();

    Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

    assertNotNull(document);
    assertEquals("Test", document.title());
    assertEquals(charsetName, document.outputSettings().charset().name());
  }

  @Test
  public void testReadToByteBufferWithMaxSize() throws IOException {
    byte[] data = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64};
    InputStream inputStream = new ByteArrayInputStream(data);

    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);

    assertNotNull(byteBuffer);
    assertEquals(5, byteBuffer.capacity());
    assertEquals(0x48, byteBuffer.get(0));
    assertEquals(0x69, byteBuffer.get(4));
  }

  @Test
  public void testReadToByteBuffer() throws IOException {
    byte[] data = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64};
    InputStream inputStream = new ByteArrayInputStream(data);

    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

    assertNotNull(byteBuffer);
    assertEquals(data.length, byteBuffer.capacity());
    assertEquals(0x48, byteBuffer.get(0));
    assertEquals(0x64, byteBuffer.get(10));
  }

  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";

    String charset = DataUtil.getCharsetFromContentType(contentType);

    assertNotNull(charset);
    assertEquals("UTF-8", charset);
  }
}