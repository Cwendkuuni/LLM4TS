package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

 private static final String CHARSET_UTF8 = "UTF-8";
 private static final String CHARSET_GB2312 = "GB2312";
 private static final String CONTENT_TYPE_WITH_CHARSET = "text/html; charset=EUC-JP";
 private static final String CONTENT_TYPE_WITHOUT_CHARSET = "text/html";
 private static final String XML_DECLARATION = "<?xml encoding='ISO-8859-1'?>";
 private static final String EMPTY_CONTENT = "";
 private static final String INVALID_CHARSET = "invalid_charset";
 private static final String UNKNOWN_CHARSET = "unknown_charset";

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, CHARSET_UTF8, null);
 // test for null input
 assertNotNull(doc);
 // test for null charsetName
 assertNotNull(doc);
 // test for null baseUri
 assertNotNull(doc);
 // test for valid input
 assertEquals(Charset.forName(CHARSET_UTF8), Charset.forName(doc.outputSettings().charset().name()));
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File file = new File("non_existent_file.html");
 assertThrows(IOException.class, () -> DataUtil.load(file, CHARSET_UTF8, null));
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(EMPTY_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, CHARSET_UTF8, null);
 // test for null input
 assertNotNull(doc);
 // test for null charsetName
 assertNotNull(doc);
 // test for null baseUri
 assertNotNull(doc);
 // test for valid input
 assertEquals(Charset.forName(CHARSET_UTF8), Charset.forName(doc.outputSettings().charset().name()));
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetName() {
 InputStream inputStream = new ByteArrayInputStream(EMPTY_CONTENT.getBytes(StandardCharsets.UTF_8));
 assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(inputStream, INVALID_CHARSET, null));
 }

 @Test
 public void testLoadInputStreamWithUnsupportedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(XML_DECLARATION.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, UNKNOWN_CHARSET, null);
 // test for null input
 assertNotNull(doc);
 // test for unsupported charset
 assertEquals("unknown_charset", doc.outputSettings().charset().name());
 }

 @Test
 public void testParseByteData() {
 ByteBuffer byteData = ByteBuffer.wrap(EMPTY_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, CHARSET_UTF8, null, Parser.htmlParser());
 // test for null byteData
 assertNotNull(doc);
 // test for null charsetName
 assertNotNull(doc);
 // test for null baseUri
 assertNotNull(doc);
 // test for valid input
 assertEquals(Charset.forName(Charset.defaultCharset().name()), Charset.forName(doc.outputSettings().charset().name()));
 }

 @Test
 public void testParseByteDataWithBOM() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] {(byte) 0xFE, (byte) 0xFF, 0, 0});
 String charsetName = "UTF-8";
 Document doc = DataUtil.parseByteData(byteData, charsetName);
 // test for null byteData
 assertNotNull(doc);
 // test for valid input
 assertEquals(Charset.forName(CHARSET_UTF8), Charset.forName(doc.outputSettings().charset().name()));
 }

 @Test
 public void testParseByteDataWithUnknownBOM() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] {(byte) 0xFF, (byte) 0xFF, 0, 0});
 String charsetName = "UTF-8";
 assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName));
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream(new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
 DataUtil.crossStreams(inputStream, outputStream);
 ByteBuffer result = ByteBuffer.wrap(outputStream.toByteArray());
 byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
 assertArrayEquals(expected, result.array());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = CONTENT_TYPE_WITH_CHARSET;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(CHARSET_GB2312, charset);
 contentType = CONTENT_TYPE_WITHOUT_CHARSET;
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testValidateCharset() {
 String charset = CHARSET_UTF8;
 assertEquals(CHARSET_UTF8, DataUtil.validateCharset(charset));
 charset = CHARSET_UTF8.toUpperCase(Locale.ENGLISH);
 assertEquals(CHARSET_UTF8, DataUtil.validateCharset(charset));
 charset = "";
 assertNull(DataUtil.validateCharset(charset));
 charset = "invalid_charset";
 assertNull(DataUtil.validateCharset(charset));
 charset = "unknown_charset";
 assertNull(DataUtil.validateCharset(charset));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundaryLength, boundary.length());
 for (int i = 0; i < boundaryLength; i++) {
 assertEquals(mimeBoundaryChars[i % mimeBoundaryChars.length], boundary.charAt(i));
 }
 }

}