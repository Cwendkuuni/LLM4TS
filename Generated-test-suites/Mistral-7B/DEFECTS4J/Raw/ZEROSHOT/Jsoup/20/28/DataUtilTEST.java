package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;

 @Before
 public void setup() {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document document = dataUtil.load(file, "UTF-8", "");
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File file = new File("does_not_exist.html");
 dataUtil.load(file, "UTF-8", "");
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Document document = dataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullStream() throws IOException {
 Document document = dataUtil.load(null, "UTF-8", "");
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
 byteBuffer.put("test.html".getBytes());
 byteBuffer.flip();
 Document document = dataUtil.parseByteData(byteBuffer, "UTF-8", "", parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
 byteBuffer.put("test.html".getBytes());
 byteBuffer.flip();
 try {
 dataUtil.parseByteData(byteBuffer, null, "", parser);
 } catch (IOException e) {
 assertEquals("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML", e.getMessage());
 }
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
 byteBuffer.put("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-16LE'></head><body>Test</body></html>".getBytes());
 byteBuffer.flip();
 Document document = dataUtil.parseByteData(byteBuffer, null, "", parser);
 assertEquals("UTF-16LE", document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithDetectedCharsetFail() throws IOException {
 ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
 byteBuffer.put("<html><head><meta charset='gb2312'></head><body>Test</body></html>".getBytes());
 byteBuffer.flip();
 Document document = dataUtil.parseByteData(byteBuffer, null, "", parser);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 assertEquals("EUC-JP", dataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 assertNull(dataUtil.getCharsetFromContentType(contentType));
 }
}