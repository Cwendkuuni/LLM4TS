import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

 private static final String FILENAME = "input.html";
 private static final String CHARSET_UTF8 = "UTF-8";
 private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
 private static final String BASE_URI = "http://example.com/";
 private static Document document;

 @BeforeClass
 public static void setup() throws IOException {
 File file = new File(FILENAME);
 document = DataUtil.load(file, CHARSET_UTF8, BASE_URI);
 }

 @Test
 public void testLoadFileWithUTF8Charset() throws IOException {
 File fileUtf8 = new File(FILENAME);
 Document documentUtf8 = DataUtil.load(fileUtf8, CHARSET_UTF8, BASE_URI);
 assertNotNull(documentUtf8);
 assertEquals(document.html(), documentUtf8.html());
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File fileInvalid = new File(FILENAME);
 Document documentInvalid = DataUtil.load(fileInvalid, "INVALID_CHARSET", BASE_URI);
 assertNotNull(documentInvalid);
 }

 @Test
 public void testLoadInputStreamWithUTF8Charset() throws IOException {
 InputStream inputStreamUtf8 = new FileInputStream(new File(FILENAME));
 Document documentUtf8FromInputStream = DataUtil.load(inputStreamUtf8, CHARSET_UTF8, BASE_URI);
 assertNotNull(documentUtf8FromInputStream);
 assertEquals(document.html(), documentUtf8FromInputStream.html());
 inputStreamUtf8.close();
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStreamInvalid = new FileInputStream(new File(FILENAME));
 Document documentInvalidFromInputStream = DataUtil.load(inputStreamInvalid, "INVALID_CHARSET", BASE_URI);
 assertNotNull(documentInvalidFromInputStream);
 inputStreamInvalid.close();
 }

 @Test
 public void testLoadInputStreamWithUTF8CharsetAndParser() throws IOException {
 InputStream inputStreamUtf8 = new FileInputStream(new File(FILENAME));
 Document documentUtf8FromInputStream = DataUtil.load(inputStreamUtf8, CHARSET_UTF8, BASE_URI, Parser.xmlParser());
 assertNotNull(documentUtf8FromInputStream);
 assertEquals(document.html(), documentUtf8FromInputStream.html());
 inputStreamUtf8.close();
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetAndParser() throws IOException {
 InputStream inputStreamInvalid = new FileInputStream(new File(FILENAME));
 Document documentInvalidFromInputStream = DataUtil.load(inputStreamInvalid, "INVALID_CHARSET", BASE_URI, Parser.xmlParser());
 assertNotNull(documentInvalidFromInputStream);
 inputStreamInvalid.close();
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream input = new FileInputStream(new File(FILENAME));
 OutputStream output = new FileOutputStream("output.txt");
 DataUtil.crossStreams(input, output);
 assertEquals(new String(Files.readAllBytes(Paths.get("input.html"))), new String(Files.readAllBytes(Paths.get("output.txt"))));
 input.close();
 output.close();
 }

 @Test
 public void testParseInputStreamWithUTF8Charset() throws IOException {
 InputStream inputStreamUtf8 = new FileInputStream(new File(FILENAME));
 Document documentUtf8FromInputStream = DataUtil.parseInputStream(inputStreamUtf8, CHARSET_UTF8, BASE_URI, Parser.htmlParser());
 assertNotNull(documentUtf8FromInputStream);
 assertEquals(document.html(), documentUtf8FromInputStream.html());
 inputStreamUtf8.close();
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStreamInvalid = new FileInputStream(new File(FILENAME));
 Document documentInvalidFromInputStream = DataUtil.parseInputStream(inputStreamInvalid, "INVALID_CHARSET", BASE_URI, Parser.htmlParser());
 assertNotNull(documentInvalidFromInputStream);
 inputStreamInvalid.close();
 }

 @Test
 public void testParseInputStreamWithUTF8CharsetAndParser() throws IOException {
 InputStream inputStreamUtf8 = new FileInputStream(new File(FILENAME));
 Document documentUtf8FromInputStream = DataUtil.parseInputStream(inputStreamUtf8, CHARSET_UTF8, BASE_URI, Parser.xmlParser());
 assertNotNull(documentUtf8FromInputStream);
 assertEquals(document.html(), documentUtf8FromInputStream.html());
 inputStreamUtf8.close();
 }

 @Test
 public void testParseInputStreamWithInvalidCharsetAndParser() throws IOException {
 InputStream inputStreamInvalid = new FileInputStream(new File(FILENAME));
 Document documentInvalidFromInputStream = DataUtil.parseInputStream(inputStreamInvalid, "INVALID_CHARSET", BASE_URI, Parser.xmlParser());
 assertNotNull(documentInvalidFromInputStream);
 inputStreamInvalid.close();
 }

 @Test
 public void testParseInputStreamWithUTF8CharsetAndBaseUri() throws IOException {
 InputStream inputStreamUtf8 = new FileInputStream(new File(FILENAME));
 Document documentUtf8FromInputStream = DataUtil.parseInputStream(inputStreamUtf8, CHARSET_UTF8, "http://example.com/path/to/file.html", Parser.htmlParser());
 assertNotNull(documentUtf8FromInputStream);
 assertEquals(document.html(), documentUtf8FromInputStream.html());
 inputStreamUtf8.close();
 }

 @Test
 public void testParseInputStreamWithInvalidCharsetAndBaseUri() throws IOException {
 InputStream inputStreamInvalid = new FileInputStream(new File(FILENAME));
 Document documentInvalidFromInputStream = DataUtil.parseInputStream(inputStreamInvalid, "INVALID_CHARSET", "http://example.com/path/to/file.html", Parser.htmlParser());
 assertNotNull(documentInvalidFromInputStream);
 inputStreamInvalid.close();
 }

 @Test
 public void testParseInputStreamWithUTF8CharsetAndParserXml() throws IOException {
 InputStream inputStreamUtf8 = new FileInputStream(new File(FILENAME));
 Document documentUtf8FromInputStream = DataUtil.parseInputStream(inputStreamUtf8, CHARSET_UTF8, BASE_URI, Parser.xmlParser());
 assertNotNull(documentUtf8FromInputStream);
 // Test for XML parser will depend on actual content of the input file
 Element root = documentUtf8FromInputStream.rootElement();
 assertEquals("html", root.tagName());
 inputStreamUtf8.close();
 }

 @Test
 public void testParseInputStreamWithInvalidCharsetAndParserXml() throws IOException {
 InputStream inputStreamInvalid = new FileInputStream(new File(FILENAME));
 Document documentInvalidFromInputStream = DataUtil.parseInputStream(inputStreamInvalid, "INVALID_CHARSET", BASE_URI, Parser.xmlParser());
 assertNotNull(documentInvalidFromInputStream);
 // Test for XML parser will depend on actual content of the input file
 Element root = documentInvalidFromInputStream.rootElement();
 assertEquals("null", root.tagName());
 inputStreamInvalid.close();
 }
}