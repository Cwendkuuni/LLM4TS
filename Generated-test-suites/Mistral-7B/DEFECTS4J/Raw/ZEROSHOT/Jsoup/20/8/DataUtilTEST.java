package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private static final String TEST_FILE = "testFile.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com/";

 @BeforeClass
 public static void setUp() throws IOException {
 File file = new File(TEST_FILE);
 DataUtil.save(file, TEST_CHARSET, TEST_BASE_URI, "<html><body></body></html>", Parser.htmlParser());
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE), TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(Charsets.UTF_8));
 Document doc = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(Charsets.UTF_8));
 Document doc = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes(Charsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithCharsetChange() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><head>" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
         "</head><body></body></html>".getBytes(Charsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
 assertEquals("ISO-8859-1", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullCharset() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE), null, TEST_BASE_URI);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(Charsets.UTF_8));
 Document doc = DataUtil.load(inputStream, null, TEST_BASE_URI);
 }
}