import org.jsoup.helper.DataUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

  @Test
  public void testLoadFile() {
    // Test case 1
    try {
      DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
    
    // Test case 2
    try {
      DataUtil.load(new File("test.html"), "ISO-8859-1", "http://example.com");
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testLoadInputStream() {
    // Test case 1
    try {
      DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com");
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
    
    // Test case 2
    try {
      DataUtil.load(new FileInputStream("test.html"), "ISO-8859-1", "http://example.com");
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testLoadInputStreamParser() {
    // Test case 1
    try {
      DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com", Parser.htmlParser());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
    
    // Test case 2
    try {
      DataUtil.load(new FileInputStream("test.html"), "ISO-8859-1", "http://example.com", Parser.htmlParser());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testCrossStreams() {
    // Test case 1
    try {
      DataUtil.crossStreams(new FileInputStream("input.txt"), new FileOutputStream("output.txt"));
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testParseByteData() {
    // Test case 1
    ByteBuffer byteData = ByteBuffer.allocate(1024);
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    
    // Test case 2
    byteData = ByteBuffer.allocate(1024);
    charsetName = "ISO-8859-1";
    baseUri = "http://example.com";
    parser = Parser.htmlParser();
    DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
  }

  @Test
  public void testReadToByteBuffer() {
    // Test case 1
    try {
      DataUtil.readToByteBuffer(new FileInputStream("input.txt"), 1024);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
    
    // Test case 2
    try {
      DataUtil.readToByteBuffer(new FileInputStream("input.txt"));
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testReadFileToByteBuffer() {
    // Test case 1
    try {
      DataUtil.readFileToByteBuffer(new File("test.txt"));
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
    assertEquals(0, byteBuffer.capacity());
  }

  @Test
  public void testGetCharsetFromContentType() {
    // Test case 1
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("UTF-8", charset);
    
    // Test case 2
    contentType = "text/html; charset=ISO-8859-1";
    charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("ISO-8859-1", charset);
    
    // Test case 3
    contentType = "text/html";
    charset = DataUtil.getCharsetFromContentType(contentType);
    assertNull(charset);
  }

  @Test
  public void testMimeBoundary() {
    String boundary = DataUtil.mimeBoundary();
    assertNotNull(boundary);
    assertEquals(32, boundary.length());
  }

  @Test
  public void testDetectCharsetFromBom() {
    // Test case 1
    ByteBuffer byteData = ByteBuffer.allocate(1024);
    String charsetName = "UTF-8";
    String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
    assertEquals(charsetName, detectedCharset);
    
    // Test case 2
    charsetName = "ISO-8859-1";
    detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
    assertEquals(charsetName, detectedCharset);
  }
}