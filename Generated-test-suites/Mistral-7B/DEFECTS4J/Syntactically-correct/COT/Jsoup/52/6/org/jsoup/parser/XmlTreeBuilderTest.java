package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private Document parseXml(String xml) {
 return XmlTreeBuilder.parseFragment(xml, "", new ParseErrorList()).get();
 }

 // Test methods for public methods in XmlTreeBuilder

 @Test
 public void testInitialiseParse() {
 // Not possible to test directly as it's a protected method
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("test", null, null);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element el = builder.insert(startTag);
 assertTrue(el != null);
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("test");
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.popStackToClose(endTag);
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token.Comment("This is a comment");
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.insert(comment);
 assertNotNull(builder.doc.childNodes().get(0));
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token.Character('A');
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.insert(character);
 TextNode textNode = (TextNode) builder.doc.childNodes().get(0);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Doctype doctype = new Doctype("doctype", "", "", "");
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.insert(doctype);
 Document document = builder.doc;
 DocumentType doctypeNode = document.documentElement().getFirstChild();
 assertEquals("doctype", doctypeNode.nodeName());
 }

 // Test edge cases and exception handling

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.process(new Token(-1, ""));
 }

 @Test
 public void testUnknownTag() {
 String xml = "<unknownTag />";
 Document document = parseXml(xml);
 Element root = document.documentElement();
 assertNotNull(root);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 String xml = "<root><child></child></root>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parseFragment(xml, null, new ParseErrorList());
 }

 @Test
 public void testEmptyInput() {
 Document document = parseXml("");
 assertNotNull(document);
 assertTrue(document.childNodes().isEmpty());
 }

 @Test
 public void testSelfClosingTags() {
 String xml = "<tag />";
 Document document = parseXml(xml);
 Element root = document.documentElement();
 assertNotNull(root);
 assertEquals(1, root.childNodes().size());
 }
}