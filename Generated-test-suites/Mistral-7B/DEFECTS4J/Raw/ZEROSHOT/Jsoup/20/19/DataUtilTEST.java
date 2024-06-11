import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setUp() {
 parser = DataUtil.Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html"); // replace with valid file path
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 StoneDocument stoneDoc = new StoneDocument(); // assuming StoneDocument is a valid Document implementation
 doc = DataUtil.load(file, charsetName, baseUri);
 StoneParser stoneParser = new StoneParser(); // assuming StoneParser is a valid Parser implementation
 StoneDocument expectedDoc = stoneParser.parseInput(new File("expectedTest.html"), baseUri); // replace with valid expected document path

 assertTrue(doc != null);
 assertEquals(expectedDoc.toString(), doc.toString());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = new File("test.html");
 String charsetName = null;
 String baseUri = "http://example.com";
 DataUtil.load(file, charsetName, baseUri);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals("<html><body></body></html>", doc.body().html());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = null;
 String baseUri = "http://example.com";
 DataUtil.load(inputStream, charsetName, baseUri, parser);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body><meta charset='UTF-8'></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, parser);
 assertEquals("<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body><meta charset='GBK'></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = null;
 String baseUri = "http://example.com";

 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, parser);
 assertEquals("<html><body></body></html>", doc.body().html());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=GBK";
 assertEquals("GBK", DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }
}