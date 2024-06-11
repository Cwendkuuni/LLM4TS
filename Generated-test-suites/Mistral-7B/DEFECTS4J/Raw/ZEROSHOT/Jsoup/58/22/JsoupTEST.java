import static org.junit.Assert.assertNotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

 @Test
 public void testParseStringWithBaseUri() {
 String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 Element div = doc.selectFirst("#content");
 assertEquals("Hello World", div.text());
 }

 @Test
 public void testParseStringWithNullBaseUri() {
 String html = "<html><head><base href=\"/test\"></head><body><a href=\"/about\">About</a></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 String href = doc.selectFirst("a").attr("href");
 assertEquals("/about", href);
 }

 @Test
 public void testParseString() {
 String html = "<html><head><title>Test</title></head><body><div id=\"content\">Hello World</div></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 Element div = doc.selectFirst("#content");
 assertEquals("Hello World", div.text());
 }

 @Test
 public void testConnect() throws IOException {
 String url = "https://www.google.com";
 Document doc = Jsoup.connect(url).get();
 assertNotNull(doc);
 assertEquals("Google", doc.title().text());
 }

 @Test
 public void testParseFileWithCharsetAndBaseUri() throws IOException {
 String filePath = "src/test/resources/JsoupTest.html";
 Document doc = Jsoup.parse(new File(filePath), "UTF-8", "http://example.com");
 assertNotNull(doc);
 assertEquals("Jsoup Test", doc.title().text());
 }

 @Test
 public void testParseFileWithBaseUri() throws IOException {
 String filePath = "src/test/resources/JsoupTest.html";
 Document doc = Jsoup.parse(new File(filePath), null, null);
 assertNotNull(doc);
 assertEquals("Jsoup Test", doc.title().text());
 }

 @Test
 public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
 String className = getClass().getName();
 String filePath = "src/test/resources/JsoupTest.html";
 InputStream in = Jsoup.class.getResourceAsStream(filePath);
 Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
 assertNotNull(doc);
 assertEquals("Jsoup Test", doc.title().text());
 in.close();
 }

 @Test
 public void testParseInputStreamWithBaseUri() throws IOException {
 String className = getClass().getName();
 String filePath = "src/test/resources/JsoupTest.html";
 InputStream in = Jsoup.class.getResourceAsStream(filePath);
 Document doc = Jsoup.parse(in, null, null);
 assertNotNull(doc);
 assertEquals("Jsoup Test", doc.title().text());
 in.close();
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div id=\"content\">Hello World</div>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 assertNotNull(doc);
 Element div = doc.selectFirst("#content");
 assertEquals("Hello World", div.text());
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String bodyHtml = "<a href=\"/about\">About</a>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
 assertFalse(doc.selectFirst("#content") instanceof Element);
 String href = doc.selectFirst("a").attr("href");
 assertEquals("/about", href);
 }

 @Test
 public void testParseStringWithWhitelist() {
 String html = "<html><head><title>Test</title></head><body><div id=\"content\"><h1>Hello <b>World</b></h1></div></body></html>";
 Whitelist whitelist = new Whitelist(new TagName("div"), new TagName("h1"));
 Document doc = Jsoup.parseBodyFragment(html, "");
 Cleaner cleaner = new Cleaner(whitelist);
 Document clean = cleaner.clean(doc);
 assertNotNull(clean);
 assertEquals(2, clean.numberOfElements());
 assertTrue(clean.select("div").size() > 0);
 assertFalse(clean.select("p").size() > 0);
 assertNull(clean.select("script").first());
 }

 @Test
 public void testIsValid() {
 String html = "<html><head></head><body><script>alert('Hello World');</script></body></html>";
 Whitelist whitelist = new Whitelist(Tag.SCRIPT);
 assertFalse(Jsoup.isValid(html, whitelist));
 }

 @Test
 public void testClean() {
 String html = "<html><head></head><body><script>alert('Hello World');</script></body></html>";
 String baseUri = "http://example.com";
 Whitelist whitelist = new Whitelist(new TagName("div"));
 Document dirty = Jsoup.parseBodyFragment(html, baseUri);
 Cleaner cleaner = new Cleaner(whitelist);
 Document clean = cleaner.clean(dirty);
 assertEquals("", clean.select("script").first().text());
 }
}