package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

 private InputStream inputStream;
 private Document document;

 @Before
 public void setUp() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Hello World</body></html>";
 inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 document = DataUtil.load(inputStream, "UTF-8", null);
 }

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("testFile.html");
 DataUtil.load(testFile, null, null);
 // Add an assertion here for a proper test case, for instance:
 // assertTrue(document.body().text().matches("Hello World"));
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Hello World".getBytes(StandardCharsets.UTF_8));
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(testInputStream, null, null);
 // Add an assertion here for a proper test case, for instance:
 // assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Hello World".getBytes("ISO-8859-1"));
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(testInputStream, "ISO-8859-1", null);
 // Add an assertion here for a proper test case, for instance:
 // assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>".getBytes(StandardCharsets.UTF_8));
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(testInputStream, null, null, Parser.xmlParser());
 // Add an assertion here for a proper test case, for instance:
 // assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithIOException() throws IOException {
 File testFile = new File("non_existent_file.html");
 document = DataUtil.load(testFile, null, null);
 fail("Expected IOException not thrown");
 }
}