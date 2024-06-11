import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

 // Test case for load(File, String, String) method
 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", "");
 assertNotNull(doc);
 }

 // Test case for load(InputStream, String, String) method with valid charset
 @Test
 public void testLoadInputStreamValidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head><meta charset='UTF-8'></head><body></body></html>";
 byte[] contentInBytes = htmlContent.getBytes();
 OutputStream output = outputStream;
 InputStream input = new ByteArrayInputStream(contentInBytes);
 Document doc = DataUtil.load(input, "UTF-8", "");
 assertEquals(htmlContent, doc.html());
 }

 // Test case for load(InputStream, String, String) method with invalid charset
 @Test(expected = IOException.class)
 public void testLoadInputStreamInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head><meta charset='GBK'></head><body></body></html>";
 byte[] contentInBytes = htmlContent.getBytes();
 OutputStream output = outputStream;
 InputStream input = new ByteArrayInputStream(contentInBytes);
 Document doc = DataUtil.load(input, "UTF-8", "");
 fail("Expected IOException");
 }

 // Test case for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head><meta charset='UTF-8'></head><body></body></html>";
 byte[] contentInBytes = htmlContent.getBytes();
 OutputStream output = outputStream;
 InputStream input = new ByteArrayInputStream(contentInBytes);
 Document doc = DataUtil.load(input, "UTF-8", "", DataUtil.Parser.xmlParser());
 assertEquals(htmlContent, doc.xml().asXml());
 }

 // Test case for crossStreams() method
 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("testInput.txt");
 File outputFile = new File("testOutput.txt");
 InputStream input = new FileInputStream(inputFile);
 OutputStream output = new FileOutputStream(outputFile);
 DataUtil.crossStreams(input, output);
 assertEquals(Files.readAllLines(Paths.get(inputFile.getAbsolutePath())).get(0),
 Files.readAllLines(Paths.get(outputFile.getAbsolutePath())).get(0));
 }

 // Test case for parseByteData() method with valid charset
 @Test
 public void testParseByteDataValidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String htmlContent = "<html><head><meta charset='UTF-8'></head><body></body></html>";
 byte[] contentInBytes = htmlContent.getBytes();
 ByteBuffer byteData = ByteBuffer.wrap(contentInBytes);
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", DataUtil.Parser.htmlParser());
 assertEquals(htmlContent, doc.html());
 }

 // Test case for parseByteData() method with invalid charset
 @Test(expected = IOException.class)
 public void testParseByteDataInvalidCharset() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 String charsetName = "GBK";
 Document doc = DataUtil.parseByteData(byteData, charsetName, "", DataUtil.Parser.htmlParser());
 fail("Expected IOException");
 }
}