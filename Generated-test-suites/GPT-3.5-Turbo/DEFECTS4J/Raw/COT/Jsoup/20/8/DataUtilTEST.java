
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
  
  // Test case for load(File in, String charsetName, String baseUri)
  @Test
  public void testLoadFile() {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    try {
      Document document = DataUtil.load(file, charsetName, baseUri);
      assertNotNull(document);
    } catch(IOException e) {
      fail("IOException occurred: " + e.getMessage());
    }
  }
  
  // Test case for load(InputStream in, String charsetName, String baseUri)
  @Test
  public void testLoadInputStream() {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri);
      assertNotNull(document);
    } catch(IOException e) {
      fail("IOException occurred: " + e.getMessage());
    }
  }
  
  // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)
  @Test
  public void testLoadInputStreamWithParser() {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
      assertNotNull(document);
    } catch(IOException e) {
      fail("IOException occurred: " + e.getMessage());
    }
  }
  
  // Test case for parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
  @Test
  public void testParseByteData() {
    ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    assertNotNull(document);
  }
  
  // Test case for readToByteBuffer(InputStream inStream)
  @Test
  public void testReadToByteBuffer() {
    InputStream inputStream = new FileInputStream("test.html");
    try {
      ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
      assertNotNull(byteData);
    } catch(IOException e) {
      fail("IOException occurred: " + e.getMessage());
    }
  }
  
  // Test case for getCharsetFromContentType(String contentType)
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("UTF-8", charset);
  }
  
}

