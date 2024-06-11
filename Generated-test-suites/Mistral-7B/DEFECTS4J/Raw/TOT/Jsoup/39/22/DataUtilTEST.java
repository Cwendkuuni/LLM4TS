import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String HTML_WITH_INVALID_CHARSET = "text/html; charset=x-invalid-charset";

 @BeforeClass
 public static void setup() {
 // Set up some test data
 String htmlData = "<html lang=\"en\"><head>" +
 " <meta charset=\"UTF-8\">" +
 " <meta http-equiv=\"Content-Type\" content=\"" + HTML_CONTENT_TYPE + "\">" +
 "</head>" +
 "<body>" +
 "<p>Test content.</p>" +
 "</body>" +
 "</html>";
 byte[] htmlBytes = htmlData.getBytes(StandardCharsets.UTF_8);
 }

 @Test
 public void testLoadFromFile() throws IOException {
 File testFile = new File("test.html");
 testFile.createNewFile();
 try (FileWriter writer = new FileWriter(testFile)) {
 writer.write(htmlData);
 }
 Document document = DataUtil.load(testFile, "UTF-8", "/");
 assertEquals("Test content.", document.body().text());
 DataUtil.load(testFile, null, "/"); // Should use default charset
 Document documentWithDefaultCharset = DataUtil.load(testFile, null, "/");
 assertEquals("Test content.", documentWithDefaultCharset.body().text());
 testFile.delete();
 }

 @Test
 public void testLoadFromInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(htmlBytes);
 Document document = DataUtil.load(inputStream, "UTF-8", "/");
 assertEquals("Test content.", document.body().text());
 Document documentWithDefaultCharset = DataUtil.load(inputStream, null, "/");
 assertEquals("Test content.", documentWithDefaultCharset.body().text());
 }

 @Test
 public void testLoadWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
 Document document = DataUtil.load(inputStream, HTML_WITH_INVALID_CHARSET, "/");
 assertEquals("Test content.", document.body().text());
 Document documentWithDefaultCharset = DataUtil.load(inputStream, null, "/");
 assertEquals("Test content.", documentWithDefaultCharset.body().text());
 }
}