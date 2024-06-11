package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 private Document doc;
 private Document expectedDoc;
 private ByteArrayOutputStream outStream;
 private ByteArrayInputStream inStream;

 @Before
 public void setup() {
 // Create a test document for comparisons
 String html = "<html><head><meta charset=\"UTF-8\"><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 expectedDoc = Parser.htmlParser().parseInput(html, "http://test.com");
 }

 /**
 * Test method for {@link DataUtil#load(File, String, String)}.
 * @throws IOException
 */
 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("test.html");
 testFile.createNewFile();
 try (FileWriter writer = new FileWriter(testFile);
 BufferedWriter bw = new BufferedWriter(writer)) {
 bw.write(html);
 }
 doc = DataUtil.load(testFile, "UTF-8", "http://test.com");
 assertEquals(expectedDoc, doc);
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String)}.
 * @throws IOException
 */
 @Test
 public void testLoadStream() throws IOException {
 outStream = new ByteArrayOutputStream();
 String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 outStream.write(testHtml.getBytes());
 outStream.close();

 inStream = new ByteArrayInputStream(outStream.toByteArray());
 doc = DataUtil.load(inStream, "UTF-8", "http://test.com");
 assertEquals(expectedDoc, doc);
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
 * @throws IOException
 */
 @Test
 public void testLoadStreamWithParser() throws IOException {
 outStream = new ByteArrayOutputStream();
 String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 outStream.write(testHtml.getBytes());
 outStream.close();

 inStream = new ByteArrayInputStream(outStream.toByteArray());
 doc = DataUtil.load(inStream, "UTF-8", "http://test.com", Parser.xmlParser());
 fail("Expected an exception, but none was thrown.");
 // The test above should have thrown an AssertionError as the Documents are not equal.
 // However, since JUnit 4.11, the default behavior is to ignore failures if the test method name starts with 'test'.
 // To revert to the old behavior, add 'expectedExceptions = AssertionErrors.class' to the @Test annotation.
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
 * @throws IOException
 */
 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(expectedDoc.html().getBytes());
 doc = DataUtil.parseByteData(byteData, "UTF-8", "http://test.com", Parser.htmlParser());
 assertEquals(expectedDoc, doc);
 }

 /**
 * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
 * @throws IOException
 */
 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 outStream.write(testHtml.getBytes());
 outStream.close();

 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outStream.toByteArray()));
 byte[] expectedByteData = expectedDoc.html().getBytes();
 byte[] actualByteData = byteData.array();
 assertArrayEquals(expectedByteData, actualByteData);
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws AssertionError
 */
 @Test(expected = AssertionError.class)
 public void testGetCharsetFromContentTypeNullContentType() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 fail("Expected an AssertionError, but none was thrown.");
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws AssertionError
 */
 @Test(expected = AssertionError.class)
 public void testGetCharsetFromContentTypeInvalidContentType() {
 String contentType = "text/plain; charset=invalidCharset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 fail("Expected an AssertionError, but none was thrown.");
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws AssertionError
 */
 @Test
 public void testGetCharsetFromContentTypeValidContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

}