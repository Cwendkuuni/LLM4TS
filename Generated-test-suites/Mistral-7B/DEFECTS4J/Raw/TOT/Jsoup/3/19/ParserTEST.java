import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Title</title></head>" +
 "<body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("Test Title", doc.title().text());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals("Test Heading", doc.getElementsByTag("h1").get(0).text());
 assertEquals("Test Paragraph.", doc.getElementsByTag("p").get(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test Heading</h1><p>Test Paragraph.</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals(1, doc.body().childNodeSize());
 assertEquals("Test Heading", doc.getElementsByTag("h1").get(0).text());
 assertEquals("Test Paragraph.", doc.getElementsByTag("p").get(0).text());
 }

 @Test
 public void testParseRelaxedBodyFragment() {
 String bodyHtml = "<body><p>Test Paragraph.</p><h1>Test Heading</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals(1, doc.body().childNodeSize());
 assertEquals("Test Paragraph.", doc.getElementsByTag("p").get(0).text());
 assertEquals("Test Heading", doc.getElementsByTag("h1").get(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseInvalidTagName() {
 String html = "<html><foo><bar></foo></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(0, doc.body().childNodeSize());
 }

 @Test
 public void testParseSelfClosingTag() {
 String html = "<html><img src=\"test.png\"/></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(1, doc.body().childNodeSize());
 Element img = doc.getElementsByTag("img").get(0);
 assertTrue(img.tag().isSelfClosing());
 }

 @Test
 public void testParseEmptyElement() {
 String html = "<html><br></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(1, doc.body().childNodeSize());
 Element br = doc.getElementsByTag("br").get(0);
 assertTrue(br.tag().isEmpty());
 }

 @Test
 public void testParseBaseTag() {
 String html = "<html><base href=\"/test/\"><body><a href=\"test2.html\">Link</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("/test/test2.html", doc.baseUri());
 }

 @Test
 public void testParseInvalidBaseTag() {
 String html = "<html><base baz=\"test/\"><body><a href=\"test2.html\">Link</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 }

 @Test
 public void testParseInvalidAttributeValue() {
 String html = "<html><div foo=\"<script>alert('XSS')\"</div></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(0, doc.body().childNodeSize());
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(0, doc.body().childNodeSize());
 assertEquals("This is a comment", doc.getElementsByTag("comment").get(0).text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><![CDATA[This is CDATA]]><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(0, doc.body().childNodeSize());
 Element cdata = doc.getElementsByTag("cdata").get(0);
 assertEquals("This is CDATA", cdata.text().toString());
 }

 @Test
 public void testParseValidXmlDeclaration() {
 String html = "<html xmlns:foo=\"bar\"<?xml version=\"1.0\" encoding=\"UTF-8\"?><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("UTF-8", doc.xmlDeclaration().encoding());
 }

 @Test(expected = org.jsoup.parser.Parser.TagMismatchException.class)
 public void testParseInvalidStartTag() {
 String html = "<html><li><body></body></html>";
 Parser.parse(html, "http://example.com");
 }

 @Test(expected = org.jsoup.parser.Parser.TagMismatchException.class)
 public void testParseInvalidEndTag() {
 String html = "<html><body><p><foo></html>";
 Parser.parse(html, "http://example.com");
 }
}