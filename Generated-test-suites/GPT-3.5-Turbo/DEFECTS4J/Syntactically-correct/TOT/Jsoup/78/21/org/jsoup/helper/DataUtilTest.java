import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {
  
  // Test case for load(File in, String charsetName, String baseUri)
  @Test
  public void testLoadFromFile() throws IOException {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    File file = File.createTempFile("test", ".html");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write(html);
    writer.close();
    
    Document document = DataUtil.load(file, "UTF-8", "http://example.com");
    
    assertNotNull(document);
    assertEquals("Hello World!", document.select("h1").text());
  }
  
  // Test case for load(InputStream in, String charsetName, String baseUri)
  @Test
  public void testLoadFromInputStream() throws IOException {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
    
    Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
    
    assertNotNull(document);
    assertEquals("Hello World!", document.select("h1").text());
  }
  
  // Test case for load(InputStream in, String charsetName, String baseUri, Parser parser)
  @Test
  public void testLoadWithCustomParser() throws IOException {
    String xml = "<root><element>Hello World!</element></root>";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    Parser parser = Parser.xmlParser();
    
    Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
    
    assertNotNull(document);
    assertEquals("Hello World!", document.select("element").text());
  }
  
  // Test case for crossStreams(final InputStream in, final OutputStream out)
  @Test
  public void testCrossStreams() throws IOException {
    String input = "Hello World!";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    DataUtil.crossStreams(inputStream, outputStream);
    
    byte[] outputBytes = outputStream.toByteArray();
    String output = new String(outputBytes, StandardCharsets.UTF_8);
    
    assertEquals(input, output);
  }
  
  // Test case for parseInputStream(InputStream input, String charsetName, String baseUri, Parser parser)
  @Test
  public void testParseInputStream() throws IOException {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
    
    Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
    
    assertNotNull(document);
    assertEquals("Hello World!", document.select("h1").text());
  }
  
  // Test case for readToByteBuffer(InputStream inStream, int maxSize)
  @Test
  public void testReadToByteBuffer() throws IOException {
    String input = "Hello World!";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    
    ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, input.length() * 2); // allocate bigger ByteBuffer
    
    assertNotNull(byteBuffer);
    assertEquals(input, new String(byteBuffer.array(), StandardCharsets.UTF_8));
  }
  
  // Test case for getCharsetFromContentType(String contentType)
  @Test
  public void testGetCharsetFromContentType() {
    String contentType = "text/html; charset=UTF-8";
    
    String charset = DataUtil.getCharsetFromContentType(contentType);
    
    assertNotNull(charset);
    assertEquals("UTF-8", charset);
  }
  
  // Test case for validateCharset(String cs)
  @Test
  public void testValidateCharset() {
    String charset = "UTF-8";
    
    String validatedCharset = DataUtil.validateCharset(charset);
    
    assertNotNull(validatedCharset);
    assertEquals("UTF-8", validatedCharset);
  }
  
  // Test case for mimeBoundary()
  @Test
  public void testMimeBoundary() {
    String mimeBoundaryRegex = "^[a-zA-Z0-9_-]{32}$";
    Pattern pattern = Pattern.compile(mimeBoundaryRegex);
    
    String mimeBoundary = DataUtil.mimeBoundary();
    
    assertNotNull(mimeBoundary);
    Matcher matcher = pattern.matcher(mimeBoundary);
    assertTrue(matcher.matches());
  }
  
}