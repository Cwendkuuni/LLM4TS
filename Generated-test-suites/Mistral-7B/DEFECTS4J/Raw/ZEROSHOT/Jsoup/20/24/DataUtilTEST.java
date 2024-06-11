package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private Document document;
 private Parser parser;

 @Before
 public void setup() {
 parser = Parser.htmlParser();
 }

 @Rule
 public ExpectedException expectedException = ExpectedException.none();

 @Rule
 public TemporaryFolder folder = new TemporaryFolder();

 @Test
 public void testLoadFile() throws IOException {
 File file = folder.newFile("test.html");
 byte[] fileContent = "<html><body><h1>Test</h1></body></html>".getBytes("UTF-8");
 FileOutputStream outputStream = new FileOutputStream(file);
 outputStream.write(fileContent);
 outputStream.close();

 document = DataUtil.load(file, "UTF-8", null);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = folder.newFile("test.html");
 byte[] fileContent = "<html><body><h1>Test</h1></body></html>".getBytes("UTF-8");
 FileOutputStream outputStream = new FileOutputStream(file);
 outputStream.write(fileContent);
 outputStream.close();

 expectedException.expect(IOException.class);
 expectedException.expectMessage("Couldn't find charset in the input stream");
 DataUtil.load(file, null, null);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
 document = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
 document = DataUtil.load(inputStream, null, null);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetInContentType() throws IOException {
 InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
 String contentType = "text/html; charset=invalidCharset";
 document = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithValidCharsetInContentType() throws IOException {
 InputStream inputStream = new FileInputStream(new File("src/test/resources/test.html"));
 String contentType = "text/html; charset=UTF-8";
 document = DataUtil.load(inputStream, contentType, null);
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes("UTF-8"));
 document = DataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes("UTF-8"));
 expectedException.expect(IllegalArgumentException.class);
 expectedException.expectMessage("Charset name must not be null");
 DataUtil.parseByteData(byteData, null, null, parser);
 }

 @Test
 public void testParseByteDataWithValidCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes("UTF-8"));
 document = DataUtil.parseByteData(byteData, "UTF-8", null, parser);
 assertNotNull(document);
 }
}