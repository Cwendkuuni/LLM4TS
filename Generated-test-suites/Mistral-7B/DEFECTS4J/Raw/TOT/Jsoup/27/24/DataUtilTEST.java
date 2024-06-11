import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DataUtilTest {

 private static Document document;

 @BeforeClass
 public static void setup() {
 try {
 // Test loading from file
 File file = new File("path/to/your/testfile.html");
 document = DataUtil.load(file, "UTF-8", "");
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLoadFromFile() {
 // Test with valid file and charset
 assertNotNull(document);
 // Test with null charset
 document = DataUtil.load(new File("path/to/your/testfile.html"), null, "");
 assertNotNull(document);
 // Test with invalid file
 File invalidFile = new File("path/to/your/invalidfile");
 Document invalidDocument = null;
 try {
 invalidDocument = DataUtil.load(invalidFile, "UTF-8", "");
 } catch (IOException e) {
 // Expected
 }
 assertNull(invalidDocument);
 }

 @Test
 public void testLoadFromInputStream() throws IOException {
 // Test loading from InputStream with valid charset
 InputStream inputStream = this.getClass().getResourceAsStream("/path/to/your/testfile.html");
 document = DataUtil.load(inputStream, "UTF-8", "");
 assertNotNull(document);
 // Test loading from InputStream with null charset
 document = DataUtil.load(inputStream, null, "");
 assertNotNull(document);
 // Test loading from InputStream with invalid charset
 InputStream invalidInputStream = this.getClass().getResourceAsStream("/path/to/your/invalidfile.html");
 Document invalidDocument = null;
 try {
 invalidDocument = DataUtil.load(invalidInputStream, "UTF-8", "");
 } catch (IOException e) {
 // Expected
 }
 assertNull(invalidDocument);
 }

 @Test
 public void testParseByteData() {
 // Test parsing valid byte data
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l'});
 document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
 assertNotNull(document);
 // Test parsing invalid byte data
 ByteBuffer invalidByteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', 0xEE});
 Document invalidDocument = null;
 try {
 invalidDocument = DataUtil.parseByteData(invalidByteData, "UTF-8", "", Parser.htmlParser());
 } catch (IOException e) {
 // Expected
 }
 assertNull(invalidDocument);
 }

 @Test
 public void testDetermineCharset() {
 // Test determining charset from meta tag
 String contentType = "text/html; charset=gb2312";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("gb2312", charset);
 // Test determining charset from invalid meta tag
 contentType = "text/html; charset=gb2312; someothertag=value";
 charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 // Test determining charset from content type header
 contentType = "text/html; charset=UTF-8";
 charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 // Test determining charset from null content type
 charset = DataUtil.getCharsetFromContentType(null);
 assertNull(charset);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 // Test reading valid data
 InputStream inputStream = this.getClass().getResourceAsStream("/path/to/your/testfile.html");
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 // Test reading empty data
 inputStream = new ByteArrayInputStream(new byte[0]);
 byteData = DataUtil.readToByteBuffer(inputStream);
 // Test reading invalid data
 inputStream = this.getClass().getResourceAsStream("/path/to/your/invalidfile.html");
 try {
 byteData = DataUtil.readToByteBuffer(inputStream);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }
}