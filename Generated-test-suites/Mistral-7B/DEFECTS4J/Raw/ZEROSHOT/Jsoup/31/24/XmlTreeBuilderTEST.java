package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.stack = new Stack<>();
 xmlTreeBuilder.stack.push(doc);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tag", null, new Attribute("attr", "value"));
 Element expectedElement = new Element("tag", "", new Attribute("attr", "value"));
 xmlTreeBuilder.insert(startTagToken);
 Element actualElement = xmlTreeBuilder.currentElement();
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 assertEquals(expectedElement.baseUri(), actualElement.baseUri());
 assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("comment");
 xmlTreeBuilder.insert(commentToken);
 Comment expectedComment = new Comment("comment", "");
 Comment actualComment = (Comment) xmlTreeBuilder.currentChild();
 assertEquals(expectedComment.getData(), actualComment.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 TextNode expectedCharacterNode = new TextNode("a", "");
 TextNode actualCharacterNode = (TextNode) xmlTreender.currentChild();
 assertEquals(expectedCharacterNode.getText(), actualCharacterNode.getText());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("doctype");
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType expectedDocumentType = new DocumentType("doctype", null, null, "");
 DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.currentChild();
 assertEquals(expectedDocumentType.getName(), actualDocumentType.getName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "unexpected");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 doc = null;
 }
}