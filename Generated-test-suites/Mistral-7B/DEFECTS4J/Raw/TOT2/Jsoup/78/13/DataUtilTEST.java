import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

 // Test case for the load method with a valid File, charset, and baseUri
 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File("testFile.html"), "UTF-8", "/baseUri");
 assertNotNull(doc);
 }

 // Test case for the load method with a null File, charset, and baseUri
 @Test(expected = IOException.class)
 public void testLoadFileNull() {
 DataUtil.load(null, "UTF-8", "/baseUri");
 }

 // Test case for the load method with a valid InputStream, charset, and baseUri
 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, "UTF-8", "/baseUri");
 assertNotNull(doc);
 }

 // Test case for the load method with a null InputStream, charset, and baseUri
 @Test(expected = IOException.class)
 public void testLoadInputStreamNull() {
 DataUtil.load(null, "UTF-8", "/baseUri");
 }

 // Test case for the load method with a valid InputStream, null charset, and baseUri
 @Test
 public void testLoadInputStreamNullCharset() throws IOException {
 Document doc = DataUtil.load(new ByteArrayInputStream("testData".getBytes()), null, "/baseUri");
 assertNotNull(doc);
 }

 // Test case for the parseInputStream method with a valid InputStream, charset, baseUri, and Parser
 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "/baseUri", Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test case for the parseInputStream method with a null InputStream, charset, baseUri, and Parser
 @Test(expected = IOException.class)
 public void testParseInputStreamNull() {
 DataUtil.parseInputStream(null, "UTF-8", "/baseUri", Parser.htmlParser());
 }

 // Test case for the parseInputStream method with a valid InputStream, null charset, baseUri, and Parser
 @Test
 public void testParseInputStreamNullCharset() throws IOException {
 Document doc = DataUtil.parseInputStream(new ByteArrayInputStream("testData".getBytes()), null, "/baseUri", Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test case for the parseInputStream method with a valid InputStream, charset, null baseUri, and Parser
 @Test
 public void testParseInputStreamNullBaseUri() throws IOException {
 Document doc = DataUtil.parseInputStream(new ByteArrayInputStream("testData".getBytes()), "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 // Test case for the crossStreams method with valid InputStream and OutputStream
 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 assertNotNull(outputStream.toString());
 }

 // Test case for the crossStreams method with null InputStream and OutputStream
 @Test(expected = IOException.class)
 public void testCrossStreamsNull() throws IOException {
 DataUtil.crossStreams(null, new ByteArrayOutputStream());
 }

 // Test case for the mimeBoundary method
 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertNotNull(boundary);
 }
}