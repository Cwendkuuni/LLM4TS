package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

 private static final String TEST_FILE_PATH = "testFile.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://localhost";
 private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String TEST_CONTENT_TYPE_NO_CHARSET = "text/html";

 @BeforeClass
 public static void setUp() throws Exception {
 File file = new File(TEST_FILE_PATH);
 if (!file.exists()) {
 file.createNewFile();
 }

 try (Writer writer = new BufferedWriter(new FileWriter(file))) {
 writer.write("<html><head><meta charset=\"UTF-8\"></head><body></body></html>");
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 Document document = DataUtil.load(new File(TEST_FILE_PATH), TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithNullCharset() throws IOException {
 Document document = DataUtil.load(new File(TEST_FILE_PATH), null, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(new File(TEST_FILE_PATH), null, TEST_BASE_URI);
 assertEquals(TEST_CHARSET, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 outputStream.write(testData.getBytes(TEST_CHARSET));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 outputStream.write(testData.getBytes(TEST_CHARSET));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 outputStream.write(testData.getBytes(TEST_CHARSET));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, TEST_BASE_URI);
 assertEquals(TEST_CHARSET, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 outputStream.write(testData.getBytes(TEST_CHARSET));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteData() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(testData.getBytes(TEST_CHARSET));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 Document document = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithNullCharset() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(testData.getBytes(TEST_CHARSET));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 Document document = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(testData.getBytes(TEST_CHARSET));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 Document document = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
 assertEquals(TEST_CHARSET, document.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = TEST_CONTENT_TYPE;
 String expectedCharset = TEST_CHARSET;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String contentType = null;
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testData = "test data";
 outputStream.write(testData.getBytes(TEST_CHARSET));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 int limit = byteData.limit();
 byte[] expectedBytes = testData.getBytes(TEST_CHARSET);
 byte[] actualBytes = new byte[limit];
 byteData.get(actualBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }
}