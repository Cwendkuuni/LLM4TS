import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RunWith(JUnit4.class)
public class DataUtilTest {

 // Test case for load(File, String, String) method
 @Test
 public void testLoadFromFile() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", "");
 Assert.assertNotNull(doc);
 }

 // Test case for load(InputStream, String, String, Parser) method with htmlParser
 @Test
 public void testLoadFromInputStreamHtmlParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test case for load(InputStream, String, String, Parser) method with xmlParser
 @Test
 public void testLoadFromInputStreamXmlParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.xml");
 Document doc = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with htmlParser
 @Test
 public void testParseInputStreamHtmlParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(Charset.forName("UTF-8")));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with xmlParser
 @Test
 public void testParseInputStreamXmlParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>".getBytes(Charset.forName("UTF-8")));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 // Test case for crossStreams(InputStream, OutputStream) method
 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(Charset.forName("UTF-8")));
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 Assert.assertEquals("Hello World", outputStream.toString(Charset.forName("UTF-8")));
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with invalid charset
 @Test(expected = IOException.class)
 public void testParseInputStreamInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("invalidCharset".getBytes());
 Document doc = DataUtil.parseInputStream(inputStream, "invalidCharset", "", DataUtil.Parser.htmlParser());
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with empty input
 @Test
 public void testParseInputStreamEmpty() {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = null;
 try {
 doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
 } catch (IOException e) {
 // Expected exception
 }
 Assert.assertNull(doc);
 }

 // Test case for getCharsetFromContentType("text/html; charset=EUC-JP") method
 @Test
 public void testGetCharsetFromContentTypeEUCJP() {
 String contentType = "text/html; charset=EUC-JP";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("EUC-JP", charset);
 }

 // Test case for getCharsetFromContentType("text/html") method
 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 // Test case for mimeBoundary() method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertNotNull(boundary);
 }

 // Test case for detectCharsetFromBom(ByteBuffer) method with UTF-8 BOM
 @Test
 public void testDetectCharsetFromBomUTF8() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertEquals("UTF-8", bomCharset.charset);
 Assert.assertEquals(3, bomCharset.offset);
 }

 // Test case for detectCharsetFromBom(ByteBuffer) method with UTF-16 BOM
 @Test
 public void testDetectCharsetFromBomUTF16() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertEquals("UTF-16", bomCharset.charset);
 Assert.assertEquals(0, bomCharset.offset);
 }

 // Test case for detectCharsetFromBom(ByteBuffer) method with UTF-32 BOM
 @Test
 public void testDetectCharsetFromBomUTF32() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertEquals("UTF-32", bomCharset.charset);
 Assert.assertEquals(0, bomCharset.offset);
 }

 // Test case for detectCharsetFromBom(ByteBuffer) method with invalid BOM
 @Test
 public void testDetectCharsetFromBomInvalid() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0x01, 0x02, 0x03});
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertNull(bomCharset);
 }
}