import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

 private static final String TEST_FILE_PATH = "testFiles/test.html";
 private static final String TEST_INPUT_STREAM_CONTENT = "This is a test file.";

 @BeforeClass
 public static void setup() {
 try {
 File testFile = new File(TEST_FILE_PATH);
 if (!testFile.exists()) {
 testFile.createNewFile();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File(TEST_FILE_PATH), "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInvalidFile() throws IOException {
 try {
 DataUtil.load(new File("nonExistentFile.txt"), "UTF-8", null);
 fail("Expected IOException to be thrown.");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testLoadInvalidCharset() throws IOException {
 try {
 DataUtil.load(new File(TEST_FILE_PATH), "invalidCharset", null);
 fail("Expected IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLoadStream() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(testInputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadStreamInvalidCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
 try {
 Document doc = DataUtil.load(testInputStream, "invalidCharset", null);
 fail("Expected IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLoadStreamEmpty() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = DataUtil.load(testInputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadStreamNullCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
 try {
 Document doc = DataUtil.load(testInputStream, null, null);
 fail("Expected IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataInvalidCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
 try {
 Document doc = DataUtil.parseByteData(byteData, "invalidCharset", null, Parser.htmlParser());
 fail("Expected IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 String result = outputStream.toString(StandardCharsets.UTF_8.name());
 assertEquals(TEST_INPUT_STREAM_CONTENT, result);
 }
}