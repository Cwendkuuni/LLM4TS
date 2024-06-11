import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 // Test for load method with valid input
 @Test
 public void testLoadValidFile() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", "/");
 Assert.assertNotNull(doc);
 }

 // Test for load method with null input
 @Test(expected = IOException.class)
 public void testLoadNullFile() {
 DataUtil.load(null, "UTF-8", "/");
 }

 // Test for load method with invalid charset
 @Test(expected = IOException.class)
 public void testLoadInvalidCharset() throws IOException {
 Document doc = null;
 try {
 doc = DataUtil.load(new File("test.html"), "XYZ", "/");
 } catch (IOException e) {
 // Swallow the exception, as expected
 }
 Assert.assertNull(doc);
 }

 // Test for load method with empty body
 @Test
 public void testLoadEmptyFile() throws IOException {
 Document doc = DataUtil.load(new File(""), "UTF-8", "/");
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.childNodeSize());
 }

 // Test for parseInputStream method with valid input
 @Test
 public void testParseInputStreamValidData() throws IOException {
 InputStream input = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseInputStream(input, "UTF-8", "/", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test for parseInputStream method with null input
 @Test(expected = IOException.class)
 public void testParseInputStreamNullInput() {
 DataUtil.parseInputStream(null, "UTF-8", "/", Parser.htmlParser());
 }

 // Test for parseInputStream method with invalid charset
 @Test(expected = IOException.class)
 public void testParseInputStreamInvalidCharset() throws IOException {
 InputStream input = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 Document doc = null;
 try {
 doc = DataUtil.parseInputStream(input, "XYZ", "/", Parser.htmlParser());
 } catch (IOException e) {
 // Swallow the exception, as expected
 }
 Assert.assertNull(doc);
 }

 // Test for parseInputStream method with empty input
 @Test
 public void testParseInputStreamEmptyData() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 Document doc = DataUtil.parseInputStream(input, "UTF-8", "/", Parser.htmlParser());
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.childNodeSize());
 }

 // Test for crossStreams method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
 DataUtil.crossStreams(inputStream, outputStream);
 String result = outputStream.toString(StandardCharsets.UTF_8.name());
 Assert.assertEquals("Test", result);
 }

 // Test for parseInputStream method with BOM
 @Test
 public void testParseInputStreamBOM() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0});
 Document doc = DataUtil.parseInputStream(input, "UTF-8", "/", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test for readToByteBuffer method
 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream input = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, 10);
 byte[] expectedBytes = "Test".getBytes(StandardCharsets.UTF_8);
 byte[] actualBytes = new byte[byteBuffer.limit()];
 byteBuffer.get(actualBytes);
 Assert.assertArrayEquals(expectedBytes, actualBytes);
 }

 // Test for getCharsetFromContentType method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 // Test for getCharsetFromContentType method with unsupported charset
 @Test
 public void testGetCharsetFromContentTypeUnsupportedCharset() {
 String contentType = "text/html; charset=XYZ";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(boundary);
 Assert.assertTrue(boundary.length() >= 32);
 }
}