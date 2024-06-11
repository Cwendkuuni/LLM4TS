import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;

public class DataUtilTest {
  
  @Test
  public void testLoadFile() throws IOException {
    File file = new File("test.html");
    String charset = "UTF-8";
    String baseUri = "http://example.com";
    Document document = DataUtil.load(file, charset, baseUri);
    assertNotNull(document);
  }

  @Test
  public void testLoadInputStream() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charset = "UTF-8";
    String baseUri = "http://example.com";
    Document document = DataUtil.load(inputStream, charset, baseUri);
    assertNotNull(document);
  }

  @Test
  public void testLoadInputStreamWithParser() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charset = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document document = DataUtil.load(inputStream, charset, baseUri, parser);
    assertNotNull(document);
  }

  @Test
  public void testCrossStreams() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("input.txt");
    OutputStream outputStream = System.out;
    DataUtil.crossStreams(inputStream, outputStream);
  }

  @Test
  public void testParseInputStream() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charset = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
    assertNotNull(document);
  }

  @Test
  public void testReadToByteBuffer() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    int maxSize = 0;
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
    assertNotNull(byteBuffer);
  }

  @Test
  public void testReadToByteBufferWithMaxSize() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    int maxSize = 1024 * 1024;
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
    assertNotNull(byteBuffer);
  }

  @Test
  public void testReadToByteBufferFromFile() throws IOException {
    File file = new File("test.txt");
    ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
    assertNotNull(byteBuffer);
  }

  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
    assertNotNull(byteBuffer);
    assertEquals(byteBuffer.capacity(), 0);
  }

  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertNotNull(charset);
    assertEquals(charset, "UTF-8");
  }

  @Test
  public void testGetCharsetFromContentTypeWithInvalidCharset() {
    String contentType = "text/html; charset=invalid";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertNull(charset);
  }

  @Test
  public void testMimeBoundary() {
    String mimeBoundary = DataUtil.mimeBoundary();
    assertNotNull(mimeBoundary);
    assertEquals(mimeBoundary.length(), 32);
  }
  
}