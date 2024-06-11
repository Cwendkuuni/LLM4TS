package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Tokenizer tokeniser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 tokeniser = new Tokenizer("", doc);
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 assertEquals(doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("tag", null, new Attribute("attr", "value"));
 Element expectedElement = new Element("tag", "baseUri", new Attribute("attr", "value"));
 xmlTreeBuilder.insert(startTag);
 assertEquals(expectedElement, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("comment");
 xmlTreeBuilder.insert(commentToken);
 assertNotNull(xmlTreebuilder.doc.body().child(0));
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('c');
 xmlTreeBuilder.insert(characterToken);
 assertEquals("c", xmlTreeBuilder.doc.body().text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("doctype");
 xmlTreeBuilder.insert(doctypeToken);
 assertNotNull(xmlTreeBuilder.doc.head());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token.UnknownToken("unknownType");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToCloseNoneFound() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.stack.peek());
 }
}