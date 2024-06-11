import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Connection.Response;
import org.junit.Before;
import org.junit.After;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
  
  // Test case for load method with File input
  @Test
  public void testLoadFile() {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    try {
      Document doc = DataUtil.load(file, charsetName, baseUri);
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }
  
  // Test case for load method with InputStream input
  @Test
  public void testLoadInputStream() {
    String html = "<html><body><h1>Test</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(html.getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    try {
      Document doc = DataUtil.load(inputStream, charsetName, baseUri);
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }
  
  // Test case for load method with InputStream input and custom Parser
  @Test
  public void testLoadInputStreamWithParser() {
    String html = "<html><body><h1>Test</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(html.getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    try {
      Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }
  
  // Test case for crossStreams method
  @Test
  public void testCrossStreams() {
    byte[] input = "Hello, World!".getBytes();
    InputStream inputStream = new ByteArrayInputStream(input);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      DataUtil.crossStreams(inputStream, outputStream);
      byte[] output = outputStream.toByteArray();
      assertArrayEquals(input, output);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }
  
  // Test case for parseByteData method with specified charsetName
  @Test
  public void testParseByteDataWithCharsetName() {
    ByteBuffer byteData = ByteBuffer.allocate(0);
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    assertNotNull(doc);
    assertEquals(charsetName, doc.outputSettings().charset().displayName());
  }
  
  // Test case for parseByteData method without specified charsetName
  @Test
  public void testParseByteDataWithoutCharsetName() {
    ByteBuffer byteData = ByteBuffer.allocate(0);
    String charsetName = null;
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    assertNotNull(doc);
    assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset().displayName());
  }
  
  // Test case for readToByteBuffer method
  @Test
  public void testReadToByteBuffer() {
    String input = "Hello, World!";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    try {
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
      byte[] output = new byte[byteBuffer.remaining()];
      byteBuffer.get(output);
      assertArrayEquals(input.getBytes(), output);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }
  
  // Test case for readFileToByteBuffer method
  @Test
  public void testReadFileToByteBuffer() {
    File file = new File("test.txt");
    try {
      ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
      assertNotNull(byteBuffer);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }
  
  // Test case for emptyByteBuffer method
  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
    assertNotNull(byteBuffer);
    assertEquals(0, byteBuffer.capacity());
  }
  
  // Test case for getCharsetFromContentType method
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("UTF-8", charset);
  }
  
  // Test case for mimeBoundary method
  @Test
  public void testMimeBoundary() {
    String mimeBoundary = DataUtil.mimeBoundary();
    assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    assertRegex("[-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]+", mimeBoundary);
  }
  
  // Helper method to assert that a string matches a regular expression
  private void assertRegex(String regex, String value) {
    assertTrue("String does not match regex: " + regex + ", value: " + value, value.matches(regex));
  }
  
}