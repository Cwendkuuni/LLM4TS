import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
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
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

 private Document htmlDocument;
 private Document xmlDocument;
 private InputStream inputStream;
 private File file;

 @Before
 public void setup() {
 htmlDocument = DataUtil.parseByteData(ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>'}), "UTF-8", null, Parser.htmlParser());
 xmlDocument = DataUtil.parseByteData(ByteBuffer.wrap(new byte[]{'<', 'g', 'o', 'd', ' ', 'p', 'r', 'o', 'p', 'e', 'r', ' ', 'G', 'D', 'S', 'S', '>', '\n', '<', '/', 'g', 'o', 'd', ' ', 'p', 'r', 'o', 'p', 'e', 'r', '>', '\n'}), "UTF-8", null, Parser.xmlParser());
 inputStream = mock(InputStream.class);
 file = Mockito.mock(File.class);
 }

 @Test
 public void testLoadHtmlFile() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.load(file, "UTF-8", null);
 assertEquals(htmlDocument, doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithNullCharset() throws IOException {
 Document doc = DataUtil.load(file, null, null);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 Document doc = DataUtil.load(file, "invalidCharset", null);
 }

 @Test
 public void testLoadHtmlInputStream() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(htmlDocument.html().getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertEquals(htmlDocument, doc);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullCharset() throws IOException {
 Document doc = DataUtil.load(inputStream, null);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 Document doc = DataUtil.load(inputStream, "invalidCharset");
 }

 @Test
 public void testLoadXmlInputStream() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(xmlDocument.html().getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
 assertEquals(xmlDocument, doc);
 }

 @Test
 public void testDetectCharsetFromHtmlHead() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 InputStream inputStream = new ByteArrayInputStream(contentType.getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromHtmlMetaTag() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 String metaTag = "meta charset=\"gb2312\"";
 InputStream inputStream = new ByteArrayInputStream((contentType + "\n<html>\n<head>\n<" + metaTag + ">\n</head>\n</html>").getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals("gb2312", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromHtmlMetaTagWithInvalidCharset() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 String metaTag = "meta charset=\"INVALID\"";
 InputStream inputStream = new ByteArrayInputStream((contentType + "\n<html>\n<head>\n<" + metaTag + ">\n</head>\n</html>").getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromHtmlMetaTagWithNoCharset() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 String metaTag = "meta charset=";
 InputStream inputStream = new ByteArrayInputStream((contentType + "\n<html>\n<head>\n<" + metaTag + ">\n</head>\n</html>").getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromHtmlMetaTagWithMixedContentType() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 String metaTag = "meta http-equiv=\"content-type\" content=\"text/html; charset=gb2312\"";
 InputStream inputStream = new ByteArrayInputStream((contentType + "\n<html>\n<head>\n<" + metaTag + ">\n</head>\n</html>").getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals("gb2312", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromHtmlMetaTagWithUTF8AsDefault() throws IOException {
 String contentType = "text/html";
 String metaTag = "meta charset=\"gb2312\"";
 InputStream inputStream = new ByteArrayInputStream((contentType + "\n<html>\n<head>\n<" + metaTag + ">\n</head>\n</html>").getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromXmlHeader() throws IOException {
 String contentType = "application/xml; charset=UTF-8";
 InputStream inputStream = new ByteArrayInputStream(contentType.getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromXmlHeaderWithInvalidCharset() throws IOException {
 String contentType = "application/xml; charset=UTF-8";
 String xmlHeader = "<?xml version=\"1.0\" encoding=\"INVALID\"?>";
 InputStream inputStream = new ByteArrayInputStream((contentType + xmlHeader).getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromXmlHeaderWithNoCharset() throws IOException {
 String contentType = "application/xml; charset=UTF-8";
 String xmlHeader = "<?xml version=\"1.0\"?>";
 InputStream inputStream = new ByteArrayInputStream((contentType + xmlHeader).getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromXmlHeaderWithUTF8AsDefault() throws IOException {
 String contentType = "application/xml";
 String xmlHeader = "<?xml version=\"1.0\" encoding=\"gb2312\"?>";
 InputStream inputStream = new ByteArrayInputStream((contentType + xmlHeader).getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals("UTF-8", doc.outputSettings().charset());
 }
}