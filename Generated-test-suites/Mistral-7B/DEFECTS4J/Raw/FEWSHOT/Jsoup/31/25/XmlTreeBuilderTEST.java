package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Test
 public void testInitialiseParse() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);
 assertNotNull(builder.doc);
 }

 @Test
 public void testProcessStartTag() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token token = new Token.StartTag("root", null, null);
 builder.process(token);

 Element root = builder.doc;
 assertNotNull(root);
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token startToken = new Token.StartTag("root", null, null);
 Token endToken = new Token.EndTag("root");
 builder.process(startToken);
 builder.process(endToken);

 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessComment() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token token = new Token.Comment("This is a comment");
 builder.process(token);

 Comment comment = (Comment) builder.stack.peek();
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token token = new Token.Character('A');
 builder.process(token);

 TextNode textNode = (TextNode) builder.stack.peek();
 assertEquals("A", textNode.data());
 }

 @Test
 public void testProcessDoctype() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token token = new Token.Doctype("html", "", "", "");
 builder.process(token);

 DocumentType documentType = (DocumentType) builder.stack.peek();
 assertEquals("html", documentType.name());
 }

 @Test
 public void testProcessUnknownTag() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token startToken = new Token.StartTag("unknown", null, null);
 builder.process(startToken);

 Element unknownElement = (Element) builder.stack.peek();
 assertNotNull(unknownElement);
 assertNull(unknownElement.tag());
 }

 @Test
 public void testProcessSelfClosingTag() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token startToken = new Token.StartTag("selfClosing", null, null, true);
 builder.process(startToken);

 Element selfClosingElement = (Element) builder.stack.peek();
 assertNotNull(selfClosingElement);
 assertNull(selfClosingElement.tag().isKnownTag());
 selfClosingElement.tag().setSelfClosing(true);
 }

 @Test
 public void testProcessNestedTags() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token startTag1 = new Token.StartTag("outer", null, null);
 builder.process(startTag1);

 Token startTag2 = new Token.StartTag("inner", null, null);
 builder.process(startTag2);

 Token endTag1 = new Token.EndTag("outer");
 builder.process(endTag1);

 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessImproperlyNestedTags() {
 builder = new XmlTreeBuilder();
 builder.initialiseParse("", "", null);

 Token startTag1 = new Token.StartTag("outer", null, null);
 builder.process(startTag1);

 Token startTag2 = new Token.StartTag("inner", null, null);
 builder.process(startTag2);

 Token endTag2 = new Token.EndTag("inner");
 builder.process(endTag2);

 Token endTag1 = new Token.EndTag("outer");
 builder.process(endTag1);

 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessMismatchedEndTags() {
 builder = new XmlTreeParser();
 builder.initialiseParse("", "", null);

 Token startTag1 = new Token.StartTag("outer", null, null);
 builder.process(startTag1);

 Token endTag2 = new Token.EndTag("inner");
 builder.process(endTag2); // should throw an exception
 }
}