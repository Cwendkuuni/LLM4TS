import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

 // Test method for load(File, String, String)
 @Test
 public void testLoadFile() throws Exception {
 Document document = DataUtil.load(new File("test.html"), "UTF-8", null);
 Assert.assertNotNull(document);
 }

 // Test method for load(InputStream, String, String)
 @Test
 public void testLoadInputStream() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.load(inputStream, "UTF-8", null);
 Assert.assertNotNull(document);
 }

 // Test method for load(InputStream, String, String, Parser)
 @Test
 public void testLoadInputStreamWithParser() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
 Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
 Assert.assertNotNull(document);
 }

 // Test method for parseByteData(ByteBuffer, String, String, Parser)
 @Test
 public void testParseByteData() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 String charsetName = "UTF-8";
 Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
 Assert.assertNotNull(document);
 }

 // Test method for detectCharsetFromBom(ByteBuffer, String)
 @Test
 public void testDetectCharsetFromBom() throws Exception {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put((byte) 0xFF).put((byte) 0xFE).put((byte) 0x00).put((byte) 0x00); // LE UTF-16
 byteData.flip();
 String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Assert.assertEquals("UTF-16", charsetName);

 byteData.clear();
 byteData.put((byte) 0xFE).put((byte) 0xFF).put((byte) 0x00).put((byte) 0x00); // BE UTF-16
 byteData.flip();
 charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Assert.assertEquals("UTF-16", charsetName);

 byteData.clear();
 byteData.put((byte) 0xEF).put((byte) 0xBB).put((byte) 0xBF); // UTF-8 BOM
 byteData.flip();
 charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Assert.assertEquals("UTF-8", charsetName);

 byteData.clear();
 byteData.put((byte) 0x00).put((byte) 0x00).put((byte) 0xFE).put((byte) 0xFF); // UTF-32 BE
 byteData.flip();
 charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Assert.assertEquals("UTF-32", charsetName);

 byteData.clear();
 byteData.put((byte) 0xFF).put((byte) 0xFE).put((byte) 0x00).put((byte) 0x00); // UTF-32 LE
 byteData.flip();
 charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Assert.assertEquals("UTF-32", charsetName);

 byteData.clear(); // No BOM
 charsetName = DataUtil.detectCharsetFromBom(byteData, null);
 Assert.assertEquals(null, charsetName);
 }

 // Test method for getCharsetFromContentType(String)
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=ISO-8859-1";
 String charsetName = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("ISO-8859-1", charsetName);

 contentType = "application/xml; charset=UTF-16";
 charsetName = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-16", charsetName);

 contentType = "text/plain; charset=UTF-8";
 charsetName = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charsetName);

 contentType = "text/html"; // null if not found
 charsetName = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(null, charsetName);
 }

 // Test method for validateCharset(String)
 @Test
 public void testValidateCharset() {
 String cs = "UTF-8";
 String validCharset = DataUtil.validateCharset(cs);
 Assert.assertEquals("UTF-8", validCharset);

 cs = "ISO-8859-1";
 validCharset = DataUtil.validateCharset(cs);
 Assert.assertEquals("ISO-8859-1", validCharset);

 cs = "GBK";
 validCharset = DataUtil.validateCharset(cs);
 Assert.assertEquals("GBK", validCharset);

 cs = "euc-JP";
 validCharset = DataUtil.validateCharset(cs);
 Assert.assertEquals("EUC-JP", validCharset);

 cs = "ISO-BAD";
 validCharset = DataUtil.validateCharset(cs);
 Assert.assertEquals(null, validCharset);
 }

 // Test method for mimeBoundary()
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertFalse(boundary.isEmpty());
 }
}