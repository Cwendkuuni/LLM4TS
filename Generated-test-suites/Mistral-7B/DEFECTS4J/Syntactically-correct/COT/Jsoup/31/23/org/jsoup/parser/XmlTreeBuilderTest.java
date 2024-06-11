package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder = new XmlTreeBuilder();
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcess_StartTag() {
 Token startTagToken = new Token.StartTag("tagName", null, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.process(startTagToken);
 Element expectedElement = new Element("tagName", "", null);
 assertEquals(expectedElement, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcess_EndTag() {
 Token endTagToken = new Token.EndTag("tagName");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 Element startTagElement = new Element("tagName", "", null);
 xmlTreeBuilder.stack.push(startTagElement);
 xmlTreeBuilder.process(endTagToken);
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 @Test(expected = AssertionError.class)
 public void testProcess_UnexpectedTokenType() {
 Token unexpectedToken = new Token(123, "Some Unexpected Token");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testProcess_Comment() {
 Token commentToken = new Token.Comment("Some Comment");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.process(commentToken);
 Comment expectedComment = new Comment("Some Comment", "");
 Node expectedNode = expectedComment;
 assertEquals(expectedNode, xmlTreeBuilder.current.childNodes().get(0));
 }

 @Test
 public void testProcess_Character() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.process(characterToken);
 TextNode expectedNode = new TextNode("A", "");
 assertEquals(expectedNode, xmlTreeBuilder.current.childNodes().get(0));
 }

 @Test
 public void testProcess_DocType() {
 Token doctypeToken = new Token.Doctype("html", null, null, "");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType expectedNode = new DocumentType("html", null, null, "");
 assertEquals(expectedNode, xmlTreeBuilder.current);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 Element startTagElement1 = new Element("tag1", "", null);
 xmlTreeBuilder.stack.push(startTagElement1);
 Element startTagElement2 = new Element("tag2", "", null);
 xmlTreeBuilder.stack.push(startTagElement2);
 Element endTagElement = new Element("tag2", "", null);
 xmlTreeBuilder.popStackToClose(endTagElement);
 assertEquals(xmlTreeBuilder.stack.size(), 1);
 assertEquals(xmlTreeBuilder.stack.peek(), startTagElement1);
 }

 @Test
 public void testPopStackToClose_EmptyStack() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tagName"));
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

}