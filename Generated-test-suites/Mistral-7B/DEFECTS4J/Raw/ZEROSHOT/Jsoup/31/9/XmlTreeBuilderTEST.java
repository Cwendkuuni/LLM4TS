package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version='1.0'><root><child/></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertEquals("http://example.com", xmlTreeBuilder.getDocument().baseUri());
 assertNull(errors.size());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 String input = "<xml version='1.0'><root><child/></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 XmlTreeBuilder xmlTreeBuilderSpy = spy(xmlTreeBuilder);
 doAnswer(invocation -> {
 throw new AssertionError("Unexpected token type: " + invocation.getArgument(0).getType());
 }).when(xmlTreeBuilderSpy).process(any(Token.class));
 xmlTreeBuilderSpy.initialiseParse(input, baseUri, errors);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml version='1.0'><root><child/><child2/></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Element root = xmlTreeBuilder.getDocument().child(0);
 Element child1 = root.child(0);
 Element child2 = root.child(1);

 assertEquals("root", root.nodeName());
 assertEquals("child", child1.nodeName());
 assertEquals("child2", child2.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml version='1.0'><root><child><child2/></child></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Element root = xmlTreeBuilder.getDocument().child(0);
 Element child = root.child(0);

 assertEquals(1, child.childSize());
 assertEquals("child", child.nodeName());
 child.removeChild(0);
 assertEquals(0, child.childSize());
 }

 @Test
 public void testProcessComment() {
 String input = "<xml version='1.0'><!-- This is a comment --><root><child/></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Node comment = xmlTreeBuilder.getDocument().child(0);
 assertEquals(Node.COMMENT_NODE, comment.nodeType());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml version='1.0'><root>&lt;test&gt;</root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Element root = xmlTreeBuilder.getDocument().child(0);
 Text textNode = (Text) root.child(0);
 assertEquals("&lt;test&gt;", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<xml version='1.0'><!DOCTYPE xml [ <!ATTLIST xml foo CDATA #REQUIRED> ]>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 DocumentType doctypeNode = xmlTreeBuilder.getDocument().doctype();
 assertEquals("xml", doctypeNode.nodeName());
 assertEquals("1.0", doctypeNode.publicId());
 assertEquals(null, doctypeNode.systemId());
 }

 @Test
 public void testProcessSelfClosingTag() {  // todo: add test for insertSelfClosingTag()
 String input = "<xml version='1.0'><root><selfClosingTag/></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Element root = xmlTreeBuilder.getDocument().child(0);
 Element selfClosingTag = root.child(0);
 assertEquals(1, selfClosingTag.childSize());
 assertEquals("selfClosingTag", selfClosingTag.nodeName());
 assertEquals(true, selfClosingTag.tagName().isSelfClosing());
 }

 @Test
 public void testPopStackToClose() {
 String input = "<xml version='1.0'><root><child><child2/></child></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Element root = xmlTreeBuilder.getDocument().child(0);
 Element child = root.child(0);

 Element child2 = child.child(0);
 root.removeChild(0);
 assertEquals(0, root.childSize());
 assertEquals("child", child.nodeName());
 assertEquals(1, child.childSize());
 assertEquals("child2", child.child(0).nodeName());
 }
}