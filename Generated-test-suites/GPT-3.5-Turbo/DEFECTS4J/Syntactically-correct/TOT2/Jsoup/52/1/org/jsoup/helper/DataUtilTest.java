import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
  
  @Test
  public void testLoadFromFile_ValidFile_ValidCharset_ValidBaseUri() throws IOException {
      File file = new File("test.html");
      String charset = "UTF-8";
      String baseUri = "http://example.com";
      
      Document document = DataUtil.load(file, charset, baseUri);
      
      assertNotNull(document);
  }
  
  @Test
  public void testLoadFromInputStream_ValidInputStream_ValidCharset_ValidBaseUri_ValidParser() throws IOException {
      String html = "<html><head></head><body></body></html>";
      ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
      String charset = "UTF-8";
      String baseUri = "http://example.com";
      
      Document document = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());
      
      assertNotNull(document);
  }
  
  @Test
  public void testLoadFromInputStream_InvalidInputStream_ThrowsIOException() {
      ByteArrayInputStream inputStream = null;
      String charset = "UTF-8";
      String baseUri = "http://example.com";
      
      assertThrows(IOException.class, () -> {
          DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());
      });
  }
  
  @Test
  public void testCrossStreams_ValidInputStream_ValidOutputStream() throws IOException {
      String text = "Hello, world!";
      ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
      
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      
      DataUtil.crossStreams(inputStream, outputStream);
      
      assertEquals(text, outputStream.toString());
  }

  @Test
  public void testReadToByteBuffer_ValidInputStream_ValidMaxSize() throws IOException {
      String text = "Hello, world!";
      ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
      int maxSize = text.length();
      
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
      
      assertEquals(text.getBytes(), byteBuffer.array());
  }

  @Test
  public void testReadToByteBuffer_ValidInputStream_UnlimitedMaxSize() throws IOException {
      String text = "Hello, world!";
      ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
      
      ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
      
      assertEquals(text.getBytes(), byteBuffer.array());
  }

  @Test
  public void testReadFileToByteBuffer_ValidFile() throws IOException {
      File file = new File("test.txt");
      byte[] content = "Hello, world!".getBytes();
      Files.write(file.toPath(), content);
      
      ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
      
      assertEquals(content, byteBuffer.array());
  }

  @Test
  public void testEmptyByteBuffer() {
      ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
      
      assertEquals(0, byteBuffer.capacity());
  }

  @Test
  public void testGetCharsetFromContentType_ContentWithCharset_ReturnsCharset() {
      String contentType = "text/html; charset=UTF-8";
      
      String charset = DataUtil.getCharsetFromContentType(contentType);
      
      assertEquals("UTF-8", charset);
  }

  @Test
  public void testGetCharsetFromContentType_NoCharsetInContent_ReturnsNull() {
      String contentType = "text/html";
      
      String charset = DataUtil.getCharsetFromContentType(contentType);
      
      assertNull(charset);
  }

  @Test
  public void testMimeBoundary() {
      String boundary = DataUtil.mimeBoundary();
      
      assertNotNull(boundary);
      assertEquals(DataUtil.boundaryLength, boundary.length());
  }

  @Test
  public void testDetectCharsetFromBom_UTF8BOM_ReturnsUTF8() {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
      String charsetName = "ISO-8859-1";
      
      String charset = DataUtil.detectCharsetFromBom(byteBuffer, charsetName);
      
      assertEquals("UTF-8", charset);
  }

  @Test
  public void testDetectCharsetFromBom_UnknownBOM_ReturnsOriginalCharset() {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});
      String charsetName = "UTF-16";
      
      String charset = DataUtil.detectCharsetFromBom(byteBuffer, charsetName);
      
      assertEquals("UTF-16", charset);
  }
}