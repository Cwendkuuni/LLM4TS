import org.jsoup.helper.DataUtil;
import org.jsoup.helper.ConverterUtils;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

 // Test for load method with valid file and charset
 @Test
 public void testLoadValidFileAndCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", "");
 Assert.assertNotNull(doc);
 }

 // Test for load method with valid file and default charset
 @Test
 public void testLoadValidFileAndDefaultCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "", "");
 Assert.assertNotNull(doc);
 }

 // Test for load method with null file
 @Test(expected = NullPointerException.class)
 public void testLoadNullFile() {
 DataUtil.load(null, "", "");
 }

 // Test for load method with invalid charset
 @Test(expected = IllegalArgumentException.class)
 public void testLoadInvalidCharset() {
 DataUtil.load(new File("test.html"), "invalidCharset", "");
 }

 // Test for load method with Invalid InputStream
 @Test(expected = IOException.class)
 public void testLoadInvalidInputStream() throws IOException {
 InputStream invalidInput = new ByteArrayInputStream(new byte[0]);
 DataUtil.load(invalidInput, "UTF-8", "");
 }

 // Test for parseInputStream method with valid file and charset
 @Test
 public void testParseInputStreamValidFileAndCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new FileInputStream("test.html"), byteArrayOutputStream);
 byteArrayOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 InputStream inputStream = new ByteArrayInputStream(bytes);
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test for parseInputStream method with invalid charset
 @Test(expected = IOException.class)
 public void testParseInputStreamInvalidCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new FileInputStream("test.html"), byteArrayOutputStream);
 byteArrayOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 InputStream inputStream = new ByteArrayInputStream(bytes);
 String invalidCharset = "invalidCharset";
 DataUtil.parseInputStream(inputStream, invalidCharset, "", Parser.htmlParser());
 }

 // Test for parseInputStream method with null parser
 @Test(expected = NullPointerException.class)
 public void testParseInputStreamNullParser() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new FileInputStream("test.html"), byteArrayOutputStream);
 byteArrayOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 InputStream inputStream = new ByteArrayInputStream(bytes);
 document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", null);
 }

 // Test for parseInputStream method with valid file and default charset
 @Test
 public void testParseInputStreamValidFileAndDefaultCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new FileInputStream("test.html"), byteArrayOutputStream);
 byteArrayOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 InputStream inputStream = new ByteArrayInputStream(bytes);
 Document doc = DataUtil.parseInputStream(inputStream, "", "", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test for parseInputStream method with empty input stream
 @Test
 public void testParseInputStreamEmptyInputStream() throws IOException {
 InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = DataUtil.parseInputStream(emptyInputStream, "UTF-8", "", Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test for crossStreams method with valid input and output streams
 @Test
 public void testCrossStreamsValidInputAndOutputStreams() throws IOException {
 byte[] inputData = "Test Data".getBytes(StandardCharsets.UTF_8);
 InputStream inputStream = new ByteArrayInputStream(inputData);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 byte[] expectedData = "Test Data".getBytes(StandardCharsets.UTF_8);
 byte[] outputData = outputStream.toByteArray();
 Assert.assertArrayEquals(expectedData, outputData);
 }

 // Test for crossStreams method with null input stream
 @Test(expected = NullPointerException.class)
 public void testCrossStreamsNullInputStream() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(null, outputStream);
 }

 // Test for crossStreams method with null output stream
 @Test(expected = NullPointerException.class)
 public void testCrossStreamsNullOutputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
 DataUtil.crossStreams(inputStream, null);
 }

 // Test for getCharsetFromContentType method with valid content type
 @Test
 public void testGetCharsetFromContentTypeValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(expectedCharset, charset);
 }

 // Test for getCharsetFromContentType method with invalid content type
 @Test
 public void testGetCharsetFromContentTypeInvalidContentType() {
 String contentType = "text/html; charset=invalidCharset";
 String expectedCharset = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(expectedCharset, charset);
 }

 // Test for validateCharset method with valid charset
 @Test
 public void testValidateCharsetValidCharset() {
 String charset = "UTF-8";
 String expectedCharset = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(charset);
 Assert.assertEquals(expectedCharset, validatedCharset);
 }

 // Test for validateCharset method with invalid charset
 @Test
 public void testValidateCharsetInvalidCharset() {
 String charset = "invalidCharset";
 String expectedCharset = null;
 String validatedCharset = DataUtil.validateCharset(charset);
 Assert.assertEquals(expectedCharset, validatedCharset);
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(boundary);
 Assert.assertNotNull(boundary.getBytes(StandardCharsets.UTF_8));
 }
}