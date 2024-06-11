package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

 private Document document;
 private final Random random = new Random();

 @Before
 public void setUp() throws Exception {
// Create a sample HTML document for testing
 String html = "<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
 document = DataUtil.load(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), "UTF-8", "");
 }

 @Test
 public void testLoadFile() throws Exception {
// Test loading a file with a valid charset in the header
 File file = new File("testfile.html");
 String fileContent = "<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test File Content</p>\n</body>\n</html>";
 FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);
 Document loadedDocument = DataUtil.load(file, "UTF-8", "");
 assertEquals("Test File Content", loadedDocument.body().text());
 FileUtils.deleteQuietly(file);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws Exception {
// Test loading a file with an invalid charset in the header
 File file = new File("testfile.html");
 String fileContent = "<html>\n<head>\n<meta charset=\"GBK\">\n<title>Test File</title>\n</head>\n<body>\n<p>Test File Content</p>\n</body>\n</html>";
 FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);
 try {
 Document loadedDocument = DataUtil.load(file, "GBK", "");
 fail("Expected IOException");
 } catch (IOException e) {
// Expected IOException
 }
 FileUtils.deleteQuietly(file);
 }

 @Test
 public void testLoadFileWithNoCharset() throws Exception {
// Test loading a file with no charset in the header
 File file = new File("testfile.html");
 String fileContent = "<html>\n<head>\n<title>Test File</title>\n</head>\n<body>\n<p>Test File Content</p>\n</body>\n</html>";
 FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);
 Document loadedDocument = DataUtil.load(file, "", "");
 assertEquals("Test File Content", loadedDocument.body().text());
 FileUtils.deleteQuietly(file);
 }

 @Test
 public void testLoadInvalidInput() throws Exception {
// Test loading an invalid input
 try {
 Document document = DataUtil.load((InputStream) null, "UTF-8", "");
 fail("Expected IOException");
 } catch (IOException e) {
// Expected IOException
 }
 }

 @Test
 public void testLoadStream() throws Exception {
// Test loading a stream with a valid charset in the header
 String html = "<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
 Document documentFromStream = DataUtil.load(inputStream, "UTF-8", "");
 assertEquals("Test Stream Content", documentFromStream.body().text());
 }

 @Test
 public void testLoadStreamWithInvalidCharset() throws Exception {
// Test loading a stream with an invalid charset in the header
 String html = "<html>\n<head>\n<meta charset=\"GBK\">\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
 try {
 Document documentFromStream = DataUtil.load(inputStream, "GBK", "");
 fail("Expected IOException");
 } catch (IOException e) {
// Expected IOException
 }
 }

 @Test
 public void testLoadStreamWithNoCharset() throws Exception {
// Test loading a stream with no charset in the header
 String html = "<html>\n<head>\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
 Document documentFromStream = DataUtil.load(inputStream, "", "");
 assertEquals("Test Stream Content", documentFromStream.body().text());
 }

 @Test
 public void testLoadStreamWithInvalidContentType() throws Exception {
// Test loading a stream with an invalid content type
 String html = "<html>\n<head>\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
 try {
 DataUtil.load(inputStream, "text/plain", "");
 fail("Expected IOException");
 } catch (IOException e) {
// Expected IOException
 }
 }

 @Test
 public void testParseInputStream() throws Exception {
// Test parsing an input stream with a valid charset in the header
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 DataUtil.load(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), "UTF-8", "", Parser.htmlParser());
 DataUtil.crossStreams(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), outputStream);
 String output = outputStream.toString(StandardCharsets.UTF_8.name());
 assertEquals(html, output);
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws Exception {
// Test parsing an input stream with an invalid charset in the header
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html>\n<head>\n<meta charset=\"GBK\">\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 try {
 DataUtil.load(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), "GBK", "", Parser.htmlParser());
 fail("Expected IOException");
 } catch (IOException e) {
// Expected IOException
 }
 }

 @Test
 public void testParseInputStreamWithNoCharset() throws Exception {
// Test parsing an input stream with no charset in the header
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html>\n<head>\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 Document documentFromStream = DataUtil.load(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), "", "", Parser.htmlParser());
 DataUtil.crossStreams(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), outputStream);
 String output = outputStream.toString(StandardCharsets.UTF_8.name());
 assertEquals(html, output);
 }

 @Test
 public void testParseInputStreamWithBom() throws Exception {
// Test parsing an input stream with a BOM
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String bomContent = "\uFEFF<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 DataUtil.load(new ByteArrayInputStream(bomContent.getBytes(StandardCharsets.UTF_8)), "UTF-8", "", Parser.htmlParser());
 DataUtil.crossStreams(new ByteArrayInputStream(bomContent.substring(1).getBytes(StandardCharsets.UTF_8)), outputStream);
 String output = outputStream.toString(StandardCharsets.UTF_8.name());
 assertEquals(html.substring(1), output);
 }

 @Test
 public void testParseInputStreamWithInvalidContentType() throws Exception {
// Test parsing an input stream with an invalid content type
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String html = "<html>\n<head>\n<title>Test Stream</title>\n</head>\n<body>\n<p>Test Stream Content</p>\n</body>\n</html>";
 try {
 DataUtil.load(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), "text/plain", "", Parser.htmlParser());
 fail("Expected IOException");
 } catch (IOException e) {
// Expected IOException
 }
 }

 @Test
 public void testReadToByteBuffer() throws Exception {
// Test reading to a byte buffer
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertEquals("Test", Charset.forName("UTF-8").decode(byteBuffer).toString());
 }

 @Test
 public void testReadToByteBufferWithCustomCharset() throws Exception {
// Test reading to a byte buffer with a custom charset
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, "UTF-16BE");
 byte[] bytes = byteBuffer.array();
 assertEquals((short) 0x4D, bytes[0]); // 'M'
 assertEquals((short) 0x45, bytes[1]); // 'E'
 assertEquals((short) 0x54, bytes[2]); // 'T'
 assertEquals((short) 0x00, bytes[3]); // null terminator for UTF-16BE
 }

 @Test
 public void testReadFileToByteBuffer() throws Exception {
// Test reading a file to a byte buffer
 File file = new File("testfile.txt");
 String fileContent = "Test";
 FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 assertEquals("Test", Charset.forName("UTF-8").decode(byteBuffer).toString());
 FileUtils.deleteQuietly(file);
 }

 @Test
 public void testEmptyByteBuffer() throws Exception {
// Test creating an empty byte buffer
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, byteBuffer.limit());
 }

 @Test
 public void testGetCharsetFromContentType() throws Exception {
// Test parsing a content type for a charset
 String contentType = "text/html; charset=UTF-8";
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html; charset=gb2312";
 assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html; charset=GBK";
 assertEquals("GBK", DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html; charset=windows-1252";
 assertEquals("windows-1252", DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html; charset=ISO-8859-1";
 assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testMimeBoundary() throws Exception {
// Test creating a mime boundary
 String boundary = DataUtil.mimeBoundary();
 assertTrue(boundary.length() >= DataUtil.boundaryLength);
 }
}