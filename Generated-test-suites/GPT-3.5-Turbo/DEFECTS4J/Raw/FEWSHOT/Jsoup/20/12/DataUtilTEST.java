package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public class DataUtilTest {
  
  @Test
  public void testLoad_File() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    // Test when the file exists
    Document doc = DataUtil.load(file, charsetName, baseUri);
    assertNotNull(doc);
    
    // Test when the file doesn't exist
    File nonExistentFile = new File("nonexistent.html");
    IOException exception = assertThrows(IOException.class, ()-> DataUtil.load(nonExistentFile, charsetName, baseUri));
    assertEquals("File not found", exception.getMessage());
  }
  
  @Test
  public void testLoad_InputStream() throws IOException {
    InputStream in = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    // Test when the input stream is valid
    Document doc = DataUtil.load(in, charsetName, baseUri);
    assertNotNull(doc);
    
    // Test when the input stream is null
    InputStream nullIn = null;
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> DataUtil.load(nullIn, charsetName, baseUri));
    assertEquals("Input stream is null", exception.getMessage());
    
    // Test when the input stream is invalid
    InputStream invalidIn = new ByteArrayInputStream("invalid".getBytes());
    IOException exception = assertThrows(IOException.class, ()-> DataUtil.load(invalidIn, charsetName, baseUri));
    assertEquals("Invalid input stream", exception.getMessage());
  }
  
  @Test
  public void testLoad_InputStream_Parser() throws IOException {
    InputStream in = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    // Test when the input stream and parser are valid
    Document doc = DataUtil.load(in, charsetName, baseUri, parser);
    assertNotNull(doc);
    
    // Test when the input stream is null
    InputStream nullIn = null;
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> DataUtil.load(nullIn, charsetName, baseUri, parser));
    assertEquals("Input stream is null", exception.getMessage());
    
    // Test when the input stream is invalid
    InputStream invalidIn = new ByteArrayInputStream("invalid".getBytes());
    IOException exception = assertThrows(IOException.class, ()-> DataUtil.load(invalidIn, charsetName, baseUri, parser));
    assertEquals("Invalid input stream", exception.getMessage());
  }
  
  @Test
  public void testParseByteData() {
    ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    // Test when the charset name is null
    Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
    assertNotNull(doc);
    
    // Test when the charset name is not null
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    assertNotNull(doc);
  }
  
  @Test
  public void testReadToByteBuffer() throws IOException {
    InputStream in = new FileInputStream("test.html");
    
    // Test when the input stream is valid
    ByteBuffer byteData = DataUtil.readToByteBuffer(in);
    assertNotNull(byteData);
    
    // Test when the input stream is null
    InputStream nullIn = null;
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> DataUtil.readToByteBuffer(nullIn));
    assertEquals("Input stream is null", exception.getMessage());
    
    // Test when the input stream is empty
    InputStream emptyIn = new ByteArrayInputStream("".getBytes());
    ByteBuffer emptyByteData = DataUtil.readToByteBuffer(emptyIn);
    assertNotNull(emptyByteData);
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    
    // Test when the content type is valid
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("UTF-8", charset);
    
    // Test when the content type is null
    String nullContentType = null;
    String charset = DataUtil.getCharsetFromContentType(nullContentType);
    assertNull(charset);
    
    // Test when the content type doesn't contain a charset
    String noCharsetContentType = "text/html";
    String charset = DataUtil.getCharsetFromContentType(noCharsetContentType);
    assertNull(charset);
  }
}