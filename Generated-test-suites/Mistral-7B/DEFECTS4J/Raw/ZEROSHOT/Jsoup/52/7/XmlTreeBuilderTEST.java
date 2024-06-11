package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document parsedDocument;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr=\"value\"/>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 assertNotNull(xmlTreeBuilder.doc);
 xmlTreeBuilder.doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr=\"value\"/>\n</root>";
 Token startTag = new Token.StartTag("root");
 xmlTreeBuilder.process(startTag);
 Element rootElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr=\"value\"><endtag/>\n</element>\n</root>";
 Token endTag = new Token.EndTag("endtag");
 xmlTreeBuilder.process(endTag);
 assertEquals(xmlTreeBuilder.stack.size() - 1, xmlTreeBuilder.stack.lastIndexOf(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1)).intValue());
 }

 @Test
 public void testProcessComment() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<!-- comment -->\n</root>";
 Token commentToken = new Token.Comment("-- comment --");
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element>character</element>\n</root>";
 Token characterToken = new Token.Character('c');
 xmlTreeBuilder.process(characterToken);
 TextNode characterNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("character", characterNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>\n<root>\n<element attr=\"value\"/>\n</root>";
 Token doctypeToken = new Token.Doctype("<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>");
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("root", doctypeNode.getName());
 }

 @Test
 public void testPopStackToClose() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element>\n  <subelement>\n  </subelement>\n</element>\n</root>";
 Token endSubelementTag = new Token.EndTag("subelement");
 xmlTreeBuilder.initialiseParse(input, null, null);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(endSubelementTag);
 assertEquals(xmlTreeBuilder.stack.size(), 2);
 }

 @Test
 public void testParseFragment() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr=\"value\"/>\n</root>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, null, null);
 assertNotNull(parsedNodes);
 assertEquals(2, parsedNodes.size());
 }
}