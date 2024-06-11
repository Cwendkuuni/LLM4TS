package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
 }

 @Test
 public void testParse() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Document document = xmlTreeBuilder.parse(xmlString, "");
 Element root = document.getRootElement();
 assertEquals("xml", root.tagName());
 assertEquals("", root.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse("", null);
 }

 @Test
 public void testInitialiseParse() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(xmlString, "");
 assertEquals(xmlTreeBuilder.doc, document.childNodes().get(0));
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(xmlString, "");
 Element tag = document.select("tag").first();
 assertEquals("tag", tag.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<xml><tag><tag2/></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(xmlString, "");
 Element tag1 = document.select("tag").first();
 Element tag2 = document.select("tag2").first();
 xmlTreeBuilder.popStackToClose(tag2.asEndTag());
 assertEquals(tag1, document.select("tag").first());
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<xml><!-- comment --></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(xmlString, "");
 Comment comment = document.select("!-- comment -->").first();
 assertEquals("comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String xmlString = "<xml>&#x1F604;</xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(xmlString, "");
 TextNode textNode = document.select("text()[1]").first();
 assertEquals("\uD83D\uDE04", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(xmlString, "");
 DocumentType doctypeNode = document.select("!DOCTYPE").first();
 assertEquals("xml", doctypeNode.name());
 assertEquals("xml.dtd", doctypeNode.systemIdentifier());
 }

 @Test
 public void testInsertNode() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = Jsoup.parse("<xml></xml>", "", Parser.xmlParser());
 Element root = document.getRootElement();
 xmlTreeBuilder.insertNode(root);
 assertEquals(root, xmlTreeBuilder.doc.childNodes().get(0));
 }

 @Test
 public void testInsertComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!-- comment -->";
 Document document = Jsoup.parse(xmlString, "", Parser.xmlParser());
 Token commentToken = document.tokenizer().next();
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test
 public void testInsertCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "&test;";
 Document document = Jsoup.parse(xmlString, "", Parser.xmlParser());
 Token characterToken = document.tokenizer().next();
 xmlTreeBuilder.insert(characterToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("test", textNode.text());
 }

 @Test
 public void testInsertDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
 Document document = Jsoup.parse(xmlString, "", Parser.xmlParser());
 Token doctypeToken = document.tokenizer().next();
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.name());
 assertEquals("xml.dtd", doctypeNode.systemIdentifier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNode() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.insertNode(null);
 }

 @Test
 public void testParseFragment() {
 String xmlString = "<xml><tag attr='value'><subtag/></tag></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlString, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(3, nodes.size());
 Element tag = (Element) nodes.get(0);
 Element subtag = (Element) nodes.get(1);
 TextNode textNode = (TextNode) nodes.get(2);
 assertEquals("tag", tag.tagName());
 assertEquals(0, tag.childNodes().size());
 assertEquals("subtag", subtag.tagName());
 assertEquals(0, subtag.childNodes().size());
 assertEquals("value", tag.attribute("attr"));
 assertEquals("", textNode.text());
 }

 @Test
 public void testPopStackToCloseNonExistentTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag/></xml>";
 Document document = xmlTreeBuilder.parse(xmlString, "");
 Element tag = document.getRootElement();
 xmlTreeBuilder.popStackToClose(new Token.EndTag("nonExistentTag"));
 assertEquals(1, document.childNodes().size());
 }
}