package org.jsoup.test;

 import org.jsoup.Jsoup;
 import org.jsoup.nodes.Document;
 import org.jsoup.nodes.Element;
 import org.jsoup.parser.Parser;
 import org.jsoup.safety.Whitelist;
 import org.junit.Assert;
 import org.junit.Test;

 public class JsoupTest {

 @Test
 public void testParseHtmlWithBaseUri() {
 String html = "<html><head><base href=\"http://test.com/\"></head><body><a href=\"about.html\">About</a></body></html>";
 Document document = Jsoup.parse(html, "http://test.com");
 Element link = document.selectFirst("a");
 Assert.assertEquals("About", link.text());
 Assert.assertEquals("http://test.com/about.html", link.attr("abs:href"));
 }

 @Test
 public void testParseHtmlWithCustomParser() {
 Parser parser = new MyCustomParser(); // implement Parser interface
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document document = Jsoup.parse(html, "", parser);
 Element title = document.title();
 Assert.assertEquals("Hello World", title.text());
 }

 @Test
 public void testParseHtmlWithoutBaseUri() {
 String html = "<html><head><base href=\"/\"></head><body><a href=\"about.html\">About</a></body></html>";
 Document document = Jsoup.parse(html);
 Element link = document.selectFirst("a");
 Assert.assertEquals("About", link.text());
 Assert.assertEquals("/about.html", link.attr("abs:href"));
 }

 @Test
 public void testConnectToUrl() {
 String url = "http://example.com";
 Document document = Jsoup.connect(url).get();
 Element title = document.title();
 Assert.assertNotNull(title);
 Assert.assertEquals("Example Domain", title.text());
 }

 @Test
 public void testParseFile() throws IOException {
 String fileName = "test.html";
 File file = new File(fileName);
 Document document = Jsoup.parse(file, "UTF-8");
 Element title = document.title();
 Assert.assertEquals("Test File Title", title.text());
 }

 @Test
 public void testParseInputStream() throws IOException {
 String fileName = "test.html";
 File file = new File(fileName);
 InputStream inputStream = new FileInputStream(file);
 Document document = Jsoup.parse(inputStream, "UTF-8");
 Element title = document.title();
 Assert.assertEquals("Test File Title", title.text());
 }

 @Test
 public void testCleanHtml() {
 String html = "<html><body><script>alert('XSS');</script><p>Test</p></body></html>";
 Whitelist whitelist = Whitelist.simpleText();
 String cleanHtml = Jsoup.clean(html, "", whitelist);
 Document document = Jsoup.parseBodyFragment(cleanHtml, "");
 Assert.assertEquals("Test", document.body().text());
 }

 @Test
 public void testIsValidHtml() {
 String html = "<html><body><script>alert('XSS');</script><p>Test</p></body></html>";
 Whitelist whitelist = Whitelist.simpleText();
 boolean isValid = Jsoup.isValid(html, whitelist);
 Assert.assertFalse(isValid);
 }
 }