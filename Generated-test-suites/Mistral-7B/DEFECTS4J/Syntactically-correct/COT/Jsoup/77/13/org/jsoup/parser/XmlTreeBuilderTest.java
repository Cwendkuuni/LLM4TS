package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 assertEquals("root", doc.tagName());
 assertNotNull(doc.child(0));
 assertEquals("child", doc.child(0).tagName());
 assertEquals("Hello", doc.child(0).text());
 }

 @Test
 public void testParseWithBaseUri() throws Exception {
 String xmlString = "<root xmlns:my=\'http://example.com\'><child xmlns:my:my:namespace=\'foo\'><grandchild>Hello</grandchild></child></root>";
 String baseUri = "http://example.com/";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 assertEquals("root", doc.tagName());
 Element child = doc.child(0);
 assertEquals("child", child.tagName());
 assertEquals("http://example.com/my:namespace", child.namespaceURI());
 Element grandchild = child.child(0);
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("Hello", grandchild.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<root></root>"), null);
 }

 @Test
 public void testProcessStartTag() throws Exception {
 Token startTagToken = new Token.StartTag("root");
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.tagName());
 }

 @Test
 public void testProcessEndTag() throws Exception {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() throws Exception {
 Token commentToken = new Token.Comment("<!-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 assertEquals(Comment.class, xmlTreeBuilder.doc.childNodes().get(0).getClass());
 }

 @Test
 public void testProcessCharacter() throws Exception {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 Element textNode = xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals(TextNode.class, textNode.getClass());
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() throws Exception {
 Token doctypeToken = new Token.Doctype("<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]>");
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 assertEquals(DocumentType.class, xmlTreeBuilder.doc.childNodes().get(0).getClass());
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<root><child>Hello</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(2, nodes.size());
 assertEquals(Element.class, nodes.get(0).getClass());
 assertEquals("root", nodes.get(0).tagName());
 assertEquals(Element.class, nodes.get(1).getClass());
 assertEquals("child", nodes.get(1).tagName());
 assertEquals("Hello", nodes.get(1).text());
 }

 @Test
 public void testParseFragmentWithBaseUri() throws Exception {
 String xmlFragment = "<root xmlns:my='http://example.com'><child xmlns:my:my:namespace='foo'><grandchild>Hello</grandchild></child></root>";
 String baseUri = "http://example.com/";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(3, nodes.size());
 assertEquals(Element.class, nodes.get(0).getClass());
 assertEquals("root", nodes.get(0).tagName());
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 assertEquals("http://example.com/my:namespace", child.namespaceURI());
 Element grandchild = (Element) nodes.get(2);
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("Hello", grandchild.text());
 }

}