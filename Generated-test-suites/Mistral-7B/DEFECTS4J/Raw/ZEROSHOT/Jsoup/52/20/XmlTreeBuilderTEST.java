package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("<test><element attribute='value'/><otherElement/></test>", null, new ParseErrorList());
 }

 @Test
 public void testInitialiseParse() {
 // Test if document is correctly initialised
 assertNotNull(document);
 assertEquals("test", document.tagName());
 }

 @Test
 public void testProcessStartTag() {
 // Test inserting an element with attributes
 Token startTag = new Token.StartTag("element", null, new Attribute("attribute", "value"));
 xmlTreeBuilder.insert(startTag);
 Element element = (Element) document.childNodes().get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attribute").get());

 // Test inserting a self-closing tag
 Token selfClosingStartTag = new Token.StartTag("otherElement", null, null, true);
 xmlTreeBuilder.insert(selfClosingStartTag);
 assertNull(document.childNodes().get(1));
 }

 @Test
 public void testProcessEndTag() {
 // Test popping an element from the stack
 Token endTag = new Token.EndTag("element");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(document.childNodes().get(0));
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 // Test validation of unexpected token type
 Token unexpectedToken = new Token(1, "UnexpectedToken", null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testProcessComment() {
 // Test inserting a comment
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertNotNull(document.childNodes().get(document.childNodes().size() - 1));
 assertEquals("This is a comment", ((Comment) document.childNodes().get(document.childNodes().size() - 1)).text());
 }

 @Test
 public void testProcessCharacter() {
 // Test inserting a character
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 assertNotNull(document.childNodes().get(document.childNodes().size() - 1));
 assertEquals("A", ((TextNode) document.childNodes().get(document.childNodes().size() - 1)).text());
 }

 @Test
 public void testProcessDoctype() {
 // Test inserting a doctype
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 assertNotNull(document.childNodes().get(0));
 assertEquals("html", ((DocumentType) document.childNodes().get(0)).nodeName());
 }
}