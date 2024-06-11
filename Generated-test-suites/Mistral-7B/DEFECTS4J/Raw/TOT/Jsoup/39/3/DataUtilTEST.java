import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {

 private InputStream inputStream;
 private File file;

 @Before
 public void setup() {
 try {
 inputStream = getClass().getResourceAsStream("/test.html");
 file = new File("test.html");
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFromFile() throws IOException {
 Document doc = DataUtil.load(file, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test content".getBytes());
 Document doc = DataUtil.load(byteArrayInputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test content".getBytes("UTF-16LE"));
 Document doc = DataUtil.load(byteArrayInputStream, "UTF-16LE", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFromInputStreamWithParser() throws IOException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test content".getBytes());
 Document doc = DataUtil.load(byteArrayInputStream, null, null, Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithMetaTag() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 String metaCharset = meta != null ? meta.attr("charset") : null;
 String expectedCharset = "UTF-16LE";
 assertEquals(expectedCharset, metaCharset);
 }

 @Test
 public void testParseByteDataWithInvalidCharsetName() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 Document doc = DataUtil.parseByteData(byteData, "InvalidCharset", null, Parser.htmlParser());
 assertNull(doc);
 }
}