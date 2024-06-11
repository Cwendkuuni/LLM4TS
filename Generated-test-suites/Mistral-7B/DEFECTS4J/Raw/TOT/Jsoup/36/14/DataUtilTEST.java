package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setUp() {
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html"); // replace with valid file path
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDoc = parser.parse(new File("expected_test.html")); // replace with expected document
 Document actualDoc = DataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testLoadInputStreamEmpty() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 String charsetName = null;
 String baseUri = "http://test.com";
 Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNull(actualDoc);
 }

 @Test
 public void testLoadInputStreamInvalidCharset() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 byteStream.write("<?xml version='1.0' encoding='Shift_JIS'?>\n<html><body>Hello</body></html>".getBytes("Shift_JIS"));
 InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 String charsetName = "Shift_JIS";
 String baseUri = "http://test.com";
 Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(actualDoc);
 // verify that the charset was actually changed to default
 assertEquals(DataUtil.defaultCharset, actualDoc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamValidCharset() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 byteStream.write("<?xml version='1.0' encoding='UTF-8'?>\n<html><body>Hello</body></html>".getBytes());
 InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);
 Document expectedDoc = parser.parse(new ByteArrayInputStream(byteStream.toByteArray()));
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testLoadInputStreamXMLParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<html><body>Hello</body></html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Parser xmlParser = DataUtil.Parser.xmlParser();
 Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, xmlParser);
 Document expectedDoc = parser.parse(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<html><body>Hello</body></html>".getBytes()));
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testParseByteDataEmpty() {
 ByteBuffer byteData = ByteBuffer.allocate(0);
 String charsetName = null;
 String baseUri = "http://test.com";
 Parser parser = DataUtil.Parser.htmlParser();
 Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNull(actualDoc);
 }

 @Test
 public void testParseByteDataDefaultCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("<?xml version='1.0' encoding='UTF-8'?>\n<html><body>Hello</body></html>".getBytes());
 String charsetName = null;
 String baseUri = "http://test.com";
 Parser parser = DataUtil.Parser.htmlParser();
 Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 Document expectedDoc = parser.parseInput(new String(byteData.array()), baseUri);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test
 public void testParseByteDataInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("<?xml version='1.0' encoding='Shift_JIS'?>\n<html><body>Hello</body></html>".getBytes());
 String charsetName = "Shift_JIS";
 String baseUri = "http://test.com";
 Parser parser = DataUtil.Parser.htmlParser();
 Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(actualDoc);
 // verify that the charset was actually changed to default
 assertEquals(DataUtil.defaultCharset, actualDoc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataValidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("<?xml version='1.0' encoding='UTF-8'?>\n<html><body>Hello</body></html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Parser parser = DataUtil.Parser.htmlParser();
 Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 Document expectedDoc = parser.parseInput(new String(byteData.array()), baseUri);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }
}