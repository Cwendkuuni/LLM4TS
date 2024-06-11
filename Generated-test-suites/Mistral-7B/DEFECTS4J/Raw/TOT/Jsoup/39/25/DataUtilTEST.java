import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

 private ByteArrayOutputStream outputStream;
 private InputStream inputStream;
 private Document document;

 @Before
 public void setUp() {
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 }

 /**
 * Test loading a file using the default charset and base URI.
 */
 @Test
 public void testLoadFileDefaultCharsetAndBaseUri() throws IOException {
 String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
 outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
 outputStream.flush();

 document = DataUtil.load(new File("testFile.html"), null, "http://example.com");
 assertNotNull(document);
 assertEquals("Hello, World!", document.body().text());
 }

 /**
 * Test loading a file with an invalid charset name.
 */
 @Test(expected = IllegalCharsetNameException.class)
 public void testLoadFileInvalidCharsetName() throws IOException {
 String fileContent = "<?xml version=\"1.0\" encoding=\"INVALID_CHARSET\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
 outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
 outputStream.flush();

 document = DataUtil.load(new File("testFile.html"), "INVALID_CHARSET", "http://example.com");
 }

 /**
 * Test loading a file using a specified charset.
 */
 @Test
 public void testLoadFileSpecifiedCharset() throws IOException {
 String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>";
 outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
 outputStream.flush();

 document = DataUtil.load(new File("testFile.html"), "ISO-8859-1", "http://example.com");
 assertNotNull(document);
 assertEquals("Hello, World!", document.body().text());
 }

 /**
 * Test loading a file with a base URI.
 */
 @Test
 public void testLoadFileWithBaseUri() throws IOException {
 String htmlContent = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"></head><body><p>Hello, World!</p></body></html>";
 String cssContent = "body { color: red; }";
 outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
 outputStream.write(cssContent.getBytes(StandardCharsets.UTF_8));
 outputStream.write("\n".getBytes(StandardCharsets.UTF_8)); // add newline between files for parsing
 outputStream.flush();

 document = DataUtil.load(new File("index.html"), null, "http://example.com/");
 assertEquals("Hello, World!\n", document.body().text());
 assertEquals("red", document.select("body").attr("style"));
 }

 /**
 * Test loading an input stream with the default charset.
 */
 @Test
 public void testLoadInputStreamDefaultCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byte[] input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>".getBytes(StandardCharsets.UTF_8);
 byteArrayOutputStream.write(input, 0, input.length);
 byteArrayOutputStream.flush();
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 document = DataUtil.load(inputStream, null, "http://example.com");
 assertNotNull(document);
 assertEquals("Hello, World!", document.body().text());
 }

 /**
 * Test loading an input stream with an invalid charset name.
 */
 @Test(expected = IllegalCharsetNameException.class)
 public void testLoadInputStreamInvalidCharsetName() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byte[] input = "<?xml version=\"1.0\" encoding=\"INVALID_CHARSET\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>".getBytes(StandardCharsets.UTF_8);
 byteArrayOutputStream.write(input, 0, input.length);
 byteArrayOutputStream.flush();
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 document = DataUtil.load(inputStream, "INVALID_CHARSET", "http://example.com");
 }

 /**
 * Test loading an input stream with a specified charset.
 */
 @Test
 public void testLoadInputStreamSpecifiedCharset() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byte[] input = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>".getBytes(StandardCharsets.UTF_8);
 byteArrayOutputStream.write(input, 0, input.length);
 byteArrayOutputStream.flush();
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 document = DataUtil.load(inputStream, "ISO-8859-1", "http://example.com");
 assertNotNull(document);
 assertEquals("Hello, World!", document.body().text());
 }

 /**
 * Test loading an input stream with a parser.
 */
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 byte[] input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello, World!</p>\n</body>\n</html>".getBytes(StandardCharsets.UTF_8);
 byteArrayOutputStream.write(input, 0, input.length);
 byteArrayOutputStream.flush();
 InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 document = DataUtil.load(inputStream, null, "http://example.com", DataUtil.Parser.xmlParser());
 assertNotNull(document);
 assertEquals("Hello, World!", document.body().text());
 }
}