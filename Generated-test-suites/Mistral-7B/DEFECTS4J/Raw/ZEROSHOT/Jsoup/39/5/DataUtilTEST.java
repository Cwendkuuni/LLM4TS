package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 private static final String TEST_FILE_PATH = "test.html"; // replace with your test file path
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com";
 private Document testDocument;

 @Before
 public void setUp() throws IOException {
 File testFile = new File(TEST_FILE_PATH);
 testDocument = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 }

 @After
 public void tearDown() throws Exception {
 if (testDocument != null) testDocument.close();
 }

 @Test
 public void testLoadFile() throws IOException {
 assertNotNull(testDocument);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 byteStream.write("Test string".getBytes());
 InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(document);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("Test string".getBytes(Charset.forName(TEST_CHARSET)));
 Document document = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 assertEquals("Test string", document.text());
 }

 @Test
 public void testParseByteDataWithDifferentCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("Test string\u00E9".getBytes(Charset.forName("ISO-8859-1")));
 Document document = DataUtil.parseByteData(byteData, "ISO-8859-1", TEST_BASE_URI, Parser.htmlParser());
 assertNotNull(document);
 assertEquals("Test string\u00E9", document.text());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 byteStream.write("Test string".getBytes());
 InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 byte[] expectedByteData = byteStream.toByteArray();
 assertEquals(expectedByteData.length, byteData.limit());
 }
}