import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DataUtilTest {

  @Test
  public void testLoadWithFile() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com/";
    Document doc = DataUtil.load(file, charsetName, baseUri);
    Assert.assertNotNull(doc);
  }

  @Test
  public void testLoadWithInputStream() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com/";
    Document doc = DataUtil.load(inputStream, charsetName, baseUri);
    Assert.assertNotNull(doc);
  }

  @Test
  public void testLoadWithParser() throws IOException {
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com/";
    Parser parser = Parser.htmlParser();
    Document doc = DataUtil.load(file, charsetName, baseUri, parser);
    Assert.assertNotNull(doc);
  }

  @Test
  public void testParseByteData() {
    ByteBuffer byteData = ByteBuffer.allocate(1024);
    String charsetName = "UTF-8";
    String baseUri = "http://example.com/";
    Parser parser = Parser.htmlParser();
    Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    Assert.assertNotNull(doc);
  }

  @Test
  public void testReadToByteBufferWithMaxSize() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    int maxSize = 1024;
    ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
    Assert.assertNotNull(byteData);
  }

  @Test
  public void testReadToByteBufferWithoutMaxSize() throws IOException {
    InputStream inputStream = new FileInputStream("test.html");
    ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
    Assert.assertNotNull(byteData);
  }

  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    Assert.assertEquals("UTF-8", charset);
  }
}