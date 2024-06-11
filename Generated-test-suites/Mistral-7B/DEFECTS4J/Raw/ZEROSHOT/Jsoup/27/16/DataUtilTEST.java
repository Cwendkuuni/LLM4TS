package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class DataUtilTest {

 private static Parser parser;

 @BeforeClass
 public static void setup() {
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFileDocument() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 Document document = DataUtil.load(file, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamDocumentWithDefaultCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String baseUri = "http://example.com";

 Document document = DataUtil.load(inputStream, null, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamDocumentWithSpecifiedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 String baseUri = "http://example.com";

 Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamDocumentWithSpecifiedParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = mock(Parser.class);

 when(parser.parseInput(anyString(), anyString())).thenReturn(mock(Document.class));

 Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataDocumentWithDefaultCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 byte[] buffer = new byte[bufferSize];
 int read;

 while ((read = inputStream.read(buffer)) != -1) {
 byteArrayOutputStream.write(buffer, 0, read);
 }

 ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
 String charsetName = null;
 String baseUri = "http://example.com";

 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataDocumentWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 byte[] buffer = new byte[bufferSize];
 int read;

 while ((read = inputStream.read(buffer)) != -1) {
 byteArrayOutputStream.write(buffer, 0, read);
 }

 ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(document);
 }

 @Test
 public void testGetCharsetFromContentType() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNullContentType() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testReadToByteBufferInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 assertNotNull(byteData);
 }
}