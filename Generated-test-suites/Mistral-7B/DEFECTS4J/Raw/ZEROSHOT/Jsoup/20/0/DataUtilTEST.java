package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

 private DataUtil dataUtil;
 private InputStream inputStream;
 private Parser parser;
 private Document document;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 inputStream = Mockito.mock(InputStream.class);
 parser = Mockito.mock(Parser.class);
 document = Mockito.mock(Document.class);
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 byte[] expectedData = "Test data".getBytes();

 when(inputStream.readAllBytes()).thenReturn(expectedData);
 when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(document);

 FileInputStream inStream = new FileInputStream(file);
 try {
 Document actualDoc = dataUtil.load(file, charsetName, baseUri);
 assertEquals(document, actualDoc);
 } finally {
 inStream.close();
 }

 verify(parser).parseInput(anyString(), eq(baseUri));
 verify(inputStream).readAllBytes();
 }

 @Test
 public void testLoadFileWithoutCharset() throws IOException {
 File file = new File("test.html");
 String baseUri = "http://test.com";
 byte[] expectedData = "Test data".getBytes();

 when(inputStream.readAllBytes()).thenReturn(expectedData);
 when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(document);

 FileInputStream inStream = new FileInputStream(file);
 try {
 Document actualDoc = dataUtil.load(file, null, baseUri);
 assertEquals(document, actualDoc);
 } finally {
 inStream.close();
 }

 verify(parser, times(2)).parseInput(anyString(), eq(baseUri));
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] expectedData = "Test data".getBytes();
 outputStream.write(expectedData);
 ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";

 when(parser.parseByteData(any(ByteBuffer.class), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(document);

 try {
 DataUtil.load(inStream, charsetName, baseUri, parser);
 } finally {
 inStream.close();
 }

 verify(parser).parseByteData(any(ByteBuffer.class), eq(charsetName), eq(baseUri), eq(parser));
 }

 @Test
 public void testLoadInputStreamWithoutCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] expectedData = "Test data".getBytes();
 outputStream.write(expectedData);
 ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
 String baseUri = "http://test.com";

 when(parser.parseByteData(any(ByteBuffer.class), anyString(), eq(baseUri), eq(parser))).thenReturn(document);

 try {
 DataUtil.load(inStream, null, baseUri, parser);
 } finally {
 inStream.close();
 }

 verify(parser, times(2)).parseByteData(any(ByteBuffer.class), anyString(), eq(baseUri), eq(parser));
 }

 @Test
 public void testParseByteDataWithCharsetDetectedFromMetaTag() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] expectedData = "Test data".getBytes("GBK");
 outputStream.write(expectedData);
 ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
 String baseUri = "http://test.com";

 String charsetName = "GBK";
 when(parser.parseByteData(any(ByteBuffer.class), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(document);
 when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(document);
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);

 try {
 DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), null, baseUri, parser);
 } finally {
 inStream.close();
 }

 verify(parser, times(3)).parseInput(anyString(), anyString());
 verify(parser).parseByteData(ByteBuffer.wrap(expectedData), eq(charsetName), eq(baseUri), eq(parser));
 }

 @Test
 public void testParseByteDataWithCharsetDetectedFromContentTypeHeader() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] expectedData = "Test data".getBytes();
 outputStream.write(expectedData);
 ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
 String baseUri = "http://test.com";
 String contentType = "text/html; charset=UTF-8";

 String charsetName = "UTF-8";
 when(parser.parseByteData(any(ByteBuffer.class), eq(charsetName), eq(baseUri), eq(parser))).thenReturn(document);
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);

 try {
 DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), contentType, baseUri, parser);
 } finally {
 inStream.close();
 }

 verify(parser, times(2)).parseInput(anyString(), anyString());
 verify(parser).parseByteData(ByteBuffer.wrap(expectedData), eq(charsetName), eq(baseUri), eq(parser));
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] expectedData = "Test data".getBytes();
 outputStream.write(expectedData);
 ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
 String baseUri = "http://test.com";

 when(parser.parseByteData(any(ByteBuffer.class), anyString(), eq(baseUri), eq(parser))).thenReturn(document);
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);

 try {
 DataUtil.parseByteData(ByteBuffer.wrap(inStream.readAllBytes()), null, baseUri, parser);
 } finally {
 inStream.close();
 }

 verify(parser, times(2)).parseInput(anyString(), anyString());
 }
}