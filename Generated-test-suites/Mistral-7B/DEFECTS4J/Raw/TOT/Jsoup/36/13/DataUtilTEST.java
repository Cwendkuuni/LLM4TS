package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

 private InputStream inputStream;
 private Document document;
 private Parser parser;

 @Before
 public void setup() {
 inputStream = mock(InputStream.class);
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 // Given
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.load(file, charsetName, baseUri);
 // Then
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 // Given
 File file = new File("test.html");
 String charsetName = null;
 String baseUri = "http://example.com";
 // When & Then
 assertEquals("Should have thrown an IOException", IOException.class, Assert.thrownBy(() -> DataUtil.load(file, charsetName, baseUri)));
 }

 @Test
 public void testLoadInputStream() throws IOException {
 // Given
 String html = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.load(inputStream, charsetName, baseUri);
 // Then
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 // Given
 String html = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
 String charsetName = null;
 String baseUri = "http://example.com";
 // When & Then
 assertEquals("Should have thrown an IOException", IOException.class, Assert.thrownBy(() -> DataUtil.load(inputStream, charsetName, baseUri)));
 }

 @Test
 public void testParseByteData() throws IOException {
 // Given
 ByteBuffer byteData = ByteBuffer.allocate(10);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 // Then
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithNullCharset() throws IOException {
 // Given
 ByteBuffer byteData = ByteBuffer.allocate(10);
 String charsetName = null;
 String baseUri = "http://example.com";
 // When & Then
 assertEquals("Should have thrown an IOException", IOException.class, Assert.thrownBy(() -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser)));
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 // Given
 ByteBuffer byteData = ByteBuffer.allocate(10);
 byteData.put("meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"".getBytes("UTF-8"));
 byteData.flip();
 String charsetName = null;
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 // Then
 assertEquals("Should have detected gb2312 charset", "gb2312", DataUtil.getCharsetFromContentType(new String(byteData.array())));
 assertNotNull(doc);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 // Given
 InputStream inputStream = new ByteArrayInputStream("test".getBytes());
 // When
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 // Then
 assertEquals("Should have read the entire input stream", "test".getBytes().length, byteData.limit());
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 // Given
 InputStream inputStream = new ByteArrayInputStream("test".getBytes());
 int maxSize = 1;
 // When
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
 // Then
 assertEquals("Should have read only one byte", 1, byteData.limit());
 }
}