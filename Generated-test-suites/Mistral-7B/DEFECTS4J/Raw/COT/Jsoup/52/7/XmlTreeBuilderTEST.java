package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.ajaxpeople.xml.test.xmlunit.XmlUnit;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.Doctype;
import org.junit.Test;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private Document parseFragment(String xmlFragment) {
 return XmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList()).get();
 }

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(null, "", new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullBaseUri() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", null, new ParseErrorList());
 }

 @Test
 public void testProcessStartTag() {
 String xmlFragment = "<tag attr=\"value\"/>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 Element element = rootElement.element("tag");
 assertNotNull(element);
 assertEquals("value", element.attributeValue("attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithInvalidTagName() {
 String xmlFragment = "<tag+/>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 }

 @Test
 public void testProcessEndTag() {
 String xmlFragment = "<tag><subtag/></tag>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 Element tagElement = rootElement.element("tag");
 Element subtagElement = tagElement.element("subtag");
 xmlTreeBuilder.popStackToClose(new EndTag("tag"));
 assertNull(tagElement);
 assertNotNull(subtagElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithInvalidTagName() {
 String xmlFragment = "<tag><subtag/></tag>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 xmlTreeBuilder.popStackToClose(new EndTag("invalidTag"));
 }

 @Test
 public void testProcessComment() {
 String xmlFragment = "<!-- This is a comment -->";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 List<Node> comments = xmlTreeBuilder.doc.getAllElements();
 Comment comment = (Comment) comments.get(0);
 assertTrue(comment.getData().contains("This is a comment"));
 }

 @Test
 public void testProcessCharacter() {
 String xmlFragment = "<tag>Hello World</tag>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 List<Node> textNodes = xmlTreeBuilder.doc.select("tag > text()");
 TextNode textNode = (TextNode) textNodes.get(0);
 assertTrue(textNode.text().contains("Hello World"));
 }

 @Test
 public void testProcessDoctype() {
 String xmlFragment = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 DocumentType doctypeNode = xmlTreeBuilder.doc.getDoctype();
 assertEquals("html", doctypeNode.getName());
 assertNotNull(doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testProcessUnknownTagAsSelfClosing() {
 String xmlFragment = "<unknown/>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment(xmlFragment);
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 assertFalse(rootElement.hasChildren());
 }

 @Test
 public void testProcessUnknownTagAsNonSelfClosing() {
 String xmlFragment = "<unknown><subtag/></unknown>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();xmlTreeBuilder.parseFragment(xmlFragment);
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 Element unknownElement = rootElement.element("unknown");
 assertNotNull(unknownElement);
 assertFalse(unknownElement.hasChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidToken() {
 String xmlFragment = "<tag>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token = new Token(1, "<tag>");
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 Document document = parseFragment("");
 assertEquals(0, document.childNodes().size());
 }

 @Test
 public void testParseFragmentWithEmptyInputButNotEmptyOutput() {
 String xmlFragment = "<root/>";
 Document document = parseFragment(xmlFragment);
 Element rootElement = document.getRootElement();
 assertTrue(rootElement.getName().equals("root"));
 }

 @Test
 public void testParseFragmentWithValidXml() {
 String xmlFragment = "<root><tag attr=\"value\"><subtag/></tag></root>";
 Document document = parseFragment(xmlFragment);
 Element rootElement = document.getRootElement();
 Element tagElement = rootElement.element("tag");
 Element subtagElement = tagElement.element("subtag");
 assertEquals("value", tagElement.attributeValue("attr"));
 assertFalse(subtagElement.hasText());
 }

 @Test(expected = org.jsoup.parser.ParseError.class)
 public void testParseFragmentWithParseError() {
 String xmlFragment = "<root><tag foo=\"bar\"><subtag/><tag/>";
 Document document = parseFragment(xmlFragment);
 assertFalse(document.getRootElement().hasChildren());
 }

 @Test
 public void testParseFragmentWithXmlDeclaration() {
 String xmlFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><tag attr=\"value\"><subtag/></tag></root>";
 Document document = parseFragment(xmlFragment);
 Element rootElement = document.getRootElement();
 assertTrue(document.outputSettings().syntax().equals(Document.OutputSettings.Syntax.xml));
 assertEquals("1.0", document.outputSettings().xmlVersion());
 assertEquals("UTF-8", document.outputSettings().encoding());
 }

}