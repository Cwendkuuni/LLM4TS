package org.jsoup.helper.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private static File testFile;
 private static InputStream testInputStream;
 private static FileInputStream testFileInputStream;
 private static ByteBuffer byteData;

 @BeforeClass
 public static void setup() throws Exception {
 testFile = new File("path/to/testfile.html");
 testInputStream = new FileInputStream(testFile);
 testFileInputStream = new FileInputStream(testFile);
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(testFile, "UTF-8", "/testBaseUri");
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 Document doc = null;
 try {
 doc = DataUtil.load(testFile, null, "/testBaseUri");
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteBuffer byteDataFromStream = DataUtil.readToByteBuffer(testInputStream);
 Document doc = DataUtil.parseByteData(byteDataFromStream, "UTF-8", "/testBaseUri", Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 Document doc = null;
 try {
 doc = DataUtil.load(testInputStream, null, "/testBaseUri", Parser.htmlParser());
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetSpecified() throws IOException {
 String invalidCharset = "GBK";
 ByteBuffer byteDataFromStream = DataUtil.readToByteBuffer(testInputStream);
 Document doc = null;
 try {
 doc = DataUtil.parseByteData(byteDataFromStream, invalidCharset, "/testBaseUri", Parser.htmlParser());
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithValidCharsetSpecified() throws IOException {
 String validCharset = "UTF-8";
 ByteBuffer byteDataFromStream = DataUtil.readToByteBuffer(testInputStream);
 Document doc = DataUtil.parseByteData(byteDataFromStream, validCharset, "/testBaseUri", Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=ISO-8859-1";
 String expectedCharset = "ISO-8859-1";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNoCharset() {
 String contentType = "text/html";
 String expectedCharset = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, charset);
 }

 @Test(expected = HelperException.class)
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteDataFromFile = DataUtil.readToByteBuffer(testFileInputStream);
 Document doc = DataUtil.parseByteData(byteDataFromFile, "GBK", "/testBaseUri", Parser.htmlParser());
 fail("Should have thrown HelperException.");
 }

 @Test
 public void testParseByteDataWithBOM() throws IOException {
 ByteBuffer byteDataFromFile = DataUtil.readToByteBuffer(testFileInputStream);
 String docData = new String(byteDataFromFile.array(), 0, byteDataFromFile.limit());
 String expectedDocData = new String(testFile.getBytes("UTF-8"));
 assertEquals(expectedDocData, docData.substring(1));
 }
}