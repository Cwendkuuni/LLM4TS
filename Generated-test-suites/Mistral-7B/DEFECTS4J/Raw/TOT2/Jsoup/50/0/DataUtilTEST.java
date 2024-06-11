import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class DataUtilTest {

 private static final int TEST_FILE_SIZE = 1024;
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://example.com";
 private static final Parser TEST_PARSER = Parser.htmlParser();
 private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String TEST_CONTENT_TYPE_NO_CHARSET = "text/html";
 private static final String TEST_CONTENT_TYPE_INVALID_CHARSET = "text/html; charset=XYZ-ABC";
 private static final String TEST_INVALID_CHARSET = "XYZ-ABC";
 private static final String TEST_EMPTY_CHARSET = "";
 private static final String TEST_MIME_BOUNDARY = DataUtil.mimeBoundary();

 @BeforeClass
 public static void setUp() {
 // Initialize static variables
 DataUtil.defaultCharset = TEST_CHARSET;
 DataUtil.bufferSize = TEST_FILE_SIZE;
 DataUtil.charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");
 }

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("test.html");
 Document doc = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[TEST_FILE_SIZE]);
 Document doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[TEST_FILE_SIZE]);
 Document doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, TEST_PARSER);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put(new byte[TEST_FILE_SIZE]);
 byteData.flip();
 Document doc = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, TEST_PARSER);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithMetaHttpEquivTag() throws IOException {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put(new byte[TEST_FILE_SIZE]);
 byteData.flip();
 String testDocData = "html<head><meta http-equiv='Content-Type' content='text/html; charset=gb2312'></head>body<p>Test</p>";
 String testMetaCharset = "gb2312";
 Document doc = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, TEST_PARSER);
 assertEquals(testMetaCharset, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithCharsetInContentType() throws IOException {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put(new byte[TEST_FILE_SIZE]);
 byteData.flip();
 Document doc = DataUtil.parseByteData(byteData, TEST_CONTENT_TYPE, TEST_BASE_URI, TEST_PARSER);
 assertEquals(TEST_CHARSET, doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithInvalidCharsetInContentType() throws IOException {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put(new byte[TEST_FILE_SIZE]);
 byteData.flip();
 Document doc = assertThrows(IOException.class, () -> DataUtil.parseByteData(byteData, TEST_CONTENT_TYPE_INVALID_CHARSET, TEST_BASE_URI, TEST_PARSER));
 assertNull(doc);
 }

 @Test
 public void testParseByteDataWithNoCharsetInContentType() throws IOException {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put(new byte[TEST_FILE_SIZE]);
 byteData.flip();
 Document doc = DataUtil.parseByteData(byteData, TEST_CONTENT_TYPE_NO_CHARSET, TEST_BASE_URI, TEST_PARSER);
 assertEquals(TEST_CHARSET, doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithEmptyCharsetInContentType() throws IOException {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 byteData.put(new byte[TEST_FILE_SIZE]);
 byteData.flip();
 Document doc = assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, TEST_EMPTY_CHARSET, TEST_BASE_URI, TEST_PARSER));
 assertNull(doc);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[TEST_FILE_SIZE]);
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 assertEquals(TEST_FILE_SIZE, byteData.limit());
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[TEST_FILE_SIZE * 2]);
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, TEST_FILE_SIZE);
 assertEquals(TEST_FILE_SIZE, byteData.limit());
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File testFile = new File("test.html");
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile);
 assertEquals(TEST_FILE_SIZE, byteData.limit());
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), DataUtil.boundaryLength);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String charset = DataUtil.getCharsetFromContentType(TEST_CONTENT_TYPE);
 assertEquals(TEST_CHARSET, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidCharset() {
 String charset = DataUtil.getCharsetFromContentType(TEST_CONTENT_TYPE_INVALID_CHARSET);
 assertNull(charset);
 }
}