import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

 private static byte[] htmlFileContent;

 @BeforeClass
 public static void init() {
 // HTML file content for testing
 htmlFileContent = new byte[] {
 // HTML content here...
 };
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("testFile.html");
 Document doc = DataUtil.load(file, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = new File("testFile.html");
 Document doc = DataUtil.load(file, null, null);
 assertNotNull(doc); // default charset should be used
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 Document doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc); // default charset should be used
 }

 @Test
 public void testLoadInputStreamWithWrongCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 Document doc = DataUtil.load(inputStream, "GBK", null);
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithMetaCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 String metaCharset = "GBK";
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertEquals(metaCharset, DataUtil.getCharsetFromContentType(doc.head().metaElementSibling(0).attr("content")));
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, "GBK", null, Parser.htmlParser());
 assertNull(doc);
 }

 @Test
 public void testParseByteDataWithUnknownMetaCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlFileContent);
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertEquals("UTF-8", doc.outputSettings().charset());
 }
}