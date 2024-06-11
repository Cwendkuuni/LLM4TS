import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class JsoupTest {

 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 doc = Jsoup.parse(html);
 }

 @Test
 public void testParseStringWithBaseUri() {
 String html = "<html><a href=\"relative.html\"></a><base href=\"http://example.com\"></base><body><h1>Hello World</h1></body></html>";
 Document parsed = Jsoup.parse(html, "http://example.com");
 assertEquals("http://example.com/relative.html", parsed.select("a[href]").first().absUrl());
 }

 @Test
 public void testParseStringWithParser() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Parser parser = org.jsoup.parser.Jsoup.parser();
 Document parsed = Jsoup.parse(html, "", parser);
 assertEquals("Hello World", parsed.select("h1").text());
 }

 @Test
 public void testParseStringWithoutBaseUri() {
 String html = "<html><base href=\"http://example.com\"><a href=\"relative.html\"></a><body><h1>Hello World</h1></body></html>";
 Document parsed = Jsoup.parse(html);
 assertEquals("http://example.com/relative.html", parsed.select("a[href]").first().absUrl());
 }

 @Test
 public void testConnectAndGet() throws Exception {
 Document docFromUrl = Jsoup.connect("https://www.google.com").get();
 assertEquals("Google", docFromUrl.title());
 }

 @Test(expected = IOException.class)
 public void testConnectWithInvalidUrl() throws Exception {
 Jsoup.connect("file:///non-existent-file.html");
 }

 @Test(expected = IOException.class)
 public void testConnectWithInvalidProtocol() throws Exception {
 Jsoup.connect("ftp://example.com");
 }

 @Test
 public void testParseFile() throws IOException {
 File inputFile = new File("test.html");
 Document docFromFile = Jsoup.parse(inputFile, "UTF-8");
 assertEquals("Test Page", docFromFile.title());
 }

 @Test(expected = IOException.class)
 public void testParseFileWithInvalidCharset() throws IOException {
 Document docFromFile = Jsoup.parse(new File("test.html"), "invalid-charset");
 fail(); // This test fails if the exception is not thrown
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document docFromInputStream = Jsoup.parse(inputStream, "UTF-8");
 assertEquals("Test Page", docFromInputStream.title());
 inputStream.close();
 }

 @Test(expected = IOException.class)
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document docFromInputStream = Jsoup.parse(inputStream, "invalid-charset");
 fail(); // This test fails if the exception is not thrown
 inputStream.close();
 }

 @Test
 public void testClean() {
 String untrustedHtml = "<script>document.write('XSS')</script>";
 String safeHtml = Jsoup.clean(untrustedHtml, Whitelist.simpleText());
 assertNull(safeHtml.matches("^<[sS]cript>.*document\\.write\\(.*XSS.*\\).*$"));
 }

 @Test
 public void testIsValid() {
 String validHtml = "<p>Hello <strong>World</strong></p>";
 assertTrue(Jsoup.isValid(validHtml, Whitelist.simpleText()));
 }

 @Test
 public void testIsInvalid() {
 String invalidHtml = "<script>document.write('XSS')</script>";
 assertFalse(Jsoup.isValid(invalidHtml, Whitelist.simpleText()));
 }
}