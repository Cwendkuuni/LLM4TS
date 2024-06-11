import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

 private DataUtil dataUtil;
 private File inputFile;
 private File nonExistentFile;
 private InputStream inputStream;
 private ByteArrayInputStream byteArrayInputStream;
 private InputStream nonExistentInputStream;

 @Before
 public void setup() {
 dataUtil = new DataUtil();
 inputFile = new File("input.html");
 nonExistentFile = new File("nonExistent.html");
 inputStream = this.getClass().getResourceAsStream("/input.html");
 byteArrayInputStream = new ByteArrayInputStream("<html></html>".getBytes());
 nonExistentInputStream = new ByteArrayInputStream(new byte[0]);
 }

 // Test for load(File, String, String) method
 @Test
 public void testLoadFile() throws IOException {
 Document document = dataUtil.load(inputFile, "UTF-8", "http://example.com");
 assertNotNull(document);
 }

 // Test for load(File, String, String) with null charset
 @Test(expected = IOException.class)
 public void testLoadFileWithNullCharset() throws IOException {
 dataUtil.load(inputFile, null, "http://example.com");
 }

 // Test for load(File, String, String) with non-existent file
 @Test(expected = FileNotFoundException.class)
 public void testLoadNonExistentFile() throws IOException {
 dataUtil.load(nonExistentFile, "UTF-8", "http://example.com");
 }

 // Test for load(InputStream, String, String) method
 @Test
 public void testLoadInputStream() throws IOException {
 Document document = dataUtil.load(inputStream, "UTF-8", "http://example.com");
 assertNotNull(document);
 }

 // Test for load(InputStream, String, String) with null charset
 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException {
 dataUtil.load(inputStream, null, "http://example.com");
 }

 // Test for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamParser() throws IOException {
 Document document = dataUtil.load(inputStream, "UTF-8", "http://example.com", DataUtil.Parser.xmlParser());
 assertNotNull(document);
 }

 // Test for parseByteData() method with null charset
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataNullCharset() {
 dataUtil.parseByteData(null, null, null, DataUtil.Parser.htmlParser());
 }

 // Test for parseByteData() method with empty byteData
 @Test
 public void testParseByteDataEmptyByteData() {
 Document document = dataUtil.parseByteData(ByteBuffer.allocate(0), "UTF-8", "http://example.com", DataUtil.Parser.htmlParser());
 assertNotNull(document);
 }

 // Test for parseByteData() method with non-existent charset
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataInvalidCharset() {
 dataUtil.parseByteData(ByteBuffer.wrap("test".getBytes()), "invalidCharset", "http://example.com", DataUtil.Parser.htmlParser());
 }

 // Test for readToByteBuffer() method with null InputStream
 @Test(expected = IOException.class)
 public void testReadToByteBufferNullInputStream() throws IOException {
 dataUtil.readToByteBuffer(null);
 }

 // Test for readToByteBuffer() method with empty InputStream
 @Test
 public void testReadToByteBufferEmptyInputStream() throws IOException {
 ByteBuffer byteData = dataUtil.readToByteBuffer(nonExistentInputStream);
 assertEquals(0, byteData.limit());
 }
}