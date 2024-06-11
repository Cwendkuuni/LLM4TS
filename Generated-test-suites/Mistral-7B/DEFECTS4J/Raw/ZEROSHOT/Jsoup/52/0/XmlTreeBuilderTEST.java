package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<xml><root><child/><child element='test'/><anotherChild/></root></xml>", "", Parser.xmlParser());
 xmlTreeBuilder.initialiseParse(doc.body().text(), "", new ParseErrorList());
 xmlTreeBuilder.runParser();
 }

 @Test
 public void testInitialiseParse() {
 assertEquals(doc, xmlTreeBuilder.doc);
 assertEquals("", xmlTreeBuilder.baseUri);
 assertNotNull(xmlTreeBuilder.errors);
 }

 @Test
 public void testProcess_StartTag() {
 Token startTagToken = new Token.StartTag("root", null, null);
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.nodeName());
 assertEquals("", rootElement.baseUri());
 assertEquals( doc, rootElement.parentNode() );
 Stack<Element> stack = xmlTreeBuilder.stack;
 assertEquals(1, stack.size());
 assertEquals(rootElement, stack.peek());
 }

 @Test
 public void testProcess_EndTag() {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcess_Character() {
 Token characterToken = new Token.Character(' ');
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals(" ", textNode.text());
 }

 @Test
 public void testProcess_Comment() {
 Token commentToken = new Token.Comment("-- This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(2);
 assertEquals("-- This is a comment", comment.text());
 }

 @Test
 public void testProcess_Doctype() {
 Token doctypeToken = new Token.Doctype("xml", null, null, null);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.nodeName());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<xml><root><child/><child element='test'/><anotherChild/></root></xml>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 assertEquals(4, parsedNodes.size());
 }
}