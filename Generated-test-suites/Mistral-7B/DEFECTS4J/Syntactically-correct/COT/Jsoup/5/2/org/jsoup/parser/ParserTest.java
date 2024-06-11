package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ParserTest {

 private Document parseDocument(String html, String baseUri) {
 return Parser.parse(html, baseUri);
 }

 @Test
 public void testParse() {
 // Test parsing a simple HTML document
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals("Test Title", doc.title().text());
 assertEquals("Test Heading", doc.body().firstChild().text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a body fragment
 String bodyHtml = "<h1>Test Heading</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

 assertEquals("Test Heading", doc.body().firstChild().text());
 assertNull(doc.head());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 // Test parsing a body fragment with relaxed parsing
 String bodyHtml = "<div><h1>Test Heading</h1></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");

 assertEquals("Test Heading", doc.body().firstChild().text());
 assertNotNull(doc.head());
 }

 @Test
 public void testParseWhenBaseUriIsNull() {
 try {
 Parser.parse("", null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testParseEmptyHtml() {
 Document doc = Parser.parse("<html></html>", "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseInvalidHtml() {
 try {
 Parser.parse("<html><body></html><html>", "http://test.com");
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Pass
 }
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><div><p>Test</p></div></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 Element div = doc.body().firstChild().getNextSibling();
 Element p = div.firstChild();
 Element endTag = div.getNextSibling();

 assertEquals("div", div.tagName());
 assertEquals("p", p.tagName());
 assertEquals("", p.text());
 assertEquals("", endTag.text());

 // Test that the end tag is closed
 assertEquals("/div", endTag.tagName());
 }

 @Test
 public void testParseStartTagWithAttributes() {
 String html = "<a href='http://example.com' title='Test Title'>Link</a>";
 Document doc = parseDocument(html, "http://test.com");
 Element link = doc.body().firstChild().getNextSibling();

 assertEquals("a", link.tagName());
 assertEquals("http://example.com", link.attr("href"));
 assertEquals("Test Title", link.attr("title"));
 assertEquals("", link.text());
 }

 @Test
 public void testParseComments() {
 String html = "<html><!-- Test Comment --><body><!-- Test Comment --></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(2, doc.childNodes().size());
 assertEquals(2, doc.body().childNodes().size());

 Comment comment1 = (Comment) doc.childNodes().get(0);
 Comment comment2 = (Comment) doc.body().childNodes().get(0);

 assertEquals("Test Comment", comment1.text());
 assertEquals("Test Comment", comment2.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><![CDATA[Test CData]]></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 TextNode textNode = (TextNode) doc.body().childNodes().get(0);
 assertEquals("Test CData", textNode.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 String html = "<html xmlns='http://example.com'><head></head><body></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertNotNull(doc.head());
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.head().childNodes().get(0);

 assertEquals("http://example.com", xmlDeclaration.xmlns());
 assertEquals("http://test.com", xmlDeclaration.baseURI());
 }

 @Test
 public void testParseDataTags() {
 String html = "<html><body><title>Test Title</title><script>console.log('Test Script')</script></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(2, doc.childNodes().size());
 assertEquals(2, doc.body().childNodes().size());

 Element title = (Element) doc.childNodes().get(0);
 assertEquals("title", title.tagName());
 assertEquals("Test Title", title.text());

 Element script = (Element) doc.body().childNodes().get(1);
 assertEquals("script", script.tagName());
 assertEquals(0, script.childNodes().size());
 }

 @Test
 public void testParseDataTagsWithData() {
 String html = "<html><body><script type='text/javascript'>console.log('Test Script')</script></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element script = (Element) doc.body().childNodes().get(0);
 assertEquals("script", script.tagName());

 assertEquals(1, script.childNodes().size());
 TextNode dataNode = (TextNode) script.childNodes().get(0);
 assertEquals("console.log('Test Script')", dataNode.text());
 }

 @Test
 public void testParseDataTagsWithAttributes() {
 String html = "<html><body><script type='text/javascript' src='script.js'></script></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element script = (Element) doc.body().childNodes().get(0);
 assertEquals("script", script.tagName());

 assertEquals(2, script.childNodes().size());
 Attribute srcAttr = script.attr("src");
 assertEquals("script.js", srcAttr.value());
 TextNode dataNode = (TextNode) script.getChildNodes().get(1);
 assertEquals("", dataNode.text());
 }

 @Test
 public void testParseEmptyDataTags() {
 String html = "<html><body><script type='text/javascript'></script></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element script = (Element) doc.body().childNodes().get(0);
 assertEquals("script", script.tagName());
 assertEquals(0, script.childNodes().size());
 }

 @Test
 public void testParseDataTagsWithEmptyText() {
 String html = "<html><body><script type='text/javascript'>// empty</script></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element script = (Element) doc.body().childNodes().get(0);
 assertEquals("script", script.tagName());
 assertEquals(0, script.childNodes().size());
 }

 @Test
 public void testParseImplicitParents() {
 String html = "<html><body><p>Test</p><img src='image.jpg'></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element p = (Element) doc.body().getChildNodes().get(0);
 assertEquals("p", p.tagName());
 assertEquals(1, p.childNodes().size());
 assertEquals("Test", p.childNodes().get(0).text());

 Element img = (Element) doc.body().getChildNodes().get(1);
 assertEquals("img", img.tagName());
 assertEquals(2, img.childNodes().size());
 assertEquals(1, img.childNodes().get(0).childNodes().size());
 assertEquals("", img.childNodes().get(0).childNodes().get(0).text());
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseImplicitParentsWithRelaxedParsing() {
 String html = "<html><body><p>Test</p><img src='image.jpg'></body></html>";
 Document doc = parseDocument(html, "http://test.com", true);

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element p = (Element) doc.body().getChildNodes().get(0);
 assertEquals("p", p.tagName());
 assertEquals(1, p.childNodes().size());
 assertEquals("Test", p.childNodes().get(0).text());

 Element img = (Element) doc.body().getChildNodes().get(1);
 assertEquals("img", img.tagName());
 assertEquals(0, img.childNodes().size());
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseImplicitParentsWithInvalidAncestor() {
 try {
 String html = "<html><body><ul><li>Test<p>Invalid Ancestor</p></li></ul></body></html>";
 Document doc = parseDocument(html, "http://test.com");
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Pass
 }
 }

 @Test
 public void testParseImplicitParentsWithEmptyBody() {
 String html = "<html><head></head><body></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 assertNull(doc.head());
 }

 @Test
 public void testParseImplicitParentsWithEmptyHead() {
 String html = "<html><head></head><body></body></html>";
 Document doc = parseDocument(html, "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 assertNull(doc.head());
 }

 @Test
 public void testParseImplicitParentsWithEmptyDocument() {
 Document doc = Parser.parse("<html></html>", "http://test.com");

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 assertNull(doc.head());
 }

 @Test
 public void testParseWithBaseUriChanged() {
 String html = "<html><body><script src='script.js'></script></body></html>";
 Document doc1 = parseDocument(html, "http://test1.com");
 String baseUri = doc1.baseURI();

 Document doc2 = parseDocument(html, "http://test2.com");
 assertEquals(baseUri, doc2.baseURI());
 }

 @Test
 public void testParseWithBaseUriChangedAndRelaxedParsing() {
 String html = "<html><body><script src='script.js'></script></body></html>";
 Document doc1 = parseDocument(html, "http://test1.com", true);
 String baseUri = doc1.baseURI();

 Document doc2 = parseDocument(html, "http://test2.com", true);
 assertEquals(baseUri, doc2.baseURI());
 }

 @Test
 public void testParseWithBaseUriChangedAndInvalidUri() {
 String html = "<html><body><script src='script://test.com'></script></body></html>";
 Document doc1 = parseDocument(html, "http://test1.com");

 try {
 Parser.parse(html, "http://test2.com");
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // Pass
 }
 }

 @Test
 public void testParseWithRelaxedParsing() {
 String html = "<html><body><div><p>Test</p></div></body></html>";
 Document doc = parseDocument(html, "http://test.com", true);

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element div = (Element) doc.body().getChildNodes().get(0);
 assertEquals("div", div.tagName());
 assertEquals(1, div.childNodes().size());
 assertEquals("p", div.childNodes().get(0).tagName());
 assertEquals("Test", div.childNodes().get(0).text());
 }

 @Test
 public void testParseWithRelaxedParsingAndImplicitParents() {
 String html = "<html><body><p>Test<img src='image.jpg'></p></body></html>";
 Document doc = parseDocument(html, "http://test.com", true);

 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.body().childNodes().size());

 Element p = (Element) doc.body().getChildNodes().get(0);
 assertEquals("p", p.tagName());
 assertEquals(2, p.childNodes().size());
 assertEquals("Test", p.childNodes().get(0).text());
 assertEquals("img", p.childNodes().get(1).tagName());
 assertEquals(2, p.childNodes().get(1).childNodes().size());
 assertEquals(1, p.childNodes().get(1).childNodes().get(0).childNodes().size());
 assertEquals("", p.childNodes().get(1).childNodes().get(0).childNodes().get(0).text());
 assertEquals("image.jpg", p.childNodes().get(1).attr("src"));
 }
}