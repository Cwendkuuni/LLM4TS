import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseHtmlDocument() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals("http://test.com/", doc.baseUri());
 assertEquals("Test Title", doc.title().text());
 assertEquals("Test Heading", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test Heading</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");

 assertEquals("http://test.com/", doc.baseUri());
 assertEquals(0, doc.head().childNodeSize());
 assertEquals("Test Heading", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Test Heading</h1>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com/");

 assertEquals("http://test.com/", doc.baseUri());
 assertEquals(0, doc.head().childNodeSize());
 assertEquals("Test Heading", doc.select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser.parse(null, "http://test.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:my=\"http://test.com/my\" xml:lang=\"en\">Test</html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals("http://test.com/my", doc.head().select("html xmlns:my").attr("xmlns:my"));
 assertEquals("en", doc.head().select("html").attr("xml:lang"));
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --><body><h1>Test Heading</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals("This is a comment", doc.select("html -->").text());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><div><p>Test</p></div></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(1, doc.body().firstChild().childNodeSize());

 assertEquals("Test", doc.body().firstChild().select("p").text());
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><body><div><input type=\"text\" name=\"test\"/></div></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(1, doc.body().firstChild().childNodeSize());

 Element div = doc.body().firstChild();
 assertEquals("div", div.tagName());
 assertEquals(1, div.childNodeSize());

 Element input = div.firstChild();
 assertEquals("input", input.tagName());
 assertEquals("text", input.tagName().toString().toLowerCase().equals("text") ? "text" : "not text");
 assertEquals("test", input.attr("name"));
 }

 @Test
 public void testParseAttribute() {
 String html = "<html><body><div class=\"test class2\" id=\"testId\" title=\"Test Title\"></div></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 Element div = doc.body().firstChild();
 assertEquals(3, div.attributeSize());

 Attribute classAttr = div.attr("class");
 assertEquals("test class2", classAttr.value());
 Attribute idAttr = div.attr("id");
 assertEquals("testId", idAttr.value());
 Attribute titleAttr = div.attr("title");
 assertEquals("Test Title", titleAttr.value());
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Test</p></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(1, doc.body().firstChild().childNodeSize());

 TextNode textNode = (TextNode) doc.body().firstChild().firstChild();
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><![CDATA[Test]]></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(1, doc.body().firstChild().childNodeSize());

 TextNode cdataNode = (TextNode) doc.body().firstChild().firstChild();
 assertEquals("[CDATA[Test]]", cdataNode.toString());
 }

 @Test
 public void testParseValidAncestors() {
 String html = "<html><body><div><p></p></div></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(1, doc.body().firstChild().childNodeSize());
 assertEquals("div", doc.body().firstChild().tagName());

 Element div = doc.body().firstChild();
 assertEquals(1, div.childNodeSize());
 assertEquals("p", div.firstChild().tagName());
 }

 @Test
 public void testParseInvalidAncestorsRelaxed() {
 String html = "<html><body><div><p></p></div><div><div></div></div></body></html>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(2, doc.body().firstChild().childNodeSize());

 Element div1 = doc.body().firstChild();
 assertEquals("div", div1.tagName());
 assertEquals(1, div1.childNodeSize());
 assertEquals("p", div1.firstChild().tagName());

 Element div2 = div1.nextSibling();
 assertEquals("div", div2.tagName());
 assertEquals(1, div2.childNodeSize());
 assertEquals("div", div2.firstChild().tagName());
 }

 @Test
 public void testParseEmptyTags() {
 String html = "<html><body><img src=\"image.png\"/><input type=\"text\"/></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(2, doc.body().firstChild().childNodeSize());

 assertEquals("img", doc.body().firstChild().get(0).tagName());
 assertEquals("input", doc.body().firstChild().get(1).tagName());
 }
}