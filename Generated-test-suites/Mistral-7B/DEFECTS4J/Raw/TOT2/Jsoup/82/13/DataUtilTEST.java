import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HelperTestCase;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParsedUrl;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest extends HelperTestCase {

 private static final String TEST_FILE = "test.html";

 @org.junit.Test
 public void testLoadFile() throws IOException {
 Document document = DataUtil.load(new File(TEST_FILE), null, null);
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testLoadInputStreamWithUtf8() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.load(inputStream, "UTF-8", null);
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.load(inputStream, "INVALID_CHARSET", null);
 Assert.assertNotNull(document);
 // Add assertion here to check if the document is empty or contains default charset
 }

 @org.junit.Test
 public void testLoadInputStreamWithInvalidCharsetAndInvalidMetaCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta charset=\"INVALID_CHARSET\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>".getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
 Assert.assertNotNull(document);
 // Add assertion here to check if the document is empty or contains default charset
 }

 @org.junit.Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testParseInputStreamWithUtf8Charset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testParseInputStreamWithUtf8AndBaseUri() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testParseInputStreamWithXmlParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.xml");
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testCrossStreams() throws IOException {
 InputStream input = getClass().getResourceAsStream("/test.html");
 OutputStream output = new ByteArrayOutputStream();
 DataUtil.crossStreams(input, output);
 Assert.assertNotNull(output.toString());
 }

 @org.junit.Test
 public void testDetectCharsetFromBom() {
 ByteBuffer byteData = ByteBuffer.allocate(4);
 byteData.put((byte) 0xEF);
 byteData.put((byte) 0xBB);
 byteData.put((byte) 0xBF);
 byteData.flip();
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertEquals("UTF-8", bomCharset.charset);
 Assert.assertTrue(bomCharset.offset);
 }

 @org.junit.Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @org.junit.Test
 public void testGetCharsetFromContentTypeInvalidCharset() {
 String contentType = "text/html; charset=INVALID_CHARSET";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 @org.junit.Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(boundary);
 Assert.assertEquals(boundary.length(), 32);
 }
}