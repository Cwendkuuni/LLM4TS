package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DataUtilTest {

 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 parser = Mockito.mock(Parser.class);
 document = Mockito.mock(Document.class);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("test.html");
 ByteBuffer byteData = DataUtil.readToByteBuffer(file);
 Document doc = DataUtil.load(file, null, "http://www.example.com");
 assertEquals(parser, DataUtil.parseByteData(byteData, null, "http://www.example.com", parser));
 assertEquals(document, doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File file = new File("test.html");
 ByteBuffer byteData = DataUtil.readToByteBuffer(file);
 String charset = "UTF-16";
 Document doc = DataUtil.load(file, charset, "http://www.example.com");
 assertEquals(parser, DataUtil.parseByteData(byteData, charset, "http://www.example.com", parser));
 assertEquals(document, doc);
 }

 @Test
 public void testLoadFileWithMetaTagCharset() throws IOException {
 File file = new File("test.html");
 String metaTagCharset = "ISO-8859-1";
 byte[] metaTagBytes = metaTagCharset.getBytes(StandardCharsets.UTF_8);
 // Add meta tag to the file
 byte[] fileContent = new byte[file.length() + metaTagBytes.length];
 System.arraycopy(DataUtil.readToByteArray(file), 0, fileContent, 0, file.length());
 System.arraycopy(metaTagBytes, 0, fileContent, file.length(), metaTagBytes.length);
 ByteBuffer byteData = ByteBuffer.wrap(fileContent);
 Document doc = DataUtil.load(new FileInputStream(file), null, "http://www.example.com");
 String decodedCharset = DataUtil.getCharsetFromContentType(doc.head().metaElementSibling(0).attr("content"));
 assertEquals(metaTagCharset, decodedCharset);
 assertEquals(parser, DataUtil.parseByteData(byteData, metaTagCharset, "http://www.example.com", parser));
 assertEquals(document, doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals(parser, DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, parser));
 assertEquals(document, doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
 String charset = "UTF-16";
 Document doc = DataUtil.load(inputStream, charset);
 assertEquals(parser, DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charset, null, parser));
 assertEquals(document, doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals(Parser.xmlParser(), DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.xmlParser()).parser());
 assertEquals(document, doc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() {
 assertThrows(IllegalArgumentException.class, () -> DataUtil.load(new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8)), "InvalidCharset"));
 }
}