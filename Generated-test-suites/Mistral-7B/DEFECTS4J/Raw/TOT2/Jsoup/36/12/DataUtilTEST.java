import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 @Test
 public void testLoadFileDocument() throws IOException {
 File testFile = new File("test.html");
 Document doc = DataUtil.load(testFile, "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamDocument() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body>Test</body></html>".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamDocumentWithParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body>Test</body></html>".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body>Test</body></html>".getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=ISO-8859-1";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("ISO-8859-1", charset);
 }

 @Test
 public void testLoadFileDocumentWithInvalidCharsetName() throws IOException {
 File testFile = new File("test.html");
 try {
 DataUtil.load(testFile, null, null);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testLoadInputStreamDocumentWithNegativeMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><body>Test</body></html>".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 try {
 DataUtil.readToByteBuffer(inputStream, -1);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseByteDataWithNullCharsetName() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[1024]);
 try {
 DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }
}