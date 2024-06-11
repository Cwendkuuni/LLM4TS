package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {
  
  private String baseUri;
  private Parser parser;
  
  @Before
  public void setUp() {
    baseUri = "https://example.com";
    parser = Parser.htmlParser();
  }
  
  @Test
  public void testLoadFromFile() throws IOException {
    File file = new File("test.html");
    Document document = DataUtil.load(file, "UTF-8", baseUri);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testLoadFromInputStream() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testLoadWithParser() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    Document document = DataUtil.load(inputStream, "UTF-8", baseUri, parser);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testCrossStreams() throws IOException {
    // TODO: Implement test
  }
  
  @Test
  public void testParseInputStream() throws IOException {
    File file = new File("test.html");
    InputStream inputStream = new FileInputStream(file);
    Document document = DataUtil.parseInputStream(inputStream, "UTF-8", baseUri, parser);
    assertNotNull(document);
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testReadToByteBuffer() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
    assertNotNull(byteBuffer);
    assertEquals(1024, byteBuffer.capacity());
  }
  
  @Test
  public void testReadToByteBufferWithMaxSize() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
    assertNotNull(byteBuffer);
  }
  
  @Test
  public void testReadFileToByteBuffer() throws IOException {
    File file = new File("test.html");
    ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
    assertNotNull(byteBuffer);
  }
  
  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
    assertNotNull(byteBuffer);
    assertEquals(0, byteBuffer.capacity());
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("UTF-8", charset);
  }
  
  @Test
  public void testValidateCharset() {
    String cs = "UTF-8";
    String validatedCharset = DataUtil.validateCharset(cs);
    assertEquals(cs, validatedCharset);
  }
  
  @Test
  public void testMimeBoundary() {
    String mimeBoundary = DataUtil.mimeBoundary();
    assertNotNull(mimeBoundary);
    assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
  }
  
  @Test
  public void testDetectCharsetFromBom() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
    byteBuffer.put((byte) 0xEF);
    byteBuffer.put((byte) 0xBB);
    byteBuffer.put((byte) 0xBF);
    byteBuffer.put((byte) 0x00);
    byteBuffer.rewind();
    DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
    assertNotNull(bomCharset);
    assertEquals("UTF-8", bomCharset.charset);
    assertTrue(bomCharset.offset);
  }
}