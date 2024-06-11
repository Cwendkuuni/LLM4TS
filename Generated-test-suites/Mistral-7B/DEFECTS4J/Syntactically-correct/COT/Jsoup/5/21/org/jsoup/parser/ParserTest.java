package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import org.junit.Test;

/**
 * Test class for Parser class
 */
public class ParserTest {

 private static final String TEST_HTML = "<html><head><title>Test Title</title></head><body><div><p><textarea></textarea></p></div></body></html>";

 @Test
 public void testParse() {
 Document doc = Parser.parse(TEST_HTML, "http://test.com");
 assertNotNull(doc);
 assertEquals("Test Title", doc.title().text());
 assertNotNull(doc.body());
 assertEquals(1, doc.body().childNodeSize());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment(TEST_HTML, "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertEquals(1, doc.body().childNodeSize());
 assertEquals(0, doc.head().childNodeSize());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragmentRelaxed(TEST_HTML, "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertEquals(1, doc.body().childNodeSize());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullHtml() {
 Parser.parse(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseEmptyHtml() {
 Document doc = Parser.parse("<html></html>", "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.childNodeSize());
 }

 @Test
 public void testParseWithComment() {
 String htmlWithComment = TEST_HTML + "<!-- This is a comment -->";
 Document doc = Parser.parse(htmlWithComment, "http://test.com");
 assertNotNull(doc);
 assertTrue(doc.body().getElementsByTag("comment").size() > 0);
 }

 @Test
 public void testParseWithCdata() {
 String htmlWithCdata = TEST_HTML + "<![CDATA[This is CDATA section]]>";
 Document doc = Parser.parse(htmlWithCdata, "http://test.com");
 assertNotNull(doc);
 assertTrue(doc.body().getElementsByTag("data").size() > 0);
 }

 @Test
 public void testParseWithXmlDeclaration() {
 String htmlWithXmlDeclaration = "<xml version='1.0' encoding='UTF-8'><html><head><title>Test Title</title></head><body><div><p><textarea></textarea></p></div></body></html>";
 Document doc = Parser.parse(htmlWithXmlDeclaration, "http://test.com");
 assertNotNull(doc);
 assertTrue(doc.head().childNode(0).tagName().equals("xml-declaration"));
 }

 @Test
 public void testParseEndTagWithMissingTag() {
 String htmlWithMissingEndTag = TEST_HTML + "</random>";
 Document doc = Parser.parse(htmlWithMissingTag, "http://test.com");
 assertNotNull(doc);
 assertNull(doc.body().getElementsByTag("random").first());
 }

 @Test
 public void testParseEndTagWithMultipleTags() {
 String htmlWithMultipleEndTags = TEST_HTML + "</div></div>";
 Document doc = Parser.parse(htmlWithMultipleEndTags, "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.body().childNodeSize());
 assertTrue(doc.body().getElementsByTag("div").size() > 0);
 }

 @Test
 public void testParseStartTagWithEmptyTag() {
 String htmlWithEmptyTag = TEST_HTML + "<img />";
 Document doc = Parser.parse(htmlWithEmptyTag, "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.body().childNodeSize());
 assertTrue(doc.body().getElementsByTag("img").size() > 0);
 }

 @Test
 public void testParseStartTagWithTagAndAttributes() {
 String htmlWithAttributes = TEST_HTML + "<p id='test' class='test-class'>Test text</p>";
 Document doc = Parser.parse(htmlWithAttributes, "http://test.com");
 assertNotNull(doc);
 Element pElement = doc.body().getElementsByTag("p").first();
 assertEquals(2, pElement.attributes().size());
 Attribute idAttribute = pElement.attribute("id");
 assertEquals("test", idAttribute.getValue());
 Attribute classNameAttribute = pElement.attribute("class");
 assertEquals("test-class", classNameAttribute.getValue());
 }

 @Test
 public void testParseStartTagWithTagAndText() {
 String htmlWithText = TEST_HTML + "<p>Test text</p>";
 Document doc = Parser.parse(htmlWithText, "http://test.com");
 assertNotNull(doc);
 Element pElement = doc.body().getElementsByTag("p").first();
 TextNode textNode = (TextNode) pElement.childNodes().get(0);
 assertEquals("Test text", textNode.text());
 }

 @Test
 public void testParseStartTagWithTagAndDataNode() {
 String htmlWithDataNode = TEST_HTML + "<p><script>alert('Test')</script></p>";
 Document doc = Parser.parse(htmlWithDataNode, "http://test.com");
 assertNotNull(doc);
 Element pElement = doc.body().getElementsByTag("p").first();
 DataNode scriptNode = (DataNode) pElement.childNodes().get(0);
 assertEquals("alert('Test')", scriptNode.text());
 }

 @Test
 public void testParseStartTagWithTagAndComment() {
 String htmlWithComment = TEST_HTML + "<p><!-- This is a comment --></p>";
 Document doc = Parser.parse(htmlWithComment, "http://test.com");
 assertNotNull(doc);
 Element pElement = doc.body().getElementsByTag("p").first();
 Comment commentNode = (Comment) pElement.childNodes().get(0);
 assertEquals("This is a comment", commentNode.data());
 }

 @Test
 public void testParseEndTagWithTagAndText() {
 String htmlWithTextInsideTag = TEST_HTML + "<p>Test text<p>";
 Document doc = Parser.parse(htmlWithTextInsideTag, "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.body().childNodeSize());
 assertTrue(doc.body().getElementsByTag("p").size() > 0);
 TextNode textNode = (TextNode) doc.body().getElementsByTag("p").first().childNodes().get(0);
 assertEquals("Test text", textNode.text());
 }

 @Test
 public void testParseEndTagWithTagAndComment() {
 String htmlWithCommentInsideTag = TEST_HTML + "<p><!-- This is a comment --></p>";
 Document doc = Parser.parse(htmlWithCommentInsideTag, "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.body().childNodeSize());
 assertTrue(doc.body().getElementsByTag("p").size() > 0);
 Comment commentNode = (Comment) doc.body().getElementsByTag("p").first().childNodes().get(1);
 assertEquals("This is a comment", commentNode.data());
 }

 @Test
 public void testParseEndTagWithTagAndCdata() {
 String htmlWithCdataInsideTag = TEST_HTML + "<p><![CDATA[This is CDATA section]]></p>";
 Document doc = Parser.parse(htmlWithCdataInsideTag, "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.body().childNodeSize());
 assertTrue(doc.body().getElementsByTag("p").size() > 0);
 DataNode cdataNode = (DataNode) doc.body().getElementsByTag("p").first().childNodes().get(0);
 assertEquals("This is CDATA section", cdataNode.text());
 }

 @Test
 public void testParseEndTagWithTagAndXmlDeclaration() {
 String htmlWithXmlDeclarationInsideTag = TEST_HTML + "<p><xml version='1.0' encoding='UTF-8'></xml></p>";
 Document doc = Parser.parse(htmlWithXmlDeclarationInsideTag, "http://test.com");
 assertNotNull(doc);
 assertEquals(1, doc.body().childNodeSize());
 assertTrue(doc.body().getElementsByTag("p").size() > 0);
 XmlDeclaration xmlDeclarationNode = (XmlDeclaration) doc.body().getElementsByTag("p").first().childNodes().get(0);
 assertEquals("1.0", xmlDeclarationNode.version());
 assertEquals("UTF-8", xmlDeclarationNode.encoding());
 }
}