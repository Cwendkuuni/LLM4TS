package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharsetDetector;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;

public class DataUtilTest {

 private Document doc;
 private ByteArrayInputStream inputStream;
 private Parser parser;

 @Before
 public void setUp() {
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document expectedDoc = parser.parse(file);
 Document actualDoc = DataUtil.load(file, null, null);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parse(inputStream);
 Document actualDoc = DataUtil.load(inputStream, "UTF-8", null);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parse(inputStream);
 Document actualDoc = null;
 try {
 actualDoc = DataUtil.load(inputStream, "UTF-16", null);
 } catch (IOException e) {
 // expected
 }
 Assert.assertNull(actualDoc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parse(inputStream);
 Document actualDoc = null;
 try {
 actualDoc = DataUtil.load(inputStream, null, null);
 } catch (IOException e) {
 // expected
 }
 Document docWithoutCharset = parser.parseInput(htmlContent, null);
 actualDoc = DataUtil.parseByteData(ByteBuffer.wrap(Arrays.copyOfRange(inputStream.readAllBytes(), 0, 100)), null, null, parser);
 assertEquals(expectedDoc, actualDoc);
 assertEquals(expectedDoc.outputSettings().charset(), docWithoutCharset.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithValidCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parseInput(htmlContent, null);
 Document actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parseInput(htmlContent, null);
 Document actualDoc = null;
 try {
 actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 } catch (IOException e) {
 // expected
 }
 Document docWithoutCharset = parser.parseInput(htmlContent, null);
 actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(expectedDoc, actualDoc);
 assertEquals(expectedDoc.outputSettings().charset(), docWithoutCharset.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidCharsetInContentType() throws IOException {
 String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parseInput(htmlContent, null);
 Document actualDoc = null;
 try {
 actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 } catch (IOException e) {
 // expected
 }
 Document docWithoutCharset = parser.parseInput(htmlContent, null);
 actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(expectedDoc, actualDoc);
 assertEquals(expectedDoc.outputSettings().charset(), docWithoutCharset.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithValidCharsetDetected() throws IOException {
 String htmlContent = "<html><head><meta charset=\"gb2312\"></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parseInput(htmlContent, null);
 Document actualDoc = null;
 try {
 actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 } catch (IOException e) {
 // expected
 }
 Document docWithoutCharset = parser.parseInput(htmlContent, null);
 actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(expectedDoc, actualDoc);
 assertEquals(expectedDoc.outputSettings().charset(), docWithoutCharset.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithBOM() throws IOException {
 String htmlContent = "\ufeff<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document expectedDoc = parser.parseInput(htmlContent, null);
 Document actualDoc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(expectedDoc, actualDoc);
 }
}