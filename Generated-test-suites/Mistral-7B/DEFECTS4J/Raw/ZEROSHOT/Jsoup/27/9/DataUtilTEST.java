package org.jsoup.helper.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DataUtilTest {

 private static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String HTML_WITH_GB2312_META = "<html lang=\"zh-CN\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n<title>Test</title>\n</head>\n<body>\n<h1>Hello</h1>\n</body>\n</html>";

 @BeforeClass
 public static void setup() {
 // Initialize Parser
 Parser parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("test.html");
 String expectedCharset = "UTF-8";
 String baseUri = "http://test.com";
 Document doc = DataUtil.load(testFile, expectedCharset, baseUri);
 assertEquals("Expected loaded document to not be null", doc, notNull(doc));
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullFile() throws IOException {
 File testFile = null;
 String charsetName = null;
 String baseUri = null;
 DataUtil.load(testFile, charsetName, baseUri);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File testFile = new File("test.html");
 String charsetName = null;
 String baseUri = null;
 DataUtil.load(testFile, charsetName, baseUri);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream testStream = new ByteArrayInputStream(HTML_WITH_GB2312_META.getBytes());
 String expectedCharset = "gb2312";
 String baseUri = "http://test.com";
 Document doc = DataUtil.load(testStream, expectedCharset, baseUri);
 assertEquals("Expected loaded document to not be null", doc, notNull(doc));
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullInputStream() throws IOException {
 InputStream testStream = null;
 String charsetName = null;
 String baseUri = null;
 DataUtil.load(testStream, charsetName, baseUri);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithEmptyInputStream() throws IOException {
 InputStream testStream = new ByteArrayInputStream(new byte[0]);
 String charsetName = null;
 String baseUri = null;
 DataUtil.load(testStream, charsetName, baseUri);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 String docData = HTML_WITH_GB2312_META;
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals("Expected loaded document to not be null", doc, notNull(doc));
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 String docData = HTML_WITH_GB2312_META;
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 String charsetName = "gb2312";
 Document doc = DataUtil.parseByteData(byteData, charsetName, null, parser);
 assertEquals("Expected loaded document to not be null", doc, notNull(doc));
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 String docData = HTML_CONTENT_TYPE + "\n" + HTML_WITH_GB2312_META;
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals("Expected loaded document to not be null", doc, notNull(doc));
 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 String detectedCharset = meta != null ? meta.attr("charset") : null;
 assertEquals("Expected detected charset to be 'gb2312'", "gb2312", detectedCharset);
 }

 @Test
 public void testParseByteDataWithDetectedCharsetButInputIsUtf8() throws IOException {
 String docData = HTML_CONTENT_TYPE + "\n" + HTML_WITH_GB2312_META;
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals("Expected loaded document to not be null", doc, notNull(doc));
 assertEquals("Expected BOM not present in decoded document", "", doc.body().text().substring(0, 1));
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected charset to be 'UTF-8'", expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeEmpty() {
 String contentType = "";
 String expectedCharset = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected charset to be null", expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String expectedCharset = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected charset to be null", expectedCharset, charset);
 }

 private static Document notNull(Document doc) {
 if (doc == null) {
 throw new RuntimeException("Expected a non-null document");
 }
 return doc;
 }
}