package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DataUtilTest {

 private static final String XML_FILE_CONTENTS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>Test</element>\n</root>";
 private static final String HTML_FILE_CONTENTS = "<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";

 @Test
 public void testLoadFileUTF8() throws IOException {
 Document doc = DataUtil.load(new File("test.txt"), "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileInvalidCharset() throws IOException {
 assertThrows(IllegalArgumentException.class, () -> {
 DataUtil.load(new File("test.txt"), "invalidCharset", null);
 });
 }

 @Test
 public void testLoadInputStreamUTF8() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamNoCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(XML_FILE_CONTENTS.getBytes());
 Document doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 assertThrows(IllegalArgumentException.class, () -> {
 DataUtil.load(inputStream, "invalidCharset", null);
 });
 }

 @Test
 public void testLoadInputStreamParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8)), outputStream);
 byte[] byteData = outputStream.toByteArray();
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataDetectCharsetUTF8() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8)), outputStream);
 byte[] byteData = outputStream.toByteArray();
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataDetectCharsetHTML5() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8)), outputStream);
 byte[] byteData = outputStream.toByteArray();
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null);
 assertEquals("UTF-8", doc.outputSettings().charset());

 Element meta = doc.select("meta[charset]").first();
 assertNotNull(meta);
 assertEquals("UTF-8", meta.attr("charset"));
 }

 @Test
 public void testParseByteDataDetectCharsetUTF16() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(XML_FILE_CONTENTS.substring(0, 2).getBytes(StandardCharsets.UTF_16LE)), outputStream);
 byte[] byteData = outputStream.toByteArray();
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null);
 assertEquals("UTF-16", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataDetectCharsetUTF32() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(new ByteArrayInputStream(XML_FILE_CONTENTS.substring(0, 4).getBytes(StandardCharsets.UTF_32BE)), outputStream);
 byte[] byteData = outputStream.toByteArray();
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null);
 assertEquals("UTF-32", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithMetaCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, null, null);
 assertEquals("UTF-8", doc.outputSettings().charset());

 Element meta = doc.select("meta[charset]").first();
 assertNotNull(meta);
 assertEquals("UTF-8", meta.attr("charset"));
 }

 @Test
 public void testLoadInputStreamWithContentType() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(XML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 String contentType = "text/xml; charset=UTF-16";
 Document doc = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-16", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithContentTypeInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 String contentType = "text/xml; charset=invalidCharset";
 Document doc = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithContentTypeUTF8() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 String contentType = "text/html; charset=UTF-8";
 Document doc = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithContentTypeUTF8AndMetaCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 String contentType = "text/html; charset=UTF-8";
 Document doc = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-8", doc.outputSettings().charset());

 Element meta = doc.select("meta[charset]").first();
 assertNotNull(meta);
 assertEquals("UTF-8", meta.attr("charset"));
 }

 @Test
 public void testLoadInputStreamWithContentTypeInvalidContentType() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
 String contentType = "text/plain";
 Document doc = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromBomUTF8() throws IOException {
 byte[] byteData = XML_FILE_CONTENTS.substring(0, 4).getBytes(StandardCharsets.UTF_8);
 assertEquals("UTF-8", DataUtil.detectCharsetFromBom(ByteBuffer.wrap(byteData), null));
 }

 @Test
 public void testDetectCharsetFromBomUTF16() throws IOException {
 byte[] byteData = XML_FILE_CONTENTS.substring(0, 2).getBytes(StandardCharsets.UTF_16LE);
 assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(byteData), null));
 }

 @Test
 public void testDetectCharsetFromBomUTF32() throws IOException {
 byte[] byteData = XML_FILE_CONTENTS.substring(0, 4).getBytes(StandardCharsets.UTF_32BE);
 assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(byteData), null));
 }

 @Test
 public void testGetCharsetFromContentTypeUTF8() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeUTF16() {
 String contentType = "text/html; charset=UTF-16";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeUTF32() {
 String contentType = "text/html; charset=UTF-32";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeXML() {
 String contentType = "text/xml";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeHTML() {
 String contentType = "text/html";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testValidateCharsetUTF8() {
 assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
 assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
 }

 @Test
 public void testValidateCharsetInvalid() {
 assertNull(DataUtil.validateCharset("invalidCharset"));
 }

 @Test
 public void testValidateCharsetEmpty() {
 assertNull(DataUtil.validateCharset(""));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertNotNull(boundary);
 assertFalse(boundary.isEmpty());
 }
}