package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 private Document doc;
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com";
 private static final String TEST_FILE_NAME = "testFile.html";
 private static final String TEST_INPUT_STREAM_CONTENT = "<html><head><meta charset=\"" + TEST_CHARSET + "\"></head><body></body></html>";

 @Before
 public void setUp() throws Exception {
 doc = parseStringToDocument(TEST_INPUT_STREAM_CONTENT, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 }

 @After
 public void tearDown() throws Exception {
 if (doc != null) {
 doc.close();
 }
 }

 // Test for load(File) method
 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File(TEST_FILE_NAME);
 doc = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 assertEquals(TEST_INPUT_STREAM_CONTENT, doc.body().html());
 }

 // Test for load(InputStream) method
 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 testOutputStream.write(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET)));
 InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
 doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
 assertEquals(TEST_INPUT_STREAM_CONTENT, doc.body().html());
 }

 // Test for load(InputStream, String, String) method with different Parser
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 testOutputStream.write(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET)));
 InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());
 doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
 assertEquals(TEST_INPUT_STREAM_CONTENT, doc.body().html()); // Even though it's an XML parser, it still returns an HTML document for simplicity
 }

 // Test for crossStreams method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET)));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI); // Load the test file into a Document first for reference
 DataUtil.crossStreams(testInputStream, outputStream);
 assertEquals(doc.body().html(), new String(outputStream.toByteArray()));
 }

 // Test for parseByteData method with default charset
 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET)));
 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, TEST_BASE_URI, Parser.htmlParser());
 assertEquals(TEST_INPUT_STREAM_CONTENT, doc.body().html());
 }

 // Test for parseByteData method with custom charset
 @Test
 public void testParseByteDataWithCustomCharset() {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName("GBK")));
 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), "GBK", TEST_BASE_URI, Parser.htmlParser());
 assertEquals(TEST_INPUT_STREAM_CONTENT, doc.body().html()); // Assuming the given content is in GBK charset
 }

 // Test for parseByteData method with unsupported charset
 @Test(expected = IllegalCharsetNameException.class)
 public void testParseByteDataWithUnsupportedCharset() {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET)));
 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), "XYZ", TEST_BASE_URI, Parser.htmlParser());
 }

 // Test for readToByteBuffer method
 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET)));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
 byte[] expectedByteArray = TEST_INPUT_STREAM_CONTENT.getBytes(Charset.forName(TEST_CHARSET));
 byteBuffer.mark();
 byte[] actualByteArray = new byte[byteBuffer.remaining()];
 byteBuffer.get(actualByteArray);
 byteBuffer.reset();
 assertArrayEquals(expectedByteArray, actualByteArray);
 }

 // Test for emptyByteBuffer method
 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, byteBuffer.limit());
 }

 // Test for getCharsetFromContentType method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals(TEST_CHARSET, DataUtil.getCharsetFromContentType(contentType));
 }

 // Test for getCharsetFromContentType method with unsupported charset
 @Test
 public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
 String contentType = "text/html; charset=XYZ";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), 32);
 }

}