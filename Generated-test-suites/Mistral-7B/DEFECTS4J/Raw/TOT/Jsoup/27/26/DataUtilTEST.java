import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private Document document;
 private Parser parser;
 private InputStream inputStream;
 private File file;

 @Before
 public void setup() {
 parser = mock(Parser.class);
 document = mock(Document.class);
 inputStream = mock(InputStream.class);
 file = mock(File.class);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File inputFile = new File("input.html");
 when(inputFile.exists()).thenReturn(true);
 when(file.length()).thenReturn(0x20000); // ~130K
 when(inputFile.getAbsoluteFile()).thenReturn(new File("path/to/input.html"));
 when(inputStream.readAllBytes()).thenReturn(new byte[bufferSize]);

 DataUtil.load(inputFile, null, "http://example.com/");

 assertEquals("http://example.com/", document.baseUri());
 verify(parser).parseInput(Mockito.anyString(), Mockito.eq("http://example.com/"));
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File inputFile = new File("input.html");
 when(inputFile.exists()).thenReturn(true);
 when(file.length()).thenReturn(0x20000); // ~130K
 when(inputFile.getAbsoluteFile()).thenReturn(new File("path/to/input.html"));
 when(inputStream.readAllBytes()).thenReturn(new byte[bufferSize]);
 when(parser.parseInput(Mockito.anyString(), Mockito.eq("http://example.com/"))).thenReturn(document);
 when(inputStream.close()).thenReturn(null);
 when(inputFile.getInputStream()).thenReturn(inputStream);

 String charset = "UTF-16";
 DataUtil.load(inputFile, charset, "http://example.com/");

 assertEquals(charset, document.outputSettings().charset());
 verify(parser).parseInput(Mockito.eq(Charset.forName(charset).decode(Mockito.any(ByteBuffer.class)).toString()), Mockito.eq("http://example.com/"));
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullFile() throws IOException {
 DataUtil.load(null, null, "http://example.com/");
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("input.html".getBytes());
 when(parser.parseInput(Mockito.anyString(), Mockito.eq("http://example.com/"))).thenReturn(document);

 DataUtil.load(inputStream, null, "http://example.com/");

 assertEquals("input.html", document.outerHtml());
 verify(parser).parseInput(Mockito.anyString(), Mockito.eq("http://example.com/"));
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("input.html".getBytes());
 when(parser.parseInput(Mockito.anyString(), Mockito.eq("http://example.com/"))).thenReturn(document);
 when(parser.parseInput(Mockito.eq("input.html"), Mockito.eq("http://example.com/"))).thenReturn(document);

 String charset = "UTF-16";
 DataUtil.load(inputStream, charset, "http://example.com/");

 assertEquals("input.html", document.outerHtml());
 verify(parser).parseInput(Mockito.eq(Charset.forName(charset).decode(Mockito.any(ByteBuffer.class)).toString()), Mockito.eq("http://example.com/"));
 }

 @Test
 public void testLoadInputStreamWithNullInputStream() {
 DataUtil.load(null, null, "http://example.com/");
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 String charsetName = null;
 String docData = "input.html";
 document = parser.parseInput(docData, "http://example.com/");

 DataUtil.parseByteData(ByteBuffer.wrap(docData.getBytes()), charsetName, "http://example.com/", parser);

 assertEquals("input.html", document.outerHtml());
 assertEquals("http://example.com/", document.baseUri());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 String charsetName = "UTF-16";
 String docData = "input.html";
 document = parser.parseInput(docData, "http://example.com/");

 DataUtil.parseByteData(ByteBuffer.wrap(docData.getBytes()), charsetName, "http://example.com/", parser);

 assertEquals("input.html", document.outerHtml());
 assertEquals("http://example.com/", document.baseUri());
 assertEquals(charsetName, document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithNullCharsetName() {
 String docData = "input.html";
 document = parser.parseInput(docData, "http://example.com/");

 DataUtil.parseByteData(ByteBuffer.wrap(docData.getBytes()), null, "http://example.com/", parser);

 assertEquals("input.html", document.outerHtml());
 assertEquals("http://example.com/", document.baseUri());
 }

 @Test
 public void testParseByteDataWithCharsetDetectedFromMetaHttpEquivTag() throws IOException {
 String charsetName = null;
 String docData = "input.html";
 document = parser.parseInput(docData, "http://example.com/");
 Document metaDocument = parser.parseInput("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><html><body>content</body></html>", null);
 Element meta = metaDocument.select("meta[http-equiv=content-type]").first();
 String foundCharset = DataUtil.getCharsetFromContentType(meta.attr("content"));

 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 DataUtil.parseByteData(byteData, foundCharset, "http://example.com/", parser);

 assertEquals("input.html", document.outerHtml());
 assertEquals("http://example.com/", document.baseUri());
 assertEquals(foundCharset, document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithCharsetDetectedFromMetaTag() throws IOException {
 String charsetName = null;
 String docData = "input.html";
 document = parser.parseInput(docData, "http://example.com/");
 Document metaDocument = parser.parseInput("<html><head><meta charset=\"gb2312\"></head><body>content</body></html>", null);
 String foundCharset = DataUtil.getCharsetFromContentType(metaDocument.head().attr("charset"));

 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 DataUtil.parseByteData(byteData, foundCharset, "http://example.com/", parser);

 assertEquals("input.html", document.outerHtml());
 assertEquals("http://example.com/", document.baseUri());
 assertEquals(foundCharset, document.outputSettings().charset());
 }

 @Test
 public void testReadToByteBufferWithNullInputStream() {
 DataUtil.readToByteBuffer(null);
 }
}