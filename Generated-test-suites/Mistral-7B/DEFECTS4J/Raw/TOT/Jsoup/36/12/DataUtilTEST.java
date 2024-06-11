package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.lenient;

public class DataUtilTest {

 private InputStream inputStream;
 private Parser parser;

 @Before
 public void setup() {
 parser = Mockito.mock(Parser.class);
 inputStream = Mockito.spy(new ByteArrayInputStream(new byte[0]));
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = new File("test.html");
 Document doc = null;
 try {
 doc = DataUtil.load(file, "InvalidCharset", null);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", null, parser);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = null;
 try {
 doc = DataUtil.load(inputStream, "InvalidCharset", null, parser);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes("UTF-8"));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes("gb2312"));
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 lenient(inputStream).doReturn(byteArrayInputStream);
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test Content".getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = null;
 try {
 doc = DataUtil.parseByteData(byteData, "InvalidCharset", null, parser);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }
}