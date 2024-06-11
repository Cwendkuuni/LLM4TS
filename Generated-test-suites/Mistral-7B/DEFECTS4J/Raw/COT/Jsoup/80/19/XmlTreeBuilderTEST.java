package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 }

 // Test for defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test for parse method with Reader input
 @Test
 public void testParseReaderInput() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 Reader input = new StringReader(xmlString);
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 Element root = doc.rootElement();
 Element child = root.child(0);
 assertEquals("Hello", child.text());
 }

 // Test for parse method with String input
 @Test
 public void testParseStringInput() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 Document doc = xmlTreeBuilder.parse(xmlString, "http://example.com");
 Element root = doc.rootElement();
 Element child = root.child(0);
 assertEquals("Hello", child.text());
 }

 // Test for initialiseParse method
 @Test
 public void testInitialiseParse() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = parseSettings;
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), baseUri, errors, settings);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri, errors, settings);
 Element root = doc.rootElement();
 Element child = root.child(0);
 assertEquals("Hello", child.text());
 }

 // Test for process method with StartTag token
 @Test
 public void testProcessStartTag() throws Exception {
 String xmlString = "<root><child/></root>";
 Token startTagToken = new Token.StartTag("child", null, null, null, null, null, null, null, null);
 Document doc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.process(startTagToken);
 Element child = doc.child(0);
 assertEquals("child", child.tagName());
 }

 // Test for process method with EndTag token
 @Test
 public void testProcessEndTag() throws Exception {
 String xmlString = "<root><child><grandchild/></child></root>";
 Token endTagToken = new Token.EndTag("grandchild", null, null, null, null, null, null, null, null);
 Document doc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.process(endTagToken);
 Element grandchild = doc.child(0).child(0);
 assertEquals(0, doc.child(0).child(0).childCount());
 }

 // Test for process method with Comment token
 @Test
 public void testProcessComment() throws Exception {
 String xmlString = "<root><!-- This is a comment --></root>";
 Token commentToken = new Token.Comment("This is a comment ");
 Document doc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) doc.child(0);
 assertEquals("This is a comment ", comment.getData());
 }

 // Test for process method with Character token
 @Test
 public void testProcessCharacter() throws Exception {
 String xmlString = "<root>&#123;</root>";
 Token characterToken = new Token.Character('&#123;');
 Document doc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = (TextNode) doc.child(0);
 assertEquals("{\\u007B}", textNode.text());
 }

 // Test for process method with Doctype token
 @Test
 public void testProcessDoctype() throws Exception {
 String xmlString = "<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED> ]><root></root>";
 Token doctypeToken = new Token.Doctype("root", null, null);
 Document doc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) doc.child(0);
 assertEquals("root", doctypeNode.nodeName());
 assertEquals("http://www.w3.org/1998/10/XML", doctypeNode.getPublicIdentifier());
 assertEquals(null, doctypeNode.getSystemIdentifier());
 }

 // Test for popStackToClose method with existing tag on stack
 @Test
 public void testPopStackToCloseExistingTag() throws Exception {
 String xmlString = "<root><child><grandchild></grandchild></child></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
 assertEquals(0, doc.child(0).child(0).childCount());
 }

 // Test for popStackToClose method with non-existing tag on stack
 @Test (expected = AssertionError.class)
 public void testPopStackToCloseNonExistingTag() throws Exception {
 String xmlString = "<root><child></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader(xmlString), null, null, parseSettings);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(new Token.EndTag("nonExistingTag"));
 }

 // Test for parseFragment method
 @Test
 public void testParseFragment() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlString, "http://example.com", ParseErrorList.noTracking(), parseSettings);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Element child = (Element) nodes.get(1);
 assertEquals("Hello", child.text());
 }

 // Test for parseFragment method with bogus XML declaration comment
 @Test
 public void testParseFragmentWithBogusXmlDeclaration() throws Exception {
 String xmlString = "<?xml version='1.0' encoding='UTF-8'?><root><child>Hello</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlString, "http://example.com", ParseErrorList.noTracking(), parseSettings);
 assertEquals(3, nodes.size());
 DocumentType doctypeNode = (DocumentType) nodes.get(0);
 assertEquals("xml", doctypeNode.nodeName());
 assertEquals("http://www.w3.org/2000/xmlns/", doctypeNode.getPublicIdentifier());
 assertEquals("http://www.w3.org/2000/xmlns/", doctypeNode.getSystemIdentifier());
 Element root = (Element) nodes.get(1);
 Element child = (Element) nodes.get(2);
 assertEquals("Hello", child.text());
 }

}