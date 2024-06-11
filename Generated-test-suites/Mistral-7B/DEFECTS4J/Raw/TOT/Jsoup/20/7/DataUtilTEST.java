package org.jsoup.helper.test;

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
import java.nio.charset.Charset;

public class DataUtilTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setup() {
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document actual = DataUtil.load(file, "UTF-8", "/");
 Document expected = parser.parseInput("<html><body></body></html>", "/"); // replace with expected document
 assertEquals(expected, actual);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body></body></html>".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document actual = DataUtil.load(inputStream, null, "/");
 Document expected = parser.parseInput("<html><body></body></html>", "/"); // replace with expected document
 assertEquals(expected, actual);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("text/html; charset=UTF-8".getBytes()); // replace with valid content type
 outputStream.write("<html><body></body></html>".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document actual = DataUtil.load(inputStream, "UTF-8", "/");
 Document expected = parser.parseInput("<html><body></body></html>", "/"); // replace with expected document
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File file = new File("invalid_file.txt");
 Document actual = DataUtil.load(file, "UTF-8", "/");
 fail("Expected IOException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidContentType() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("application/pdf".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document actual = null;
 try {
 actual = DataUtil.load(inputStream, "UTF-8", "/");
 } catch (IOException e) {
 // expected
 }
 fail("Expected IOException to be thrown");
 }
}