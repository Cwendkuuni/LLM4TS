import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File testFile = new File("testFile.html");
 String expectedCharset = "UTF-8";
 Document document = DataUtil.load(testFile, null, "");

 Mockito.verify(parser(), Mockito.times(1)).parseInput(Mockito.anyString(), Mockito.eq(""));
 PowerMockito.mockStatic(Charset.class);
 PowerMockito.whenNew(Charset.class, Mockito.anyString()).thenReturn(Charset.forName(expectedCharset));

 Assert.assertEquals("Expected document to be loaded with default charset", expectedCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File testFile = new File("testFile.html");
 String expectedCharset = "ISO-8859-1";
 Document document = DataUtil.load(testFile, expectedCharset, "");

 Mockito.verify(parser(), Mockito.times(1)).parseInput(Mockito.anyString(), Mockito.eq(""));
 PowerMockito.mockStatic(Charset.class);
 PowerMockito.whenNew(Charset.class, Mockito.anyString()).thenReturn(Charset.forName(expectedCharset));

 Assert.assertEquals("Expected document to be loaded with specified charset", expectedCharset, document.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullInput() {
 File testFile = null;
 String charsetName = "UTF-8";
 String baseUri = "";

 Assert.assertThrows("Expected an IOException to be thrown", IOException.class, () -> DataUtil.load(testFile, charsetName, baseUri));
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream("testStream".getBytes(StandardCharsets.UTF_8));
 String expectedCharset = "UTF-8";
 Document document = DataUtil.load(testStream, null, "");

 Mockito.verify(parser(), Mockito.times(1)).parseByteData(Mockito.any(ByteBuffer.class), Mockito.eq(""), Mockito.eq(""), Mockito.eq(Parser.htmlParser()));
 PowerMockito.mockStatic(Charset.class);
 PowerMockito.whenNew(Charset.class, Mockito.anyString()).thenReturn(Charset.forName(expectedCharset));

 Assert.assertEquals("Expected document to be loaded with default charset", expectedCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream("testStream".getBytes(StandardCharsets.UTF_8));
 String expectedCharset = "ISO-8859-1";
 Document document = DataUtil.load(testStream, expectedCharset, "");

 Mockito.verify(parser(), Mockito.times(1)).parseByteData(Mockito.any(ByteBuffer.class), Mockito.eq(expectedCharset), Mockito.eq(""), Mockito.eq(Parser.htmlParser()));
 PowerMockito.mockStatic(Charset.class);
 PowerMockito.whenNew(Charset.class, Mockito.anyString()).thenReturn(Charset.forName(expectedCharset));

 Assert.assertEquals("Expected document to be loaded with specified charset", expectedCharset, document.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullInput() {
 InputStream testStream = null;
 String charsetName = "UTF-8";
 String baseUri = "";

 Assert.assertThrows("Expected an IOException to be thrown", IOException.class, () -> DataUtil.load(testStream, charsetName, baseUri));
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream testStream = new ByteArrayInputStream("testStream".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "";
 Parser parser = Mockito.mock(Parser.class);
 Document document = DataUtil.load(testStream, charsetName, baseUri, parser);

 Mockito.verify(parser(), Mockito.times(1)).parseByteData(Mockito.any(ByteBuffer.class), Mockito.eq(charsetName), Mockito.eq(""), Mockito.eq(parser));

 Assert.assertNotNull("Expected document to be loaded successfully", document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Test Data</body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

 Assert.assertEquals("Expected document to be parsed with default charset", testData, document.toString());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 String testData = "<html><head><meta charset=\"ISO-8859-1\"></meta></head><body>Test Data</body></html>";
 String expectedCharset = "ISO-8859-1";
 ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.parseByteData(byteData, expectedCharset, "", Parser.htmlParser());

 Assert.assertEquals("Expected document to be parsed with specified charset", expectedCharset, document.outputSettings().charset());
 Assert.assertEquals("Expected document to be parsed with specified data", testData, document.toString());
 }

 @Test
 public void testParseByteDataWithNullCharset() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></meta></head><body>Test Data</body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

 Assert.assertEquals("Expected document to be parsed with default charset", testData, document.toString());
 }

 @Test
 public void testParseByteDataWithEmptyCharset() throws IOException {
 String testData = "<html><head><meta charset=\"\"></meta></head><body>Test Data</body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.parseByteData(byteData, "", "", Parser.htmlParser());

 Assert.assertEquals("Expected document to be parsed with default charset", testData, document.toString());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 String testData = "<html><head><meta charset=\"ABC\"></meta></head><body>Test Data</body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.parseByteData(byteData, "ABC", "", Parser.htmlParser());

 Assert.assertEquals("Expected document to be parsed with default charset", testData, document.toString());
 }

 @Test
 public void testReadToByteBufferWithNullInputStream() throws IOException {
 InputStream testStream = null;
 ByteBuffer byteData = DataUtil.readToByteBuffer(testStream);

 Assert.assertNull("Expected byte buffer to be null when input stream is null");
 }

 @Test
 public void testReadToByteBufferWithEmptyInputStream() throws IOException {
 InputStream testStream = new ByteArrayInputStream(new byte[0]);
 ByteBuffer byteData = DataUtil.readToByteBuffer(testStream);

 Assert.assertEquals("Expected empty byte buffer when input stream is empty", 0, byteData.limit());
 }

 @Test
 public void testReadToByteBufferWithValidInputStream() throws IOException {
 String testData = "testData";
 InputStream testStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(testStream);

 Assert.assertEquals("Expected byte buffer to contain test data", testData.length(), byteData.limit());
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String contentType = null;
 String expectedCharset = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);

 Assert.assertEquals("Expected null charset when content type is null", expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);

 Assert.assertEquals("Expected charset to be extracted from content type", expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidContentType() {
 String contentType = "text/plain";
 String expectedCharset = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);

 Assert.assertEquals("Expected null charset when content type does not contain charset information", expectedCharset, charset);
 }
}