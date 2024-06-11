import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DataUtilTest {

 // Test case for load(File) method
 @Test
 public void testLoadFile() {
 File inputFile = new File("input.html");
 Document doc = null;
 try {
 doc = DataUtil.load(inputFile, "UTF-8", null);
 Assert.assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 // Test case for load(InputStream) method
 @Test
 public void testLoadInputStream() throws IOException {
 File inputFile = new File("input.html");
 FileInputStream in = new FileInputStream(inputFile);
 Document doc = DataUtil.load(in, "UTF-8", null);
 in.close();
 Assert.assertNotNull(doc);
 }

 // Test case for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 File inputFile = new File("input.html");
 FileInputStream in = new FileInputStream(inputFile);
 Document doc = DataUtil.load(in, "UTF-8", null, Parser.xmlParser());
 in.close();
 Assert.assertNotNull(doc);
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with default charset and htmlParser
 @Test
 public void testParseInputStreamWithDefaultCharsetAndHtmlParser() throws IOException {
 InputStream inputStream = new FileInputStream(new File("input.html"));
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 inputStream.close();
 Assert.assertNotNull(doc);
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with specified charset and htmlParser
 @Test
 public void testParseInputStreamWithSpecifiedCharsetAndHtmlParser() throws IOException {
 InputStream inputStream = new FileInputStream(new File("input.html"));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 inputStream.close();
 Assert.assertNotNull(doc);
 }

 // Test case for parseInputStream(InputStream, String, String, Parser) method with specified charset and xmlParser
 @Test
 public void testParseInputStreamWithSpecifiedCharsetAndXmlParser() throws IOException {
 InputStream inputStream = new FileInputStream(new File("input.html"));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
 inputStream.close();
 Assert.assertNotNull(doc);
 }

 // Test case for crossStreams(InputStream, OutputStream) method
 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("input.html");
 File outputFile = new File("output.html");
 InputStream inputStream = new FileInputStream(inputFile);
 OutputStream outputStream = new FileOutputStream(outputFile);
 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 outputStream.close();
 // Check if outputFile has been written correctly
 // (This part depends on the content of input.html)
 }

 // Test case for detectCharsetFromBom(ByteBuffer) method
 @Test
 public void testDetectCharsetFromBom() {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("input.html"));
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertNotNull(bomCharset);
 }

 // Edge case: Test for empty InputStream
 @Test(expected = IOException.class)
 public void testParseInputStreamWithEmptyStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 }

 // Error scenario: Test for unsupported charset
 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamWithUnsupportedCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("input.html"));
 Document doc = null;
 try {
 doc = DataUtil.parseInputStream(inputStream, "XYZ", null, Parser.htmlParser());
 } catch (IOException e) {
 // Swallow the IOException
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Invalid charset name: XYZ", e.getMessage());
 } finally {
 inputStream.close();
 }
 }
}