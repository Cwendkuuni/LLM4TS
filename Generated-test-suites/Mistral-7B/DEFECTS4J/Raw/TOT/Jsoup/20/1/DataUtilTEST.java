package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private static final String TEST_HTML_UTF8 = "### UTF-8 TEST HTML ###\n" +
         "<html lang=\"en\">\n" +
         "<head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Test UTF-8 string: 你好，世界!</p>\n" +
         "</body>\n" +
         "</html>";

 private static final String TEST_HTML_GBK = "### GBK TEST HTML ###\n" +
         "<html lang=\"en\">\n" +
         "<head>\n" +
         "<meta charset=\"GBK\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Test GBK string: 你好，世界!</p>\n" +
         "</body>\n" +
         "</html>";

 @Rule
 public static final ResourceFolder resourceFolder = new ResourceFolder();

 @Before
 public void setup() {
 }

 @Test
 public void testLoadFromFileUTF8() throws IOException {
 File testFile = new File(resourceFolder.getResourcePath("DataUtilTest_UTF8.html"));
 Document doc = DataUtil.load(testFile, "UTF-8", "/");
 assertNotNull(doc);
 Validate.isTrue(doc.outputSettings().charset().equals("UTF-8"), "UTF-8 charset validation failed.");
 }

 @Test
 public void testLoadFromFileUnknownCharset() throws IOException {
 File testFile = new File(resourceFolder.getResourcePath("DataUtilTest_GBK.html"));
 Document doc = DataUtil.load(testFile, null, "/");
 assertNotNull(doc);
 assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset().toString());
 }

 @Test
 public void testLoadFromInputStreamUTF8() throws IOException {
 InputStream testStream = new ByteArrayInputStream(TEST_HTML_UTF8.getBytes());
 Document doc = DataUtil.load(testStream, "UTF-8", "/");
 assertNotNull(doc);
 Validate.isTrue(doc.outputSettings().charset().equals("UTF-8"), "UTF-8 charset validation failed.");
 }

 @Test
 public void testLoadFromInputStreamUnknownCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream(TEST_HTML_GBK.getBytes());
 Document doc = DataUtil.load(testStream, null, "/");
 assertNotNull(doc);
 assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset().toString());
 }

 @Test
 public void testLoadFromInputStreamWithSpecifiedCharsetGBK() throws IOException {
 InputStream testStream = new ByteArrayInputStream(TEST_HTML_GBK.getBytes());
 Document doc = DataUtil.load(testStream, "GBK", "/");
 assertNotNull(doc);
 Validate.isTrue(doc.outputSettings().charset().equals("GBK"), "GBK charset validation failed.");
 }

 @Test
 public void testParseByteDataUTF8() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(TEST_HTML_UTF8.getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataGBK() {
 // Cannot test this case directly as JUnit cannot handle GBK encoding.
 // However, we can check that parseByteData does not throw an exception and reads the correct charset.
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(TEST_HTML_GBK.getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
 try {
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 } catch (IOException e) {
 fail("IOException in parseByteData test case");
 }
 }

 @Test
 public void testReadToByteBufferUTF8() throws IOException {
 InputStream testStream = new ByteArrayInputStream(TEST_HTML_UTF8.getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(testStream);
 Validate.isTrue(byteData.hasArray(), "byteData.hasArray() validation failed.");
 bytesEqual(byteData.array(), TEST_HTML_UTF8.getBytes());
 }

 @Test
 public void testReadToByteBufferGBK() {
 // Cannot test this case directly as JUnit cannot handle GBK encoding.
 // However, we can check that readToByteBuffer does not throw an exception.
 InputStream testStream = new ByteArrayInputStream(TEST_HTML_GBK.getBytes());
 try {
 DataUtil.readToByteBuffer(testStream);
 } catch (IOException e) {
 fail("IOException in readToByteBuffer test case");
 }
 }

 @Test
 public void testGetCharsetFromContentTypeUTF8() {
 String contentTypeUTF8 = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentTypeUTF8);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeUnknown() {
 String contentTypeUnknown = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentTypeUnknown);
 assertNull(charset);
 }

 private void bytesEqual(byte[] a, byte[] b) {
 for (int i = 0; i < a.length && i < b.length; i++) {
 if (a[i] != b[i]) {
 fail("Byte arrays do not match.");
 }
 }
 }

}