import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 @Test
 public void testLoadFile() {
 Document doc = null;
 File testFile = new File("test.html"); // replace with valid file path
 String charsetName = "UTF-8";
 String baseUri = "http://localhost:8080";
 try {
 doc = DataUtil.load(testFile, charsetName, baseUri);
 } catch (IOException e) {
 // handle exception
 }
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() {
 Document doc = null;
 String charsetName = "UTF-8";
 String baseUri = "http://localhost:8080";
 InputStream testStream = new ByteArrayInputStream(new byte[]{"<html><body></body></html>"[0]}); // replace with valid input stream
 try {
 doc = DataUtil.load(testStream, charsetName, baseUri);
 } catch (IOException e) {
 // handle exception
 }
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() {
 Document doc = null;
 String charsetName = "UTF-8";
 String baseUri = "http://localhost:8080";
 InputStream testStream = new ByteArrayInputStream(new byte[]{"<html><body></body></html>"[0]}); // replace with valid input stream
 Parser parser = Parser.htmlParser();
 try {
 doc = DataUtil.load(testStream, charsetName, baseUri, parser);
 } catch (IOException e) {
 // handle exception
 }
 Assert.assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() {
 InputStream input = new ByteArrayInputStream(new byte[]{"<html><body></body></html>"[0]});
 OutputStream output = new ByteArrayOutputStream();
 DataUtil.crossStreams(input, output);
 ByteBuffer expected = ByteBuffer.wrap(new byte[]{"<html><body></body></html>"[0]});
 ByteBuffer actual = DataUtil.readToByteBuffer(output.toInputStream());
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullCharset() {
 Document doc = null;
 File testFile = new File("test.html"); // replace with valid file path
 String baseUri = "http://localhost:8080";
 try {
 doc = DataUtil.load(testFile, null, baseUri);
 } catch (IOException e) {
 // expected exception
 }
 Assert.assertNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() {
 Document doc = null;
 InputStream testStream = new ByteArrayInputStream(new byte[]{"<html><body></body></html>"[0]}); // replace with valid input stream
 String baseUri = "http://localhost:8080";
 try {
 doc = DataUtil.load(testStream, null, baseUri);
 } catch (IOException e) {
 // expected exception
 }
 Assert.assertNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() {
 Document doc = null;
 InputStream testStream = new ByteArrayInputStream(new byte[]{"<html><body></body></html>"[0]}); // replace with valid input stream
 String charsetName = "invalidCharset";
 String baseUri = "http://localhost:8080";
 try {
 doc = DataUtil.load(testStream, charsetName, baseUri);
 } catch (IOException e) {
 // expected exception
 }
 Assert.assertNull(doc);
 }

 @Test
 public void testParseByteData() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 String charsetName = "UTF-8";
 String baseUri = "http://localhost:8080";
 Parser parser = Parser.htmlParser();
 String docData = "<html><body></body></html>";
 byteData.put(docData.getBytes(StandardCharsets.UTF_8));
 byteData.flip();
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 Assert.assertNotNull(doc);
 }

 @Test(expected = IllegalCharsetNameException.class)
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 String charsetName = "invalidCharset";
 String baseUri = "http://localhost:8080";
 Parser parser = Parser.htmlParser();
 String docData = "<html><body></body></html>";
 byteData.put(docData.getBytes(StandardCharsets.UTF_8));
 byteData.flip();
 try {
 DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 } catch (IllegalCharsetNameException e) {
 // expected exception
 }
 }
}