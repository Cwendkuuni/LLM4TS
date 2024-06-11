import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

 private static Document document;
 private static ByteArrayOutputStream outputStream;
 private static InputStream inputStream;
 private static Random random;

 @BeforeClass
 public static void setup() {
MockitoAnnotations.initMocks(DataUtilTest.class);
 document = new Document();
 outputStream = new ByteArrayOutputStream();
 random = new Random();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document loadedDocument = DataUtil.load(file, "UTF-8", "");
 assertNotNull(loadedDocument);
 assertEquals("<html><body></body></html>", loadedDocument.outerHtml());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document loadedDocument = DataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(loadedDocument);
 assertEquals("<html><body></body></html>", loadedDocument.outerHtml());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1));
 assertThrows(IOException.class, () -> DataUtil.load(inputStream, "invalidCharset", ""));
 }

 @Test
 public void testParseInputStream() throws IOException {
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document loadedDocument = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
 assertNotNull(loadedDocument);
 assertEquals("<html><body></body></html>", loadedDocument.outerHtml());
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1));
 assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser()));
 }

 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("input.txt");
 File outputFile = new File("output.txt");
 InputStream inputStream = new FileInputStream(inputFile);
 OutputStream outputStream = new FileOutputStream(outputFile);
 DataUtil.crossStreams(inputStream, outputStream);
 assertEquals(inputFile.length(), outputFile.length());
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, byteBuffer.limit());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 String content = "test content";
 InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertEquals(content.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 String content = "test content";
 InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, content.length() + 1);
 assertEquals(content.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File file = new File("test.txt");
 ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
 assertEquals(file.length(), byteBuffer.limit());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidCharset() {
 String contentType = "text/html; charset=invalidCharset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertEquals(charset, validatedCharset);
 }

 @Test
 public void testValidateCharsetWithNullCharset() {
 String charset = null;
 String validatedCharset = DataUtil.validateCharset(charset);
 assertNull(validatedCharset);
 }

 @Test
 public void testValidateCharsetWithInvalidCharset() {
 String charset = "invalidCharset";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertNull(validatedCharset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), 32);
 }

 @Test
 public void testDetectCharsetFromBom() throws IOException {
 byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};
 ByteBuffer byteBuffer = ByteBuffer.wrap(bomUtf8);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
 assertEquals("UTF-8", bomCharset.charset);
 assertEquals(3, bomCharset.offset);
 }

 @Test
 public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
 byte[] bomUtf32 = {0x00, 0x00, 0xFE, 0xFF};
 ByteBuffer byteBuffer = ByteBuffer.wrap(bomUtf32);
 BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
 assertNull(bomCharset);
 }
}