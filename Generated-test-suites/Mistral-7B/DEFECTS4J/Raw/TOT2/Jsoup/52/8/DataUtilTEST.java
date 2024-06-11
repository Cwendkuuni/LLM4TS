import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 // Test case for load method with valid file path, default charset, and baseUri
 @Test
 public void testLoadFile() {
 File file = new File("validFilePath.html");
 Document doc = null;
 try {
 doc = DataUtil.load(file, null, null);
 assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 // Test case for load method with valid file path, specified charset, and baseUri
 @Test
 public void testLoadFileWithCharset() {
 File file = new File("validFilePath.html");
 Document doc = null;
 try {
 doc = DataUtil.load(file, "UTF-16", null);
 assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 // Test case for load method with valid input stream, default charset, and baseUri
 @Test
 public void testLoadInputStream() throws IOException {
 InputStream in = this.getClass().getResourceAsStream("/validInput.html");
 Document doc = null;
 try {
 doc = DataUtil.load(in, null, null);
 assertNotNull(doc);
 } finally {
 in.close();
 }
 }

 // Test case for load method with valid input stream, specified charset, and baseUri
 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 InputStream in = this.getClass().getResourceAsStream("/validInput.html");
 Document doc = null;
 try {
 doc = DataUtil.load(in, "UTF-16", null);
 assertNotNull(doc);
 } finally {
 in.close();
 }
 }

 // Test case for load method with valid input stream, specified parser, default charset, and baseUri
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream in = this.getClass().getResourceAsStream("/validInput.html");
 Document doc = null;
 try {
 doc = DataUtil.load(in, null, null, DataUtil.Parser.xmlParser());
 assertNotNull(doc);
 } finally {
 in.close();
 }
 }

 // Test case for crossStreams method with valid input and output streams
 @Test
 public void testCrossStreams() throws IOException {
 File tempFile = File.createTempFile("test", ".html");
 InputStream in = new FileInputStream(new File("validInput.html"));
 OutputStream out = new FileOutputStream(tempFile);
 DataUtil.crossStreams(in, out);
 in.close();
 out.close();

 Document doc = DataUtil.load(tempFile, null, null);
 assertNotNull(doc);
 }

 // Test case for parseByteData method with valid byte data, default charset, and baseUri
 @Test
 public void testParseByteDataDefaultCharset() {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("validByteData.html"));
 String charsetName = null;
 Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test case for parseByteData method with valid byte data, specified charset, and baseUri
 @Test
 public void testParseByteDataSpecifiedCharset() {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("validByteData.html"));
 String charsetName = "UTF-16";
 Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test case for parseByteData method with valid byte data, default charset, and specified parser
 @Test
 public void testParseByteDataDefaultParser() {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("validByteData.html"));
 String charsetName = null;
 Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.xmlParser());
 assertNotNull(doc);
 }

 // Test case for parseByteData method with valid byte data, specified charset, and specified parser
 @Test
 public void testParseByteDataSpecifiedParser() {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("validByteData.html"));
 String charsetName = "UTF-16";
 Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.xmlParser());
 assertNotNull(doc);
 }

 // Test case for getCharsetFromContentType method with valid content type
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 // Test case for validateCharset method with valid charset
 @Test
 public void testValidCharset() {
 String charset = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertEquals("UTF-8", validatedCharset);
 }

 // Test case for validateCharset method with invalid charset
 @Test (expected = IllegalArgumentException.class)
 public void testInvalidCharset() {
 String charset = "InvalidCharset";
 String validatedCharset = DataUtil.validateCharset(charset);
 }

 // Test case for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 int length = boundary.length();
 for (int i = 0; i < length; i++) {
 assertEquals(i < length - 1, boundary.charAt(i) != '-');
 }
 }
}