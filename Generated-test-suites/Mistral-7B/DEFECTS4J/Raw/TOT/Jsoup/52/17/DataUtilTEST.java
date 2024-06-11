package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

 private Document doc;
 private ByteArrayOutputStream outputStream;
 private Random random;

 @Before
 public void setUp() {
 outputStream = new ByteArrayOutputStream();
 random = new Random();
 }

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 doc = DataUtil.load(file, charsetName, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = new File("test.html");
 String charsetName = "invalidCharset";
 doc = DataUtil.load(file, charsetName, null);
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(
 "<html><head><meta charset=\"UTF-8\"></meta></head><body></body>".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "/test";
 doc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(
 "<html><head><meta charset=\"invalidCharset\"></meta></head><body></body>".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "/test";
 doc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(
 "<html><head><meta charset=\"UTF-8\"></meta></head><body></body>".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "/test";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 OutputStream outputStream = this.outputStream;
 DataUtil.crossStreams(inputStream, outputStream);
 assertNotNull(outputStream.size());
 }

 @Test
 public void testParseByteDataWithValidCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(
 new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></meta></head><body></body>".getBytes(StandardCharsets.UTF_8)));
 String charsetName = "UTF-8";
 String baseUri = "/test";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(
 new ByteArrayInputStream("<html><head><meta charset=\"invalidCharset\"></meta></head><body></body>".getBytes(StandardCharsets.UTF_8)));
 String charsetName = "UTF-8"; // should not be used in this test
 String baseUri = "/test";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNull(doc);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(
 new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></meta><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"></meta></head><body></body>".getBytes(StandardCharsets.UTF_8)));String charsetName = null;
 String baseUri = "/test";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 assertEquals("GBK", doc.outputSettings().charset());
 }
}