import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head></head><body></body></html>");
 }

 @Test
 public void testParseEmptyHtml() {
 assertEquals(3, doc.childNodes().size());
 assertTrue(doc.tagName().equals("html"));
 assertTrue(doc.body().childNodes().isEmpty());
 }

 @Test
 public void testParseHtmlWithEmptyBody() {
 parser = new Parser("<html><head></head></html>", "", false);
 doc = parser.parse();
 assertEquals(1, doc.childNodes().size());
 assertTrue(doc.tagName().equals("html"));
 assertTrue(doc.head().childNodes().isEmpty());
 }

 @Test
 public void testParseHtmlWithBody() {
 parser = new Parser("<html><head></head><body><p>Hello World</p></body></html>", "", false);
 doc = parser.parse();
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 Element p = body.childNodes().get(0);
 assertEquals("Hello World", p.text().toString());
 }

 @Test
 public void testParseHtmlWithTags() {
 parser = new Parser("<html><head><title>Title</title></head><body><p>Hello World<a href='link.html'>Link</a></p></body></html>", "", false);
 doc = parser.parse();
 Element title = doc.head().title();
 assertEquals("Title", title.text().toString());
 Element p = doc.body().get(0);
 assertEquals("Hello World<a href='link.html'>Link</a>", p.outerHtml().toString());
 }

 @Test
 public void testParseBodyFragment() {
 parser = new Parser("<p>Hello World</p>", "", true);
 doc = parser.parse();
 Element p = doc.body();
 assertEquals("Hello World", p.text().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 parser = new Parser(null, "", false);
 try {
 parser.parse();
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyBaseUri() {
 parser = new Parser("<html></html>", "", false);
 try {
 parser.parse();
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseHtmlWithComments() {
 parser = new Parser("<html><!-- This is a comment --><head></head><body><p>Hello World</p></body></html>", "", false);
 doc = parser.parse();
 assertEquals(4, doc.childNodes().size());
 Element comment = null;
 for (Element node : doc.childNodes()) {
 if (node.tagName().equals("!--")) {
 comment = (Comment) node;
 break;
 }
 }
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testParseHtmlWithXmlDeclaration() {
 parser = new Parser("<xml version='1.0' encoding='UTF-8' standalone='yes'><html></html>", "", false);
 doc = parser.parse();
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.childNodes().get(0);
 assertEquals("1.0", xmlDecl.version());
 assertEquals("UTF-8", xmlDecl.encoding());
 assertEquals("yes", xmlDecl.standalone());
 }

 @Test
 public void testParseHtmlWithAttribute() {
 parser = new Parser("<p id='p1' class='class1 class2'>Hello World</p>", "", false);
 doc = parser.parse();
 Element p = doc.body().get(0);
 assertEquals("p1", p.attr("id"));
 assertEquals(2, p.classNames().size());
 assertEquals("class1 class2", p.attr("class"));
 }

 @Test
 public void testParseHtmlWithAttributesInTag() {
 parser = new Parser("<a href='link.html' target='_blank' id='link1'>Link</a>", "", false);
 doc = parser.parse();
 Element a = doc.body().get(0);
 assertEquals("link.html", a.attr("href"));
 assertEquals("_blank", a.attr("target"));
 assertEquals("link1", a.attr("id"));
 }

 @Test
 public void testParseHtmlWithAttributesInTagAsMap() {
 parser = new Parser("<div id='div1' title='Title' class='class1 class2'>Text</div>", "", false);
 doc = parser.parse();
 Element div = doc.body().get(0);
 Map<String, String> attributes = div.attributes();
 assertEquals("div1", attributes.get("id"));
 assertEquals("Title", attributes.get("title"));
 assertEquals(2, attributes.size());
 }

 @Test
 public void testParseHtmlWithCdata() {
 parser = new Parser("<p><![CDATA[This is some CDATA]]></p>", "", false);
 doc = parser.parse();
 Element p = doc.body().get(0);
 TextNode textNode = (TextNode) p.childNodes().get(0);
 assertEquals("This is some CDATA", textNode.text());
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 parser = new Parser("<html><head><base href='http://example.com'><body><a href='page.html'>Link</a></body></html>", "", false);
 doc = parser.parse();
 assertEquals("http://example.com", doc.baseUri());
 }
}