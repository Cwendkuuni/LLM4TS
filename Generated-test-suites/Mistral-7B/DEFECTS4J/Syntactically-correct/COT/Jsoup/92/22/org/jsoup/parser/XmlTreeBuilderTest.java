package org.jsoup.parser;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 // Basic test cases for public methods

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 assertInstanceOf(ParseSettings.class, builder.defaultSettings());
 }

 @Test
 public void testParse() throws Exception {
 String xmlString = "<xml><tag>Hello</tag></xml>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = builder.parse(new StringReader(xmlString));
 assertNotNull(doc);
 }

 @Test
 public void testParseWithBaseUri() throws Exception {
 String xmlString = "<xml base=\"test.xml\"><tag>Hello</tag></xml>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = builder.parse(new StringReader(xmlString), "http://test.com");
 assertNotNull(doc);
 }

 @Test
 public void testProcessStartTag() throws Exception {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = new Document();
 Token startTag = new Token.StartTag("tag", null, null, null, null);
 builder.process(startTag);
 Element el = (Element) doc.childNodes().get(0);
 assertInstanceOf(Element.class, el);
 assertInstanceOf(Tag.class, el.tag());
 }

 @Test
 public void testProcessEndTag() throws Exception {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = new Document();
 Element element = new Element("tag");
 doc.appendChild(element);
 stack.push(doc);
 Token endTag = new Token.EndTag("tag");
 builder.process(endTag);
 assertTrue(doc.childNodes().size() == 0);
 }

 @Test
 public void testProcessComment() throws Exception {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = new Document();
 Token comment = new Token.Comment("This is a comment");
 builder.process(comment);
 Comment commentNode = (Comment) doc.childNodes().get(0);
 assertEquals("This is a comment", commentNode.getData());
 }

 @Test
 public void testProcessCharacter() throws Exception {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = new Document();
 Token character = new Token.Character('A');
 builder.process(character);
 TextNode textNode = (TextNode) doc.childNodes().get(0);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() throws Exception {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = new Document();
 Token doctype = new Token.Doctype("html", null, null);
 builder.process(doctype);
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 assertEquals("html", doctypeNode.getName());
 }

 // Edge cases and exception handling scenarios

 @Test(expected = IllegalArgumentException.class)
 public void testProcessNullToken() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.process(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidTokenType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token invalidToken = new Token(123, "test");
 builder.process(invalidToken);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPopStackEmpty() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.popStackToClose(new Token.EndTag("tag"));
 }

 @Test(expected = JDOMException.class)
 public void testParseFragmentWithInvalidInput() throws Exception {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String invalidInput = "<tag>";
 List<Node> nodes = builder.parseFragment(invalidInput, null, null, null);
 // JDOMException is thrown by Jsoup when parsing invalid input
 }

}