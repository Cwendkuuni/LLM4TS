import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

 private static Document doc = null;

 @BeforeClass
 public static void setUp() {
 try {
 File file = new File("path/to/your/testfile.html");
 doc = DataUtil.load(file, "UTF-8", null);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFileWithValidCharset() {
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithNullCharset() {
 try {
 Document doc = DataUtil.load(new File("path/to/your/testfile.html"), null, null);
 assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFileWithInvalidCharset() {
 File file = new File("path/to/your/testfile.html");
 try {
 Document doc = DataUtil.load(file, "InvalidCharset", null);
 assertNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 ByteArrayResource resource = new ByteArrayResource(new byte[]{"<html><body></body></html>"});
 InputStream inputStream = resource.getInputStream();
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayResource resource = new ByteArrayResource(new byte[]{"<html><body></body></html>"});
 InputStream inputStream = resource.getInputStream();
 Document doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 ByteArrayResource resource = new ByteArrayResource(new byte[]{"<html><body></body></html>"});
 InputStream inputStream = resource.getInputStream();
 Document doc = DataUtil.load(inputStream, "InvalidCharset", null);
 assertNull(doc);
 }

 @Test
 public void testParseByteDataWithValidCharset() {
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>', '\n', '<', 'b', 'o', 'd', 'y', '>', '\n', '</', 'b', 'o', 'd', 'y', '>', '\n', '</', 'h', 't', 'm', 'l', '>'}), "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>', '\n', '<', 'b', 'o', 'd', 'y', '>', '\n', '</', 'b', 'o', 'd', 'y', '>', '\n', '</', 'h', 't', 'm', 'l', '>'}), null, null, Parser.htmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 Document doc = DataUtil.parseByteData(ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l', '>', '\n', '<', 'b', 'o', 'd', 'y', '>', '\n', '</', 'b', 'o', 'd', 'y', '>', '\n', '</', 'h', 't', 'm', 'l', '>'}), "InvalidCharset", null, Parser.htmlParser());
 assertNull(doc);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }
}