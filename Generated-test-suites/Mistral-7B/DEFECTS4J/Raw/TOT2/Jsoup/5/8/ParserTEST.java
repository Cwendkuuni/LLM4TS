import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser(""); // empty HTML
 Document doc = parser.parse();
 assertEquals("", doc.body().text());

 parser = new Parser("<html><body></body></html>", "http://example.com");
 doc = parser.parse();
 assertEquals("<html><body></body></html>", doc.html().text());
 assertEquals("", doc.body().text());

 parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 doc = parser.parse();
 assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 parser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser parser = new Parser("<html><body></body></html>", null, false);
 parser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com", true);
 Document doc = parser.parse();
 assertEquals("<html><head/><body><p>Hello World</p></body></html>", doc.html().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com", true);
 parser.relaxed = true;
 Document doc = parser.parse();
 assertEquals("<html><head/><body><p>Hello World</p>", doc.html().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<div id='test'><p>Hello World</p></div>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.getElementById("test");
 assertEquals("<div id='test'><p>Hello World</p></div>", div.html().text());
 assertTrue(div.tag().isTagName("div"));
 assertTrue(div.hasAttr("id"));
 assertEquals("test", div.attr("id"));
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!comment-->\n<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element comment = doc.select("html > comment()").get(0);
 assertEquals("", comment.text());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><head><body><div><p>Hello World</p></div></body></head></html>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.select("body > div").get(0);
 assertEquals("<html><head><body><div></div></body></head></html>", doc.html().text());
 assertNull(div.child(0));
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<!DOCTYPE html SYSTEM \"foo.dtd\">\n<html><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 XmlDeclaration decl = doc.select("html > xml-declaration()").get(0);
 assertEquals("<!DOCTYPE html SYSTEM \"foo.dtd\">", decl.data());
 }

 @Test
 public void testParseEmptyElement() {
 Parser parser = new Parser("<img src='image.jpg' />", "http://example.com");
 Document doc = parser.parse();
 Element img = doc.select("img").get(0);
 assertEquals("<img src='image.jpg' />", img.html().text());
 assertFalse(img.hasChildren());
 }

 @Test
 public void testParseImplicitParent() {
 Parser parser = new Parser("<p><strong>Hello World</strong></p>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.select("p").get(0);
 assertEquals("<p><strong>Hello World</strong></p>", p.html().text());
 assertFalse(p.hasParent());
 assertTrue(p.parent().tag().isTagName("body"));
 }

 @Test
 public void testParseInvalidParent() {
 Parser parser = new Parser("<html><p><strong>Hello World</strong></html>", "http://example.com");
 Document doc = parser.parse();
 fail("Expected IllegalArgumentException: <strong> is not a valid ancestor of <html>");
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser("<html><head><base href='/foo'><title>Test</title></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals("/foo", doc.baseUri());
 }
}