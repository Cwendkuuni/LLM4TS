package org.jsoup.helper.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

 private static InputStream inputStream;
 private static File file;

 @BeforeClass
 public static void setUp() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n" +
         "<meta charset=\"UTF-8\">\n<meta http-equiv=\"content-type\" content=\"text/html; charset=gb2312\">\n</head>\n" +
         "<body>\n<h1>Hello World</h1>\n</body>\n</html>";

 inputStream = new ByteArrayInputStream(testData.getBytes());
 file = new File("test.html");
 try (FileOutputStream outputStream = new FileOutputStream(file)) {
 outputStream.write(testData.getBytes());
 }
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(file, null, null);
 assertEquals("UTF-8", document.outputSettings().charset());
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 Document document = DataUtil.load(file, "gb2312", null);
 assertEquals("gb2312", document.outputSettings().charset());
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(inputStream, null, null);
 assertEquals("UTF-8", document.outputSettings().charset());
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 Document document = DataUtil.load(inputStream, "gb2312", null);
 assertEquals("gb2312", document.outputSettings().charset());
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedParser() throws IOException {
 Parser parser = DataUtil.parser().xmlParser();
 Document document = DataUtil.load(inputStream, null, null, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
 Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertEquals("UTF-8", document.outputSettings().charset());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
 Document document = DataUtil.parseByteData(byteData, "gb2312", null, Parser.htmlParser());
 assertEquals("gb2312", document.outputSettings().charset());
 assertNotNull(document);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=gb2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("gb2312", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new FileInputStream(file);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 // Do some checks on byteData here.
 }

 @Test
 public void testCloseStream() throws IOException {
 FileInputStream inputStream = new FileInputStream(file);
 try {
 Document document = DataUtil.load(inputStream, null, null);
 } finally {
 inputStream.close();
 }
 }
}