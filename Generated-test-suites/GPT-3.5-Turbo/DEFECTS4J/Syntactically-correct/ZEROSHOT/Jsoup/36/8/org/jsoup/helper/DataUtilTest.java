import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
  
  private File testFile;
  
  @Before
  public void setUp() {
    testFile = new File("test.txt");
  }
  
  @Test
  public void testLoadFile() {
    try {
      Document document = DataUtil.load(testFile, "UTF-8", "https://www.example.com");
      assertNotNull("Document should not be null", document);
    }
    catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream() {
    try {
      FileInputStream inStream = new FileInputStream(testFile);
      Document document = DataUtil.load(inStream, "UTF-8", "https://www.example.com");
      assertNotNull("Document should not be null", document);
      inStream.close();
    }
    catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStreamWithParser() {
    try {
      FileInputStream inStream = new FileInputStream(testFile);
      Document document = DataUtil.load(inStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
      assertNotNull("Document should not be null", document);
      inStream.close();
    }
    catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseByteData() {
    try {
      FileInputStream inStream = new FileInputStream(testFile);
      ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
      Document document = DataUtil.parseByteData(byteData, "UTF-8", "https://www.example.com", Parser.htmlParser());
      assertNotNull("Document should not be null", document);
      inStream.close();
    }
    catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testReadToByteBuffer() {
    try {
      FileInputStream inStream = new FileInputStream(testFile);
      ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 100);
      assertNotNull("Byte buffer should not be null", byteData);
      inStream.close();
    }
    catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    String charset = DataUtil.getCharsetFromContentType(contentType);
    assertEquals("Charset should be UTF-8", "UTF-8", charset);
  }

}