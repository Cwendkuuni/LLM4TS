package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
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

 private File file;
 private InputStream inputStream;
 private Document document;
 private Parser parser;

 @Before
 public void setUp() {
 file = new File("testFile.html");
 inputStream = mock(InputStream.class);
 parser = mock(Parser.class);
 }

 @After
 public void tearDown() throws Exception {
 if (file.exists()) {
 file.delete();
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 document = DataUtil.load(file, "UTF-8", null);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullCharset() throws IOException {
 document = DataUtil.load(file, null, null);
 }

 @Test
 public void testParseByteData() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 document = DataUtil.parseByteData(byteData, "UTF-8", null, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 document = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals("UTF-8", DataUtil.getDefaultCharset());
 }

 @Test
 public void testParseByteDataWithMetaCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 String charset = "GB2312";
 String docData = "<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\">\n<body>\n<p>Test</p>\n</body>\n</html>";
 when(parser.parseInput(anyString(), anyString())).thenReturn(Document.newDocument(docData));
 document = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(charset, document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidMetaCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 String charset = "INVALID";
 String docData = "<?xml version=\"1.0\" encoding=\"" + "UTF-8" + "\"?>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\">\n<body>\n<p>Test</p>\n</body>\n</html>";
 when(parser.parseInput(anyString(), anyString())).thenReturn(Document.newDocument(docData));
 document = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithContentTypeCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 String charset = "ISO-8859-1";
 String contentType = "text/html; charset=" + charset;
 document = DataUtil.parseByteData(byteData, contentType, null, parser);
 assertEquals(charset, document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithContentTypeNoCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 String contentType = "text/html";
 document = DataUtil.parseByteData(byteData, contentType, null, parser);
 assertEquals("UTF-8", document.outputSettings().charset());
 }
}