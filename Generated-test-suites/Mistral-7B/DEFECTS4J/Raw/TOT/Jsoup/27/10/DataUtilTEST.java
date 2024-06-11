import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private File testFile;
 private InputStream testInputStream;
 private String charsetName;
 private String baseUri;

 @Before
 public void setUp() throws Exception {
 testFile = new File("path/to/test/file.html");
 testInputStream = new FileInputStream(testFile);
 charsetName = "UTF-8";
 baseUri = "http://example.com";
 }

 @Test
 public void testLoadFile() throws IOException {
 Document document = DataUtil.load(testFile, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadStreamWithDefaultCharset() throws IOException {
 Document document = DataUtil.load(testInputStream, null, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadStreamWithSpecifiedCharset() throws IOException {
 charsetName = "UTF-16";
 Document document = DataUtil.load(testInputStream, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadStreamWithParser() throws IOException {
 Parser parser = DataUtil.parser().xmlParser();
 Document document = DataUtil.load(testInputStream, charsetName, baseUri, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[]{"<html><head></head><body></body></html>"
         .getBytes("UTF-8")});
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[]{"<html><head></head><body></body></html>"
         .getBytes("ISO-8859-1")});
 Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), "ISO-8859-1", baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 File invalidFile = new File("path/to/invalid/file");
 Document document = DataUtil.load(invalidFile, charsetName, baseUri);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testLoadStreamWithNullInputStream() throws IOException {
 Document document = DataUtil.load(null, charsetName, baseUri);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testLoadStreamWithNullCharset() throws IOException {
 Document document = DataUtil.load(testInputStream, null, baseUri);
 fail("Expected IOException");
 }

 @Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullByteData() {
 Document document = DataUtil.parseByteData(null, charsetName, baseUri, Parser.htmlParser());
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullCharsetName() {
 Document document = DataUtil.parseByteData(readToByteBuffer(testInputStream), null, baseUri, Parser.htmlParser());
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullBaseUri() {
 Document document = DataUtil.parseByteData(readToByteBuffer(testInputStream), charsetName, null, Parser.htmlParser());
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testParseByteDataWithNullParser() {
 Document document = DataUtil.parseByteData(readToByteBuffer(testInputStream), charsetName, baseUri, null);
 fail("Expected NullPointerException");
 }
}