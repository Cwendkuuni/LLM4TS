import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import java.util.LinkedList;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 @Test
 public void testParser_parse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParser_parseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com");
 Document doc = parser.parse();
 assertNotNull(doc);
 assertEquals("Hello World", doc.body().text());
 }

 @Test(expected = NullPointerException.class)
 public void testParser_parse_NullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 parser.parse(); // should throw NullPointerException
 }

 @Test(expected = NullPointerException.class)
 public void testParser_parse_NullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 parser.parse(); // should throw NullPointerException
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();
 Element comment = doc.select("html comment").first();
 assertNotNull(comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns:html=\"http://www.w3.org/1999/xhtml\">", "http://example.com");
 Document doc = parser.parse();
 XmlDeclaration xmlDecl = doc.select("xml-declaration").first();
 assertNotNull(xmlDecl);
 assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.xmlns());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals(1, doc.body().childNodeSize());
 assertEquals("Hello World", doc.body().child(0).text());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><body><h1 id=\"title\">Test</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element h1 = doc.body().firstChild();
 assertNotNull(h1);
 assertEquals("h1", h1.tagName());
 assertEquals("Test", h1.id());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<link rel=\"stylesheet\" href=\"style.css\"/>", "http://example.com");
 Document doc = parser.parse();
 Element link = doc.select("link").first();
 assertNotNull(link);
 assertEquals("stylesheet", link.attr("rel"));
 assertEquals("style.css", link.attr("href"));
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.body().child(0);
 assertNotNull(textNode);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><![CDATA[ <p>Hello World</p> ]]> </html>", "http://example.com");
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.body().child(0);
 assertNotNull(textNode);
 assertEquals("<p>Hello World</p>", textNode.text());
 }

 @Test
 public void testAddChildToParent() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element head = doc.head();
 Element p = doc.body().child(0);
 Element title = new Element(Tag.valueOf("h1"), "http://example.com");
 title.appendChild(new TextNode("Test Title"));
 head.appendChild(title);
 Element body = doc.body();
 body.appendChild(p);
 body.appendChild(title);
 assertEquals(2, body.childNodeSize());
 }

 @Test
 public void testPopStackToClose() {
 Parser parser = new Parser("<html><body><p>Hello World<p><p>Test</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals(2, doc.body().childNodeSize());
 Element p1 = doc.body().child(0);
 Element p2 = doc.body().child(1);
 Element pToClose = parser.popStackToClose(Tag.valueOf("p"));
 assertEquals(p1, pToClose);
 assertEquals(1, doc.body().childNodeSize());
 }
}