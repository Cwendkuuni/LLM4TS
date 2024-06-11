package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * XmlTreeBuilderTest.java
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" ><root/></xml>";
 xmlTreeBuilder.initialiseParse(input, null, new ParseErrorList());
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullInput() {
 xmlTreeBuilder.initialiseParse(null, null, new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullBaseUri() {
 xmlTreeBuilder.initialiseParse("<xml version=\"1.0\" ><root/></xml>", null, new ParseErrorList());
 }

 @Test
 public void testProcessStartTag() {
 Token startToken = new Token(Token.StartTag.class, "<root>", 0, 3, 4, null);
 Element rootElement = xmlTreeBuilder.insert(startToken);
 assertTrue(rootElement != null);
 }

 @Test
 public void testProcessEndTag() {
 Token endToken = new Token(Token.EndTag.class, "</root>", 0, 3, 4, null);
 xmlTreeBuilder.popStackToClose(endToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "-- My Comment --", 0, 3, 4, null);
 xmlTreeBuilder.insert(commentToken);
 assertTrue(xmlTreeBuilder.doc.childNodes().size() == 1);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, "Some Text", 0, 3, 4, null);
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("Some Text", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE html SYSTEM \"about:legacy-compat\">", 0, 3, 4, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("html", documentType.nodeName());
 }

 @Test
 public void testProcessUnknownTagAsSelfClosing() {
 Token startTag = new Token(Token.StartTag.class, "<unknowntag />", 0, 3, 4, null);
 Element element = xmlTreeBuilder.insert(startTag);
 assertNotNull(element);
 assertFalse(element.hasChildren());
 }

 @Test
 public void testProcessUnknownTagAsNonSelfClosing() {
 Token startTag = new Token(Token.StartTag.class, "<unknowntag>", 0, 3, 4, null);
 Element element = xmlTreeBuilder.insert(startTag);
 assertNotNull(element);
 assertTrue(element.hasChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnknownTagWithNullName() {
 Token startTag = new Token(Token.StartTag.class, null, 0, 3, 4, null);
 xmlTreeBuilder.insert(startTag);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testPopStackToCloseWithEmptyStack() {
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</root>", 0, 3, 4, null));
 }

 @Test
 public void testPopStackToCloseWithNonMatchingTagName() {
 xmlTreeBuilder.initialiseParse("<xml version=\"1.0\" ><root><foo/><bar/></root></xml>", null, new ParseErrorList());
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</bar>", 0, 3, 4, null)); // non-matching tag name
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</foo>", 0, 3, 4, null)); // matching tag name
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }
}