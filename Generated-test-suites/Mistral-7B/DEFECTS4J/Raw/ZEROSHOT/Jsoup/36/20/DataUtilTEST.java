package org.jsoup.helper;

import org.jsoup.Document;
import org.jsoup.Element;
import org.jsoup.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.xml.bind.*"})
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

 private File testFile;
 private InputStream testInputStream;
 private Document testDocument;
 private Parser testParser;

 @Before
 public void setUp() throws Exception {
 testFile = new File("test.html");
 testInputStream = new ByteArrayInputStream("Test Data".getBytes());
 testDocument = mock(Document.class);
 testParser = mock(Parser.class);
 PowerMockito.mockStatic(Charset.class);
 }

 @After
 public void tearDown() throws Exception {
 if (testFile != null && testFile.exists()) {
 testFile.delete();
 }
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 String charset = "UTF-8";
 String baseUri = "/test";
 File file = new File("test.html");
 file.createNewFile();
 writeToFile("Test Data", file, charset);
 Document doc = DataUtil.load(file, charset, baseUri);
 assertEquals("Test Data", doc.text());
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 String baseUri = "/test";
 File file = new File("test.html");
 file.createNewFile();
 writeToFile("Test Data", file);
 Document doc = DataUtil.load(file, null, baseUri);
 assertEquals("Test Data", doc.text());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 String charset = "UTF-8";
 String baseUri = "/test";
 Document doc = DataUtil.load(testInputStream, charset, baseUri);
 assertEquals("Test Data", doc.text());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 String baseUri = "/test";
 Document doc = DataUtil.load(testInputStream, null);
 assertEquals("Test Data", doc.text());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String charset = "UTF-8";
 String baseUri = "/test";
 Parser parser = mock(Parser.class);
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(testDocument);
 Document doc = DataUtil.load(testInputStream, charset, baseUri, parser);
 assertEquals("Test Data", testDocument.text());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 String charset = "InvalidCharset";
 String baseUri = "/test";
 File file = new File("test.html");
 file.createNewFile();
 writeToFile("Test Data", file, charset);
 try {
 DataUtil.load(file, charset, baseUri);
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 String charset = "InvalidCharset";
 String baseUri = "/test";
 try {
 DataUtil.load(testInputStream, charset, baseUri);
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 String baseUri = "/test";
 ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertEquals("Test Data", doc.text());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 String charset = "UTF-8";
 String baseUri = "/test";
 ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
 Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
 assertEquals("Test Data", doc.text());
 }

 @Test
 public void testDetermineCharsetFromContentType() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testDetermineCharsetFromContentTypeFail() throws IOException {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 private void writeToFile(String data, File file, String charset) throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Charset.forName(charset).encode(data.getBytes(), outputStream, false);
 FileWriter fileWriter = new FileWriter(file);
 fileWriter.write(outputStream.toByteArray());
 fileWriter.close();
 }
}