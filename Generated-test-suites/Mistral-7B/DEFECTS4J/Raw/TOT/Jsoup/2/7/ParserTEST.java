import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("<html><head><title>Test</title></head><body>" +
 "<h1>Hello<h1>" +
 "<p>World</p>" +
 "</body></html>", "http://test.com");
 doc = parser.parse();
 }

 @Test
 public void testParseDocument() {
 // Test for basic parsing of a valid HTML document
 assertEquals("Test", doc.title());
 Element title = doc.select("title").first();
 assertEquals("Test", title.text());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello", h1.text());
 Element p = doc.select("p").first();
 assertEquals("World", p.text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test for parsing a body fragment
 String bodyHtml = "<h1>Hello</h1><p>World</p>";
 Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 Element fragmentTitle = fragmentDoc.select("title").first();
 assertNull(fragmentTitle);
 Element fragmentH1 = fragmentDoc.select("h1").first();
 assertEquals("Hello", fragmentH1.text());
 Element fragmentP = fragmentDoc.select("p").first();
 assertEquals("World", fragmentP.text());
 }

 @Test
 public void testParseEmptyDocument() {
 // Test for parsing an empty document
 Parser emptyParser = new Parser("", "http://test.com", false);
 Document emptyDoc = emptyParser.parse();
 assertTrue(emptyDoc.body().children().isEmpty());
 }

 @Test
 public void testParseInvalidHtml() {
 // Test for parsing invalid HTML
 Parser invalidParser = new Parser("<!-- Comment --><html>", "http://test.com");
 Document invalidDoc = invalidParser.parse();
 assertNotNull(invalidDoc.body().children().get(0)); // Comments are ignored
 }

 @Test
 public void testParseXmlDecl() {
 // Test for parsing XML declaration
 Parser xmlParser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test</title></head><body>", "http://test.com");
 Document xmlDoc = xmlParser.parse();
 assertEquals("UTF-8", xmlDoc.xmlParser().encoding());
 }

 @Test
 public void testParseEndTag() {
 // Test for parsing end tags
 Parser endTagParser = new Parser("<html><head><title>Test</title></head><body><p>World<p></body></html>", "http://test.com");
 Document endTagDoc = endTagParser.parse();
 assertEquals(2, endTagDoc.body().children().size());
 }

 @Test
 public void testParseStartTag() {
 // Test for parsing start tags
 Parser startTagParser = new Parser("<html><head><title>Test</title></head><body><h1>Hello<img src=\"test.jpg\" alt=\"Test\"></h1></body></html>", "http://test.com");
 Document startTagDoc = startTagParser.parse();
 assertEquals(2, startTagDoc.body().children().size());
 Element h1 = startTagDoc.select("h1").first();
 assertEquals("Hello", h1.text());
 Element image = h1.select("img").first();
 assertNotNull(image);
 assertEquals("test.jpg", image.absUrl("src"));
 assertEquals("Test", image.attributes().get("alt").getValue());
 }

 @Test
 public void testParseAttribute() {
 // Test for parsing attributes
 Parser attributeParser = new Parser("<html><head><title>Test</title></head><body><a href=\"test.html\" class=\"test\" id=\"link\">Link</a></body></html>", "http://test.com");
 Document attributeDoc = attributeParser.parse();
 Element link = attributeDoc.select("a").first();
 assertEquals("test.html", link.attributes().get("href").getValue());
 assertEquals("test", link.attributes().get("class").getValue());
 assertEquals("link", link.id());
 }

 @Test
 public void testParseTextNode() {
 // Test for parsing text nodes
 Parser textNodeParser = new Parser("<html><body><p>Test Test Test</p></body></html>", "http://test.com");
 Document textNodeDoc = textNodeParser.parse();
 Element p = textNodeDoc.select("p").first();
 assertEquals("Test Test Test", p.text());
 }

 @Test
 public void testParseCdata() {
 // Test for parsing CDATA sections
 Parser cdataParser = new Parser("<html><body><![CDATA[Test Test Test]]></body></html>", "http://test.com");
 Document cdataDoc = cdataParser.parse();
 Element p = cdataDoc.select("body").first();
 assertEquals("Test Test Test", p.text());
 }
}