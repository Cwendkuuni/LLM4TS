import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {
  
  @Test
  public void testLoadFile() {
    // Test case for loading a file into a Document
    try {
      File file = new File("test.html");
      Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
      
      // Assert that the Document is not null
      assertNotNull(doc);
      
      // Assert that the Document has the correct base URI
      assertEquals("http://example.com", doc.baseUri());
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream() {
    // Test case for loading an input stream into a Document
    try {
      InputStream in = new FileInputStream("test.html");
      Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
      
      // Assert that the Document is not null
      assertNotNull(doc);
      
      // Assert that the Document has the correct base URI
      assertEquals("http://example.com", doc.baseUri());
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStreamWithParser() {
    // Test case for loading an input stream into a Document with a specific Parser
    try {
      InputStream in = new FileInputStream("test.html");
      Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
      
      // Assert that the Document is not null
      assertNotNull(doc);
      
      // Assert that the Document has the correct base URI
      assertEquals("http://example.com", doc.baseUri());
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseByteDataWithCharset() {
    // Test case for parsing byte data into a Document with a specified charset
    ByteBuffer byteData = ByteBuffer.wrap("Hello, world!".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    // Assert that the Document is not null
    assertNotNull(doc);
    
    // Assert that the Document has the correct base URI
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testParseByteDataWithoutCharset() {
    // Test case for parsing byte data into a Document without a specified charset
    ByteBuffer byteData = ByteBuffer.wrap("Hello, world!".getBytes());
    String charsetName = null;
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    // Assert that the Document is not null
    assertNotNull(doc);
    
    // Assert that the Document has the correct base URI
    assertEquals("http://example.com", doc.baseUri());
  }
  
  @Test
  public void testReadToByteBufferWithMaxSize() {
    // Test case for reading an input stream into a byte buffer with a maximum size
    try {
      InputStream in = new FileInputStream("test.html");
      int maxSize = 1024;
      
      ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
      
      // Assert that the byte buffer is not null
      assertNotNull(byteData);
      
      // Assert that the byte buffer size is less than or equal to the specified maximum size
      assertTrue(byteData.capacity() <= maxSize);
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBufferWithoutMaxSize() {
    // Test case for reading an input stream into a byte buffer without a maximum size
    try {
      InputStream in = new FileInputStream("test.html");
      
      ByteBuffer byteData = DataUtil.readToByteBuffer(in);
      
      // Assert that the byte buffer is not null
      assertNotNull(byteData);
    } catch (IOException e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    // Test case for extracting a charset from a content type header
    String contentType = "text/html; charset=UTF-8";
    
    String charset = DataUtil.getCharsetFromContentType(contentType);
    
    // Assert that the charset is not null
    assertNotNull(charset);
    
    // Assert that the charset is correct
    assertEquals("UTF-8", charset);
  }
}