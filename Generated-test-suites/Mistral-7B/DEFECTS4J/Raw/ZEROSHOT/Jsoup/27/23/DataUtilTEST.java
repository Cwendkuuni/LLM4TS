package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DataUtilTest {

 private Parser parser;
 private InputStream inputStream;
 private File file;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 parser = mock(Parser.class);
 inputStream = mock(InputStream.class);
 file = mock(File.class);
 }

 @Test
 public void testLoad_File() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("html content".getBytes("UTF-8"));
 when(file.length()).thenReturn(outputStream.size());
 when(file.getInputStream()).thenReturn(new ByteArrayInputStream(outputStream.toByteArray()));

 Document document = DataUtil.load(file, charsetName, baseUri);
 assertEquals("html content", document.body().text());
 }

 @Test
 public void testLoad_InputStream() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("html content".getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

 Document document = DataUtil.load(inputStream, charsetName, baseUri);
 assertEquals("html content", document.body().text());
 }

 @Test
 public void testLoad_InputStream_DefaultCharset() throws IOException {
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream("text/html; charset=gb2312\r\n\r\n<html lang='zh-CN'>".getBytes("UTF-8"));

 Document document = DataUtil.load(inputStream, null, baseUri);
 assertEquals("", document.body().text()); // defaultCharset is used.

 document = DataUtil.load(inputStream, baseUri);
 assertEquals("", document.body().text()); // defaultCharset is used.

 document = DataUtil.load(inputStream, "", baseUri);
 assertEquals("", document.body().text()); // empty charsetName is treated as defaultCharset.
 }

 @Test
 public void testLoad_InputStream_CustomParser() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = mock(Parser.class);
 when(parser.parseInput("html content", baseUri)).thenReturn(mock(Document.class));

 Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals("html content", document.body().text());
 }

 @Test
 public void testParseByteData() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 String docData = "html content";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals("html content", document.body().text());
 }

 @Test
 public void testParseByteData_NoCharsetName() throws IOException {
 String baseUri = "http://example.com";
 String docData = "html content";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

 Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
 assertEquals("html content", document.body().text());
 }

 @Test
 public void testParseByteData_DefaultCharset() throws IOException {
 String baseUri = "http://example.com";
 String docData = "text/html; charset=gb2312\r\n\r\n<html lang='zh-CN'>";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

 Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
 assertEquals("", document.body().text());

 document = DataUtil.parseByteData(byteData, baseUri, parser);
 assertEquals("", document.body().text());
 }

 @Test
 public void testParseByteData_CustomCharset() throws IOException {
 String charsetName = "gb2312";
 String baseUri = "http://example.com";
 String docData = "<html lang='zh-CN'>";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));

 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals("", document.body().text()); // charset is ignored.

 document = DataUtil.parseByteData(byteData, null, baseUri, parser); // defaultCharset is used.
 assertEquals("", document.body().text());

 document = DataUtil.parseByteData(byteData, charsetName + "\r\n" + docData, baseUri, parser); // charset is used.
 assertEquals("<html lang='zh-CN'>", document.body().html());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 byte[] expected = "test data".getBytes();
 byte[] actual = new byte[byteData.limit()];
 byteData.get(actual);
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testReadToByteBuffer_IOException() throws IOException {
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.read()).thenThrow(new IOException());
 DataUtil.readToByteBuffer(inputStream);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=gb2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("gb2312", charset);
 }

 @Test
 public void testGetCharsetFromContentType_NoCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }
}