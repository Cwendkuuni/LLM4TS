package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private Document document;
 private InputStream inputStream;
 private File file;

 @Before
 public void setup() throws IOException {
 file = new File("sample.html");
 inputStream = new FileInputStream(file);
 document = DataUtil.load(file, "UTF-8", "");
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(file, null, "");
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 Document doc = DataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 String charset = "ISO-8859-1";
 Document doc = DataUtil.load(inputStream, charset, "");
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 String charset = "xyz";
 try {
 Document doc = DataUtil.load(inputStream, charset, "");
 fail("Expected HelperException");
 } catch (HelperException e) {
 // Expected
 }
 }

 @Test
 public void testParseInputStream() throws IOException {
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testParseInputStreamWithCharsetAndParser() throws IOException {
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.xmlParser());
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testCrossStreams() throws IOException {
 File tempFile = File.createTempFile("datautil", null);
 File tempFileOutput = new File(tempFile.getAbsolutePath() + "_out");
 InputStream inputStream = new FileInputStream(file);
 OutputStream outputStream = new FileOutputStream(tempFileOutput);
 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 outputStream.close();

 // Check that the original file is still there and unchanged.
 assertEquals(new String(new File(file.getAbsolutePath()).getBytes()), new String(new File(file.getAbsolutePath()).length(), File.INPUT_STREAM));

 // Check that the output file contains the data from the input file.
 Document doc = DataUtil.load(tempFile, null, "");
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testParseInputStreamWithBOMAndUTF8() throws IOException {
 byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0x3C, (byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
 ByteBuffer bomByteBuffer = ByteBuffer.wrap(bom);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
 Document doc = null;
 if (bomCharset != null) {
 inputStream = new ByteArrayInputStream(bom);
 doc = DataUtil.parseInputStream(inputStream, bomCharset.charset, "", Parser.htmlParser());
 inputStream.close();
 } else {
 doc = DataUtil.load(file, null, "");
 }
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testParseInputStreamWithBOMAndUTF16() throws IOException {
 byte[] bom = {(byte) 0xFF, (byte) 0xFE, (byte) 0x3C, (byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
 ByteBuffer bomByteBuffer = ByteBuffer.wrap(bom);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
 Document doc = null;
 if (bomCharset != null) {
 inputStream = new ByteArrayInputStream(bom);
 doc = DataUtil.parseInputStream(inputStream, bomCharset.charset, "", Parser.htmlParser());
 inputStream.close();
 } else {
 doc = DataUtil.load(file, null, "");
 }
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testParseInputStreamWithBOMAndUTF32() throws IOException {
 byte[] bom = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, (byte) 0x3C, (byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
 ByteBuffer bomByteBuffer = ByteBuffer.wrap(bom);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
 Document doc = null;
 if (bomCharset != null) {
 inputStream = new ByteArrayInputStream(bom);
 doc = DataUtil.parseInputStream(inputStream, bomCharset.charset, "", Parser.htmlParser());
 inputStream.close();
 } else {
 doc = DataUtil.load(file, null, "");
 }
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testParseInputStreamWithNoBOM() throws IOException {
 Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(document.body().text(), doc.body().text());
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 String charset = "xyz";
 Document doc = null;
 try {
 doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
 fail("Expected HelperException");
 } catch (HelperException e) {
 // Expected
 }
 }
}