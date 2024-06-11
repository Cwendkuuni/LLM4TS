package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseErrorList errors;
 private Stack<Node> stack;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 errors = new ParseErrorList();
 stack = new Stack<>();
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element1 tag=\"test\"/>\n<element2 tag=\"test2\"/>\n<comment>\nThis is a comment.\n</comment>\n<element3 tag=\"test3\" />\n</root>", "", errors);
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("element1", null, new Attribute("tag", "test"));
 xmlTreeBuilder.process(startTag);
 Element element1 = (Element) stack.peek();
 assertEquals("element1", element1.nodeName());
 assertEquals("test", element1.tag().name());
 assertNotNull(element1.baseUri());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("element1");
 xmlTreeBuilder.process(endTag);
 assertFalse(stack.contains(stack.peek()));
 stack.pop();
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token.Comment("This is a comment.");
 xmlTreeBuilder.process(comment);
 assertEquals(1, stack.size());
 Comment commentNode = (Comment) stack.peek();
 assertEquals("This is a comment.", commentNode.data());
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token(" ");
 xmlTreeBuilder.process(character);
 assertEquals(1, stack.size());
 TextNode textNode = (TextNode) stack.peek();
 assertEquals(" ", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = new Token.Doctype("html", "", "");
 xmlTreeBuilder.process(doctype);
 assertEquals(1, stack.size());
 DocumentType documentType = (DocumentType) stack.peek();
 assertEquals("html", documentType.nodeName());
 }

 @Test
 public void testProcessUnknownTag() {
 Token startTag = new Token.StartTag("unknownTag", null, null);
 xmlTreeBuilder.process(startTag);
 assertEquals(1, stack.size());
 Element unknownTag = (Element) stack.peek();
 assertFalse(unknownTag.isSelfClosing());
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token startTag = new Token.StartTag("img", null, null);
 startTag.setSelfClosing(true);
 xmlTreeBuilder.process(startTag);
 assertEquals("img", stack.peek().nodeName());
 xmlTreeBuilder.process(new Token.EndTag("img"));
 assertFalse(stack.contains(stack.peek()));
 stack.pop();
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedToken() {
 Token unexpectedToken = new Token(123);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element1 tag=\"test\"/>\n<element2 tag=\"test2\"/>\n<comment>\nThis is a comment.\n</comment>\n</root>", "", errors);
 Token endTag = new Token.EndTag("element1");
 xmlTreeBuilder.popStackToClose(endTag);
 assertFalse(stack.contains(stack.peek()));
 stack.pop();
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element1 tag=\"test\"/>\n<element2 tag=\"test2\"/>\n<unknownTag>\n</unknownTag>\n</root>", "", errors);
 Token endTag = new Token.EndTag("unknownTag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertFalse(stack.contains(stack.peek()));
 stack.pop();
 }

 @Test
 public void testProcessEOF() {
 Token eof = new Token.EOF();
 xmlTreeBuilder.process(eof);
 assertEquals(0, stack.size());
 }
}