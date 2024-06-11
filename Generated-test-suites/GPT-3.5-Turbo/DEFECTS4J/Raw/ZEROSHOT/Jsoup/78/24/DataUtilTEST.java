package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {
  
  private File file;
  private InputStream inputStream;
  private String charsetName;
  private String baseUri;
  private Parser parser;
  
  @BeforeEach
  public void setup() {
    file = new File("test.html");
    charsetName = "UTF-8";
    baseUri = "http://example.com";
    parser = Parser.htmlParser();
  }
  
  @Test
  public void testLoadFile() {
    try {
      Document document = DataUtil.load(file, charsetName, baseUri);
      
      assertNotNull(document);
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream() {
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri);
      
      assertNotNull(document);
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadParser() {
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
      
      assertNotNull(document);
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testCrossStreams() {
    // Test method here
  }
  
  @Test
  public void testParseInputStream() {
    try {
      Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
      
      assertNotNull(document);
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBuffer() {
    try {
      ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
      
      assertNotNull(buffer);
      assertTrue(buffer.capacity() > 0);
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadFileToByteBuffer() {
    try {
      ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
      
      assertNotNull(buffer);
      assertTrue(buffer.capacity() > 0);
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer buffer = DataUtil.emptyByteBuffer();
    
    assertNotNull(buffer);
    assertEquals(0, buffer.capacity());
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    
    String charset = DataUtil.getCharsetFromContentType(contentType);
    
    assertEquals("UTF-8", charset);
  }
}