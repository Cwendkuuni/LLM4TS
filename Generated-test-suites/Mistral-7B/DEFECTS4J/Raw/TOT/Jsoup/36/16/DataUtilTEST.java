import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private static ByteBuffer byteData;
 private static File testFile;

 @BeforeClass
 public static void setUp() throws Exception {
 testFile = new File("path/to/test/file.html");
 byteData = DataUtil.readToByteBuffer(new FileInputStream(testFile));
 }

 @Test
 public void testLoadFile() throws Exception {
 Document doc = DataUtil.load(testFile, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInvalidFile() throws Exception {
 File invalidFile = new File("path/to/non-existent/file.html");
 try {
 Document doc = DataUtil.load(invalidFile, null, null);
 fail("Expected IOException not thrown.");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws Exception {
 File invalidCharsetFile = new File("path/to/test/file_with_invalid_charset.html");
 try {
 Document doc = DataUtil.load(invalidCharsetFile, "invalidCharset", null);
 fail("Expected IllegalArgumentException not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLoadInputStream() throws Exception {
 InputStream input = new FileInputStream(testFile);
 Document doc = DataUtil.load(input, null, null);
 assertNotNull(doc);
 input.close();
 }

 @Test
 public void testLoadInvalidInputStream() throws Exception {
 InputStream invalidInput = new FileInputStream(new File("path/to/non-existent/file.html"));
 try {
 Document doc = DataUtil.load(invalidInput, null, null);
 fail("Expected IOException not thrown.");
 } catch (IOException e) {
 // Expected
 }
 finally {
 invalidInput.close();
 }
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws Exception {
 InputStream input = new FileInputStream(testFile);
 try {
 Document doc = DataUtil.load(input, "invalidCharset", null);
 fail("Expected IllegalArgumentException not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 finally {
 input.close();
 }
 }

 @Test
 public void testLoadInputStreamWithParser() throws Exception {
 InputStream input = new FileInputStream(testFile);
 Parser parser = DataUtil.parser().xmlParser();
 Document doc = DataUtil.load(input, null, null, parser);
 assertNotNull(doc);
 input.close();
 }

 @Test
 public void testParseByteData() throws Exception {
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws Exception {
 String invalidCharset = "invalidCharset";
 Document doc = DataUtil.parseByteData(byteData, invalidCharset, null, Parser.htmlParser());
 assertNull(doc);
 }

 @Test
 public void testParseByteDataWithValidCharset() throws Exception {
 String validCharset = "UTF-8";
 Document doc = DataUtil.parseByteData(byteData, validCharset, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithParser() throws Exception {
 String validCharset = "UTF-8";
 Document doc = DataUtil.parseByteData(byteData, validCharset, null, DataUtil.parser().xmlParser());
 assertNotNull(doc);
 }
}