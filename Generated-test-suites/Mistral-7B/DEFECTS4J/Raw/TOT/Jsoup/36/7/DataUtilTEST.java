import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private Document htmlDocument;
 private Document xmlDocument;
 private Parser parser;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, null, null);
 assertEquals("Expected default charset to be used when not specified", "UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File file = new File("test.html");
 Document doc = DataUtil.load(file, "UTF-16BE", null);
 assertEquals("Expected specified charset to be used", "UTF-16BE", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputSteamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] utf8Bytes = "JUnit test data".getBytes();
 outputStream.write(utf8Bytes);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null);
 assertEquals("Expected default charset to be used when not specified", "UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputSteamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] gb2312Bytes = "JUnit test data".getBytes("GB2312");
 outputStream.write(gb2312Bytes);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "GB2312", null);
 assertEquals("Expected specified charset to be used", "GB2312", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 Document doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals("Expected default charset to be used when not specified", "UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{65, 6D, 65, 78, 74}); // "ebcdic"
 Document doc = DataUtil.parseByteData(byteData, "EBCDIC", null, parser);
 assertEquals("Expected specified charset to be used", "EBCDIC", doc.outputSettings().charset());
 }

 @Test
 public void testDetermineCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected charset to be extracted from content type correctly", "UTF-8", charset);
 }

 @Test
 public void testDetermineCharsetFromContentTypeWithInvalidCharset() {
 String contentType = "text/html; charset=X-1234";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull("Expected null when charset is not supported", charset);
 }

 @Test
 public void testDetermineCharsetFromContentTypeWithNoCharsetSpecified() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected null when charset is not specified", null, charset);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] utf8Bytes = "JUnit test data".getBytes();
 outputStream.write(utf8Bytes);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 byte[] expectedBytes = Arrays.copyOfRange(utf8Bytes, 0, utf8Bytes.length);
 byte[] actualBytes = new byte[byteData.limit()];
 byteData.get(actualBytes);
 assertArrayEquals("Expected byte buffer to contain the same data as input stream", expectedBytes, actualBytes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadToByteBufferWithNegativeMaxSize() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] utf8Bytes = "JUnit test data".getBytes();
 outputStream.write(utf8Bytes);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 DataUtil.readToByteBuffer(inputStream, -1);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidHttpEquivTag() {
 String contentType = "text/html; charset=UTF-8";
 String metaContentType = "text/html; charset=GB2312";
 Document doc = parser.parseInput(contentType, null);
 Element meta = doc.createElement("meta");
 meta.attr("http-equiv", "content-type");
 meta.attr("content", metaContentType);
 doc.head().appendChild(meta);
 String charset = DataUtil.getCharsetFromContentType(doc.html().toString());
 assertNull("Expected null when charset is not found in the meta tag", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithValidHttpEquivTag() {
 String contentType = "text/html; charset=UTF-8";
 String metaContentType = "text/html; charset=Shift_JIS";
 Document doc = parser.parseInput(contentType, null);
 Element meta = doc.createElement("meta");
 meta.attr("http-equiv", "content-type");
 meta.attr("content", metaContentType);
 doc.head().appendChild(meta);
 String charset = DataUtil.getCharsetFromContentType(doc.html().toString());
 assertEquals("Expected charset to be extracted from meta tag correctly", "Shift_JIS", charset);
 }
}