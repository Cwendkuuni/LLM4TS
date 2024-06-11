package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.LinkedList;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class ParserTest {

 private Parser parseInstance;
 private Document doc;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 parseInstance = new Parser("", "", false);
 doc = parseInstance.parse();
 }

 @Test
 public void testParse() {
 // Test for empty input
 assertEquals(doc, parseInstance.parse(""));

 // Test for parse method
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 Document parsedDoc = Parser.parse(html, "");
 assertEquals(doc, parsedDoc);

 // Test for parseBodyFragment method
 html = "<body><h1>Test Heading</h1></body>";
 parsedDoc = Parser.parseBodyFragment(html, "");
 assertEquals(doc.body(), parsedDoc.body());

 // Test for parseBodyFragmentRelaxed method
 html = "<body><h1>Test Heading</h1></body>";
 parsedDoc = Parser.parseBodyFragmentRelaxed(html, "");
 assertEquals(doc.body(), parsedDoc.body());
 }

 @Test
 public void testParseStartTag() {
 // Test for tag with no attributes
 String tagName = "p";
 parseInstance.tq.put("<?xml version='1.0' encoding='UTF-8'?>\n<" + tagName + ">");
 parseInstance.tq.put("</" + tagName + ">");
 Element pElement = parseInstance.parse();
 assertEquals(tagName, pElement.tagName());
 assertNotNull(pElement.attributes());
 assertFalse(pElement.children().size() > 0);

 // Test for tag with attributes
 tagName = "img";
 String attributeName = "src";
 String attributeValue = "test.jpg";
 parseInstance.tq.put("<" + tagName + " src='" + attributeValue + "' alt='Test Image'>");
 parseInstance.tq.put("</" + tagName + ">");
 Element imgElement = parseInstance.parse();
 assertEquals(tagName, imgElement.tagName());
 Attributes attributes = imgElement.attributes();
 assertEquals(attributeName, attributes.get("src"));
 assertEquals(attributeValue, attributes.get("src").getValue());
 assertFalse(imgElement.children().size() > 0);
 }

 @Test
 public void testParseEndTag() {
 // Test for tag with no content
 String tagName = "p";
 parseInstance.tq.put("<p></p>");
 Element pElement = parseInstance.parse();
 assertEquals(tagName, pElement.tagName());
 assertNotNull(pElement.attributes());
 assertFalse(pElement.children().size() > 0);

 // Test for tag with content
 tagName = "div";
 parseInstance.tq.put("<div><p>Test Content</p></div>");
 Element divElement = parseInstance.parse();
 assertEquals(tagName, divElement.tagName());
 assertNotNull(divElement.attributes());
 assertEquals(1, divElement.children().size());
 Element pChild = (Element) divElement.child(0);
 assertEquals("p", pChild.tagName());
 assertEquals("Test Content", pChild.text());
 }

 @Test
 public void testParseComment() {
 String commentData = "-- Test Comment -->";
 parseInstance.tq.put("<!--" + commentData + "-->");
 Element comment = parseInstance.parse();
 assertInstanceOf(Comment.class, comment);
 assertEquals(commentData, comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 String xmlDeclarationData = "<xml version='1.0' encoding='UTF-8' standalone='yes'?>";
 parseInstance.tq.put(xmlDeclarationData);
 XmlDeclaration xmlDeclaration = (XmlDeclaration) parseInstance.parse();
 assertEquals(xmlDeclarationData, xmlDeclaration.xmlDecl().data());
 }

 @Test
 public void testParseTextNode() {
 String textData = "Test Text";
 parseInstance.tq.put("<p>" + textData + "</p>");
 Element pElement = parseInstance.parse();
 assertEquals("p", pElement.tagName());
 assertEquals(1, pElement.children().size());
 TextNode textNode = (TextNode) pElement.child(0);
 assertEquals(textData, textNode.text());
 }

 @Test
 public void testParseCdata() {
 String cdataData = "<![CDATA[Test CDATA]]>";
 parseInstance.tq.put(cdataData);
 TextNode cdataNode = (TextNode) parseInstance.parse();
 assertEquals("Test CDATA", cdataNode.text());
 }

 @Test
 public void testParseAttributes() {
 // Test for single attribute
 String tagName = "input";
 String attributeName = "type";
 String attributeValue = "text";
 parseInstance.tq.put("<" + tagName + " type='" + attributeValue + "'>");
 parseInstance.tq.put("</" + tagName + ">");
 Element inputElement = parseInstance.parse();
 Attribute attribute = inputElement.attributes().get("type");
 assertEquals(attributeName, attribute.getKey());
 assertEquals(attributeValue, attribute.getValue());

 // Test for multiple attributes
 tagName = "a";
 String hrefAttributeName = "href";
 String hrefAttributeValue = "test.html";
 String targetAttributeName = "target";
 String targetAttributeValue = "_blank";
 parseInstance.tq.put("<" + tagName + " href='" + hrefAttributeValue + "' target='" + targetAttributeValue + "'>");
 parseInstance.tq.put("</" + tagName + ">");
 Element aElement = parseInstance.parse();
 assertEquals(2, aElement.attributes().size());
 Attribute hrefAttribute = aElement.attributes().get("href");
 assertEquals(hrefAttributeName, hrefAttribute.getKey());
 assertEquals(hrefAttributeValue, hrefAttribute.getValue());
 Attribute targetAttribute = aElement.attributes().get("target");
 assertEquals(targetAttributeName, targetAttribute.getKey());
 assertEquals(targetAttributeValue, targetAttribute.getValue());
 }

 @Test
 public void testParseEmptyElement() {
 String tagName = "img";
 parseInstance.tq.put("<" + tagName + "/>");
 Element imgElement = parseInstance.parse();
 assertEquals(tagName, imgElement.tagName());
 assertNotNull(imgElement.attributes());
 assertFalse(imgElement.children().size() > 0);
 tagName = "br";
 parseInstance.tq.put("<" + tagName + "/>");
 imgElement = parseInstance.parse();
 assertEquals(tagName, imgElement.tagName());
 assertNotNull(imgElement.attributes());
 assertFalse(imgElement.children().size() > 0);
 }

 @Test
 public void testParseInvalidInput() {
 parseInstance.tq.put("<!--<html>-->");
 // The parse method should throw an exception in this case
 try {
 parseInstance.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 parseInstance.tq.put("<html><head><title><script>alert('XSS')</script></title>");
 // The parse method should throw an exception in this case
 try {
 parseInstance.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @After
 public void tearDown() {
 parseInstance = null;
 doc = null;
 }
}