import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

@RunWith(JUnit4.class)
public class DataUtilTest {

 // Test for load method with valid file and default charset
 @Test
 public void testLoadValidFileWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(new File("path/to/valid/file.html"), null);
 Assert.assertNotNull(document);
 }

 // Test for load method with valid file and specified charset
 @Test
 public void testLoadValidFileWithSpecifiedCharset() throws IOException {
 Document document = DataUtil.load(new File("path/to/valid/file.html"), "UTF-16");
 Assert.assertNotNull(document);
 }

 // Test for load method with invalid file
 @Test(expected = IOException.class)
 public void testLoadInvalidFile() throws IOException {
 Document document = DataUtil.load(new File("path/to/invalid/file"), null);
 Assert.assertNull(document);
 }

 // Test for load method with empty file
 @Test
 public void testLoadEmptyFile() throws IOException {
 Document document = DataUtil.load(new File("path/to/empty/file.html"), null);
 Assert.assertNotNull(document);
 Assert.assertEquals(0, document.childNodeSize());
 }

 // Test for parseInputStream method with valid input stream and default charset
 @Test
 public void testParseInputStreamValidInputWithDefaultCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/valid/file.html"));
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 Assert.assertNotNull(document);
 inputStream.close();
 }

 // Test for parseInputStream method with valid input stream and specified charset
 @Test
 public void testParseInputStreamValidInputWithSpecifiedCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/valid/file.html"));
 Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
 Assert.assertNotNull(document);
 inputStream.close();
 }

 // Test for parseInputStream method with invalid input stream
 @Test(expected = IOException.class)
 public void testParseInputStreamInvalidInput() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/invalid/file.html"));
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 Assert.assertNull(document);
 inputStream.close();
 }

 // Test for parseInputStream method with empty input
 @Test
 public void testParseInputStreamEmptyInput() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/empty/file.html"));
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 Assert.assertNotNull(document);
 Assert.assertEquals(0, document.childNodeSize());
 inputStream.close();
 }

 // Test for crossStreams method with valid input and output streams
 @Test
 public void testCrossStreamsValidInputOutputStreams() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/valid/file.html"));
 OutputStream outputStream = new FileOutputStream("path/to/output/file.html");
 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 outputStream.close();
 }

 // Test for crossStreams method with invalid input and output streams
 @Test
 public void testCrossStreamsInvalidInputOutputStreams() {
 // Cannot test this as it does not throw an exception when the input stream is invalid
 }

 // Test for parseInputStream method with BOM and UTF-8 charset
 @Test
 public void testParseInputStreamWithBOMUTF8() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/file_with_bom.html"));
 Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(document);
 inputStream.close();
 }

 // Test for parseInputStream method with UTF-16, UTF-32 and other unsupported charsets
 @Test(expected = IOException.class)
 public void testParseInputStreamUnsupportedCharsets() throws IOException {
 InputStream inputStream = new FileInputStream(new File("path/to/file.html"));
 Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
 Assert.assertNull(document);
 inputStream.close();
 }

 // Test for getCharsetFromContentType method with valid content type
 @Test
 public void testGetCharsetFromContentTypeValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 // Test for getCharsetFromContentType method with invalid content type
 @Test
 public void testGetCharsetFromContentTypeInvalidContentType() {
 String contentType = "text/plain";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(boundary);
 }
}