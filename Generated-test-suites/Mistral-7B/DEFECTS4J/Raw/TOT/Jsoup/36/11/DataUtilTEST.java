import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 @Mock
 private Parser parser;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 }

 @Test
 public void testLoadFileCharsetKnown() throws IOException {
 File file = new File("test.html");
 InputStream inputStream = new FileInputStream(file);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = parser.parseInput("test.html content", baseUri);

 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);
 Document actualDocument = dataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadFileCharsetUnknown() throws IOException {
 File file = new File("test.html");
 InputStream inputStream = new FileInputStream(file);
 String baseUri = "http://example.com";
 String charsetName = null;
 Document expectedDocument = parser.parseInput("test.html content", baseUri);

 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);
 when(parser.parseInput(anyString())).thenReturn(expectedDocument);
 Document actualDocument = dataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);

 // check that default charset was used
 assertEquals("UTF-8", dataUtil.getDefaultCharset());
 }

 @Test
 public void testLoadInputStreamCharsetKnown() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = parser.parseInput("test.html content", baseUri);

 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);
 Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamCharsetUnknown() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
 String baseUri = "http://example.com";
 String charsetName = null;
 Document expectedDocument = parser.parseInput("test.html content", baseUri);

 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);
 when(parser.parseInput(anyString())).thenReturn(expectedDocument);
 Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);

 // check that default charset was used
 assertEquals("UTF-8", dataUtil.getDefaultCharset());
 }

 @Test
 public void testLoadInputStreamCharsetDetected() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes("gb2312"));
 String baseUri = "http://example.com";
 String charsetName = null;
 Document expectedDocument = parser.parseInput("test.html content", baseUri);

 when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);
 when(parser.parseInput(anyString())).thenReturn(expectedDocument);
 when(parser.parseInput(anyString())).thenReturn(expectedDocument);
 when(parser.parseInput(eq("gb2312"), anyString())).thenReturn(expectedDocument);
 Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);

 // check that detected charset was used
 assertEquals("gb2312", dataUtil.getDefaultCharset());
 }

 @Test(expected = IOException.class)
 public void testLoadInvalidFile() throws IOException {
 File file = new File("test.html");
 String charsetName = null;
 String baseUri = "http://example.com";
 when(parser.parseInput(anyString(), anyString())).thenThrow(new IOException());
 dataUtil.load(file, charsetName, baseUri);
 }

 @Test(expected = IOException.class)
 public void testLoadInvalidInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 String charsetName = null;
 String baseUri = "http://example.com";
 when(parser.parseInput(anyString(), anyString())).thenThrow(new IOException());
 dataUtil.load(inputStream, charsetName, baseUri);
 }

 @Test
 public void testParseByteDataDefaultCharset() {
 String docData = "test.html content";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
 String charsetName = null;
 String baseUri = "http://example.com";
 Document expectedDocument = parser.parseInput(docData, baseUri);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseByteDataSpecifiedCharset() {
 String docData = "test.html content";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Document expectedDocument = parser.parseInput(docData, baseUri);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseByteDataDetectedCharset() {
 String docData = "test.html content";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("gb2312"));
 String charsetName = null;
 String baseUri = "http://example.com";
 Document expectedDocument = parser.parseInput(docData, baseUri);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);

 // check that detected charset was used
 assertEquals("gb2312", dataUtil.getDefaultCharset());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes("UTF-8"));
 ByteBuffer expectedByteData = ByteBuffer.wrap("test.html content".getBytes("UTF-8"));
 ByteBuffer actualByteData = dataUtil.readToByteBuffer(inputStream);
 assertEquals(expectedByteData, actualByteData);
 }

 @Test
 public void testReadToByteBufferMaxSize() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10);
 InputStream inputStream = outputStream.toInputStream();
 ByteBuffer expectedByteData = ByteBuffer.wrap("test.html content".getBytes("UTF-8"));
 ByteBuffer actualByteData = dataUtil.readToByteBuffer(inputStream, 10);
 assertEquals(expectedByteData, actualByteData);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeEmpty() {
 String contentType = "text/html";
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertNull(actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeInvalid() {
 String contentType = "text/html; bogus";
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertNull(actualCharset);
 }
}