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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("sample.html");
 Document doc = DataUtil.load(file, null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File file = new File("sample.html");
 Document doc = DataUtil.load(file, "UTF-16LE", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(in, null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_16LE));
 InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(in, "UTF-16LE", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(in, null, null, DataUtil.Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testExceptionWhenInputStreamClosed() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
 DataUtil.crossStreams(in, new ByteArrayOutputStream()); // Close the input stream in the middle
 Document doc = DataUtil.load(in, null, null);
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_16LE));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, DataUtil.Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 Document doc = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
 Assert.assertNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testMimeBoundary() {
 String mimeBoundary = DataUtil.mimeBoundary();
 }
}