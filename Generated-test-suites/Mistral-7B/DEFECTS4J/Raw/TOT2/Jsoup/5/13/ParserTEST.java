import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("Test Page", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 assertEquals("", doc.head().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Hello World</h1>"; // missing closing tag
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
 assertEquals("Hello World", doc.body().text()); // should still parse correctly with relaxed parsing
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 String html = null;
 try {
 Parser.parse(html, "http://test.com");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 try {
 Parser.parse(html, null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("", doc.head().text());
 assertEquals("", doc.body().text());
 assertTrue(doc.body().child(0) instanceof Comment);
 }

 @Test
 public void testParseCdata() {
 String html = "<html><![CDATA[This is some CDATA]]></html>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("", doc.head().text());
 assertEquals("This is some CDATA", doc.body().child(0).text());
 }

 @Test
 public void testParseStartTagWithEmptyAttribute() {
 String html = "<p class=\"\"></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertFalse(doc.body().child(0).hasAttr("class"));
 }

 @Test
 public void testParseStartTagWithAttr() {
 String html = "<p class=\"test\" id=\"testId\"></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).hasAttr("class"));
 assertEquals("test", doc.body().child(0).attr("class"));
 assertTrue(doc.body().child(0).hasAttr("id"));
 assertEquals("testId", doc.body().child(0).attr("id"));
 }

 @Test
 public void testParseStartTagWithAttrValueQuotes() {
 String html = "<p class=\"test class=\"test2\" id=\"test 'Id'\"></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).hasAttr("class"));
 assertEquals("test class=test2", doc.body().child(0).attr("class"));
 assertTrue(doc.body().child(0).hasAttr("id"));
 assertEquals("test 'Id'", doc.body().child(0).attr("id"));
 }

 @Test
 public void testParseStartTagWithAttrValueDoubleQuotes() {
 String html = "<p class=\"test class=\"test2\" id=\"test \"Id\"\"></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).hasAttr("class"));
 assertEquals("test class=test2", doc.body().child(0).attr("class"));
 assertTrue(doc.body().child(0).hasAttr("id"));
 assertEquals("test \"Id\"", doc.body().child(0).attr("id"));
 }

 @Test
 public void testParseStartTagWithAttrValueSingleQuote() {
 String html = "<p class=\"test class=\"test2\" id=\"test 'Id'\"></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).hasAttr("class"));
 assertEquals("test class=test2", doc.body().child(0).attr("class"));
 assertTrue(doc.body().child(0).hasAttr("id"));
 assertEquals("test 'Id'", doc.body().child(0).attr("id"));
 }

 @Test
 public void testParseStartTagWithAttrValueSingleQuoteAfterDoubleQuote() {
 String html = "<p class=\"test class=\"test2\" id=\"test \"Id\'></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).hasAttr("class"));
 assertEquals("test class=test2", doc.body().child(0).attr("class"));
 assertTrue(doc.body().child(0).hasAttr("id"));
 assertEquals("test \"Id'\", should be treated as one attribute", doc.body().child(0).attr("id"));
 }

 @Test
 public void testParseStartTagWithAttrValueSingleQuotesInQuotes() {
 String html = "<p class=\"test class=\"test2\" id=\"test 'Id's Test'\"></p>";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("p", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).hasAttr("class"));
 assertEquals("test class=test2", doc.body().child(0).attr("class"));
 assertTrue(doc.body().child(0).hasAttr("id"));
 assertEquals("test 'Id's Test'", doc.body().child(0).attr("id"));
 }

 @Test
 public void testParseStartTagWithEmptyTag() {
 String html = "<img />";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("img", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).isEmpty());
 }

 @Test
 public void testParseStartTagWithSelfClosingTag() {
 String html = "<img src=\"image.png\" />";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("img", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).isSelfClosing());
 }

 @Test
 public void testParseStartTagWithNonSelfClosingTag() {
 String html = "<img src=\"image.png\">";
 Document doc = Parser.parse(html, "http://test.com");
 assertEquals("img", doc.body().child(0).tagName());
 assertFalse(doc.body().child(0).isSelfClosing());
 }

 @Test
 public void testParseStartTagWithUnknownTag() {
 String html = "<foo bar=\"baz\"></foo>";
 Document doc = Parser.parse(html, "http://test.com");
 // test passes if no exception is thrown
 }
}