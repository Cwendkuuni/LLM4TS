package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

 private Document document;
 private InputStream inputStream;
 private File file;

 @Before
 public void setUp() throws Exception {
 inputStream = Mockito.mock(InputStream.class);
 file = Mockito.spy(new File("test.html"));
 }

 @After
 public void tearDown() throws Exception {
 if (file.exists()) {
 file.delete();
 }
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 String expectedCharset = "UTF-8";
 String expectedBaseUri = "http://www.example.com";
 File testFile = new File("test.html");
 testFile.createNewFile();
 FileWriter fileWriter = new FileWriter(testFile);
 fileWriter.write("<html><head><meta charset='" + expectedCharset + "'></head><body></body></html>");
 fileWriter.close();

 document = DataUtil.load(file, expectedCharset, expectedBaseUri);
 assertEquals(expectedCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithoutCharset() throws IOException {
 String expectedBaseUri = "http://www.example.com";
 File testFile = new File("test.html");
 testFile.createNewFile();
 FileWriter fileWriter = new FileWriter(testFile);
 fileWriter.write("<html><head></head><body></body></html>");
 fileWriter.close();

 document = DataUtil.load(file, null, expectedBaseUri);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 String expectedCharset = "UTF-8";
 String expectedBaseUri = "http://www.example.com";
 String testData = "<html><head><meta charset='" + expectedCharset + "'></head><body></body></html>";
 byte[] testBytes = testData.getBytes(Charset.forName(expectedCharset));
 inputStream = new ByteArrayInputStream(testBytes);

 document = DataUtil.load(inputStream, expectedCharset, expectedBaseUri);
 assertEquals(expectedCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithoutCharset() throws IOException {
 String expectedBaseUri = "http://www.example.com";
 String testData = "<html><head></head><body></body></html>";
 byte[] testBytes = testData.getBytes(Charset.forName("UTF-8"));
 inputStream = new ByteArrayInputStream(testBytes);

 document = DataUtil.load(inputStream, null, expectedBaseUri);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithCharset() throws IOException {
 String expectedCharset = "UTF-8";
 String expectedBaseUri = "http://www.example.com";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset='" + expectedCharset + "'></head><body></body></html>".getBytes(Charset.forName(expectedCharset)));
 ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());

 document = DataUtil.parseByteData(byteBuffer, expectedCharset, expectedBaseUri, Parser.htmlParser());
 assertEquals(expectedCharset, document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithoutCharset() throws IOException {
 String expectedBaseUri = "http://www.example.com";
 byte[] testBytes = "<html><head></head><body></body></html>".getBytes(Charset.forName("UTF-8"));
 ByteBuffer byteBuffer = ByteBuffer.wrap(testBytes);

 document = DataUtil.parseByteData(byteBuffer, null, expectedBaseUri, Parser.htmlParser());
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File testFile = new File("test.html");
 testFile.createNewFile();
 FileWriter fileWriter = new FileWriter(testFile);fileWriter.write("<html><head></head><body></body></html>");
 fileWriter.close();

 try {
 DataUtil.load(file, "invalidCharset", null);
 fail("Should have thrown an IOException.");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testParseByteDataWithSpuriousByteOrderMark() throws IOException {
 String expectedBaseUri = "http://www.example.com";
 String testData = "\ufeff<html><head></head><body></body></html>";
 ByteBuffer byteBuffer = ByteBuffer.wrap(testData.getBytes(Charset.forName("UTF-8")));

 document = DataUtil.parseByteData(byteBuffer, null, expectedBaseUri, Parser.htmlParser());
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 String testData = "test";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(testData.getBytes(Charset.forName("UTF-8")));
 assertEquals(expectedByteBuffer.limit(), byteBuffer.limit());
 }
}