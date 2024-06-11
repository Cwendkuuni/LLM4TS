import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DataUtilTest {

 private Document doc;
 private String charsetName = "UTF-8";
 private String baseUri = "http://example.com";

 @Before
 public void setUp() throws Exception {
 }

 // Test method for load(File, String, String)
 @Test
 public void testLoadFile() throws IOException {
 File inputFile = new File("test.html");
 doc = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 }

 // Test method for load(InputStream, String, String)
 @Test
 public void testLoadInputStream() throws IOException {
 byte[] inputData = "<html><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
 doc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(doc);
 }

 // Test method for load(InputStream, String, String, Parser)
 @Test
 public void testLoadInputStreamParser() throws IOException {
 byte[] inputData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
 doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test method for parseByteData(ByteBuffer, String, String, Parser)
 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'B', 'O', 'M', ' ', 'H', 'T', 'M', 'L'}); // BOM
 String charsetNameFromMeta = "gb2312";
 String baseUri = "http://example.com";
 doc = DataUtil.parseByteData(byteData, charsetNameFromMeta, baseUri, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test method for readToByteBuffer(InputStream)
 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 byte[] expectedData = "test".getBytes(StandardCharsets.UTF_8);
 byte[] actualData = new byte[byteData.limit()];
 byteData.get(actualData);
 assertArrayEquals(expectedData, actualData);
 }

 // Test method for getCharsetFromContentType(String)
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=gb2312";
 String expectedCharset = "gb2312";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 // Edge cases and exception handling tests

 // Test method for load(File, null, null)
 @Test(expected = NullPointerException.class)
 public void testLoadFileNullCharset() throws IOException {
 File inputFile = new File("test.html");
 doc = DataUtil.load(inputFile, null, baseUri);
 fail("Expected NullPointerException");
 }

 // Test method for load(InputStream, null, null)
 @Test(expected = NullPointerException.class)
 public void testLoadInputStreamNull() throws IOException {
 doc = DataUtil.load(null, charsetName, baseUri);
 fail("Expected NullPointerException");
 }

 // Test method for load(InputStream, null, null, Parser)
 @Test(expected = NullPointerException.class)
 public void testLoadInputStreamParserNull() throws IOException {
 doc = DataUtil.load(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)), null, baseUri, null);
 fail("Expected NullPointerException");
 }

 // Test method for load(InputStream, "", null)
 @Test(expected = IllegalArgumentException.class)
 public void testLoadInputStreamEmptyCharset() throws IOException {
 doc = DataUtil.load(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)), "", baseUri);
 fail("Expected IllegalArgumentException");
 }

 // Test method for parseByteData(ByteBuffer, null, null, Parser)
 @Test(expected = NullPointerException.class)
 public void testParseByteDataNullCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String baseUri = "http://example.com";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
 fail("Expected NullPointerException");
 }

 // Test method for parseByteData(ByteBuffer, "", null, Parser)
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataEmptyCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String baseUri = "http://example.com";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.parseByteData(byteData, "", baseUri, parser);
 fail("Expected IllegalArgumentException");
 }

 // Test method for parseByteData(ByteBuffer, String, null, Parser)
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataNullBaseUri() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String charsetName = "gb2312";
 Parser parser = DataUtil.Parser.htmlParser();
 doc = DataUtil.parseByteData(byteData, charsetName, null, parser);
 fail("Expected IllegalArgumentException");
 }

 // Test method for parseByteData(ByteBuffer, String, Parser, null)
 @Test(expected = NullPointerException.class)
 public void testParseByteDataNullParser() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String charsetName = "gb2312";
 String baseUri = "http://example.com";
 doc = DataUtil.parseByteData(byteData, charsetName, null, null);
 fail("Expected NullPointerException");
 }
}