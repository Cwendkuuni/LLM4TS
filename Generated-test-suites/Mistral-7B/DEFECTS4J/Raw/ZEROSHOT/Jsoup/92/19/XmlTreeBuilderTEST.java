package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<test><tag attr=\"value\"><subtag/></tag></test>"), "baseUrl");
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tag", null, null, null, null, null, null, false);
 Element expectedElement = new Element("tag", "baseUrl", null);
 xmlTreeBuilder.insert(startTagToken);
 assertEquals(expectedElement, document.getElementsByTagName("tag").get(0));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, document.getElementsByTagName("tag").size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 Comment expectedComment = new Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(expectedComment, document.getElementsByTagName("comment").get(0));
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('a');
 TextNode expectedTextNode = new TextNode("a");
 xmlTreeBuilder.insert(characterToken);
 assertEquals(expectedTextNode, document.getElementsByText("a").get(0));
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null, null);
 DocumentType expectedDocumentType = new DocumentType("html", null, null);
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(expectedDocumentType, document.getDoctype());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<fragment><tag attr=\"value\"><subtag/></tag></fragment>";
 List<Node> expectedNodes = document.parseFragment(inputFragment, document, "baseUrl", new Parser(xmlTreeBuilder));
 Element fragmentElement = (Element) expectedNodes.get(0);
 assertEquals("tag", fragmentElement.nodeName());
 assertEquals("value", fragmentElement.attributeValue("attr"));
 }

 @Test
 public void testInitialiseParse() {
 Document expectedDocument = new Document();
 xmlTreeBuilder.initialiseParse(new StringReader("<test><tag attr=\"value\"><subtag/></tag></test>"), "baseUrl", new Parser(xmlTreeBuilder));
 assertEquals(document, expectedDocument);
 assertEquals("test", expectedDocument.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Token(-1, null);
 xmlTreeBuilder.process(invalidToken);
 }
}