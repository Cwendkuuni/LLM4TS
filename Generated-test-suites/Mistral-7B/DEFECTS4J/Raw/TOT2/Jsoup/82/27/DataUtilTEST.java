import org.jsoup.helper.DataUtil;
import org.jsoup.helper.ConstraintViolationException;
import org.jsoup.helper.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private InputStream inputStream;
 private OutputStream outputStream;
 private Document document;

 @Before
 public void setUp() {
 inputStream = Mockito.mock(InputStream.class);
 outputStream = Mockito.mock(OutputStream.class);
 document = Mockito.mock(Document.class);
 }

 @Test
 public void loadFileTest() throws IOException {
 File file = Mockito.mock(File.class);
 Mockito.when(file.exists()).thenReturn(true);
 DataUtil.load(file, null, null);
 // add assertions here for the expected behavior of the loadFile method
 }

 @Test
 public void loadInputStreamTest() throws IOException {
 InputStream input = Mockito.mock(InputStream.class);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 DataUtil.load(input, charsetName, baseUri);
 // add assertions here for the expected behavior of the loadInputStream method
 }

 @Test
 public void loadInputStreamWithParserTest() throws IOException {
 InputStream input = Mockito.mock(InputStream.class);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Mockito.mock(Parser.class);
 DataUtil.load(input, charsetName, baseUri, parser);
 // add assertions here for the expected behavior of the loadInputStreamWithParser method
 }

 @Test
 public void crossStreamsTest() throws IOException {
 InputStream input = Mockito.mock(InputStream.class);
 OutputStream output = Mockito.mock(OutputStream.class);
 DataUtil.crossStreams(input, output);
 // add assertions here for the expected behavior of the crossStreams method
 }

 @Test
 public void parseInputStreamTest() throws IOException {
 InputStream input = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><element>Hello World</element></root>".getBytes(StandardCharsets.UTF_8));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Mockito.mock(Parser.class);
 Document expectedDocument = Mockito.mock(Document.class);
 Mockito.when(parser.parseInput(input, baseUri)).thenReturn(expectedDocument);
 DataUtil.parseInputStream(input, charsetName, baseUri, parser);
 // add assertions here for the expected behavior of the parseInputStream method
 }

 @Test(expected = UncheckedIOException.class)
 public void parseInputStreamWithIOExceptionTest() throws IOException {
 InputStream input = Mockito.mock(InputStream.class);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Mockito.mock(Parser.class);
 Mockito.when(parser.parseInput(input, baseUri)).thenThrow(new IOException("Parsing failed"));
 DataUtil.parseInputStream(input, charsetName, baseUri, parser);
 }

 @Test(expected = ConstraintViolationException.class)
 public void parseInputStreamWithConstraintViolationTest() {
 InputStream input = Mockito.mock(InputStream.class);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Mockito.mock(Parser.class);
 // ConstraintViolationException should be thrown in parseInputStream method when charsetName is null
 DataUtil.parseInputStream(input, null, baseUri, parser);
 }

 @Test
 public void readToByteBufferTest() throws IOException {
 InputStream input = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
 int maxSize = 5;
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap("Test\r\n".getBytes(StandardCharsets.UTF_8));
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(input, maxSize);
 Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
 }

 @Test(expected = IOException.class)
 public void readToByteBufferWithIOExceptionTest() throws IOException {
 InputStream input = Mockito.mock(InputStream.class);
 int maxSize = 5;
 DataUtil.readToByteBuffer(input, maxSize);
 // IOException should be thrown when reading from inputStream fails
 }

 @Test
 public void mimeBoundaryTest() {
 String expectedMimeBoundary = "boundary--TestBoundary";
 String actualMimeBoundary = DataUtil.mimeBoundary();
 Assert.assertEquals(expectedMimeBoundary, actualMimeBoundary);
 }

 @Test
 public void detectCharsetFromBomTest() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
 BomCharset expectedBomCharset = new BomCharset("UTF-8", true);
 BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);
 Assert.assertEquals(expectedBomCharset, actualBomCharset);
 }

 @Test
 public void getCharsetFromContentTypeTest() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void getCharsetFromContentTypeWithInvalidCharsetTest() {
 String contentType = "text/html; charset=invalidCharset";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 Assert.assertNull(actualCharset);
 }
}