package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokeniser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokeniser = new Tokenizer();
 }

 @Test
 public void testInitialiseParse() {
 Document doc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "", new ParseErrorList());
 xmlTreeBuilder.stack.add(doc);
 Assert.assertEquals(doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tagName", null, new Attribute("attributeName", "attributeValue"));
 xmlTreeBuilder.process(startTagToken);
 Element el = xmlTreeBuilder.stack.peek();
 Tag tag = Tag.valueOf("tagName");
 Assert.assertEquals(tag, el.tagName());
 Assert.assertEquals("attributeValue", el.getAttribute("attributeName"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tagName");
 xmlTreeBuilder.popStackToClose(endTagToken);
 Assert.assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("commentData");
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.stack.peek();
 Assert.assertEquals("commentData", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('c');
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.stack.peek();
 Assert.assertEquals("c", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("doctypeName", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.peek();
 Assert.assertEquals("doctypeName", doctypeNode.name());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(123);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToCloseWithNotFound() {
 Token endTagToken = new Token.EndTag("tagName");
 xmlTreeBuilder.popStackToClose(endTagToken);
 Assert.assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseWithFound() {
 Token startTagToken = new Token.StartTag("tagName", null, new Attribute("attributeName", "attributeValue"));
 Token endTagToken = new Token.EndTag("tagName");
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 xmlTreeBuilder.popStackToClose(endTagToken);
 Assert.assertNull(xmlTreeBuilder.stack.peek());
 }
}