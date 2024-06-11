package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Unit tests for {@link XmlTreeBuilder} class
 */
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
 String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element attr1=\"value1\" attr2=\"value2\"></element>\n" +
         "</root>";
 Reader input = new StringReader(xmlData);
 String baseUri = "http://test.com";
 Document document = xmlTreeBuilder.parse(input, baseUri);
 assertNotNull(document);
 assertEquals("root", document.tagName());
 Element rootElement = document.element(0);
 assertEquals("element", rootElement.tagName());
 assertEquals("value1", rootElement.attribute("attr1"));
 assertEquals("value2", rootElement.attribute("attr2"));
 }

 @Test
 public void testParseWithStringAndBaseUri() {
 String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element attr1=\"value1\" attr2=\"value2\"></element>\n" +
         "</root>";
 String baseUri = "http://test.com";
 Document document = xmlTreeBuilder.parse(xmlData, baseUri);
 assertNotNull(document);
 assertEquals("root", document.tagName());
 Element rootElement = document.element(0);
 assertEquals("element", rootElement.tagName());
 assertEquals("value1", rootElement.attribute("attr1"));
 assertEquals("value2", rootElement.attribute("attr2"));
 }

 @Test
 public void testProcessStartTag() {
 String xmlData = "<root><element attr1=\"value1\" attr2=\"value2\"></element></root>";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Element> stack = new Stack<>();
 Element doc = new Element("root");
 stack.push(doc);

 Token token = new Token.StartTag("element", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.process(token);

 Element element = stack.peek();
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attribute("attr1"));
 assertEquals("value2", element.attribute("attr2"));
 }

 @Test
 public void testProcessEndTag() {
 String xmlData = "<root><element attr1=\"value1\"></element></root>";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Element> stack = new Stack<>();
 Element doc = new Element("root");
 stack.push(doc);

 Token token = new Token.EndTag("element");
 xmlTreeBuilder.process(token);

 Element element = stack.pop();
 assertEquals("element", element.tagName());
 assertTrue(element.hasAttrs());
 assertFalse(stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 String xmlData = "<!-- This is a comment -->";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Node> stack = new Stack<>();
 Node documentNode = document.childNodes().get(0);
 stack.push(documentNode);

 Token token = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(token);

 Comment comment = (Comment) stack.peek();
 assertEquals("This is a comment", comment.text());
 assertTrue(stack.size() == 1);
 }

 @Test
 public void testProcessCharacter() {
 String xmlData = "<root>&#x1F600;</root>";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Node> stack = new Stack<>();
 Node documentNode = document.childNodes().get(0);
 stack.push(documentNode);

 Token token = new Token.Character('#', "x1F600");
 xmlTreeBuilder.process(token);

 TextNode textNode = (TextNode) stack.peek();
 assertEquals("\uD83D\uDE00", textNode.text());
 assertTrue(stack.size() == 1);
 }

 @Test
 public void testProcessDoctype() {
 String xmlData = "<!DOCTYPE root [ <!ATTLIST element attr1 CDATA #REQUIRED > ]>\n<root><element attr1=\"value\"></element></root>";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Node> stack = new Stack<>();
 Node documentNode = document.childNodes().get(0);
 stack.push(documentNode);

 Token token = new Token.Doctype("root");
 xmlTreeBuilder.process(token);

 DocumentType documentType = (DocumentType) stack.peek();
 assertEquals("root", documentType.nodeName());
 assertNotNull(documentType.publicId());
 assertNotNull(documentType.systemId());
 assertTrue(stack.size() == 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessTokenWithUnknownType() {
 String xmlData = "<root></root>";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Node> stack = new Stack<>();
 Node documentNode = document.childNodes().get(0);
 stack.push(documentNode);

 Token token = new Token();
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testPopStackToClose() {
 String xmlData = "<root><a><b></b></a></root>";
 Document document = Jsoup.parse(xmlData, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Stack<Element> stack = new Stack<>();
 Element doc = new Element("root");
 stack.push(doc);

 for (Element element : document.elements()) {
 xmlTreeBuilder.process(element.token());
 }

 assertEquals(4, stack.size());
 xmlTreeBuilder.popStackToClose(new Token.EndTag("b"));
 assertEquals(3, stack.size());
 xmlTreeBuilder.popStackToClose(new Token.EndTag("a"));
 assertEquals(2, stack.size());
 }

 @Test
 public void testProcessFragment() {
 String xmlData = "<root><element attr1=\"value1\" attr2=\"value2\"></element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlData, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(4, nodes.size());
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());
 Element element = (Element) nodes.get(1);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attribute("attr1"));
 assertEquals("value2", element.attribute("attr2"));
 }

 @Test
 public void testProcessFragmentWithXmlDeclaration() {
 String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr1=\"value1\" attr2=\"value2\"></element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlData, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(5, nodes.size());
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());
 DocumentDeclaration xmlDeclaration = (DocumentDeclaration) nodes.get(1);
 assertEquals("xml", xmlDeclaration.nodeName());
 assertEquals("1.0", xmlDeclaration.xmlVersion());
 Element element = (Element) nodes.get(2);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attribute("attr1"));
 assertEquals("value2", element.attribute("attr2"));
 }
}