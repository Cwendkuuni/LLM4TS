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
 doc = Jsoup.parse("", null, xmlTreeBuilder); // initialize an empty document
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml><tag attr=\"value\"/></xml>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 assertEquals("xml", xmlTreeBuilder.doc.nodeName());
 assertEquals("", xmlTreeBuilder.baseUri);
 assertNotNull(xmlTreeBuilder.stack);
 assertFalse(xmlTreeBuilder.stack.isEmpty());
 assertSame(doc, xmlTreeBuilder.stack.get(0));
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml><tag attr=\"value\"/></xml>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 xmlTreeBuilder.runParser();
 Element tagElement = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("tag", tagElement.nodeName());
 assertEquals("value", tagElement.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml><tag><tag2/></tag></xml>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 xmlTreeBuilder.runParser();
 Element tag1Element = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element tag2Element = (Element) tag1Element.childNodes().get(0);
 xmlTreeBuilder.popStackToClose(xmlTreeBuilder.tokeniser.getEndTag());
 assertEquals(0, tag1Element.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 String input = "<xml><!-- comment --></xml>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 xmlTreeBuilder.runParser();
 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml>text</xml>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 xmlTreeBuilder.runParser();
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("text", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml PUBLIC \" -//W3C//DTD XML 1.0//EN\" \"xml.dtd\">";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 xmlTreeBuilder.runParser();
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.nodeName());
 assertNotNull(doctypeNode.publicIdentifier());
 assertNotNull(doctypeNode.systemIdentifier());
 }

 @Test
 public void testParseFragment() {
 String input = "<xml><tag attr=\"value\"/></xml>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
 assertEquals(1, fragmentNodes.size());
 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("xml", fragmentElement.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser.next(); // advance tokeniser to process unexpected token
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.getToken());
 }
}