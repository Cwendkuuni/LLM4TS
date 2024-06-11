import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

 private static ByteArrayInputStream byteArrayInputStream;
 private static Document document;

 @BeforeClass
 public static void setUp() {
 String html = " <!DOCTYPE html>\n" +
         "<html xmlns:og=\"http://ogp.org/ns/\">\n" +
         "<head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
         "</head>\n" +
         "<body>\n" +
         "</body>\n" +
         "</html>";
 byteArrayInputStream = new ByteArrayInputStream(html.getBytes());
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), "UTF-8", "/test/");
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 Document doc = DataUtil.load(new File("test.html"), null, "/test/");
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 Document doc = DataUtil.load(byteArrayInputStream, "UTF-8", "/test/", Parser.htmlParser());
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 Document doc = DataUtil.load(byteArrayInputStream, null, "/test/", Parser.htmlParser());
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 Document doc = DataUtil.load(byteArrayInputStream, null, "/test/", Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 DataUtil.load(new File("test.html"), "invalidCharset", "/test/");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 DataUtil.load(byteArrayInputStream, "invalidCharset", "/test/", Parser.htmlParser());
 }

 @Test
 public void testParseByteData() throws IOException {
 String html = " <!DOCTYPE html>\n" +
         "<html xmlns:og=\"http://ogp.org/ns/\">\n" +
         "<head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
         "</head>\n" +
         "<body>\n" +
         "</body>\n" +
         "</html>";
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(html.getBytes()));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", "/test/", Parser.htmlParser());
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(byteArrayInputStream);
 try {
 Document doc = DataUtil.parseByteData(byteData, "invalidCharset", "/test/", Parser.htmlParser());
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }
 }

}