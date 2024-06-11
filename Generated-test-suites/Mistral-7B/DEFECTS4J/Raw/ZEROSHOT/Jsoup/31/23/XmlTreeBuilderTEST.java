package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseErrorList parseErrorList;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseErrorList = new ParseErrorList();
 xmlTreeBuilder.initialiseParse("", "", parseErrorList);
 doc = xmlTreeBuilder.doc;
 }

 @Test
 public void testProcessStartTag() {
 // Test processStartTag with valid tag
 Token startTagToken = new Token.StartTag("tag", null, new Attribute("attr", "value"), null);
 xmlTreeBuilder.process(startTagToken);
 Element tagElement = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("tag", tagElement.nodeName());
 assertEquals("value", tagElement.attribute("attr").getValue());
 }

 @Test(expected = AssertionError.class)
 public void testProcessStartTagInvalidToken() {
 // Test processStartTag with invalid token
 Token invalidToken = new Token(100, null);
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testProcessEndTag() {
 // Test processEndTag with valid tag
 Token endTagToken = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = AssertionError.class)
 public void testProcessEndTagInvalidTag() {
 // Test processEndTag with invalid tag
 Token endTagToken = new Token.EndTag("invalidTag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 }

 @Test
 public void testProcessComment() {
 // Test processComment with valid comment
 Token commentToken = new Token.Comment("this is a comment");
 xmlTreeBuilder.process(commentToken);
 assertEquals(1, doc.childNodes().size());
 assertEquals("this is a comment", doc.childNodes().item(0).nodeValue());
 }

 @Test
 public void testProcessCharacter() {
 // Test processCharacter with valid character
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.process(characterToken);
 Element textNode = (Element) doc.childNodes().item(0);
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 // Test processDoctype with valid doctype
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) doc.childNodes().item(0);
 assertEquals("html", doctypeNode.nodeName());
 assertEquals(null, doctypeNode.publicIdentifier());
 assertEquals(null, doctypeNode.systemIdentifier());
 }

 @Test
 public void testPopStackToClose() {
 // Test popStackToClose with valid tag name
 List<Element> stackElements = new ArrayList<>();
 stackElements.add(new Element("outer", null, new ArrayList<>()));
 stackElements.add(new Element("inner", null, new ArrayList<>()));
 xmlTreeBuilder.stack = stackElements;
 Token endTagToken = new Token.EndTag("inner");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("outer", xmlTreeBuilder.stack.peek().nodeName());
 }

 @Test
 public void testPopStackToCloseInvalidTag() {
 // Test popStackToClose with invalid tag name
 List<Element> stackElements = new ArrayList<>();
 stackElements.add(new Element("outer", null, new ArrayList<>()));
 xmlTreeBuilder.stack = stackElements;
 Token endTagToken = new Token.EndTag("invalidTag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(1, xmlTreebuilder.stack.size());
 assertEquals("outer", xmlTreeBuilder.stack.peek().nodeName());
 }
}