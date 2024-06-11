package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() {
 String xmlString = "<xml><tag attr=\"value\"><subTag/></tag></xml>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
 Element root = xmlDoc.rootElement();
 assertEquals("xml", root.tagName());
 assertEquals(1, root.childElementCount());
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<tag attr=\"value\"/>";
 Token startTag = new Token.StartTag("tag", "xml:namespace:tag", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 Element expectedElement = new Element("tag", "", null, null);
 expectedElement.attributes().add("xml:namespace", "xml:namespace:tag");
 assertEquals(expectedElement, xmlTreeBuilder.insert(startTag));
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<tag><subTag/></tag>";
 Token endTag = new Token.EndTag("tag", null, null, null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTag);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<tag attr=\"value\"><subTag/></tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("tag", root.tagName());
 assertEquals(1, root.childElementCount());
 }

 @Test
 public void testInsertNode() {
 Element element = new Element("tag");
 Text textNode = new TextNode("text", "");
 xmlTreeBuilder.insertNode(textNode);
 assertEquals(textNode, xmlTreeBuilder.currentElement().child(0));
 }

 @Test
 public void testInsertComment() {
 String xmlString = "<!--comment-->";
 Token commentToken = new Token.Comment("comment--", null, null, null);
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.currentElement().child(0);
 assertEquals("comment--", comment.data());
 }

 @Test
 public void testInsertCharacter() {
 String xmlString = "<tag>text</tag>";
 Token characterToken = new Token.Character('t', "text", null, null, null, null, null, null);
 xmlTreeBuilder.insert(characterToken);
 Text textNode = (Text) xmlTreeBuilder.currentElement().child(0);
 assertEquals("text", textNode.text());
 }

 @Test
 public void testInsertDocumentType() {
 String xmlString = "<!DOCTYPE tag SYSTEM \"doc.dtd\">";
 Token doctypeToken = new Token.Doctype("tag", null, null, null);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement().child(0);
 assertEquals("tag", documentTypeNode.tagName());
 assertEquals("doc.dtd", documentTypeNode.systemId());
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 String xmlString = "<unknownTag/><tag><subTag/></tag>";
 xmlTreeBuilder.parse(xmlString, "");
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknownTag", null, null, null, null, null, null, null));
 assertEquals(1, xmlTreeBuilder.stack.size());
 Element tagElement = (Element) xmlTreeBuilder.stack.get(0);
 assertEquals("tag", tagElement.tagName());
 }

 @Test
 public void testParseWithErrors() {
 String xmlString = "<tag<tag>";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.parseFragment(xmlString, "", errors, ParseSettings.preserveCase);
 assertEquals(1, errors.size());
 Element errorElement = errors.get(0).getErrorNode();
 assertEquals("<tag<tag>", errorElement.outerHtml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUriInParse() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputInParse() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInputInParseFragment() {
 xmlTreeBuilder.parseFragment(null, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test
 public void testParseWithEmptyInput() {
 Document doc = xmlTreeBuilder.parse("", "");
 assertNull(doc);
 }

 @Test
 public void testParseWithEmptyInputFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(0, nodes.size());
 }

 @Test
 public void testXmlDeclarationProcessedAsComment() {
 String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 xmlTreeBuilder.parse(xmlString, "");
 Comment xmlDeclaration = (Comment) xmlTreeBuilder.currentElement().child(0);
 assertTrue(xmlDeclaration.isWhitespace());
 }

 @Test
 public void testXmlDeclarationProcessedAsElement() {
 String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tag/>";
 xmlTreeBuilder.parse(xmlString, "");
 Document doc = xmlTreeBuilder.currentElement();
 DocumentType xmlDeclarationNode = doc.doctype();
 assertEquals("xml", xmlDeclarationNode.name());
 assertEquals("UTF-8", xmlDeclarationNode.getEntities().get("encoding").value());
 assertEquals(1, doc.childNodeSize());
 Element tagElement = (Element) doc.getRootElement();
 assertEquals("tag", tagElement.tagName());
 }

 @Test
 public void testXmlDeclarationWithExternalEntity() {
 String xmlString = "<!DOCTYPE myRoot [ <!ENTITY myEntity SYSTEM \"file:///path/to/entity.xml\"> ]><root>";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.parseFragment(xmlString, "", errors, ParseSettings.preserveCase);
 assertEquals(1, errors.size());
 Document doc = errors.get(0).getErrorNode().parentDocument();
 assertEquals("root", doc.getRootElement().tagName());
 Entity entity = doc.getEntities().get("myEntity");
 assertEquals("file:///path/to/entity.xml", entity.systemId());
 }
}