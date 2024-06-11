import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String XHTML_CONTENT_TYPE = "application/xhtml+xml; charset=UTF-16";
 private static final String BAD_CONTENT_TYPE = "application/octet-stream";

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 File testFile = new File("test.html");
 Document doc = DataUtil.load(testFile, "UTF-8", "");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithNullCharset() throws IOException {
 File testFile = new File("test.html");
 Document doc = DataUtil.load(testFile, null, "");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File testFile = new File("test.html");
 Document doc = DataUtil.load(testFile, "", "");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", "");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, "");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "", "");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithXmlParser() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html xmlns='http://www.w3.org/1999/xhtml'><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File testFile = new File("testFileDoesNotExist.html");
 Document doc = DataUtil.load(testFile, "", "");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidStream() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = null;
 try {
 doc = DataUtil.load(inputStream, "", "");
 } catch (IOException e) {
 // expected
 }
 Assert.assertNull(doc);
 }

 @Test
 public void testParseByteDataWithValidCharset() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
 Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
 Document doc = DataUtil.parseByteData(byteData, "", "", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 String testString = "<html><body></body></html>";
 byteStream.write(testString.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(byteStream.toByteArray()));
 Document doc = assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, "ISO-8859-1", "", Parser.htmlParser()));
 Assert.assertNull(doc);
 }

 @Test
 public void testGetCharsetFromContentTypeWithValidContentType() {
 String contentType = HTML_CONTENT_TYPE;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidContentType() {
 String contentType = BAD_CONTENT_TYPE;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithXhtmlContentType() {
 String contentType = XHTML_CONTENT_TYPE;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }
}