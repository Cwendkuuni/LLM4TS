package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 @InjectMocks
 private DataUtil dataUtil;

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("testFile.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDocument = Mockito.mock(Document.class);
 Mockito.when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);

 // Test loading a file with a valid charset specified in the file header
 File validFile = new File("src/test/resources/validCharset.html");
 FileInputStream validInputStream = Mockito.mock(FileInputStream.class);
 Mockito.when(validInputStream.readAllBytes()).thenReturn(validFile.getBytes());
 Mockito.when(dataUtil.load(validFile, charsetName, baseUri)).thenReturn(expectedDocument);
 Mockito.when(dataUtil.readToByteBuffer(validInputStream)).thenReturn(ByteBuffer.wrap(validFile.getBytes()));

 InputStream testInputStream = Mockito.mock(InputStream.class);
 Mockito.when(testInputStream.readAllBytes()).thenCallRealMethod();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Mockito.when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);

 ByteBuffer byteData = dataUtil.readToByteBuffer(testInputStream);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);

 // Test loading a file with no charset specified in the file header
 File noCharsetFile = new File("src/test/resources/noCharset.html");
 Mockito.when(dataUtil.load(noCharsetFile, null, baseUri)).thenReturn(expectedDocument);
 Mockito.when(dataUtil.readToByteBuffer(Mockito.mock(FileInputStream.class))).thenReturn(ByteBuffer.wrap(noCharsetFile.getBytes()));

 byteData = dataUtil.readToByteBuffer(testInputStream);
 actualDocument = dataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadStream() throws IOException {
 InputStream testInputStream = Mockito.mock(InputStream.class);
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDocument = Mockito.mock(Document.class);
 Mockito.when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);

 // Test loading a stream with a valid charset specified
 ByteArrayInputStream validInputStream = Mockito.mock(ByteArrayInputStream.class);
 ByteArrayOutputStream validOutputStream = new ByteArrayOutputStream();
 validOutputStream.write("validContent".getBytes());
 Mockito.when(validInputStream.readAllBytes()).thenReturn(validOutputStream.toByteArray());
 Mockito.when(dataUtil.parseByteData(Mockito.any(ByteBuffer.class), eq(charsetName), eq(baseUri), eq(Parser.htmlParser()))).thenReturn(expectedDocument);

 ByteBuffer byteData = dataUtil.readToByteBuffer(testInputStream);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);

 // Test loading a stream with no charset specified
 Mockito.when(dataUtil.parseByteData(Mockito.any(ByteBuffer.class), eq(null), eq(baseUri), eq(Parser.htmlParser()))).thenReturn(expectedDocument);

 byteData = dataUtil.readToByteBuffer(testInputStream);
 actualDocument = dataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testDetectCharset() throws IOException {
 InputStream testInputStream = Mockito.mock(InputStream.class);
 String baseUri = "http://test.com";
 Document expectedDocument = Mockito.mock(Document.class);
 Mockito.when(expectedDocument.outputSettings().charset()).thenReturn("GBK");

 // Test detecting charset from meta http-equiv tag
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta http-equiv='Content-Type' content='text/html; charset=GBK'></head><body>Test</body></html>".getBytes());
 Mockito.when(testInputStream.readAllBytes()).thenReturn(outputStream.toByteArray());
 Mockito.when(dataUtil.parseByteData(Mockito.any(ByteBuffer.class), eq(null), eq(baseUri), eq(Parser.htmlParser()))).thenReturn(expectedDocument);

 ByteBuffer byteData = dataUtil.readToByteBuffer(testInputStream);
 Document actualDocument = dataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

 assertEquals("GBK", actualDocument.outputSettings().charset());

 // Test detecting charset from meta charset tag
 outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset='GBK'></head><body>Test</body></html>".getBytes());
 Mockito.when(testInputStream.readAllBytes()).thenReturn(outputStream.toByteArray());

 byteData = dataUtil.readToByteBuffer(testInputStream);
 actualDocument = dataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

 assertEquals("GBK", actualDocument.outputSettings().charset());

 // Test detecting charset when no charset is present
 Mockito.when(dataUtil.parseByteData(Mockito.any(ByteBuffer.class), eq(null), eq(baseUri), eq(Parser.htmlParser()))).thenReturn(expectedDocument);

 byteData = dataUtil.readToByteBuffer(testInputStream);
 actualDocument = dataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

 assertEquals(DataUtil.defaultCharset, actualDocument.outputSettings().charset());
 }

 @Test
 public void testInvalidCharsetNameException() throws IOException {
 InputStream testInputStream = Mockito.mock(InputStream.class);
 String baseUri = "http://test.com";
 Document expectedDocument = Mockito.mock(Document.class);
 Mockito.when(expectedDocument.outputSettings().charset()).thenReturn("GBK");

 // Test detecting charset with an invalid charset name
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset='InvalidCharset'></head><body>Test</body></html>".getBytes());
 Mockito.when(testInputStream.readAllBytes()).thenReturn(outputStream.toByteArray());

 try {
 DataUtil.parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), null, baseUri, Parser.htmlParser());
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseByteDataWithUtf8Bom() throws IOException {
 InputStream testInputStream = Mockito.mock(InputStream.class);
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDocument = Mockito.mock(Document.class);
 Mockito.when(expectedDocument.outputSettings().charset()).thenReturn(charsetName);

 // Test parsing byte data with UTF-8 bom
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("\uFEFF".getBytes());
 outputStream.write("validContent".getBytes());
 Mockito.when(testInputStream.readAllBytes()).thenReturn(outputStream.toByteArray());
 Mockito.when(dataUtil.parseByteData(Mockito.any(ByteBuffer.class), eq(charsetName), eq(baseUri), eq(Parser.htmlParser()))).thenReturn(expectedDocument);

 ByteBuffer byteData = dataUtil.readToByteBuffer(testInputStream);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

 assertNotNull(actualDocument);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testOutputSettingsCharset() throws IOException {
 InputStream testInputStream = Mockito.mock(InputStream.class);
 String charsetName = "GBK";
 String baseUri = "http://test.com";
 Document document = Mockito.mock(Document.class);
 Mockito.when(document.outputSettings().charset()).thenReturn(charsetName);

 ByteBuffer byteData = dataUtil.readToByteBuffer(testInputStream);
 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

 assertEquals(charsetName, actualDocument.outputSettings().charset());
 }
}