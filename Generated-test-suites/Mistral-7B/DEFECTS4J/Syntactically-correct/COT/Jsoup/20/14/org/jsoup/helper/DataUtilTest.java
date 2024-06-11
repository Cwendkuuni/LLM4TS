import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

 private Document document;
 private InputStream inputStream;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() {
 // Initialize testing objects here if necessary
 }

 /**
 * Test method for {@link DataUtil#load(File, String, String)}
 */
 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html"); // Replace with actual test file
 Document result = DataUtil.load(file, "UTF-8", "");
 assertNotNull(result);
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String)}
 */
 @Test
 public void testLoadInputStream() throws IOException {
 String htmlContent = "test html content";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = DataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(result);
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
 */
 @Test
 public void testLoadInputStreamParser() throws IOException {
 String htmlContent = "test html content";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
 assertNotNull(result);
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with default charset
 */
 @Test
 public void testParseByteDataDefaultCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document result = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
 assertNotNull(result);
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with specified charset
 */
 @Test
 public void testParseByteDataSpecifiedCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document result = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
 assertNotNull(result);
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with non-UTF-8 charset
 */
 @Test(expected = IOException.class)
 public void testParseByteDataNonUTF8Charset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document result = DataUtil.parseByteData(byteData, "ISO-8859-1", "", Parser.htmlParser());
 fail("Expected IOException");
 }

 /**
 * Test method for {@link DataUtil#readToByteBuffer(InputStream)}
 */
 @Test
 public void testReadToByteBuffer() throws IOException {
 String htmlContent = "test html content";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteBuffer result = DataUtil.readToByteBuffer(inputStream);
 assertEquals(htmlContent.length(), result.remaining());
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}
 */
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String result = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", result);
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)} with no charset specified
 */
 @Test
 public void testGetCharsetFromContentTypeNoCharset() {
 String contentType = "text/html";
 String result = DataUtil.getCharsetFromContentType(contentType);
 assertNull(result);
 }
}