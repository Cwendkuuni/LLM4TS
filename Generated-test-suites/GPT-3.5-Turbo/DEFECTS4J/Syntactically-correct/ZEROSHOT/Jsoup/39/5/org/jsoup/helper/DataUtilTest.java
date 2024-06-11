import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {
  
  @Test
  public void testLoadFile() {
    // Test case 1: Loading a file with valid parameters
    File file = new File("test.html");
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com/";
    
    try {
      Document document = DataUtil.load(file, charsetName, baseUri);
      // Assert the document is not null
      assertNotNull(document);
      // Assert the document has the correct baseUri
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail(e.getMessage());
    }
    
    // Test case 2: Loading a file with invalid charsetName
    charsetName = "InvalidCharset";
    
    try {
      Document document = DataUtil.load(file, charsetName, baseUri);
      // Assert the document is not null
      assertNotNull(document);
      // Assert the document has the correct baseUri
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStream() {
    // Test case 1: Loading an InputStream with valid parameters
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(html.getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com/";
    
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri);
      // Assert the document is not null
      assertNotNull(document);
      // Assert the document has the correct baseUri
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail(e.getMessage());
    }
    
    // Test case 2: Loading an InputStream with invalid charsetName
    charsetName = "InvalidCharset";
    
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri);
      // Assert the document is not null
      assertNotNull(document);
      // Assert the document has the correct baseUri
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testLoadInputStreamParser() {
    // Test case 1: Loading an InputStream with valid parameters
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(html.getBytes());
    String charsetName = "UTF-8";
    String baseUri = "http://www.example.com/";
    Parser parser = Parser.htmlParser();
    
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
      // Assert the document is not null
      assertNotNull(document);
      // Assert the document has the correct baseUri
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail(e.getMessage());
    }
    
    // Test case 2: Loading an InputStream with invalid charsetName
    charsetName = "InvalidCharset";
    
    try {
      Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
      // Assert the document is not null
      assertNotNull(document);
      // Assert the document has the correct baseUri
      assertEquals(baseUri, document.baseUri());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testParseByteData() {
    // Test case 1: Parsing byteData with charsetName null
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
    String charsetName = null;
    String baseUri = "http://www.example.com/";
    Parser parser = Parser.htmlParser();
    
    Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    // Assert the document is not null
    assertNotNull(document);
    // Assert the document has the correct baseUri
    assertEquals(baseUri, document.baseUri());
    
    // Test case 2: Parsing byteData with valid charsetName
    charsetName = "UTF-8";
    
    document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    // Assert the document is not null
    assertNotNull(document);
    // Assert the document has the correct baseUri
    assertEquals(baseUri, document.baseUri());
  }
  
  @Test
  public void testReadToByteBuffer() {
    // Test case 1: Reading input stream into byte buffer with unlimited maxSize
    String html = "<html><body><h1>Hello, World!</h1></body></html>";
    InputStream inputStream = new ByteArrayInputStream(html.getBytes());
    
    try {
      ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
      // Assert the byte data is not null
      assertNotNull(byteData);
      // Assert the byte data has the correct length
      assertEquals(html.getBytes().length, byteData.array().length);
    } catch (IOException e) {
      fail(e.getMessage());
    }
    
    // Test case 2: Reading input stream into byte buffer with limited maxSize
    int maxSize = 10;
    
    try {
      ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
      // Assert the byte data is not null
      assertNotNull(byteData);
      // Assert the byte data has the correct length
      assertEquals(maxSize, byteData.array().length);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testGetCharsetFromContentType() {
    // Test case 1: Getting charset from a content type header with valid charset
    String contentType = "text/html; charset=UTF-8";
    
    String charset = DataUtil.getCharsetFromContentType(contentType);
    // Assert the charset is not null
    assertNotNull(charset);
    // Assert the charset is correct
    assertEquals("UTF-8", charset);
    
    // Test case 2: Getting charset from a content type header with invalid charset
    contentType = "text/html; charset=InvalidCharset";
    
    charset = DataUtil.getCharsetFromContentType(contentType);
    // Assert the charset is null
    assertNull(charset);
    
    // Test case 3: Getting charset from a content type header without charset
    contentType = "text/html";
    
    charset = DataUtil.getCharsetFromContentType(contentType);
    // Assert the charset is null
    assertNull(charset);
  }
}