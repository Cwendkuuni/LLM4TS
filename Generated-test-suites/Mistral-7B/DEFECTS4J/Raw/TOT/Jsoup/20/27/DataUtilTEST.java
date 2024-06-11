import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 @Test
 public void testLoadFile() {
 File testFile = new File("testFile.html"); // Replace with the path to a valid HTML file
 String charsetName = "UTF-8"; // Replace with the correct character set if necessary
 String baseUri = "http://localhost:8080"; // Replace with the base URI if necessary

 try {
 Document doc = DataUtil.load(testFile, charsetName, baseUri);
 Assert.assertNotNull(doc);
 } catch (IOException e) {
 Assert.fail("Failed to load file: " + e.getMessage());
 }
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream testInputStream = getClass().getResourceAsStream("/testFile.html"); // Replace with the path to a valid HTML file
 String charsetName = null;
 String baseUri = "http://localhost:8080"; // Replace with the base URI if necessary

 try {
 Document doc = DataUtil.load(testInputStream, charsetName, baseUri);
 Assert.assertNotNull(doc);
 } finally {
 testInputStream.close();
 }
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream testInputStream = getClass().getResourceAsStream("/testFile.html"); // Replace with the path to a valid HTML file
 String charsetName = "UTF-16"; // Replace with the correct character set if necessary
 String baseUri = "http://localhost:8080"; // Replace with the base URI if necessary

 try {
 Document doc = DataUtil.load(testInputStream, charsetName, baseUri);
 Assert.assertNotNull(doc);
 } finally {
 testInputStream.close();
 }
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 InputStream testInputStream = getClass().getResourceAsStream("/testFile.html"); // Replace with the path to a valid HTML file
 String charsetName = null;
 String baseUri = "http://localhost:8080"; // Replace with the base URI if necessary

 byte[] testBytes = new byte[1024]; // Replace with the size of the test data
 testInputStream.read(testBytes);

 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(testBytes), charsetName, baseUri, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 InputStream testInputStream = getClass().getResourceAsstream("/testFile.html"); // Replace with the path to a valid HTML file
 String charsetName = "UTF-16"; // Replace with the correct character set if necessary
 String baseUri = "http://localhost:8080"; // Replace with the base URI if necessary

 byte[] testBytes = new byte[1024]; // Replace with the size of the test data
 testInputStream.read(testBytes);

 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(testBytes), charsetName, baseUri, Parser.htmlParser());
 Assert.assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testIOExceptionOnFileLoad() throws IOException {
 File testFile = new File("testFileDoesNotExist.html"); // Replace with the path to a non-existent file
 String charsetName = "UTF-8";
 String baseUri = "http://localhost:8080";

 try {
 DataUtil.load(testFile, charsetName, baseUri);
 Assert.fail("Did not throw an IOException when file does not exist.");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test(expected = IOException.class)
 public void testIOExceptionOnInputStream() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
 String charsetName = "UTF-8";
 String baseUri = "http://localhost:8080";

 try {
 DataUtil.load(testInputStream, charsetName, baseUri);
 Assert.fail("Did not throw an IOException on empty input stream.");
 } catch (IOException e) {
 // Expected exception
 }
 }
}