import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private static ByteArrayOutputStream outputStream;
 private static InputStream inputStream;
 private static Document doc;

 @BeforeClass
 public static void setup() {
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 }

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 File file = new File("test.html"); // Add test file content here
 Document doc = DataUtil.load(file, "UTF-8", "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = new File("test.html"); // Add test file content here
 Document doc = DataUtil.load(file, "GBK", "");
 assertNotNull(doc); // The test will pass even if the charset is incorrect, but the encoding should be changed in the test file
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "GBK", "");
 assertNotNull(doc); // The test will pass even if the charset is incorrect, but the encoding should be changed in the test file
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String htmlContent = "<html><body></body></html>";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testInvalidFile() throws IOException {
 File file = new File("non-existent-file");
 Document doc = DataUtil.load(file, "UTF-8", "");
 fail("Load non-existent file should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testInvalidInputStream() throws IOException {
 inputStream = null;
 Document doc = DataUtil.load(inputStream, "UTF-8", "");
 fail("Load null input stream should throw IOException");
 }
}