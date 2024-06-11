import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

public class ParserTest {

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Welcome to Test Page</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element root = doc.body();
 Element title = root.getElementsByTag("title").first();

 Assert.assertEquals("Test Page", title.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Welcome to Test Page</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 Element root = doc;
 Element title = root.getElementsByTag("h1").first();

 Assert.assertEquals("Welcome to Test Page", title.text());
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1<script>alert('Hello World')</script>";
 Document doc = null;
 try {
 doc = Parser.parse(html, "http://test.com");
 } catch (Exception e) {
 // expected
 }

 Assert.assertNull(doc);
 }

 @Test
 public void testParseEmptyHtml() {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element root = doc;

 Assert.assertTrue(root.tagName().equals("html"));
 Assert.assertTrue(root.childElements().isEmpty());
 }

 @Test
 public void testParseXmlDeclaration() {
 String html = "<xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Document doc = Parser.parse(html, "http://test.com");
 Element xmlDecl = doc.getElementsByTag("xml").first();

 Assert.assertTrue(xmlDecl.attributes().size() == 3);
 Assert.assertEquals("1.0", xmlDecl.attributes().get("version").getValue());
 Assert.assertEquals("UTF-8", xmlDecl.attributes().get("encoding").getValue());
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element comment = doc.body().child(0);

 Assert.assertTrue(comment.tagName().equals("comment"));
 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseScriptTag() {
 String html = "<html><head><script src=\"script.js\"></script></head></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element script = doc.head().getElementsByTag("script").first();

 Assert.assertTrue(script.tagName().equals("script"));
 Assert.assertEquals("script.js", script.attr("src"));
 }

 @Test
 public void testParseBaseTag() {
 String html = "<html><head><base href=\"/test/\"></head><body><a href=\"about.html\">About</a></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element base = doc.head().getElementsByTag("base").first();
 Element link = doc.body().getElementsByTag("a").first();

 Assert.assertTrue(base.tagName().equals("base"));
 Assert.assertEquals("/test/", base.attr("href"));
 Assert.assertEquals("about.html", link.attr("href"));
 }

 @Test
 public void testParseInvalidTagName() {
 String html = "<html><invalidTag></html>";
 Document doc = null;
 try {
 doc = Parser.parse(html, "http://test.com");
 } catch (Exception e) {
 // expected
 }

 Assert.assertNull(doc);
 }
}