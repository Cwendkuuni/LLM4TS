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
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test.html content".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test.html content".getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream in = new ByteArrayInputStream("test input".getBytes());
 DataUtil.crossStreams(in, outputStream);
 String result = outputStream.toString();
 Assert.assertEquals("test input", result);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 Document doc = null;
 try {
 doc = DataUtil.load(new File("test.html"), "invalid_charset", null);
 } catch (IOException e) {
 // expected
 }
 Assert.assertNull(doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFileWithNullBaseUri() {
 try {
 DataUtil.load(new File("test.html"), null, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullByteData() {
 try {
 DataUtil.parseByteData(null, "UTF-8", null, Parser.htmlParser());
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullParser() {
 try {
 DataUtil.parseByteData(ByteBuffer.wrap("test".getBytes()), "UTF-8", null, null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidCharset() {
 String contentType = "text/html; charset=invalid_charset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(charset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 Assert.assertTrue(boundary.length() == 32);
 }
}