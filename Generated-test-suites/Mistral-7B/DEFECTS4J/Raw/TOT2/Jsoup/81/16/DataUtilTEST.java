import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FileDocument;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 @Test
 public void testLoadFileDocument() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), null, null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamDocumentWithDefaultCharset() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 Document doc = DataUtil.load(input, null, null);
 Assert.assertNotNull(doc);
 input.close();
 }

 @Test
 public void testLoadInputStreamDocumentWithSpecifiedCharset() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 Document doc = DataUtil.load(input, "UTF-16", null);
 Assert.assertNotNull(doc);
 input.close();
 }

 @Test
 public void testLoadInputStreamDocumentWithParser() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 Document doc = DataUtil.load(input, null, null, Parser.xmlParser());
 Assert.assertNotNull(doc);
 input.close();
 }

 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("test.html");
 File outputFile = new File("output.html");
 InputStream input = new FileInputStream(inputFile);
 OutputStream output = new FileOutputStream(outputFile);
 DataUtil.crossStreams(input, output);
 input.close();
 output.close();

 FileDocument inputDoc = FileDocument.fromFile(inputFile);
 Document doc = DataUtil.parseInputStream(inputDoc.inputStream(), null, null, Parser.htmlParser());
 Assert.assertEquals(new String(Files.readAllBytes(outputFile.toPath()), StandardCharsets.UTF_8), doc.body().html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInputStream() {
 DataUtil.load(null, null, null);
 }

 @Test
 public void testDetectCharsetFromBOM() {
 ByteBuffer bom = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(bom);
 Assert.assertEquals("UTF-16", bomCharset.charset);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=GB2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals("GB2312", charset);
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 String charsetName = "UTF-16";
 String baseUri = "http://example.com";
 Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
 input.close();

 Assert.assertNotNull(doc);
 }

 @Test(expected = UncheckedIOException.class)
 public void testParseInputStreamWithIOException() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 ByteBuffer firstBytes = DataUtil.readToByteBuffer(input, 1024);
 DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 ByteBuffer byteData = DataUtil.readToByteBuffer(input);
 input.close();

 Assert.assertNotNull(byteData);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 InputStream input = new FileInputStream(new File("test.html"));
 ByteBuffer byteData = DataUtil.readToByteBuffer(input, 1024);
 Assert.assertEquals(1024, byteData.limit());
 input.close();
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
 Assert.assertNotNull(emptyByteBuffer);
 Assert.assertEquals(0, emptyByteBuffer.limit());
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File file = new File("test.html");
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
 Assert.assertNotNull(byteData);
 }
}