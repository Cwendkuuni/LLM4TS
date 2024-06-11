package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
  
  @Test
  public void testLoadFile() {
    File in = new File("file.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.load(in, charsetName, baseUri);
      Assert.assertNotNull(doc);
      Assert.assertEquals(baseUri, doc.baseUri());
      Assert.assertEquals("text/html", doc.outputSettings().contentType());
    } catch (IOException e) {
      Assert.fail("Failed to load file: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream() {
    InputStream in = getClass().getResourceAsStream("file.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.load(in, charsetName, baseUri);
      Assert.assertNotNull(doc);
      Assert.assertEquals(baseUri, doc.baseUri());
      Assert.assertEquals("text/html", doc.outputSettings().contentType());
    } catch (IOException e) {
      Assert.fail("Failed to load input stream: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStreamWithParser() {
    InputStream in = getClass().getResourceAsStream("file.xml");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.xmlParser();
    
    try {
      Document doc = DataUtil.load(in, charsetName, baseUri, parser);
      Assert.assertNotNull(doc);
      Assert.assertEquals(baseUri, doc.baseUri());
      Assert.assertEquals("text/xml", doc.outputSettings().contentType());
    } catch (IOException e) {
      Assert.fail("Failed to load input stream with parser: " + e.getMessage());
    }
  }
  
  @Test
  public void testCrossStreams() {
    InputStream in = getClass().getResourceAsStream("file.html");
    OutputStream out = new ByteArrayOutputStream();
    
    try {
      DataUtil.crossStreams(in, out);
      String result = out.toString();
      Assert.assertNotNull(result);
      Assert.assertTrue(result.contains("Example Domain"));
    } catch (IOException e) {
      Assert.fail("Failed to cross streams: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseInputStream() {
    InputStream in = getClass().getResourceAsStream("file.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    try {
      Document doc = DataUtil.parseInputStream(in, charsetName, baseUri, Parser.htmlParser());
      Assert.assertNotNull(doc);
      Assert.assertEquals(baseUri, doc.baseUri());
      Assert.assertEquals("text/html", doc.outputSettings().contentType());
    } catch (IOException e) {
      Assert.fail("Failed to parse input stream: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBuffer() {
    InputStream in = getClass().getResourceAsStream("file.html");
    
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
      Assert.assertNotNull(byteBuffer);
      Assert.assertTrue(byteBuffer.limit() > 0);
    } catch (IOException e) {
      Assert.fail("Failed to read to byte buffer: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBufferWithMaxSize() {
    InputStream in = getClass().getResourceAsStream("file.html");
    int maxSize = 0;
    
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
      Assert.assertNotNull(byteBuffer);
      Assert.assertTrue(byteBuffer.limit() > 0);
    } catch (IOException e) {
      Assert.fail("Failed to read to byte buffer with max size: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBufferFromFile() {
    File file = new File("file.html");
    
    try {
      ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
      Assert.assertNotNull(byteBuffer);
      Assert.assertTrue(byteBuffer.limit() > 0);
    } catch (IOException e) {
      Assert.fail("Failed to read file to byte buffer: " + e.getMessage());
    }
  }
  
  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
    Assert.assertNotNull(byteBuffer);
    Assert.assertEquals(0, byteBuffer.limit());
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertEquals("UTF-8", charset);
  }
  
  @Test
  public void testGetCharsetFromContentTypeNotFound() {
    String contentType = "text/html";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertNull(charset);
  }
  
  @Test
  public void testMimeBoundary() {
    String mimeBoundary = DataUtil.mimeBoundary();
    Assert.assertNotNull(mimeBoundary);
    Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
  }
  
  @Test
  public void testDetectCharsetFromBom() {
    ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
    BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
    Assert.assertNotNull(bomCharset);
    Assert.assertEquals("UTF-8", bomCharset.charset);
    Assert.assertTrue(bomCharset.offset);
  }
}