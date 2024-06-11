import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

 private static final String XML_SAMPLE = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
 private static final String HTML_SAMPLE = "<html><body></body></html>";
 private static Document xmlDoc;
 private static Document htmlDoc;

 @BeforeClass
 public static void setup() throws IOException {
 xmlDoc = DataUtil.parseString(XML_SAMPLE, "UTF-8", null);
 htmlDoc = DataUtil.parseString(HTML_SAMPLE, null, null);
 }

 @Test
 public void testLoadFileWithUtf8Charset() throws IOException {
 File sampleFile = new File("src/test/resources/testFile.html");
 Document doc = DataUtil.load(sampleFile, "UTF-8", null);
 assertEquals("Content of the test file.", doc.text(), DataUtil.parseString(DataUtil.readFileToString(sampleFile), "UTF-8").text());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 File sampleFile = new File("src/test/resources/testFile.html");
 Document doc = null;
 try {
 doc = DataUtil.load(sampleFile, "invalidCharset", null);
 } catch (IOException e) {
 // expected
 }
 assertNotNull("No IOException thrown.", e);
 }

 @Test
 public void testLoadInputStreamWithUtf8Charset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertEquals("Content of the XML sample.", doc.text(), XML_SAMPLE);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null);
 assertEquals("Content of the XML sample.", doc.text(), XML_SAMPLE);
 }

 @Test
 public void testLoadInputStreamWithHtmlParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(HTML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
 assertEquals("Content of the HTML sample.", doc.text(), HTML_SAMPLE);
 }

 @Test
 public void testLoadInputStreamWithXmlParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals("Content of the XML sample.", doc.text(), XML_SAMPLE);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 byte[] expectedOutput = XML_SAMPLE.getBytes(Charset.forName("UTF-8"));
 byte[] actualOutput = outputStream.toByteArray();
 assertArrayEquals("Cross streams output does not match expected output.", expectedOutput, actualOutput);
 }

 @Test
 public void testParseInputStreamWithUtf8Charset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 assertEquals("Content of the XML sample.", doc.text(), XML_SAMPLE);
 }

 @Test
 public void testParseInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 assertEquals("Content of the XML sample.", doc.text(), XML_SAMPLE);
 }

 @Test
 public void testParseInputStreamWithCustomParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 Document doc = DataUtil.parseInputStream(inputStream, null, null, new Parser() {
 @Override public Document parseInput(String input, String baseUri) {
 throw new UnsupportedOperationException("Not supported.");
 }

 @Override public Document parseInput(String input, String baseUri, Parser.ParserName parserName) {
 throw new UnsupportedOperationException("Not supported.");
 }

 @Override public Document parseInput(InputStream input, String charsetName, String baseUri) throws IOException {
 Validate.notNull(input, "input cannot be null.");
 Validate.notNull(charsetName, "charsetName cannot be null.");
 Document doc = new Document();
 doc.outputSettings().charset(Charset.forName(charsetName));
 return doc;
 }

 @Override public Parser parserName() {
 throw new UnsupportedOperationException("Not supported.");
 }
 });
 assertEquals("Content of the XML sample.", doc.text(), XML_SAMPLE);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(XML_SAMPLE.getBytes(Charset.forName("UTF-8")));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, XML_SAMPLE.length());
 byte[] expectedByteData = XML_SAMPLE.getBytes(Charset.forName("UTF-8"));
 byte[] actualByteData = new byte[byteData.limit()];
 byteData.get(actualByteData);
 assertArrayEquals("ReadToByteBuffer output does not match expected output.", expectedByteData, actualByteData);
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 assertEquals("Expected empty byte buffer.", byteData.position(), 0);
 assertEquals("Expected empty byte buffer.", byteData.limit(), 0);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=gb2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected 'gb2312'.", charset, "gb2312");
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidCharset() {
 String contentType = "text/html; charset=invalidCharset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull("Expected null.", charset);
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertEquals("Expected 'UTF-8'.", charset, validatedCharset);
 }

 @Test
 public void testValidateCharsetWithInvalidCharset() {
 String charset = "invalidCharset";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertNull("Expected null.", validatedCharset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals("Expected boundary length.", boundary.length(), DataUtil.boundaryLength);
 }
}