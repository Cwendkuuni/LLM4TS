import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JsoupTest {

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"/about\"></a></body></html>";
 Document document = Jsoup.parse(html, "http://example.com");
 String expectedLinkHref = "http://example.com/about";
 assertEquals(expectedLinkHref, document.select("a[href]").attr("abs:href"));
 }

 @Test
 public void testParseWithCustomParser() {
 String html = "<xml><data>Hello World</data></xml>";
 Document document = Jsoup.parse(html, "http://example.com", Parser.xmlParser());
 String expectedData = "Hello World";
 assertEquals(expectedData, document.select("data").text());
 }

 @Test
 public void testParseWithoutBaseUri() {
 String html = "<html><head><base href=\"/\"></base></head><body><a href=\"/about\"></a></body></html>";
 Document document = Jsoup.parse(html);
 String expectedLinkHref = "/about";
 assertEquals(expectedLinkHref, document.select("a[href]").attr("abs:href"));
 }

 @Test
 public void testConnectAndParse() throws IOException {
 String url = "http://example.com";
 Document document = Jsoup.connect(url).get();
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testConnectAndParseWithMalformedUrl() {
 String url = "file:/example.com";
 Jsoup.connect(url).get();
 }

 @Test(expected = IOException.class)
 public void testConnectAndParseWithTimeout() throws IOException {
 String url = "http://slow-responding-site.com";
 Document document = Jsoup.connect(url).timeout(500).get();
 assertNotNull(document);
 }

 @Test
 public void testParseFile() throws IOException {
 String charsetName = "UTF-8";
 String filePath = "test.html";
 Document document = Jsoup.parse(new File(filePath), charsetName);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testParseFileWithInvalidCharset() throws IOException {
 String charsetName = "InvalidCharset";
 String filePath = "test.html";
 Jsoup.parse(new File(filePath), charsetName);
 }

 @Test
 public void testParseInputstream() throws IOException {
 String charsetName = "UTF-8";
 String filePath = "test.html";
 InputStream inputStream = new FileInputStream(new File(filePath));
 Document document = Jsoup.parse(inputStream, charsetName);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testParseInputstreamWithInvalidCharset() throws IOException {
 String charsetName = "InvalidCharset";
 String filePath = "test.html";
 InputStream inputStream = new FileInputStream(new File(filePath));
 Jsoup.parse(inputStream, charsetName);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World</p>";
 Document document = Jsoup.parseBodyFragment(bodyHtml);
 String expectedText = "Hello World";
 assertEquals(expectedText, document.text());
 }

 @Test
 public void testClean() {
 String bodyHtml = "<script src=\"malicious.js\"></script>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.relaxed();
 Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
 String expectedCleanHtml = "<p></p>";
 assertEquals(expectedCleanHtml, Jsoup.clean(document.body().html(), baseUri, whitelist));
 }

 @Test
 public void testCleanWithOutputSettings() {
 String bodyHtml = "<script src=\"malicious.js\"></script>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.relaxed();
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.prettyPrint(true);
 Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
 String expectedCleanHtml = "<p></p>";
 assertEquals(expectedCleanHtml, Jsoup.clean(document.body().html(), baseUri, whitelist, outputSettings));
 }

 @Test
 public void testIsValid() {
 String bodyHtml = "<p><script src=\"malicious.js\"></script></p>";
 Whitelist whitelist = Whitelist.relaxed();
 boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
 assertFalse(isValid);
 }
}