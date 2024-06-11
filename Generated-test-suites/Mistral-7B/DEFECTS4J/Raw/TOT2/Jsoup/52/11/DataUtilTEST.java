import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FileInputSource;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

 private static final String TEST_FILE = "test.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com/";

 @org.junit.Test
 public void testLoadFile() throws IOException {
 File file = new File(TEST_FILE);
 Document doc = DataUtil.load(file, TEST_CHARSET, TEST_BASE_URI);
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testLoadInputStream1() throws IOException {
 File file = new File(TEST_FILE);
 InputStream inputStream = new FileInputSource(file).inputStream();
 Document doc = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testLoadInputStream2() throws IOException {
 byte[] testData = "Test Data".getBytes(Charset.forName(TEST_CHARSET));
 InputStream inputStream = new ByteArrayInputStream(testData);
 Document doc = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 byte[] testData = "Test Data".getBytes(Charset.forName("nonExistentCharset"));
 InputStream inputStream = new ByteArrayInputStream(testData);
 Document doc = null;
 try {
 doc = DataUtil.load(inputStream, "nonExistentCharset", TEST_BASE_URI, Parser.htmlParser());
 } catch (IOException e) {
 // Expected
 }
 Assert.assertNull(doc);
 }

 @org.junit.Test
 public void testParseByteData1() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE));
 Document doc = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testParseByteDataWithBOM() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE + "_UTF-32.html"));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()); // null charset and baseUri are allowed
 Assert.assertNotNull(doc);
 }

 @org.junit.Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE));
 Document doc = null;
 try {
 doc = DataUtil.parseByteData(byteData, "nonExistentCharset", TEST_BASE_URI, Parser.htmlParser());
 } catch (IOException e) {
 // Expected
 }
 Assert.assertNull(doc);
 }

 @org.junit.Test
 public void testCrossStreams() throws IOException {
 File file = new File("test_input.html");
 InputStream inputStream = new FileInputStream(file);
 OutputStream outputStream = new FileOutputStream("test_output.html");
 DataUtil.crossStreams(inputStream, outputStream);
 File testFile = new File("test_output.html");
 Document doc = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 Assert.assertNotNull(doc);
 }
}