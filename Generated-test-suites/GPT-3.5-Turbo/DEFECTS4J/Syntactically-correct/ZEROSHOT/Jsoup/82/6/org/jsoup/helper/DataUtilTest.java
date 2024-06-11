import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

  @Test
  public void testLoadFile() {
    File file = new File("test.html");
    try {
      Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }

  @Test
  public void testLoadInputStream() {
    String html = "<html><head><title>Test</title></head><body></body></html>";
    InputStream in = new ByteArrayInputStream(html.getBytes());
    try {
      Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }

  @Test
  public void testLoadInputStreamParser() {
    String html = "<html><head><title>Test</title></head><body></body></html>";
    InputStream in = new ByteArrayInputStream(html.getBytes());
    try {
      Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }

  @Test
  public void testCrossStreams() {
    byte[] inputBytes = {1, 2, 3, 4, 5};
    byte[] expectedBytes = {1, 2, 3, 4, 5};
    InputStream in = new ByteArrayInputStream(inputBytes);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      DataUtil.crossStreams(in, out);
      assertArrayEquals(expectedBytes, out.toByteArray());
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }

  @Test
  public void testParseInputStream() {
    String html = "<html><head><title>Test</title></head><body></body></html>";
    InputStream in = new ByteArrayInputStream(html.getBytes());
    try {
      Document doc = DataUtil.parseInputStream(in, "UTF-8", "http://example.com", Parser.htmlParser());
      assertNotNull(doc);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }

  @Test
  public void testReadToByteBuffer() {
    byte[] expectedBytes = {1, 2, 3, 4, 5};
    InputStream in = new ByteArrayInputStream(expectedBytes);
    try {
      ByteBuffer buffer = DataUtil.readToByteBuffer(in, 5);
      byte[] actualBytes = buffer.array();
      assertArrayEquals(expectedBytes, actualBytes);
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
  }

  @Test
  public void testMimeBoundary() {
    String mimeBoundary = DataUtil.mimeBoundary();
    assertNotNull(mimeBoundary);
    assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
  }

}