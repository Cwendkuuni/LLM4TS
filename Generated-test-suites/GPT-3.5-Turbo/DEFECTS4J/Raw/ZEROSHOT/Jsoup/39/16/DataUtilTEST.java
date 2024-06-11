import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {
  
  // Test load(File, String, String) method
  @Test
  public void testLoad_withFile() {
    try {
      File file = new File("test.html");
      Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");
      
      assertNotNull(document);
      assertEquals("https://www.example.com", document.baseUri());
    } catch(IOException e) {
      fail("An unexpected exception was thrown.");
    }
  }
  
  // Test load(InputStream, String, String) method
  @Test
  public void testLoad_withInputStream() {
    try {
      InputStream inputStream = new FileInputStream("test.html");
      Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
      
      assertNotNull(document);
      assertEquals("https://www.example.com", document.baseUri());
    } catch(IOException e) {
      fail("An unexpected exception was thrown.");
    }
  }
  
  // Test load(InputStream, String, String, Parser) method
  @Test
  public void testLoad_withParser() {
    try {
      InputStream inputStream = new FileInputStream("test.html");
      Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
      
      assertNotNull(document);
      assertEquals("https://www.example.com", document.baseUri());
    } catch(IOException e) {
      fail("An unexpected exception was thrown.");
    }
  }
  
  // Test parseByteData(ByteBuffer, String, String, Parser) method
  @Test
  public void testParseByteData() {
    String htmlData = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    ByteBuffer byteBuffer = ByteBuffer.wrap(htmlData.getBytes(Charset.forName("UTF-8")));
    Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "https://www.example.com", Parser.htmlParser());
    
    assertNotNull(document);
    assertEquals("https://www.example.com", document.baseUri());
    assertEquals("Test", document.title());
    assertEquals("Hello World!", document.body().text());
  }
  
  // Test readToByteBuffer(InputStream, int) method
  @Test
  public void testReadToByteBuffer() {
    try {
      InputStream inputStream = new FileInputStream("test.html");
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
      
      assertNotNull(byteBuffer);
      assertTrue(byteBuffer.capacity() > 0);
    } catch(IOException e) {
      fail("An unexpected exception was thrown.");
    }
  }
  
  // Test readToByteBuffer(InputStream) method
  @Test
  public void testReadToByteBuffer_withInputStream() {
    try {
      InputStream inputStream = new FileInputStream("test.html");
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
      
      assertNotNull(byteBuffer);
      assertTrue(byteBuffer.capacity() > 0);
    } catch(IOException e) {
      fail("An unexpected exception was thrown.");
    }
  }
  
  // Test getCharsetFromContentType(String) method
  @Test
  public void testGetCharsetFromContentType() {
    String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
    assertEquals("UTF-8", charset);
    
    charset = DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP");
    assertEquals("EUC-JP", charset);
    
    charset = DataUtil.getCharsetFromContentType("text/html");
    assertNull(charset);
    
    charset = DataUtil.getCharsetFromContentType(null);
    assertNull(charset);
  }
}