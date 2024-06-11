package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * XmlTreeBuilderTest.java
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for all methods in XmlTreeBuilder class

 // Test method: initialiseParse
 @Test
 public void testInitialiseParse() {
 // To be implemented based on available inputs
 }

 // Test method: process
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("tag", null, null);
 Element expected = new Element("tag", null);
 Element actual = xmlTreeBuilder.insert(startTag);
 assertEquals(expected.nodeName(), actual.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 // To be implemented to check if the correct element is popped out from the stack
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("comment");
 xmlTreeBuilder.insert(commentToken);
 // To be implemented to check if the comment node is correctly added to the tree
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 // To be implemented to check if the text node is correctly added to the tree
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("doctype");
 xmlTreeBuilder.insert(doctypeToken);
 // To be implemented to check if the document type node is correctly added to the tree
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "unexpectedToken");
 xmlTreeBuilder.process(unexpectedToken);
 }

 // Test method: insertNode
 @Test
 public void testInsertNode() {
 Element element = new Element("tag", null);
 xmlTreeBuilder.insertNode(element);
 // To be implemented to check if the element is correctly added to the tree
 }

 // Test method: insert
 @Test
 public void testInsertStartTag() {
 Token startTag = new Token.StartTag("tag", null, null);
 Element expected = new Element("tag", null);
 Element actual = xmlTreeBuilder.insert(startTag);
 assertEquals(expected, actual);
 }

 @Test
 public void testInsertEndTag() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 Element expected = stack.pop(); // Assuming stack is a Stack<Element>
 Element actual = null; // To be implemented to get the actual popped element
 assertEquals(expected, actual);
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("comment");
 Comment expected = new Comment("comment", null);
 Comment actual = (Comment) xmlTreeBuilder.insert(commentToken);
 assertEquals(expected.getData(), actual.getData());
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('a');
 TextNode expected = new TextNode("a", null);
 TextNode actual = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertEquals(expected.getText(), actual.getText());
 }

 @Test
 public void testInsertDocumentType() {
 Token doctypeToken = new Token.Doctype("doctype");
 DocumentType expected = new DocumentType("doctype", null, null, null);
 DocumentType actual = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals(expected.getName(), actual.getName());
 }

 // Test method: popStackToClose
 @Test
 public void testPopStackToCloseNotFound() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseFound() {
 // To be implemented based on available inputs and assuming stack is a Stack<Element>
 }
}