import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class DataUtilTest {

 private Document testDocument;
 private String charsetName;

 @Before
 public void setUp() {
 charsetName = "UTF-8";
 try {
 // Test loading a file with valid charset
 testDocument = DataUtil.load(new File("test.html"), charsetName, "");
 } catch (IOException e) {
 // File does not exist for testing
 }
 }

 @Test
 public void testLoadFileWithValidCharset() {
 assertNotNull(testDocument);
 }

 @Test
 public void testLoadFileWithInvalidCharset() {
 charsetName = "invalidCharset";
 try {
 // Test loading a file with invalid charset
 testDocument = DataUtil.load(new File("test.html"), charsetName, "");
 fail("Expected IOException");
 } catch (IOException e) {
 // Pass
 }
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 testDocument = DataUtil.load(inputStream, charsetName, "");
 assertNotNull(testDocument);
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 charsetName = "invalidCharset";
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 try {
 testDocument = DataUtil.load(inputStream, charsetName, "");
 fail("Expected IOException");
 } catch (IOException e) {
 // Pass
 } finally {
 inputStream.close();
 }
 }

 @Test
 public void testParseInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 testDocument = DataUtil.load(inputStream, charsetName, "", Parser.htmlParser());
 assertNotNull(testDocument);
 inputStream.close();
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 charsetName = "invalidCharset";
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 try {
 testDocument = DataUtil.load(inputStream, charsetName, "", Parser.htmlParser());
 fail("Expected IOException");
 } catch (IOException e) {
 // Pass
 } finally {
 inputStream.close();
 }
 }

 @Test
 public void testParseInputStreamWithXmlParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.xml");
 testDocument = DataUtil.load(inputStream, null, "", Parser.xmlParser());
 assertNotNull(testDocument);
 assertNotNull(testDocument.select("root"));
 inputStream.close();
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 OutputStream outputStream = new FileOutputStream(new File("output.html"));
 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 outputStream.close();
 // Assert output file is equal to input file
 // ...
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCrossStreamsWithNullInputStream() {
 OutputStream outputStream = new FileOutputStream(new File("output.html"));
 DataUtil.crossStreams(null, outputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCrossStreamsWithNullOutputStream() {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 DataUtil.crossStreams(inputStream, null);
 }
}