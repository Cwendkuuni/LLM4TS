package org.jsoup.helper.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final String TEST_FILE_NAME = "testFile.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://example.com/";
 private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File(TEST_FILE_NAME);
 testFile.createNewFile();
 try (FileOutputStream fileOutput = new FileOutputStream(testFile)) {
 fileOutput.write("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 }
 Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String testData = "<html><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 Assert.assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String testData = "<html><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseByteData() throws IOException {
 String testData = "<html><body><h1>Test</h1></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8))) {
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 Assert.assertNotNull(document);
 }
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 String testData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"> <body><h1>Test</h1></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8))) {
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, TEST_BASE_URI, Parser.htmlParser());
 Assert.assertEquals("UTF-16", document.outputSettings().charset());
 }
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 String testData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=X-INVALID-CHARSET\"> <body><h1>Test</h1></body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try (ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8))) {
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, TEST_BASE_URI, Parser.htmlParser());
 Assert.assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }
 }

 @Test
 public void testGetCharsetFromContentType() throws IOException {
 String testContentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(testContentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 String testData = "<html><body><h1>Test</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 byte[] testBuffer = new byte[byteData.limit()];
 byteData.get(testBuffer);
 Assert.assertArrayEquals(testData.getBytes(StandardCharsets.UTF_8), testBuffer);
 }
}