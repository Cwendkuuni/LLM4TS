import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-16LE", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("your HTML content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("your HTML content".getBytes(StandardCharsets.UTF_16LE));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-16LE", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("your XML content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 byte[] htmlContent = "your HTML content".getBytes(StandardCharsets.UTF_8);
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent);
 Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 byte[] htmlContent = "your HTML content".getBytes(StandardCharsets.UTF_16LE);
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent);
 Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, DataUtil.Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("your HTML content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Assert.assertNotNull(byteData);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("your HTML content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
 Assert.assertNotNull(byteData);
 }

 @Test(expected = IOException.class)
 public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("your HTML content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, -1);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
 String contentType = "text/html; charset=x-unknown-charset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidContentType() {
 String contentType = "application/unknown";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }
}