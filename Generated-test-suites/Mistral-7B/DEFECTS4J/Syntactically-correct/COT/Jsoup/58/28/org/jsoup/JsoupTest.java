import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class JsoupTest {

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><head></head><body><h1>Hello</h1></body></html>";
 String baseUri = "https://example.com";
 Document document = Jsoup.parse(html, baseUri);
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", document.html());
 }

 @Test
 public void testParseWithBaseUriAndParser() {
 String html = "<html><head></head><body><h1>Hello</h1></body></html>";
 String baseUri = "https://example.com";
 Parser parser = new Parser();
 Document document = Jsoup.parse(html, baseUri, parser);
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", document.html());
 }

 @Test
 public void testParseWithoutBaseUri() {
 String html = "<html><head></head><body><h1>Hello</h1></body></html>";
 Document document = Jsoup.parse(html);
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", document.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConnectWithInvalidUrl() {
 Jsoup.connect("file:///nonexistentfile.html");
 }

 @Test
 public void testParseFileWithCharsetName() throws IOException {
 String charsetName = "UTF-8";
 File file = new File("test.html");
 Document document = Jsoup.parse(file, charsetName, "");
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
 }

 @Test
 public void testParseFileWithoutCharsetName() throws IOException {
 File file = new File("test.html");
 Document document = Jsoup.parse(file, "");
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
 }

 @Test(expected = IOException.class)
 public void testParseInputStreamWithInvalidCharsetName() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 String charsetName = "InvalidCharset";
 Document document = Jsoup.parse(inputStream, charsetName, "");
 }

 @Test
 public void testParseInputStreamWithDefaultCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = Jsoup.parse(inputStream, "", "");
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Test</h1>";
 Document document = Jsoup.parseBodyFragment(bodyHtml, "");
 assertNotNull(document);
 assertEquals("<h1>Test</h1>", document.html());
 }

 @Test
 public void testParseWithWhitelist() {
 String bodyHtml = "<h1 id='test' class='test' title='Test Title'>Test</h1>";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("h1");
 Document document = Jsoup.parseBodyFragment(bodyHtml, "");
 assertNotNull(document);
 assertEquals("<h1 id='test' class='test'>Test</h1>", document.html());
 }

 @Test
 public void testCleanWithWhitelist() {
 String bodyHtml = "<h1 id='test' class='test' title='Test Title'>Test<script>alert('Hello')</script></h1>";
 String baseUri = "https://example.com";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("h1");
 Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
 String cleanedHtml = Jsoup.clean(document.body().html(), baseUri, whitelist);
 assertEquals("<h1 id='test' class='test'>Test</h1>", cleanedHtml);
 }

 @Test
 public void testIsValidWithWhitelist() {
 String bodyHtml = "<h1 id='test' class='test' title='Test Title'>Test<script>alert('Hello')</script></h1>";
 Whitelist whitelist = new Whitelist();
 whitelist.addTags("h1");
 assertTrue(Jsoup.isValid(bodyHtml, whitelist));
 }

 @Test
 public void testIsValidWithInvalidHtml() {
 String bodyHtml = "<script>alert('Hello')</script>";
 Whitelist whitelist = new Whitelist();
 assertFalse(Jsoup.isValid(bodyHtml, whitelist));
 }

}