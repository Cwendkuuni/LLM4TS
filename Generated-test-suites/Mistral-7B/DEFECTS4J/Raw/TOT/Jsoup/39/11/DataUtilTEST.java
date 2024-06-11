import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 try {
 doc = DataUtil.load(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), "UTF-8", null);
 } catch (IOException e) {
 // Test should not throw an IOException.
 }
 }

 @Test
 public void testLoadFileWithUtf8Charset() throws IOException {
 File inputFile = new File("inputFileWithUtf8Charset.html");
 Document result = DataUtil.load(inputFile, "UTF-8", null);
 assertEquals(doc, result);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 File inputFile = new File("inputFileWithInvalidCharset.html");
 Document result = DataUtil.load(inputFile, "UnknownCharset", null);
 fail("Expected IOException but got " + result);
 }

 @Test
 public void testLoadInputStreamWithUtf8Charset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 Document result = DataUtil.load(inputStream, "UTF-8", null);
 assertEquals(doc, result);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 Document result = DataUtil.load(inputStream, null, null);
 fail("Expected IOException but got " + result);
 }

 @Test
 public void testLoadInputStreamWithHtmlParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 Document result = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
 assertEquals(doc, result);
 }

 @Test
 public void testLoadInputStreamWithXmlParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 Document result = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
 fail("Expected IllegalArgumentException but got " + result);
 }

 @Test
 public void testParseByteDataWithUtf8Charset() {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document result = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertEquals(doc, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataWithInvalidCharset() {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document result = DataUtil.parseByteData(byteData, "InvalidCharset", null, Parser.htmlParser());
 fail("ExpectedIllegalArgumentException but got " + result);
 }

}