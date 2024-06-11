package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element attr=\"value\"><subElement attr2=\"value2\"/></element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.child(0);
 assertEquals("element", root.child(0).tagName());
 assertEquals("value", root.child(0).attr("attr"));
 Element subElement = root.child(0).child(0);
 assertEquals("subElement", subElement.tagName());
 assertEquals("value2", subElement.attr("attr2"));
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("element", "", null, null, null, null,null, null, null, null, false, null, null, null, null, null);
 Element element = xmlTreeBuilder.insert(startTagToken);
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("element");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("<!-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("Comment", xmlTreeBuilder.currentElement().child(0).nodeName());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().child(0);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", "", null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement().child(0);
 assertEquals("html", documentTypeNode.getText());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<subElement attr2=\"value2\"/><element attr=\"value\"/>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), parseSettings);
 Element root = (Element) nodes.get(0);
 assertEquals("element", root.tagName());
 Element subElement = (Element) nodes.get(1);
 assertEquals("subElement", subElement.tagName());
 }
}