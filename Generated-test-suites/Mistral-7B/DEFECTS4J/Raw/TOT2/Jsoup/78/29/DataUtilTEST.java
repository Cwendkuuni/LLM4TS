import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(new File("test.html"), null, null);
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 Document document = DataUtil.load(new File("test.html"), "UTF-16", null);
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, null);
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_16));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, "UTF-16", null);
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
 Assert.assertNotNull(document);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("input".getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 byte[] output = outputStream.toByteArray();
 Assert.assertArrayEquals("input".getBytes(StandardCharsets.UTF_8), output);
 }

 @Test
 public void testParseInputStreamWithDefaultParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.parseInputStream(inputStream, null, null);
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_16));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null);
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.xmlParser());
 Assert.assertNotNull(document);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024); // read whole file
 Assert.assertNotNull(byteBuffer);
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File file = new File("test.html");
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 Assert.assertNotNull(byteBuffer);
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 Assert.assertEquals(0, byteBuffer.limit());
 }

 @Test(expected = IOException.class)
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.parseInputStream(inputStream, "NON_EXISTING_CHARSET", null); // non-existent charset
 }

 @Test(expected = IOException.class)
 public void testReadInputStreamWithNegativeBufferSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test content".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1024);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(charset);
 Assert.assertEquals("UTF-8", validatedCharset);
 }

 @Test
 public void testMimeBoundary() {
 String mimeBoundary = DataUtil.mimeBoundary();
 Assert.assertEquals(32, mimeBoundary.length());
 }
}