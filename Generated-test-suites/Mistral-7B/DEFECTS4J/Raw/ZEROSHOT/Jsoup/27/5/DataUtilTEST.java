package org.jsoup.helper;

import org.jsoup.Document;
import org.jsoup.Element;
import org.jsoup.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser htmlParser;
 private Parser xmlParser;
 private File tempFile;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 htmlParser = mock(Parser.class);
 xmlParser = mock(Parser.class);
 tempFile = new File("tempFile.html");
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 writeFile(tempFile, expectedHtml);

 Document actualDocument = dataUtil.load(tempFile, null, null);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 String charsetName = "UTF-8";

 writeFile(tempFile, expectedHtml);

 Document actualDocument = dataUtil.load(tempFile, charsetName, null);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testLoadFileWithDetectedCharset() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 String charsetName = null;

 writeFile(tempFile, expectedHtml);

 Document actualDocument = dataUtil.load(tempFile, charsetName, null);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

 Document actualDocument = dataUtil.load(inputStream, null, null);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 String charsetName = "UTF-8";

 InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

 Document actualDocument = dataUtil.load(inputStream, charsetName, null);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testLoadInputStreamWithDetectedCharset() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

 Document actualDocument = dataUtil.load(inputStream, null, null);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));
 Parser parser = mock(Parser.class);

 Document actualDocument = dataUtil.load(inputStream, null, null, parser);

 Mockito.verify(parser, Mockito.times(1)).parseInput(expectedHtml, null);
 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

 Document actualDocument = dataUtil.parseByteData(byteData, null, null, htmlParser);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 String charsetName = "UTF-8";
 ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, null, htmlParser);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testParseByteDataWithDetectedCharset() {
 String expectedHtml = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
 ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

 Document actualDocument = dataUtil.parseByteData(byteData, null, null, htmlParser);

 assertEquals(expectedHtml, actualDocument.html());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";

 String actualCharset = dataUtil.getCharsetFromContentType(contentType);

 assertEquals("UTF-8", actualCharaset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String contentType = null;

 String actualCharset = dataUtil.getCharsetFromContentType(contentType);

 assertNull(actualCharset);
 }

 private void writeFile(File file, String content) throws IOException {
 try (FileOutputStream outputStream = new FileOutputStream(file)) {
 outputStream.write(content.getBytes(StandardCharsets.UTF_8));
 }
 }

}