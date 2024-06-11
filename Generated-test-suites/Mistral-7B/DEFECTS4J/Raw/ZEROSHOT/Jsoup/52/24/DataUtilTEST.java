package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

 private static final String TEST_FILE = "testFile.html";
 private static final String TEST_CHARSET = "UTF-8";

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 File testFile = new File(TEST_FILE);
 String testData = "<html><head><meta charset=\"" + TEST_CHARSET + "\"/></head><body><h1>Test</h1></body></html>";
 testFile.createNewFile();
 try (RandomAccessFile raf = new RandomAccessFile(testFile, "rw");
 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
 bos.write(testData.getBytes());
 Charset charset = Charset.forName(TEST_CHARSET);
 byte[] bytes = bos.toByteArray();
 raf.write(bytes, 0, bytes.length);
 }
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 File testFile = new File(TEST_FILE);
 testFile.delete();
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE), TEST_CHARSET, null);
 assertEquals("Test", doc.titleText());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 String testData = "<html><head><meta charset=\"" + TEST_CHARSET + "\"/></head><body><h1>Test</h1></body></html>";
 bos.write(testData.getBytes());
 byte[] bytes = bos.toByteArray();
 InputStream is = new ByteArrayInputStream(bytes);
 Document doc = DataUtil.load(is, TEST_CHARSET, null);
 assertEquals("Test", doc.titleText());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 String testData = "<html><head><meta charset=\"" + TEST_CHARSET + "\"/></head><body><h1>Test</h1></body></html>";
 bos.write(testData.getBytes());
 byte[] bytes = bos.toByteArray();
 InputStream is = new ByteArrayInputStream(bytes);
 Parser parser = DataUtil.Parser.htmlParser();
 Document doc = DataUtil.load(is, TEST_CHARSET, null, parser);
 assertEquals("Test", doc.titleText());
 }

 @Test
 public void testCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testCharsetFromContentTypeInvalid() {
 String contentType = "text/html; charset=Invalid";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testCharsetFromContentTypeEmpty() {
 String contentType = "text/html";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testDetectCharsetFromBomUtf8() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 bos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
 byte[] bytes = bos.toByteArray();
 ByteArrayInputStream is = new ByteArrayInputStream(bytes);
 ByteBuffer byteData = DataUtil.readToByteBuffer(is);
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testDetectCharsetFromBomUtf16Le() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 bos.write(new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x00});
 byte[] bytes = bos.toByteArray();
 ByteArrayInputStream is = new ByteArrayInputStream(bytes);
 ByteBuffer byteData = DataUtil.readToByteBuffer(is);
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("UTF-16", charset);
 }

 @Test
 public void testDetectCharsetFromBomUtf16Be() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 bos.write(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE});
 byte[] bytes = bos.toByteArray();
 ByteArrayInputStream is = new ByteArrayInputStream(bytes);
 ByteBuffer byteData = DataUtil.readToByteBuffer(is);
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("UTF-16", charset);
 }

 @Test
 public void testDetectCharsetFromBomUtf32Le() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 bos.write(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00});
 byte[] bytes = bos.toByteArray();
 ByteArrayInputStream is = new ByteArrayInputStream(bytes);
 ByteBuffer byteData = DataUtil.readToByteBuffer(is);
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("UTF-32", charset);
 }

 @Test
 public void testDetectCharsetFromBomUtf32Be() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 bos.write(new byte[]{0xFF, 0xFF, 0x00, 0x00, (byte) 0x00, (byte) 0x00, 0x00, 0x00});
 byte[] bytes = bos.toByteArray();
 ByteArrayInputStream is = new ByteArrayInputStream(bytes);
 ByteBuffer byteData = DataUtil.readToByteBuffer(is);
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("UTF-32", charset);
 }

 @Test
 public void testDetectCharsetFromBomInvalid() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 bos.write(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
 byte[] bytes = bos.toByteArray();
 ByteArrayInputStream is = new ByteArrayInputStream(bytes);
 ByteBuffer byteData = DataUtil.readToByteBuffer(is);
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertNull(charset);
 }
}