package org.jsoup.parser;

import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertNotNull;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertSame;import static org.junit.Assert.assertThrows;import static org.junit.Assert.assertTrue;import static org.junit.Assert.fail;

import java.io.StringReader;import java.util.List;

import org.jsoup.nodes.Attribute;import org.jsoup.nodes.CDataNode;import org.jsoup.nodes.Comment;import org.jsoup.nodes.Document;import org.jsoup.nodes.DocumentType;import org.jsoup.nodes.Element;import org.jsoup.nodes.TextNode;

/**
 * Unit tests for {@link XmlTreeBuilder} class
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;

 @Before public void setup() { builder = new XmlTreeBuilder(); doc = new Document(); }

 /**
 * Test defaultSettings method
 */
 @Test public void testDefaultSettings() { ParseSettings settings = builder.defaultSettings(); assertEquals(ParseSettings.PreserveCase.class, settings.getClass()); }

 /**
 * Test initialiseParse method with valid input
 */
 @Test public void testInitialiseParseValidInput() { StringReader input = new StringReader("<root><child/></root>"); builder.initialiseParse(input, "", new Parser(builder)); assertNotNull(builder.getDoc()); }

 /**
 * Test initialiseParse method with null input
 */
 @Test(expected = NullPointerException.class) public void testInitialiseParseNullInput() { builder.initialiseParse(null, "", new Parser(builder)); }

 /**
 * Test initialiseParse method with empty input
 */
 @Test public void testInitialiseParseEmptyInput() { StringReader input = new StringReader(""); builder.initialiseParse(input, "", new Parser(builder)); assertNotNull(builder.getDoc()); assertEquals(0, builder.getDoc().childNodes().size()); }

 /**
 * Test parse method with valid input
 */
 @Test public void testParseValidInput() { Document parsed = builder.parse(new StringReader("<root><child/></root>"), ""); assertEquals(doc, parsed); }

 /**
 * Test parse method with null input
 */
 @Test(expected = NullPointerException.class) public void testParseNullInput() { builder.parse(null, ""); fail("Expected NullPointerException"); }

 /**
 * Test parse method with empty input
 */
 @Test public void testParseEmptyInput() { Document parsed = builder.parse(new StringReader(""), ""); assertEquals(doc, parsed); assertEquals(0, parsed.childNodes().size()); }

 /**
 * Test process method with start tag
 */
 @Test public void testProcessStartTag() { builder.pushTag("root"); assertEquals("root", builder.currentElement().nodeName()); }

 /**
 * Test process method with end tag
 */
 @Test public void testProcessEndTag() { builder.pushTag("root"); builder.pushTag("child"); builder.popTag(); assertEquals("root", builder.currentElement().nodeName()); }

 /**
 * Test process method with comment
 */
 @Test public void testProcessComment() { builder.pushTag("root"); builder.addInput("<!-- This is a comment -->"); assertEquals("root", builder.currentElement().nodeName()); assertNotNull(builder.getDoc().childNodes().get(0)); assertEquals("This is a comment", ((Comment) builder.getDoc().childNodes().get(0)).text()); }

 /**
 * Test process method with character
 */
 @Test public void testProcessCharacter() { builder.pushTag("root"); builder.addInput("Hello World"); assertEquals("root", builder.currentElement().nodeName()); assertNotNull(builder.getDoc().childNodes().get(0)); assertEquals("Hello World", ((TextNode) builder.getDoc().childNodes().get(0)).text()); }

 /**
 * Test process method with doctype
 */
 @Test public void testProcessDoctype() { builder.pushTag("root"); builder.addInput("<?xml version='1.0'?>"); assertEquals("root", builder.currentElement().nodeName()); assertNotNull(builder.getDoc().childNodes().get(0)); assertTrue(builder.getDoc().childNodes().get(0) instanceof DocumentType); }

 /**
 * Test insertNode method with element
 */
 @Test public void testInsertNodeElement() { Element element = new Element("child"); builder.pushTag("root"); builder.insertNode(element); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); assertSame(element, builder.getDoc().childNodes().get(0)); }

 /**
 * Test insertNode method with comment
 */
 @Test public void testInsertNodeComment() { Comment comment = new Comment("This is a comment"); builder.pushTag("root"); builder.insertNode(comment); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); assertSame(comment, builder.getDoc().childNodes().get(0)); }

 /**
 * Test insertNode method with text node
 */
 @Test public void testInsertNodeTextNode() { TextNode textNode = new TextNode("Hello World"); builder.pushTag("root"); builder.insertNode(textNode); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); assertSame(textNode, builder.getDoc().childNodes().get(0)); }

 /**
 * Test insertNode method with cdata node
 */
 @Test public void testInsertNodeCdataNode() { CDataNode cdataNode = new CDataNode("<![CDATA[Hello World]]>"); builder.pushTag("root"); builder.insertNode(cdataNode); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); assertSame(cdataNode, builder.getDoc().childNodes().get(0)); }

 /**
 * Test insertNode method with document type node
 */
 @Test public void testInsertNodeDocumentTypeNode() { DocumentType documentTypeNode = new DocumentType("html", null, null); builder.pushTag("root"); builder.insertNode(documentTypeNode); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); assertSame(documentTypeNode, builder.getDoc().childNodes().get(0)); }

 /**
 * Test popStackToClose method with unknown tag
 */
 @Test public void testPopStackToCloseUnknownTag() { builder.pushTag("root"); builder.pushTag("unknown"); builder.popStackToClose("unknown"); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); }

 /**
 * Test popStackToClose method with known tag
 */
 @Test public void testPopStackToCloseKnownTag() { builder.pushTag("root"); builder.pushTag("child"); builder.popStackToClose("child"); assertEquals("root", builder.currentElement().nodeName()); assertEquals(1, builder.getDoc().childNodes().size()); assertNotNull(builder.getDoc().childNodes().get(0)); }

 /**
 * Test popStackToClose method with no matching tag on stack
 */
 @Test public void testPopStackToCloseNoMatchingTag() { builder.pushTag("root"); assertEquals("root", builder.currentElement().nodeName()); builder.popStackToClose("child"); assertEquals("root", builder.currentElement().nodeName()); }

 /**
 * Test parseFragment method with valid input
 */
 @Test public void testParseFragmentValidInput() { String input = "<root><child/></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(2, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("child", nodes.get(1).nodeName()); }

 /**
 * Test parseFragment method with null input
 */
 @Test(expected = NullPointerException.class) public void testParseFragmentNullInput() { builder.parseFragment(null, "", ""); fail("Expected NullPointerException"); }

 /**
 * Test parseFragment method with empty input
 */
 @Test public void testParseFragmentEmptyInput() { List<Node> nodes = builder.parseFragment("", "", ""); assertEquals(0, nodes.size()); }

 /**
 * Test parseFragment method with input containing unknown tag
 */
 @Test public void testParseFragmentUnknownTag() { String input = "<root><child><unknown/></child></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(3, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("child", nodes.get(1).nodeName()); assertNull(nodes.get(2)); }

 /**
 * Test parseFragment method with input containing self closing tag
 */
 @Test public void testParseFragmentSelfClosingTag() { String input = "<root><child/></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(2, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("child", nodes.get(1).nodeName()); assertFalse(nodes.get(1).hasChildren()); }

 /**
 * Test parseFragment method with input containing attributes
 */
 @Test public void testParseFragmentAttributes() { String input = "<root id='rootId' attr='value'><child attr='value2'></child></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(3, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("root", nodes.get(0).id()); assertEquals("value", nodes.get(0).attr("id")); assertEquals("child", nodes.get(1).nodeName()); assertEquals("value2", nodes.get(1).attr("attr")); }

 /**
 * Test parseFragment method with input containing comments
 */
 @Test public void testParseFragmentComments() { String input = "<root><!-- This is a comment --><child><!-- Another comment --></child></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(4, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("!-- This is a comment -->", nodes.get(1).text()); assertEquals("child", nodes.get(2).nodeName()); assertEquals("!-- Another comment -->", nodes.get(3).text()); }

 /**
 * Test parseFragment method with input containing cdata
 */
 @Test public void testParseFragmentCdata() { String input = "<root><![CDATA[This is some CDATA]]><child></child></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(3, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertNotNull(nodes.get(1)); assertTrue(nodes.get(1) instanceof TextNode); assertEquals("This is some CDATA", ((TextNode) nodes.get(1)).text()); assertEquals("child", nodes.get(2).nodeName()); }

 /**
 * Test parseFragment method with input containing document type declaration
 */
 @Test public void testParseFragmentDocumentTypeDeclaration() { String input = "<!DOCTYPE root SYSTEM 'test.dtd' [ <!ATTLIST root id CDATA #REQUIRED> ]><root id='test'></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(2, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("test", nodes.get(0).attr("id")); }

 /**
 * Test parseFragment method with input containing multiple root elements
 */
 @Test public void testParseFragmentMultipleRoots() { String input = "<root><child/></root><root><child/></root>"; List<Node> nodes = builder.parseFragment(input, "", ""); assertEquals(4, nodes.size()); assertEquals("root", nodes.get(0).nodeName()); assertEquals("child", nodes.get(1).nodeName()); assertEquals("root", nodes.get(2).nodeName()); assertEquals("child", nodes.get(3).nodeName()); }
}