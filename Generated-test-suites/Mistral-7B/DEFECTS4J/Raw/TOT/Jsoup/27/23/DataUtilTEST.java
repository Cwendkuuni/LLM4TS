import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

 private static Document doc;
 private static Parser parser;

 @BeforeClass
 public static void setup() {
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 DocUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithNullCharset() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = null;
 String baseUri = "http://test.com";
 Document doc = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 // Since the default charset is UTF-8, the document should still be loaded and parsed correctly
 }

 @Test
 public void testLoadFileWithInvalidCharsetDetectedFromMetaTag() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = null;
 String baseUri = "http://test.com";
 InputStream stream = new ByteArrayInputStream("" +
         "<html>\n" +
         "<head>\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\n" +
         "</head>\n" +
         "<body>\n" +
         "</body>\n".getBytes());
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 try (InputStream in = inputFile.length() > 0 ? new FileInputStream(inputFile) : stream) {
 ByteBuffer byteData = DataUtil.readToByteBuffer(in);
 doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 }
 assertNotNull(doc);
 String charsetDetected = doc.select("meta[http-equiv=content-type], meta[charset]").first().attr("charset");
 assertEquals("GBK", charsetDetected);
 }

 @Test
 public void testLoadFileWithValidCharsetSpecifiedInContentTypeHeader() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = "GBK";
 String baseUri = "http://test.com";
 Document doc = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithIOException() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 InputStream stream = Mockito.mock(InputStream.class);
 Mockito.when(stream.available()).thenReturn(-1);
 DocUtil.load(inputFile, charsetName, baseUri, stream);
 }

 @Test
 public void testParseByteDataWithValidCharsetDetectedFromMetaTag() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 String charsetName = null;
 String baseUri = "http://test.com";
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 String charsetDetected = doc.select("meta[http-equiv=content-type], meta[charset]").first().attr("charset");
 // Since there is no meta tag or charset specified in the test data, the default charset (UTF-8) will be used
 }

 @Test
 public void testParseByteDataWithValidCharsetSpecifiedInContentTypeHeader() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithIOException() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 try {
 DocUtil.parseByteData(byteData, charsetName, baseUri, parser);
 } catch (IOException e) {
 // expected
 }
 }
}