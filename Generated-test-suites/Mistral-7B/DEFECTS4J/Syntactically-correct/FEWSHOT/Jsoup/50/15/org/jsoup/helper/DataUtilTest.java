package org.jsoup.helper;

 import static org.junit.Assert.*;
 import org.junit.Test;
 import org.jsoup.nodes.Document;
 import org.jsoup.nodes.Element;
 import org.jsoup.parser.Parser;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.nio.ByteBuffer;
 import java.nio.charset.Charset;
 import java.util.Locale;
 import java.util.Random;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;

 public class DataUtilTest {

 @Test
 public void testLoadFromFile() throws IOException {
 File testFile = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document doc = DataUtil.load(testFile, charsetName, baseUri);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadFromInputStream() throws IOException {
 InputStream inputStream = new FileInputStream("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document doc = DataUtil.load(inputStream, charsetName, baseUri);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadWithCustomParser() throws IOException {
 InputStream inputStream = new FileInputStream("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Parser parser = Parser.xmlParser();
 Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertTrue(doc != null);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://test.com", Parser.htmlParser());
 assertTrue(doc != null);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNoCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testGetCharsetFromContentTypeInvalidCharset() {
 String contentType = "text/html; charset=invalid-charset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertFalse(boundary.isEmpty());
 }
 }