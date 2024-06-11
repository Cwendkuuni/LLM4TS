package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for process method with valid start tag
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tagName", null, null);
 xmlTreeBuilder.process(startTagToken);
 Element expectedElement = new Element("tagName", null, null);
 assertEquals(expectedElement, xmlTreeBuilder.stack.peek());
 }

 // Test for process method with valid end tag
 @Test
 public void testProcessEndTag() {
 String tagName = "tagName";
 xmlTreeBuilder.initialiseParse("", "", null); // initialise stack with empty document
 xmlTreeBuilder.stack.push(new Element(tagName, "baseUri")); // push an element onto the stack
 Token endTagToken = new Token.EndTag(tagName, null);
 xmlTreeBuilder.process(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek()); // stack should be empty after processing end tag
 }

 // Test for process method with comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("commentData");
 xmlTreeBuilder.process(commentToken);
 Comment expectedComment = new Comment("commentData", "baseUri");
 assertEquals(expectedComment, xmlTreeBuilder.stack.peek());
 }

 // Test for process method with character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('c');
 xmlTreeBuilder.process(characterToken);
 TextNode expectedTextNode = new TextNode("c", "baseUri");
 assertEquals(expectedTextNode, xmlTreeBuilder.stack.peek());
 }

 // Test for process method with doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("doctypeName", "publicID", "systemID", "");
 xmlTreeBuilder.process(doctypeToken);
 DocumentType expectedDocumentType = new DocumentType("doctypeName", "publicID", "systemID", "baseUri");
 assertEquals(expectedDocumentType, xmlTreeBuilder.stack.peek());
 }

 // Test for process method with unexpected token type
 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "data", null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 // Test for popStackToClose method with tag not found
 @Test
 public void testPopStackToCloseTagNotFound() {
 Token endTagToken = new Token.EndTag("tagName", null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek());
 }

 // Test for popStackToClose method with tag found
 @Test
 public void testPopStackToCloseTagFound() {
 String tagName = "tagName";
 xmlTreeBuilder.initialiseParse("", "", null); // initialise stack with empty document
 xmlTreeBuilder.stack.push(new Element(tagName, "baseUri")); // push an element onto the stack
 xmlTreeBuilder.stack.push(new Element("outerTag", "baseUri")); // push another element onto the stack
 Token endTagToken = new Token.EndTag(tagName, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(xmlTreeBuilder.stack.peek().nodeName(), "outerTag");
 }

}