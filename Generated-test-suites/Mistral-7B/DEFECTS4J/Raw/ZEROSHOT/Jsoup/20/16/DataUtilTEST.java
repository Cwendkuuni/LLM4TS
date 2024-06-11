package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

 private Document document;
 private Parser parser;

 @Before
 public void setUp() {
 parser = mock(Parser.class);
 document = mock(Document.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 InputStream inputStream = new FileInputStream(file);
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);
 when(parser.parseByteData(Mockito.any(ByteBuffer.class), Mockito.anyString(), Mockito.anyString(), Mockito.eq(parser))).thenReturn(document);

 Document actual = DataUtil.load(file, charsetName, baseUri);
 assertEquals(document, actual);
 inputStream.close();
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);
 when(parser.parseByteData(Mockito.any(ByteBuffer.class), Mockito.anyString(), Mockito.anyString(), Mockito.eq(parser))).thenReturn(document);

 Document actual = DataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(document, actual);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

 Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(document, actual);
 }

 @Test(expected = IOException.class)
 public void testParseByteDataIOException() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes());
 String charsetName = null;
 String baseUri = "http://test.com";
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

 try {
 DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 Parser parser = Parser.xmlParser();
 when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

 Document actual = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(document, actual);
 }
}