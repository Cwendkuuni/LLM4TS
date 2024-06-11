package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

public class XmlTreeBuilderTest {

 // Test for initialiseParse method
 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version='1.0' encoding='UTF-8'/>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 Document doc = xmlTreeBuilder.doc;
 Assert.assertEquals("1.0", doc.outputSettings().getXmlDeclaration().getVersion());
 }

 // Test for process method with StartTag
 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root><child/></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
 Element root = (Element) nodes.get(0);
 Element child = (Element) nodes.get(1);
 Assert.assertEquals("child", child.nodeName());
 }

 // Test for process method with EndTag
 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root><child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(xmlTreeBuilder.tokeniser.peekNextToken().asEndTag());
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
 Element root = (Element) nodes.get(0);
 Assert.assertEquals(1, root.childNodes().size());
 }

 // Test for process method with Character
 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root>&#x304;</root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
 TextNode textNode = (TextNode) nodes.get(2);
 Assert.assertEquals("ə", textNode.text());
 }

 // Test for process method with Doctype
 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<!DOCTYPE root [ <!ENTITY e '&e;' > ]>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
 DocumentType doctypeNode = (DocumentType) nodes.get(0);
 Assert.assertEquals("root", doctypeNode.nodeName());
 }

 // Test for popStackToClose method with unknown tag
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseUnknownTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root><foo/></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(xmlTreeBuilder.tokeniser.peekNextToken().asEndTag());
 }

 // Test for popStackToClose method with non-matching tag
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseNonMatchingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root><bar><foo/></bar></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(xmlTreeBuilder.tokeniser.peekNextToken().asEndTag());
 }
}