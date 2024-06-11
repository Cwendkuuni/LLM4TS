import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Document emptyDocument;
 private Document documentWithCharset;
 private Document documentWithMetaCharset;
 private Document documentWithXmlDeclaration;
 private Document documentWithContentType;
 private Document documentWithIoException;

 @Before
 public void setUp() throws IOException {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 emptyDocument = new Document();
 documentWithCharset = new Document();
 documentWithMetaCharset = new Document();
 documentWithXmlDeclaration = new Document();
 documentWithContentType = new Document();
 documentWithIoException = new Document();
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("testFile.html");
 Document result = dataUtil.load(file, null);
 assertEquals("Expected a non-empty document", !result.isEmpty(), result);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 String charset = "UTF-16";
 File file = new File("testFile.html");
 Document result = dataUtil.load(file, charset);
 assertEquals("Expected a non-empty document with specified charset", charset, result.outputSettings().charset().name());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.load(inputStream, null);
 assertEquals("Expected a non-empty document", !result.isEmpty(), result);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 String charset = "UTF-16";
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.load(inputStream, charset);
 assertEquals("Expected a non-empty document with specified charset", charset, result.outputSettings().charset().name());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 String charset = "UTF-16";
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.load(inputStream, charset, parser);
 assertEquals("Expected a non-empty document with specified charset", charset, result.outputSettings().charset().name());
 }

 @Test
 public void testLoadInputStreamWithIoException() {
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.read()).thenThrow(new IOException());
 assertThrows(IOException.class, () -> dataUtil.load(inputStream, null));
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
 OutputStream outputStream = new ByteArrayOutputStream();
 dataUtil.crossStreams(inputStream, outputStream);
 assertEquals("Expected test data in output stream", "test data", outputStream.toString());
 }

 @Test
 public void testParseInputStreamWithDefaultCharset() throws IOException {
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.parseInputStream(inputStream, null, null, parser);
 assertEquals("Expected a non-empty document", !result.isEmpty(), result);
 }

 @Test
 public void testParseInputStreamWithSpecifiedCharset() throws IOException {
 String charset = "UTF-16";
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.parseInputStream(inputStream, charset, null, parser);
 assertEquals("Expected a non-empty document with specified charset", charset, result.outputSettings().charset().name());
 }

 @Test
 public void testParseInputStreamWithContentTypeHeader() throws IOException {
 String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=GBK'></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.parseInputStream(inputStream, null, null, parser);
 assertEquals("Expected charset 'GBK' in document", "GBK", result.outputSettings().charset().name());
 }

 @Test
 public void testParseInputStreamWithInvalidContentTypeHeader() throws IOException {
 String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; barset=GBK'></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 assertThrows(IllegalArgumentException.class, () -> dataUtil.parseInputStream(inputStream, null, null, parser));
 }

 @Test
 public void testParseInputStreamWithXmlDeclaration() throws IOException {
 String xmlContent = "<?xml encoding='ISO-8859-1'?>\n<root/>";
 InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
 Document result = dataUtil.parseInputStream(inputStream, null, null, parser);
 assertEquals("Expected charset 'ISO-8859-1' in document", "ISO-8859-1", result.outputSettings().charset().name());
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer result = DataUtil.emptyByteBuffer();
 assertEquals("Expected an empty byte buffer", result.limit(), 0);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 String htmlContent = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
 ByteBuffer result = DataUtil.readToByteBuffer(inputStream, 1024 * 32);
 assertEquals("Expected the content of the input stream in the byte buffer", htmlContent, new String(result.array(), StandardCharsets.UTF_8));
 }

 @Test
 public void testReadToByteBufferWithIoException() {
 InputStream inputStream = mock(InputStream.class);
 when(inputStream.read()).thenThrow(new IOException());
 ByteBuffer result = DataUtil.readToByteBuffer(inputStream, 1024 * 32);
 assertNull("Expected null when an IOException occurs during reading from the input stream", result);
 }
}