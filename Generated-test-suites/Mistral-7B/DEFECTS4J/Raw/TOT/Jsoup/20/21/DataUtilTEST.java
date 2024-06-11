import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private Document doc;
 private InputStream inputStream;
 private String charsetName = "UTF-8";
 private String baseUri = "http://example.com";
 private Parser parser = Parser.htmlParser();

 @Before
 public void setUp() {
 // Set up any necessary resources or initializations here
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html"); // Replace with actual file path
 Document doc = DataUtil.load(file, charsetName, baseUri);
 assertEquals("Expected document content", "<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, baseUri);
 assertEquals("Expected document content", "<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body></body></html>".getBytes(StandardCharsets.ISO_8859_1));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "ISO-8859-1", baseUri);
 assertEquals("Expected document content", "<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset='UTF-8'></head><body><div id='test'>Hello World</div></body></html>".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, baseUri, parser);
 String expectedId = "test";
 assertEquals("Expected div id", expectedId, doc.getElementById(expectedId).tagName());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullBaseUri() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Invalid Test Data".getBytes());
 DataUtil.load(inputStream, null, null);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals("Expected document content", "<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset='ISO-8859-1'></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", baseUri, parser);
 assertEquals("Expected document content", "<html><head><meta charset='ISO-8859-1'></head><body></body></html>", doc.html());
 }

 @Test
 public void testParseByteDataWithNullCharsetName() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
 assertEquals("Expected document content", "<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 int limit = byteData.limit();
 byte[] expectedData = new byte[limit];
 byteData.get(expectedData);
 assertArrayEquals("Expected byte array", expectedData, "<html><body></body></html>".getBytes());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected charset", "UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull("Expected null charset", charset);
 }
}