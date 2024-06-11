package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.*;
import java.io.*;

public class DataUtilTest {
  
  @Test
  public void testLoadFile_withValidFile_returnsDocument() {
    File file = new File("path/to/file");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.load(file, charsetName, baseUri);
      Assert.assertNotNull(doc);
    } catch (IOException e) {
      Assert.fail("IO error occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadFile_withInvalidFile_throwsIOException() {
    File file = new File("path/to/invalid/file");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.load(file, charsetName, baseUri);
      Assert.fail("No exception thrown");
    } catch (IOException e) {
      // Exception should be thrown
    }
  }
  
  @Test
  public void testLoadInputStream_withValidInputStream_returnsDocument() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.load(inputStream, charsetName, baseUri);
      Assert.assertNotNull(doc);
    } catch (IOException e) {
      Assert.fail("IO error occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream_withInvalidInputStream_throwsIOException() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.load(inputStream, charsetName, baseUri);
      Assert.fail("No exception thrown");
    } catch (IOException e) {
      // Exception should be thrown
    }
  }
  
  @Test
  public void testLoadInputStream_withValidInputStreamAndParser_returnsDocument() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    try {
      Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
      Assert.assertNotNull(doc);
    } catch (IOException e) {
      Assert.fail("IO error occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream_withInvalidInputStreamAndParser_throwsIOException() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    try {
      Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
      Assert.fail("No exception thrown");
    } catch (IOException e) {
      // Exception should be thrown
    }
  }
  
  @Test
  public void testParseByteData_withValidByteDataAndCharset_returnsDocument() {
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    Assert.assertNotNull(doc);
  }
  
  @Test
  public void testParseByteData_withInvalidByteDataAndCharset_returnsNull() {
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    Assert.assertNull(doc);
  }
  
  @Test
  public void testReadToByteBuffer_withValidInputStreamAndMaxSize_returnsByteBuffer() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    int maxSize = 1024;
    
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
      Assert.assertNotNull(byteBuffer);
    } catch (IOException e) {
      Assert.fail("IO error occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBuffer_withInvalidInputStreamAndMaxSize_throwsIOException() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    int maxSize = -1;
    
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
      Assert.fail("No exception thrown");
    } catch (IOException e) {
      // Exception should be thrown
    }
  }
  
  @Test
  public void testReadToByteBuffer_withValidInputStream_returnsByteBuffer() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
      Assert.assertNotNull(byteBuffer);
    } catch (IOException e) {
      Assert.fail("IO error occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBuffer_withInvalidInputStream_throwsIOException() {
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
      Assert.fail("No exception thrown");
    } catch (IOException e) {
      // Exception should be thrown
    }
  }
  
  @Test
  public void testGetCharsetFromContentType_withValidContentType_returnsCharset() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertEquals("UTF-8", charset);
  }
  
  @Test
  public void testGetCharsetFromContentType_withNoCharset_returnsNull() {
    String contentType = "text/html";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertNull(charset);
  }
  
  @Test
  public void testGetCharsetFromContentType_withUnsupportedCharset_returnsNull() {
    String contentType = "text/html; charset=invalidCharset";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertNull(charset);
  }
  
  @Test
  public void testGetCharsetFromContentType_withEmptyCharset_returnsNull() {
    String contentType = "text/html; charset=";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertNull(charset);
  }
  
  @Test
  public void testGetCharsetFromContentType_withInvalidContentType_returnsNull() {
    String contentType = "invalidContentType";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertNull(charset);
  }
  
}