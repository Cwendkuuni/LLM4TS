package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadWithFile() throws IOException {
 File file = new File("test.html");
 String charset = "UTF-8";
 String baseUri = "http://test.com";
 Document result = DataUtil.load(file, charset, baseUri);
 assertNotNull(result);
 }

 @Test
 public void testLoadWithInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String charset = "UTF-8";
 String baseUri = "http://test.com";
 Document result = DataUtil.load(inputStream, charset, baseUri);
 assertNotNull(result);
 }

 @Test
 public void testLoadWithInputStreamAndParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String charset = "UTF-8";
 String baseUri = "http://test.com";
 Parser parser = mock(Parser.class);
 Document result = DataUtil.load(inputStream, charset, baseUri, parser);
 assertNotNull(result);
 }

 @Test
 public void testParseByteData() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String charset = "UTF-8";
 String baseUri = "http://test.com";
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document result = DataUtil.parseByteData(byteData, charset, baseUri, parser);
 assertNotNull(result);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String charset = null;
 String baseUri = "http://test.com";
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document result = DataUtil.parseByteData(byteData, charset, baseUri, parser);
 assertNotNull(result);

 Element meta = mock(Element.class);
 when(meta.hasAttr("http-equiv")).thenReturn(true);
 when(meta.attr("content")).thenReturn("text/html; charset=GBK");
 when(parser.parseInput(anyString(), anyString())).thenReturn(new Document());
 result.select("meta[http-equiv=content-type], meta[charset]").first(meta);
 assertNotNull(meta);
 assertEquals("GBK", result.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String charset = "UTF-16";
 String baseUri = "http://test.com";
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document result = DataUtil.parseByteData(byteData, charset, baseUri, parser);
 assertNull(result);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer result = DataUtil.readToByteBuffer(inputStream, 0);
 byte[] expected = "test.html".getBytes();
 byte[] resultBytes = result.array();
 assertArrayEquals(expected, resultBytes);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer result = DataUtil.readToByteBuffer(inputStream, 10);
 byte[] expected = "test.html".substring(0, 10).getBytes();
 byte[] resultBytes = result.array();
 assertArrayEquals(expected, resultBytes);
 }

 @Test(expected = IOException.class)
 public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer result = DataUtil.readToByteBuffer(inputStream, -1);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String result = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", result);
 }

 @Test
 public void testGetCharsetFromContentTypeWithUnknownCharset() {
 String contentType = "text/html; charset=X-Unknown";
 String result = DataUtil.getCharsetFromContentType(contentType);
 assertNull(result);
 }

 @Test
 public void testGetCharsetFromContentTypeWithEmptyContentType() {
 String contentType = "text/html";
 String result = DataUtil.getCharsetFromContentType(contentType);
 assertNull(result);
 }
}