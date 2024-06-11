import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
  
  private Document document;
  private DataUtil dataUtil;
  
  @Before
  public void setup() {
    document = new Document("");
    dataUtil = new DataUtil();
  }
  
  @Test
  public void testLoadFromFile() throws IOException {
    File input = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    Document loadedDocument = dataUtil.load(input, charsetName, baseUri);
    
    assertNotNull(loadedDocument);
    assertEquals(baseUri, loadedDocument.baseUri());
  }
  
  @Test
  public void testLoadFromInputStream() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    Document loadedDocument = dataUtil.load(inputStream, charsetName, baseUri);
    
    assertNotNull(loadedDocument);
    assertEquals(baseUri, loadedDocument.baseUri());
  }

  @Test
  public void testLoadFromInputStreamWithParser() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    Document loadedDocument = dataUtil.load(inputStream, charsetName, baseUri, parser);
    
    assertNotNull(loadedDocument);
    assertEquals(baseUri, loadedDocument.baseUri());
  }
  
  @Test
  public void testCrossStreams() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    File outputFile = new File("output.html");
    dataUtil.crossStreams(inputStream, outputFile);
    
    assertTrue(outputFile.exists());
  }
  
  @Test
  public void testParseInputStream() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    Parser parser = Parser.htmlParser();
    
    Document parsedDocument = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    
    assertNotNull(parsedDocument);
    assertEquals(baseUri, parsedDocument.baseUri());
  }
  
  @Test
  public void testReadToByteBuffer() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("test.html");
    int maxSize = 1024;
    
    ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);
    
    assertTrue(byteBuffer.capacity() <= maxSize);
  }
  
  @Test
  public void testEmptyByteBuffer() {
    ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();
    
    assertEquals(0, byteBuffer.capacity());
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    
    String charset = dataUtil.getCharsetFromContentType(contentType);
    
    assertEquals("UTF-8", charset);
  }
  
  @Test
  public void testGetCharsetFromContentTypeNoCharset() {
    String contentType = "text/html";
    
    String charset = dataUtil.getCharsetFromContentType(contentType);
    
    assertNull(charset);
  }
  
  @Test
  public void testValidateCharset() {
    String charset = "UTF-8";
    
    String validatedCharset = dataUtil.validateCharset(charset);
    
    assertEquals(charset, validatedCharset);
  }
  
  @Test
  public void testValidateCharsetInvalid() {
    String charset = "InvalidCharset";
    
    String validatedCharset = dataUtil.validateCharset(charset);
    
    assertNull(validatedCharset);
  }
  
  @Test
  public void testMimeBoundary() {
    String mimeBoundary = dataUtil.mimeBoundary();
    
    assertEquals(32, mimeBoundary.length());
  }
  
  @Test
  public void testDetectCharsetFromBom() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
    byteBuffer.put(new byte[]{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
    byteBuffer.flip();
    
    DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
    
    assertEquals("UTF-8", bomCharset.charset);
    assertTrue(bomCharset.offset);
  }
}