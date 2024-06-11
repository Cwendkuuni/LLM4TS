package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.TextDocument;
import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {

 private Parser parser;
 private Document document;

 @Before
 public void setUp() {
 parser = mock(Parser.class);
 document = mock(Document.class);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File in = new File("test.html");
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(in);
 Document doc = DataUtil.load(in, null, null);
 verify(parser).parseInput(anyString(), anyString());
 assertEquals(in.getAbsolutePath(), doc.baseUri());
 assertEquals(byteData.toString(), doc.body().html());
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File in = new File("test.html");
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(in);
 String charsetName = "UTF-16";
 Document doc = DataUtil.load(in, charsetName, null);
 verify(parser).parseInput(eq(byteData.toString()), anyString());
 assertEquals(charsetName, doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 File in = new File("test.html");
 String charsetName = "XYZ-123";
 Document doc = DataUtil.load(in, charsetName, null);
 verify(parser, never()).parseInput(anyString(), anyString());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream in = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(in);
 Document doc = DataUtil.load(in, null, null);
 verify(parser).parseInput(anyString(), anyString());
 assertEquals("test.html", doc.baseUri().toString());
 assertEquals(byteData.toString(), doc.body().html());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream in = new ByteArrayInputStream("test.html".getBytes());
 String charsetName = "UTF-16";
 Document doc = DataUtil.load(in, charsetName, null);
 verify(parser).parseInput(eq(byteDataToByteArray(byteDataToString(byteData).getBytes())), anyString());
 assertEquals(charsetName, doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream in = new ByteArrayInputStream("test.html".getBytes());
 String charsetName = "UTF-16";
 Document doc = DataUtil.load(in, charsetName, null, parser);
 verify(parser).parseInput(eq(byteDataToByteArray(byteDataToString(byteData).getBytes())), anyString());
 assertEquals(charsetName, doc.outputSettings().charset());
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream in = mock(InputStream.class);
 OutputStream out = mock(OutputStream.class);
 DataUtil.crossStreams(in, out);
 verify(in).read(anyByteArray(), anyInt(), anyInt());
 verify(out).write(anyByteArray(), anyInt(), anyInt());
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes());
 String charsetName = "UTF-16";
 Document doc = DataUtil.parseByteData(byteData, charsetName, null, parser);
 verify(parser).parseInput(eq(byteData.toString()), anyString());
 assertEquals(charsetName, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("test.html".getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 verify(parser).parseInput(eq(byteData.toString()), anyString());
 assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithBOM() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { UNICODE_BOM, 'T', 'e', 's', 't' });
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 verify(parser).parseInput(eq("tes" + String.valueOf((char) UNICODE_BOM)), anyString());
 assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithMetaCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<?meta charset=\"GBK\"?>test.html".getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 verify(parser).parseInput(eq("test.html"), anyString());
 assertEquals("GBK", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithContentTypeCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("text/html; charset=EUC-JPtest.html".getBytes());
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 verify(parser).parseInput(eq("test.html"), anyString());
 assertEquals("EUC-JP", doc.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html; charset=iso-8859-1";
 assertEquals("iso-8859-1", DataUtil.getCharsetFromContentType(contentType));
 contentType = "text/html";
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), DataUtil.boundaryLength);
 for (int i = 0; i < DataUtil.boundaryLength; i++) {
 assertEquals(DataUtil.mimeBoundaryChars[i % DataUtil.mimeBoundaryChars.length], boundary.charAt(i));
 }
 }

 @Test
 public void testReadToByteBufferFromInputStream() throws IOException {
 InputStream in = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(in);
 assertEquals(byteData.limit(), in.available());
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 DataUtil.crossStreams(in, out);
 assertEquals(new String(out.toByteArray()), byteData.toString());
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File in = new File("test.html");
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(in);
 assertEquals(in.length(), byteData.limit());
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 assertEquals(0, byteData.limit());
 }

 private static byte[] byteDataToString(ByteBuffer byteData) {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 byteData.flip();
 out.write(byteData.array(), 0, byteData.limit());
 byteData.clear();
 return out.toByteArray();
 }

 private static InputStream byteArrayToInputStream(byte[] bytes) {
 return new ByteArrayInputStream(bytes);
 }

 private static byte[] byteArrayToByteBuffer(byte[] bytes) {
 return ByteBuffer.wrap(bytes);
 }
}