package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Testing defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Testing parse method with String input
 @Test
 public void testParseStringInput() {
 String xmlString = "<root><child><grandchild>Hello</grandchild></child></root>";
 Document xmlDocument = xmlTreeBuilder.parse(xmlString, "");
 Element root = xmlDocument.getRootElement();
 assertEquals("root", root.tagName());
 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 Element grandchild = child.getChild(0);
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("Hello", grandchild.text());
 }

 // Testing parse method with Reader input
 @Test
 public void testParseReaderInput() {
 String xmlString = "<root><child><grandchild>Hello</grandchild></child></root>";
 Reader reader = new StringReader(xmlString);
 Document xmlDocument = xmlTreeBuilder.parse(reader, "");
 Element root = xmlDocument.getRootElement();
 assertEquals("root", root.tagName());
 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 Element grandchild = child.getChild(0);
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("Hello", grandchild.text());
 }

 // Testing initialiseParse method
 @Test
 public void testInitialiseParse() {
 String xmlString = "<root><child><grandchild>Hello</grandchild></child></root>";
 Reader reader = new StringReader(xmlString);
 Document xmlDocument = new Document(); // Creating an empty document to test initialiseParse method
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(reader, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(xmlDocument, xmlTreeBuilder.doc); // doc should be the same instance as the one passed to initialiseParse method
 }

 // Testing process method with StartTag
 @Test
 public void testProcessStartTag() {
 String xmlString = "<root><child></child></root>";
 Token token = Jsoup.parse(xmlString, "").body().select("*").first(); // getting the first token of the xml document
 xmlTreeBuilder.process(token);
 Element root = xmlTreeBuilder.doc.getRootElement();
 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 }

 // Testing process method with EndTag
 @Test
 public void testProcessEndTag() {
 String xmlString = "<root><child><grandchild></grandchild></child></root>";
 Token token = Jsoup.parse(xmlString, "").body().select("*").get(2); // getting the third token of the xml document, which is the EndTag
 xmlTreeBuilder.process(token);
 assertEquals(xmlTreeBuilder.stack.size(), 1); // stack size should be 1 after popping the Element
 }

 // Testing process method with Comment
 @Test
 public void testProcessComment() {
 String xmlString = "<!--This is a comment-->";
 Token token = Jsoup.parse(xmlString, "").body().select("*").first(); // getting the first token of the xml document, which is the Comment
 xmlTreeBuilder.process(token);
 assertEquals(xmlTreeBuilder.doc.childNodes().size(), 1); // comment node should be the only child node of the document
 }

 // Testing process method with Character
 @Test
 public void testProcessCharacter() {
 String xmlString = "<root>&#x26;Hello</root>";
 Token token = Jsoup.parse(xmlString, "").body().select("*").first(); // getting the first token of the xml document, which is the Character
 xmlTreeBuilder.process(token);
 assertEquals(xmlTreeBuilder.doc.childNodes().get(0).toString(), "&#x26;Hello");
 }

 // Testing process method with Doctype
 @Test
 public void testProcessDoctype() { // Need to provide a valid xml document with Doctype for proper testing
 String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">"; // Invalid Doctype for testing purpose
 Token token = Jsoup.parse(xmlString, "").body().select("*").first(); // getting the first token of the xml document, which is the Doctype
 xmlTreeBuilder.process(token);
 assertEquals(xmlTreeBuilder.doc.childNodes().get(0).toString(), xmlString);
 }

 // Testing popStackToClose method with unknown tag
 @Test(expected = RuntimeException.class)
 public void testPopStackToCloseUnknownTag() { // Need to provide a proper input for testing
 String xmlString = "<root><unknown></root>";
 Token startTag = Jsoup.parse(xmlString, "").body().select("*").first();
 xmlTreeBuilder.process(startTag); // Processing the StartTag
 xmlTreeBuilder.process(startTag); // Processing the EndTag with the same tag name, but not found in the stack
 xmlTreeBuilder.popStackToClose(startTag.asEndTag()); // Popping the stack to close the unknown tag
 }

 // Testing popStackToClose method with known tag
 @Test
 public void testPopStackToCloseKnownTag() {
 String xmlString = "<root><child><grandchild></grandchild></child></root>";
 Token startTag = Jsoup.parse(xmlString, "").body().select("child").get(0); // getting the StartTag of the child element
 xmlTreeBuilder.process(startTag); // Processing the StartTag
 xmlTreeBuilder.process(startTag); // Processing the EndTag of the child element
 Element child = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1); // getting the last Element in the stack, which is the child element
 xmlTreeBuilder.popStackToClose(startTag.asEndTag()); // Popping the stack to close the child element
 assertEquals(xmlTreeBuilder.stack.size(), 1); // stack size should be 1 after popping the root element
 }

 // Testing parseFragment method
 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child><grandchild>Hello</grandchild></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(nodes.get(0).nodeName(), "root");
 }

}