import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, null);

 assertEquals("Hello World", document.head().title().text());
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testParseHtmlWithBaseUri() {
 String html = "<html><base href=\"/test\"><body><a href=\"about.html\"></a></body></html>";
 document = parser.parseInput(html, "/test/index.html");

 String expectedLink = "/test/about.html";
 assertEquals(expectedLink, document.selectFirst("a[href]").attr("href"));
 }

 @Test
 public void testParseXml() {
 String xml = "<root><element>Hello World</element></root>";
 List<Node> nodes = Parser.parseXmlFragment(xml, null);
 Element element = (Element) nodes.get(0);
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseFragment() {
 String html = "<div id=\"content\">Content</div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(html, context, null);

 assertEquals(1, nodes.size());
 Element div = (Element) nodes.get(0);
 assertEquals("Content", div.text());
 assertEquals("content", div.id());
 }

 @Test
 public void testTrackErrors() {
 String html = "<html><body><script>throw new Error('Test Error')</script></body></html>";
 Parser parserWithTrackingErrors = new Parser(new HtmlTreeBuilder());
 parserWithTrackingErrors.setTrackErrors(1);
 List<ParseError> errors = parserWithTrackingErrors.parseInput(html, null).errors();

 assertEquals(1, errors.size());
 assertEquals("Test Error", errors.get(0).message());
 }

 @Test
 public void testParseWithInvalidHtml() {
 String html = "<html><body><script>throw new Error('Test Error')</script><div>";
 assertThrows(RuntimeException.class, () -> parser.parseInput(html, null));
 }

 @Test
 public void testParseWithInvalidXml() {
 String xml = "<root<element>Hello World</element>";
 assertThrows(RuntimeException.class, () -> Parser.parseXmlFragment(xml, null));
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parserWithXmlTreeBuilder = new Parser(new XmlTreeBuilder());
 parserWithXmlTreeBuilder.setTreeBuilder(new HtmlTreeBuilder());
 assertInstanceOf(HtmlTreeBuilder.class, parserWithXmlTreeBuilder.getTreeBuilder());
 }

 @Test
 public void testParseSettings() {
 ParseSettings settings = parser.settings();
 settings.setTagNameStart("tag");
 parser = new Parser(new HtmlTreeBuilder());
 parser.settings(settings);
 String html = "<html><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, null);
 assertEquals("tag:h1", document.head().tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<body><h1>Hello World</h1></body>";
 document = Parser.parseBodyFragment(html, null);
 assertEquals("Hello World", document.head().title().text());
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testUnescapeEntities() {
 String html = "<p>&lt;p&gt;This is a &lt;strong&gt;test&lt;/strong&gt;</p>";
 String text = Parser.unescapeEntities(html, false);
 assertEquals("<p><p>This is a <strong>test</strong></p>", text);
 }

 @Test
 public void testParseWithBaseUriAndFragment() {
 String html = "<html><base href=\"/test\"><body><a href=\"about.html#fragment\"></a></body></html>";
 document = parser.parseInput(html, "/test/index.html#otherFragment");
 String expectedLink = "/test/about.html#fragment";
 assertEquals(expectedLink, document.selectFirst("a[href]").attr("href"));
 }

 @Test
 public void testParseWithCustomTreeBuilder() {
 Parser parserWithCustomTreeBuilder = new Parser(new CustomTreeBuilder());
 document = parserWithCustomTreeBuilder.parseInput("<html><body><h1>Hello World</h1></body></html>", null);
 assertInstanceOf(CustomTreeBuilder.CustomElement.class, document.head());
 }
}

class CustomTreeBuilder extends TreeBuilder {

 public CustomElement newElement(String tagName, List<Attribute> attributes) {
 return new CustomTreeBuilder.CustomElement(tagName, attributes);
 }

 static class CustomElement extends Element {
 public CustomElement(String tagName, List<Attribute> attributes) {
 super(tagName, attributes);
 }
 }
}