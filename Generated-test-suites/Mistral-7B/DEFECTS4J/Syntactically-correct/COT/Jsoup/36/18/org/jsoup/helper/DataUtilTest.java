package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

 private static final String TEST_FILE = "test.html";
 private static final String DEFAULT_CHARSET = "UTF-8";
 private static final String BASE_URI = "file:/test/";
 private static final Parser PARSER = Parser.htmlParser();

 @Test
 public void testLoadFile() throws IOException {
 File file = new File(TEST_FILE);
 Document doc = DataUtil.load(file, DEFAULT_CHARSET, BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileNullCharset() throws IOException {
 File file = new File(TEST_FILE);
 Document doc = DataUtil.load(file, null, BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileInvalidCharset() throws IOException {
 File file = new File(TEST_FILE);
 Document doc = null;
 String invalidCharset = "GBK";
 try {
 doc = DataUtil.load(file, invalidCharset, BASE_URI);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
 Document doc = DataUtil.load(inputStream, DEFAULT_CHARSET, BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamNullCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
 Document doc = DataUtil.load(inputStream, null, BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
 Document doc = null;
 String invalidCharset = "GBK";
 try {
 doc = DataUtil.load(inputStream, invalidCharset, BASE_URI);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
 Document doc = DataUtil.load(inputStream, DEFAULT_CHARSET, BASE_URI, PARSER);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataNullCharset() {
 ByteBuffer byteData = ByteBuffer.allocate(1024);
 Document doc = null;
 String baseUri = "file:/test/";
 try {
 doc = DataUtil.parseByteData(byteData, null, baseUri, PARSER);
 } catch (IOException e) {
 // not expected
 }
 assertNull(doc);
 }

 @Test
 public void testParseByteDataValidData() {
 byte[] data = "<html></html>".getBytes();
 ByteBuffer byteData = ByteBuffer.wrap(data);
 Document doc = DataUtil.parseByteData(byteData, DEFAULT_CHARSET, BASE_URI, PARSER);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataInvalidCharset() {
 byte[] data = "<html></html>".getBytes();
 ByteBuffer byteData = ByteBuffer.wrap(data);
 Document doc = null;
 String invalidCharset = "GBK";
 try {
 doc = DataUtil.parseByteData(byteData, invalidCharset, BASE_URI, PARSER);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testReadToByteBufferNullInputStream() {
 InputStream inputStream = null;
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 assertNull(byteData);
 }

 @Test
 public void testReadToByteBufferValidData() throws IOException {
 byte[] data = "<html></html>".getBytes();
 InputStream inputStream = new ByteArrayInputStream(data);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 assertNotNull(byteData);
 }

 @Test
 public void testReadToByteBufferInvalidCharset() throws IOException {
 byte[] data = "<html></html>".getBytes();
 InputStream inputStream = new ByteArrayInputStream(data);
 ByteBuffer byteData = null;
 try {
 byteData = DataUtil.readToByteBuffer(inputStream, "GBK");
 } catch (IOException e) {
 // expected
 }
 assertNull(byteData);
 }

 @Test
 public void testGetCharsetFromContentTypeValidCharset() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeInvalidCharset() {
 String contentType = "text/html; charset=gb2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testGetCharsetFromContentTypeEmptyContentType() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test(expected = NullPointerException.class)
 public void testGetCharsetFromContentTypeNullContentType() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 }
}