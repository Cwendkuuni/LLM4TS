package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><tag attr='value'>Hello World</tag></root>", "", parser);
 }

 @Test
 public void testProcessStartTag() {
 Element tag = parser.insert(new Token.StartTag("tag"));
 assertEquals("tag", tag.nodeName());
 assertEquals("value", tag.tagName());
 assertEquals("", tag.tagText());
 assertTrue(tag.hasAttrSpecified("attr"));
 assertEquals("value", tag.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 Element tag = doc.selectElements("tag").first();
 parser.popStackToClose(new Token.EndTag("tag"));
 assertNull(tag);
 }

 @Test
 public void testProcessComment() {
 parser.insert(new Token.Comment("-- This is a comment --"));
 List<Node> comments = doc.getElementsByTag("comment");
 assertEquals(1, comments.size());
 assertEquals("-- This is a comment --", comments.get(0).text());
 }

 @Test
 public void testProcessCharacter() {
 parser.insert(new Token.Character('?'));
 List<Node> textNodes = doc.getElementsByTag("text");
 assertEquals(2, textNodes.size());
 assertEquals("Hello World", textNodes.get(1).text());
 assertEquals("?", textNodes.get(0).text());
 }

 @Test
 public void testProcessDoctype() {
 parser.insert(new Token.Doctype(new Document.InternalSerializer(), "<!DOCTYPE root SYSTEM \"test.dtd\" [ <!ATTLIST tag attr CDATA #REQUIRED > ]>"));
 List<Node> doctypeNodes = doc.getElementsByTag("!DOCTYPE");
 assertEquals(1, doctypeNodes.size());
 DocumentType doctype = (DocumentType) doctypeNodes.get(0);
 assertEquals("root", doctype.nodeName());
 assertFalse(doctype.isVoidElement());
 assertTrue(doctype.entitiesUsed().isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testValidateUnexpectedTokenType() {
 parser = new XmlTreeBuilder();
 parser.process(new Token(200)); // Unexpected token type: 200
 }

 @Test
 public void testParseFragment() {
 List<Node> fragment = parser.parseFragment("<tag attr='value'>Hello</tag><tag attr='value'>World</tag>", "", new ParseErrorList());
 assertEquals(2, fragment.size());
 assertInstanceOf(Element.class, fragment.get(0));
 assertInstanceOf(Element.class, fragment.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 parser.parseFragment(null, "", new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 parser.parseFragment("", "", new ParseErrorList());
 }
}