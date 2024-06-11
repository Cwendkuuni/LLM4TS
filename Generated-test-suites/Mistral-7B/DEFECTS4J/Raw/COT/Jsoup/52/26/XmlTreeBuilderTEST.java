package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><tag attr1='value1'>Content<tag2 attr2='value2' /></tag></root>", "", Parser.xmlParser());
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("input", "", new ParseErrorList());
 assertEquals("input", xmlTreeBuilder.input());
 assertEquals("", xmlTreeBuilder.baseUri());
 assertNotNull(xmlTreeBuilder.errors());
 stack.push(doc); // manually add document to the stack since we can't mock Stack in JUnit
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullInput() {
 xmlTreeBuilder.initialiseParse(null, "", new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullBaseUri() {
xmlTreeBuilder.initialiseParse("input", null, new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullErrors() {
 xmlTreeBuilder.initialiseParse("input", "", null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.TokenType.START_TAG, "<tag attr1='value1'>", 1, 1, 10);
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlTreeBuilder, "input");
 xmlTreeBuilder.process(startTagToken);
 Element tagElement = doc.select("tag").first();
 assertEquals(tagElement, xmlTreeBuilder.currentElement());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithoutMatchingStartTag() {
 Token endTagToken = new Token(Token.TokenType.END_TAG, "</tag2>", 1, 1, 15);
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlTreeBuilder, "input");
 xmlTreeBuilder.process(endTagToken);
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.TokenType.COMMENT, "-- This is a comment -->", 1, 1, 12);
 xmlTreeBuilder.process(commentToken);
 assertEquals(1, doc.childNodes().size());
 assertEquals(commentToken.getData(), doc.childNodes().get(0).outerHtml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessCharacterWithNullData() {
 Token characterToken = new Token(Token.TokenType.CHARACTER, null, 1, 1, 1);
 xmlTreeBuilder.process(characterToken);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.TokenType.CHARACTER, "a", 1, 1, 1);
 xmlTreeBuilder.process(characterToken);
 assertEquals(1, doc.childNodes().size());
 TextNode textNode = (TextNode) doc.childNodes().get(0);
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.TokenType.DOCTYPE, "<!DOCTYPE root [ <!ATTLIST tag attr1 CDATA #REQUIRED > ]>", 1, 1, 1);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType documentType = doc.doctype();
 assertEquals("root", documentType.getName());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><tag attr1='value1'>Content<tag2 attr2='value2' /></tag></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 assertEquals(3, nodes.size());
 Element rootElement = (Element) nodes.get(0);
 assertEquals("tag", rootElement.childNodes().get(0).nodeName());
 assertEquals("tag2", rootElement.childNodes().get(1).nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "", new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 xmlTreeBuilder.parseFragment("input", null, new ParseErrorList());
 }
}