package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase, defaultSettings);
 }

 @Test
 public void testParseWithBaseUri() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 Document document = xmlTreeBuilder.parse(input, baseUri);

 assertEquals("http://example.com", document.baseUri());
 Element root = document.rootElement();
 Element child = root.child(0);
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";

 xmlTreeBuilder.initialiseParse(input, baseUri, null, ParseSettings.preserveCase);

 assertEquals(XmlTreeBuilder.class, xmlTreeBuilder.stack.peek());
 assertEquals(Document.class, xmlTreeBuilder.stack.size() - 1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child></child></root>";
 Token startTag = new Token.StartTag("root");
 Element rootElement = xmlTreeBuilder.insert(startTag);

 assertEquals("root", rootElement.nodeName());
 assertEquals("http://example.com", rootElement.baseUri());
 assertTrue(xmlTreeBuilder.stack.contains(rootElement));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><child></child></root>";
 Token endTag = new Token.EndTag("root");

 xmlTreeBuilder.popStackToClose(endTag);

 assertFalse(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
 }

 @Test
 public void testProcessComment() {
 String input = "<!-- This is a comment -->";
 Token commentToken = new Token.Comment("This is a comment");

 xmlTreeBuilder.insert(commentToken);

 Comment comment = xmlTreeBuilder.doc.child(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<root>&amp;Hello World;</root>";
 Token characterToken = new Token.Character('&');
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);

 assertEquals("&amp;", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE root SYSTEM \"http://example.com/document.dtd\">";
 Token doctypeToken = new Token.Doctype("root", "", "http://example.com/document.dtd");
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);

 assertEquals("root", documentType.nodeName());
 assertEquals("http://example.com/document.dtd", documentType.systemIdentifier());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<child><grandChild>Hello GrandChild</grandChild></child>";
 String baseUri = "http://example.com";
 List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, null, ParseSettings.preserveCase);

 assertEquals(2, childNodes.size());
 Element child = (Element) childNodes.get(0);
 Element grandChild = (Element) child.child(0);
 assertEquals("Hello GrandChild", grandChild.text());
 }

 @Test(expected = RuntimeException.class)
 public void testUnknownTokenType() {
 String input = "<root><child></child></root>";
 Token unknownToken = new Token(-1, "UnknownToken");

 xmlTreeBuilder.process(unknownToken);
 }
}