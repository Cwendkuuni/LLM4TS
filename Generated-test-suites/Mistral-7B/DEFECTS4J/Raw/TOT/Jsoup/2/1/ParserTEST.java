import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setUp() {
 doc = new Document("");
 parser = new Parser("", "", false);
 }

 @Test
 public void testParse() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 doc = Parser.parse(html, "");

 assertEquals("Test Title", doc.title().text());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNotNull(doc.getElementsByTag("h1").first());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test Heading</h1></body>";
 doc = Parser.parseBodyFragment(bodyHtml, "");

 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.getElementsByTag("h1").first());
 assertEquals(0, doc.head().childElementsSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 parser = new Parser(null, "", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 parser = new Parser("", null, false);
 }

 @Test
 public void testParseComment() {
 String htmlWithComment = "<html><!-- This is a comment --><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 doc = Parser.parse(htmlWithComment, "");

 Element comment = doc.getFirstMetaElement();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 String htmlWithXmlDecl = "<html xmlns:foo=\"http://foo.com\" xml:lang=\"en\">";
 doc = Parser.parse(htmlWithXmlDecl, "");

 XmlDeclaration xmlDeclaration = doc.getXmlDeclaration();
 assertEquals("http://foo.com", xmlDeclaration.xmlns("foo"));
 assertEquals("en", xmlDeclaration.xmlLang());
 }

 @Test
 public void testParseEndTag() {
 String htmlWithEndTag = "<div><p>Test Paragraph<p/></div>";
 doc = Parser.parse(htmlWithEndTag, "");

 Element div = doc.getElementsByTag("div").first();
 assertEquals(1, div.childElementsSize());
 assertEquals("Test Paragraph", div.getElementsByTag("p").first().text());
 }

 @Test
 public void testParseStartTag() {
 String htmlWithStartTag = "<a href=\"test.html\" title=\"Test Title\"><img src=\"image.jpg\" alt=\"Test Image\"></a>";
 doc = Parser.parse(htmlWithStartTag, "");

 Element link = doc.getElementsByTag("a").first();
 assertEquals("test.html", link.absUrl("href"));
 assertEquals("Test Title", link.attribute("title"));

 Element img = link.getElementsByTag("img").first();
 assertEquals("image.jpg", img.absUrl("src"));
 assertEquals("Test Image", img.attribute("alt"));
 }

 @Test
 public void testParseAttribute() {
 String htmlWithAttribute = "<input type=\"text\" name=\"username\" value=\"Test User\">";
 doc = Parser.parse(htmlWithAttribute, "");

 Element input = doc.getElementsByTag("input").first();
 Assert.assertEquals("text", input.tagName());
 Attributes attributes = input.attributes();
 assertEquals("Test User", attributes.get("value"));
 assertEquals("username", attributes.get("name"));
 }

 @Test
 public void testParseTextNode() {
 String htmlWithTextNode = "<p>Test Paragraph</p>";
 doc = Parser.parse(htmlWithTextNode, "");

 Element p = doc.getElementsByTag("p").first();
 assertEquals("Test Paragraph", p.text());
 }

 @Test
 public void testParseCdata() {
 String htmlWithCdata = "<script><![CDATA[alert('Hello, world!');]]></script>";
 doc = Parser.parse(htmlWithCdata, "");
 Element script = doc.getElementsByTag("script").first();
 TextNode cdata = (TextNode) script.child(0);
 assertEquals("alert('Hello, world!');", cdata.text());
 }
}