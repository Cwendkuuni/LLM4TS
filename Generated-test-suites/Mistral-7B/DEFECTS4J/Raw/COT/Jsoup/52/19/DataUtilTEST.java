package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(new File("testFile.html"), null, null);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 Document document = DataUtil.load(new File("testFile.html"), "UTF-16", null);
 // Add assertions based on the content of testFile.html
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes("UTF-8"));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, null);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes("UTF-16"));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, "UTF-16", null);
 // Add assertions based on the content of the test data
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes("UTF-8"));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertNotNull(document);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes("UTF-8"));
 DataUtil.crossStreams(inputStream, outputStream);
 byte[] output = outputStream.toByteArray();
 assertEquals("Test content", new String(output, "UTF-8"));
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
 assertNull(document);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("Test content".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
 assertNotNull(document);
 // Add assertions based on the content of the test data
 }

 @Test(expected = IllegalStateException.class)
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("Test content".getBytes("UTF-123"));
 String charsetName = "UTF-123";
 Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), 32);
 }
}