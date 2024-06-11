package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithReaderAndBaseUri() {
 String xmlInput = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
 assertNotNull(xmlDocument);
 assertEquals("<xml><root><child>Hello World</child></root></xml>", xmlDocument.html());
 }

 @Test
 public void testParseWithStringAndBaseUri() {
 String xmlInput = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 Document xmlDocument = xmlTreeBuilder.parse(xmlInput, baseUri);
 assertNotNull(xmlDocument);
 assertEquals("<xml><root><child>Hello World</child></root></xml>", xmlDocument.html());
 }

 @Test
 public void testInitialiseParse() {
 String xmlInput = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, errors, settings);
 assertEquals("http://example.com", xmlTreeBuilder.doc.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessTokenWithInvalidTokenType() {
 Token invalidToken = new Token(-1, 1, "InvalidToken");
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testInsertNodeWithNullNode() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element rootElement = new Element("root");
 xmlTreeBuilder.insertNode(null);
 assertEquals(0, rootElement.childNodes().size());
 }

 @Test
 public void testInsertElement() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<xml><root><child>Hello World</child></root></xml>";
 Document xmlDocument = Jsoup.parse(xmlInput, ParseSettings.preserveCase());
 Element rootElement = xmlDocument.select("root").first();
 xmlTreeBuilder.insert(rootElement.getElementsByTag("child").first().asStartTag());
 Element newChildElement = rootElement.child(1);
 assertEquals("Hello World", newChildElement.text());
 }

 @Test
 public void testInsertComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<!-- This is a comment -->";
 Document xmlDocument = Jsoup.parse(xmlInput, ParseSettings.preserveCase());
 xmlTreeBuilder.insert(xmlDocument.select("comment()").first().asToken());
 Comment newComment = xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("This is a comment", newComment.getData());
 }

 @Test
 public void testInsertCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<xml><root>&amp; this is a test</root></xml>";
 Document xmlDocument = Jsoup.parse(xmlInput, ParseSettings.preserveCase());
 xmlTreeBuilder.insert(xmlDocument.body().child(0).asToken());
 TextNode newTextNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("&amp; this is a test", newTextNode.text());
 }

 @Test
 public void testInsertDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
 Document xmlDocument = Jsoup.parse(xmlInput, ParseSettings.preserveCase());
 xmlTreeBuilder.insert(xmlDocument.head().child(0).asToken());
 DocumentType newDoctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", newDoctypeNode.nodeName());
 }

 @Test
 public void testPopStackToCloseWithNoMatchingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<xml><root><child><grandchild></grandchild></child></root></xml>";
 Document xmlDocument = Jsoup.parse(xmlInput, ParseSettings.preserveCase());
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
 assertEquals(2, xmlDocument.childNodes().size());
 }

 @Test
 public void testPopStackToCloseWithMatchingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<xml><root><child><grandchild></grandchild></child></root></xml>";
 Document xmlDocument = Jsoup.parse(xmlInput, ParseSettings.preserveCase());
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertEquals(1, xmlDocument.childNodes().size());
 }

 @Test
 public void testParseFragmentWithNullInputFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 List<Node> nodes = xmlTreeBuilder.parseFragment(null, baseUri, errors, settings);
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithEmptyInputFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 String xmlInput = "";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, baseUri, errors, settings);
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithInvalidXmlInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 String xmlInput = "<xml<root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, baseUri, errors, settings);
 assertTrue(errors.size() > 0);
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithValidXmlInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 String xmlInput = "<xml><root><child>Hello World</child></root></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, baseUri, errors, settings);
 assertEquals(2, nodes.size());
 }

}