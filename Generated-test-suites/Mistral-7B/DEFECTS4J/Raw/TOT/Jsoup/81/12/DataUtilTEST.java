package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final String TEST_FILE_NAME = "test.html";
 private static final String TEST_CONTENT = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>";
 private static final String TEST_BASE_URI = "http://example.com/";
 private static final String TEST_CHARSET = "UTF-8";
 private static final int TEST_BUFFER_SIZE = 1024 * 32;
 private DataUtil dataUtil;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 }

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 File testFile = new File(getClass().getResource(TEST_FILE_NAME).getFile());
 Document doc = dataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File testFile = new File(getClass().getResource(TEST_FILE_NAME).getFile());
 Document doc = dataUtil.load(testFile, null, TEST_BASE_URI);
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = dataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = dataUtil.load(testInputStream, null, TEST_BASE_URI);
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.ISO_8859_1));
 Document doc = null;
 try {
 doc = dataUtil.load(testInputStream, "ISO-8859-1", TEST_BASE_URI);
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testParseInputStreamWithValidCharset() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream(TEST_BUFFER_SIZE);
 DataUtil.crossStreams(new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8)), testOutputStream);
 testOutputStream.close();
 byte[] testBytes = testOutputStream.toByteArray();
 InputStream testInputStream = new ByteArrayInputStream(testBytes);
 Document doc = dataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testParseInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream(TEST_BUFFER_SIZE);
 DataUtil.crossStreams(new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8)), testOutputStream);
 testOutputStream.close();
 byte[] testBytes = testOutputStream.toByteArray();
 InputStream testInputStream = new ByteArrayInputStream(testBytes);
 Document doc = dataUtil.parseInputStream(testInputStream, null, TEST_BASE_URI, Parser.htmlParser());
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testParseInputStreamWithBOM() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream(TEST_BUFFER_SIZE + 4);
 testOutputStream.write(new byte[] {0xEF, 0xBB, 0xBF, 0x00}); // UTF-8 BOM
 DataUtil.crossStreams(new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8)), testOutputStream);
 testOutputStream.close();
 byte[] testBytes = testOutputStream.toByteArray();
 InputStream testInputStream = new ByteArrayInputStream(testBytes);
 Document doc = dataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertEquals(TEST_BASE_URI, doc.baseUri());
 assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Test content</p></body></html>", doc.html());
 }

 @Test
 public void testParseInputStreamWithBOMAndInvalidCharset() throws IOException {
 ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream(TEST_BUFFER_SIZE + 4);
 testOutputStream.write(new byte[] {0xEF, 0xBB, 0xBF, 0x00}); // UTF-8 BOM
 testOutputStream.write(new byte[] {0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x61, 0x79, 0x65, 0x00}); // 'Config' UTF-16 BOM
 DataUtil.crossStreams(new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8)), testOutputStream);
 testOutputStream.close();
 byte[] testBytes = testOutputStream.toByteArray();
 InputStream testInputStream = new ByteArrayInputStream(testBytes);
 Document doc = null;
 try {
 doc = dataUtil.parseInputStream(testInputStream, "UTF-16", TEST_BASE_URI, Parser.htmlParser());
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testDetectCharsetFromBom() {
 byte[] testBytes = TEST_CONTENT.getBytes(StandardCharsets.UTF_8);
 ByteBuffer testByteBuffer = ByteBuffer.wrap(testBytes);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(testByteBuffer);
 assertEquals("UTF-8", bomCharset.charset);
 assertFalse(bomCharset.offset);
 }

 @Test
 public void testDetectCharsetFromBomUTF16() {
 byte[] testBytes = new byte[] {0xFF, 0xFE, (byte) 0x00, 0x00, 0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x61, 0x79, 0x65, 0x00, 0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x61, 0x79, 0x65, 0x00};
 ByteBuffer testByteBuffer = ByteBuffer.wrap(testBytes);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(testByteBuffer);
 assertEquals("UTF-16", bomCharset.charset);
 assertTrue(bomCharset.offset);
 }

 @Test
 public void testDetectCharsetFromBomUTF32() {
 byte[] testBytes = new byte[] {0xFF, 0xFF, 0xEF, 0xBB, 0xBF, 0x00, 0x00, 0x00, 0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x61, 0x79, 0x65, 0x00, 0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x61, 0x79, 0x65};
 ByteBuffer testByteBuffer = ByteBuffer.wrap(testBytes);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(testByteBuffer);
 assertEquals("UTF-32", bomCharset.charset);
 assertTrue(bomCharset.offset);
 }

 @Test
 public void testDetectCharsetFromBomNone() {
 byte[] testBytes = new byte[] {0x43, 0x6F, 0x6D, 0x70, 0x6C, 0x61, 0x79, 0x65};
 ByteBuffer testByteBuffer = ByteBuffer.wrap(testBytes);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(testByteBuffer);
 assertNull(bomCharset);
 }
}