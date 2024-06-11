import org.jsoup.helper.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.*;

import java.io.*;

public class DataUtilTest {
  
  @Test
  public void testLoadFile() {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    try {
      Document doc = DataUtil.load(file, charsetName, baseUri);
      assertNotNull(doc);
    } catch(IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream() {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    try {
      Document doc = DataUtil.load(in, charsetName, baseUri);
      assertNotNull(doc);
    } catch(IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStreamWithParser() {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    try {
      Document doc = DataUtil.load(in, charsetName, baseUri, parser);
      assertNotNull(doc);
    } catch(IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseByteData() {
    ByteBuffer byteData = ByteBuffer.allocate(10);
    byteData.put("test".getBytes());
    byteData.rewind();
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    assertNotNull(doc);
  }
  
  @Test
  public void testReadToByteBufferWithMaxSize() {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    int maxSize = 10;
    try {
      ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
      assertNotNull(byteData);
    } catch(IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBuffer() {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    try {
      ByteBuffer byteData = DataUtil.readToByteBuffer(in);
      assertNotNull(byteData);
    } catch(IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("UTF-8", charset);
  }
  
  @Test
  public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
    String contentType = "text/html; charset=invalidcharset";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertNull(charset);
  }
  
  @Test
  public void testGetCharsetFromContentTypeWithEmptyCharset() {
    String contentType = "text/html; charset=";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertNull(charset);
  }
  
}