package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element attr=\"value\"></element></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

 Element root = xmlDocument.rootElement();
 Element element = root.child(0);

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<element attr=\"value\"></element>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element fragmentElement = (Element) fragmentNodes.get(0);

 assertEquals("element", fragmentElement.tagName());
 assertEquals("value", fragmentElement.attribute("attr"));
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element attr=\"value\"></element></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").asXml();

 Element root = Jsoup.parse(xmlDocument).rootElement();
 Element element = root.child(0);
 Token startTag = root.child(0).childTokens().get(0);

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Element", startTag.tagName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element><subElement/></element></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").asXml();

 Element root = Jsoup.parse(xmlDocument).rootElement();
 Element element = root.child(0);
 Element subElement = element.child(0);
 Token endTag = element.childTokens().get(1);

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("subElement", subElement.tagName());
 assertEquals("Element", endTag.tagName());
 assertEquals("EndTag", endTag.type());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><!-- comment --></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").asXml();

 Element root = Jsoup.parse(xmlDocument).rootElement();
 Token commentToken = root.childTokens().get(0);

 assertEquals("root", root.tagName());
 assertEquals("Comment", commentToken.tagName());
 assertEquals("comment", commentToken.getData());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root>&amp;<entity/></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").asXml();

 Element root = Jsoup.parse(xmlDocument).rootElement();
 List<Node> rootChildren = root.childNodes();
 CharacterReference characterReference = (CharacterReference) rootChildren.get(0);
 TextNode textNode = (TextNode) rootChildren.get(1);

 assertEquals("root", root.tagName());
 assertEquals("#text", textNode.tagName());
 assertEquals("&amp;", characterReference.getData());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE root SYSTEM \"file:/path/to/system.dtd\" [ <!ENTITY e \"entity\"> ]>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").asXml();

 DocumentType doctype = (DocumentType) xmlDocument.child(0);
 assertEquals("root", doctype.getName());
 assertEquals("[", doctype.getSystemId());
 assertEquals("file:/path/to/system.dtd", doctype.getPublicId());
 }

 @Test (expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><![CDATA[ <html> ]]>"; // This should be a CDATA node, but is being treated as a start tag
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").asXml();
 }
}