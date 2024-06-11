import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 // Method 1 - load(File, String, String)
 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(file, charsetName, baseUri);
 // Basic functionality check
 Assert.assertNotNull(doc);
 // Edge cases and exception handling
 File nonExistentFile = new File("nonExistentFile.html");
 Assert.assertNull(DataUtil.load(nonExistentFile, charsetName, baseUri));
 File emptyFile = new File("emptyFile.html");
 Assert.assertNotNull(DataUtil.load(emptyFile, charsetName, baseUri)); // default document created when file is empty
 }

 // Method 1 - load(InputStream, String, String)
 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(inputStream, charsetName, baseUri);
 // Basic functionality check
 Assert.assertNotNull(doc);
 // Edge cases and exception handling
 InputStream emptyInputStream = Mockito.mock(InputStream.class);
 Mockito.when(emptyInputStream.available()).thenReturn(0);
 Assert.assertNull(DataUtil.load(emptyInputStream, charsetName, baseUri)); // empty input stream
 }

 // Method 1 - load(InputStream, String, String, Parser)
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
 // Basic functionality check
 Assert.assertNotNull(doc);
 // Edge cases and exception handling
 InputStream invalidXmlInputStream = createTestInputStream("<root>"); // missing XML declaration
 Document docInvalidXml = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
 Assert.assertNotNull(docInvalidXml); // parser will handle the error and return a Document object
 }

 // Method 2 - parseInputStream(InputStream, String, String, Parser)
 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
 // Basic functionality check
 Assert.assertNotNull(doc);
 // Edge cases and exception handling
 InputStream emptyInputStream = Mockito.mock(InputStream.class);
 Mockito.when(emptyInputStream.available()).thenReturn(0);
 InputStream invalidBomInputStream = createTestInputStream("\uFEFF<?xml version='1.0' encoding='UTF-8'?>\n<root></root>"); // invalid BOM
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
 Assert.assertNull(DataUtil.parseInputStream(byteArrayInputStream, charsetName, baseUri, Parser.htmlParser())); // empty input stream
 Assert.assertNull(DataUtil.parseInputStream(emptyInputStream, charsetName, baseUri, Parser.htmlParser())); // empty input stream
 Assert.assertNull(DataUtil.parseInputStream(invalidBomInputStream, charsetName, baseUri, Parser.htmlParser())); // invalid BOM
 }

 // Method 3 - readToByteBuffer(InputStream)
 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>");
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 // Basic functionality check
 Assert.assertNotNull(byteBuffer);
 Assert.assertEquals(byteBuffer.remaining(), inputStream.available());
 }

 // Method 3 - readToByteBuffer(File)
 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File file = new File("test.html");
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 // Basic functionality check
 Assert.assertNotNull(byteBuffer);
 // Edge cases and exception handling
 File nonExistentFile = new File("nonExistentFile.html");
 Assert.assertNull(DataUtil.readFileToByteBuffer(nonExistentFile));
 }

 // Edge cases and exception handling for methods that don't accept file/input stream arguments
 @Test(expected = IllegalArgumentException.class)
 public void testLoadWithNullFile() {
 DataUtil.load(null, "UTF-8", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadWithNullInputStream() {
 DataUtil.load(null, "UTF-8", "http://example.com", Parser.htmlParser());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamWithNullInputStream() throws IOException {
 DataUtil.parseInputStream(null, "UTF-8", "http://example.com", Parser.htmlParser());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamWithNullCharset() throws IOException {
 DataUtil.parseInputStream(createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>"), null, "http://example.com", Parser.htmlParser());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamWithNullBaseUri() throws IOException {
 DataUtil.parseInputStream(createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>"), "UTF-8", null, Parser.htmlParser());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamWithNullParser() throws IOException {
 DataUtil.parseInputStream(createTestInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<root></root>"), "UTF-8", "http://example.com", null);
 }

 // Test for methods without arguments
 @Test
 public void testEmptyMethods() {
 DataUtil.emptyByteBuffer();
 DataUtil.mimeBoundary();
 }

 // Test for getCharsetFromContentType()
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 // Test for validateCharset()
 @Test
 public void testValidateCharsetValid() {
 String charset = "UTF-8";
 String validCharset = DataUtil.validateCharset(charset);
 Assert.assertEquals("UTF-8", validCharset);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateCharsetNull() {
 String charset = null;
 String validCharset = DataUtil.validateCharset(charset);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateCharsetInvalid() {
 String charset = "INVALID_CHARSET";
 String validCharset = DataUtil.validateCharset(charset);
 }

 private InputStream createTestInputStream(String content) throws IOException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
 return byteArrayInputStream;
 }
}