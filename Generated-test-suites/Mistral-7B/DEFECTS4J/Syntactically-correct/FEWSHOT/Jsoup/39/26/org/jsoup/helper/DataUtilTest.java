package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.parseByteData;
import static org.jsoup.parser.Parser.htmlParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

 @Test
 public void testLoadDocumentWithDefaultCharset() throws IOException {
 Document doc = load(new File("testfile.html"), null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadDocumentWithSpecifiedCharset() throws IOException {
 Charset utf8 = Charset.forName("UTF-8");
 Document doc = load(new File("testfile.html"), "UTF-8", null);
 assertNotNull(doc);
 assertEquals(utf8, doc.outputSettings().charset());
 }

 @Test
 public void testLoadDocumentWithInvalidCharset() throws IOException {
 Document doc = load(new File("testfile.html"), "ABC", null);
 assertNotNull(doc);
 assertNull(doc.outputSettings().charset());
 }

 @Test
 public void testLoadDocumentWithEmptyInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadDocumentWithNullInputStream() throws IOException {
 Document doc = load(null, null, null);
 assertNull(doc);
 }

 @Test
 public void testLoadDocumentWithNullFile() throws IOException {
 Document doc = load(null, null, null);
 assertNull(doc);
 }

 @Test
 public void testLoadDocumentWithByteData() throws IOException {
 String html = "<html><body>Test</body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(html.getBytes("UTF-8"));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadDocumentWithByteDataWithDefaultCharset() throws IOException {
 String html = "<html><body>Test</body></html>";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(html.getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document doc = parseByteData(byteData, null, null, htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 String charsetName = "UTF-8";
 ByteBuffer byteData = ByteBuffer.wrap("<html><body>Test</body></html>".getBytes(Charset.forName(charsetName)));
 Document doc = parseByteData(byteData, charsetName, null, htmlParser());
 assertNotNull(doc);
 assertEquals(charsetName, doc.outputSettings().charset().name());
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("<html><body>Test</body></html>".getBytes());
 Document doc = parseByteData(byteData, null, null, htmlParser());
 assertNotNull(doc);
 assertNull(doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 String charsetName = "ABC";
 ByteBuffer byteData = ByteBuffer.wrap("<html><body>Test</body></html>".getBytes());
 Document doc = parseByteData(byteData, charsetName, null, htmlParser());
 assertNotNull(doc);
 assertNull(doc.outputSettings().charset());
 }

 @Test
 public void testReadToByteBufferWithDefaultMaxSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body>Test</body></html>".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 assertNotNull(byteData);
 }

 @Test
 public void testReadToByteBufferWithSpecifiedMaxSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body>Test</body></html>".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 5);
 assertNotNull(byteData);
 assertEquals(5, byteData.limit());
 }

 @Test
 public void testReadToByteBufferWithUnlimitedMaxSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body>Test</body></html>".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
 assertNotNull(byteData);
 assertEquals(0, byteData.limit());
 }

 @Test
 public void testReadToByteBufferWithNullInputStream() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(null);
 assertNull(byteData);
 }
}