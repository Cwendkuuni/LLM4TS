import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 // Test for load method with file input and default charset
 @Test
 public void testLoadFileDefaultCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), null);
 assertNotNull(doc);
 }

 // Test for load method with file input and specified charset
 @Test
 public void testLoadFileSpecifiedCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-16");
 assertNotNull(doc);
 }

 // Test for load method with input stream and default charset
 @Test
 public void testLoadInputStreamDefaultCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.load(inputStream, null);
 assertNotNull(doc);
 }

 // Test for load method with input stream and specified charset
 @Test
 public void testLoadInputStreamSpecifiedCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.load(inputStream, "UTF-16");
 assertNotNull(doc);
 }

 // Test for load method with input stream, default charset, and Parser.xmlParser()
 @Test
 public void testLoadInputStreamDefaultCharsetParserXML() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.xml");
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertNotNull(doc);
 }

 // Test for load method with input stream, default charset, and Parser.htmlParser() (expected IOException)
 @Test(expected = IOException.class)
 public void testLoadInputStreamDefaultCharsetParserHTML() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.xml");
 Document doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
 fail("Expected IOException");
 }

 // Test for crossStreams method with valid input and output streams
 @Test
 public void testCrossStreams() throws IOException {
 InputStream in = getClass().getResourceAsStream("/test.html");
 OutputStream out = new ByteArrayOutputStream();
 DataUtil.crossStreams(in, out);
 // Check if output stream has valid content
 String actualContent = new String(out.toByteArray(), "UTF-8");
 assertTrue(actualContent.contains("Test content"));
 in.close();
 out.close();
 }

 // Test for parseInputStream method with valid input, default charset, and htmlParser
 @Test
 public void testParseInputStreamWithDefaultCharsetAndHTMLParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 // Check if document has valid content
 assertEquals("Test content", doc.select("p").text());
 inputStream.close();
 }

 // Test for parseInputStream method with valid input, specified charset, and htmlParser
 @Test
 public void testParseInputStreamWithSpecifiedCharsetAndHTMLParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
 // Check if document has valid content
 assertEquals("Test content", doc.select("p").text());
 inputStream.close();
 }

 // Test for parseInputStream method with valid input, default charset, and xmlParser (expected IOException)
 @Test(expected = IOException.class)
 public void testParseInputStreamWithDefaultCharsetAndXMLParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
 fail("Expected IOException");
 }

 // Test for readToByteBuffer method with valid input stream
 @Test
 public void testReadToByteBufferWithValidInputStream() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 // Check if byte buffer has valid content
 String actualContent = Charset.forName("UTF-8").decode(byteBuffer).toString();
 assertTrue(actualContent.contains("Test content"));
 inputStream.close();
 }

 // Test for readToByteBuffer method with null input stream
 @Test
 public void testReadToByteBufferWithNullInputStream() {
 assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(null));
 }

 // Test for readFileToByteBuffer method with valid file
 @Test
 public void testReadFileToByteBufferWithValidFile() throws IOException {
 File file = new File("test.html");
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 // Check if byte buffer has valid content
 String actualContent = Charset.forName("UTF-8").decode(byteBuffer).toString();
 assertTrue(actualContent.contains("Test content"));
 }

 // Test for readFileToByteBuffer method with null file
 @Test
 public void testReadFileToByteBufferWithNullFile() {
 assertThrows(NullPointerException.class, () -> DataUtil.readFileToByteBuffer(null));
 }

 // Test for emptyByteBuffer method
 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, byteBuffer.limit());
 }

 // Test for getCharsetFromContentType method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 // Test for validateCharset method
 @Test
 public void testValidateCharset() {
 String invalidCharset = "invalid-charset";
 String validCharset1 = "UTF-8";
 String validCharset2 = "UTF-16";
 assertNull(DataUtil.validateCharset(null));
 assertNull(DataUtil.validateCharset(""));
 assertNull(DataUtil.validateCharset(invalidCharset));
 assertEquals("UTF-8", DataUtil.validateCharset(validCharset1));
 assertEquals("UTF-16", DataUtil.validateCharset(validCharset2));
 }

 // Test for mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String mimeBoundary = DataUtil.mimeBoundary();
 assertFalse(mimeBoundary.isEmpty());
 }
}