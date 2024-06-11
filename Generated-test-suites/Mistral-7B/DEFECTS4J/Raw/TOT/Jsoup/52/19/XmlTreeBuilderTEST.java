package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenCollector;
import org.jsoup.parser.Tokenizer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private Tokenizer tokenizer;
 private ParseErrorList errors;
 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 tokenizer = new Tokenizer();
 errors = new ParseErrorList();
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseFragmentWithSimpleXML() throws IOException {
 String xmlFragment = "<root><child1>Hello World</child1><child2/></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errors);
 Document doc = nodes.get(0);
 assertEquals("root", doc.tagName());
 Element root = (Element) doc.childNodes().get(0);
 assertEquals("child1", root.tagName());
 assertEquals("Hello World", root.childNodes().get(0).nodeValue());
 Element child2 = (Element) root.childNodes().get(1);
 assertTrue(child2.tagName().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithInvalidXML() throws IOException {
 String xmlFragment = "<root><child1>Hello World<child2>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errors);
 assertNotNull(nodes); // test should fail due to parse error
 }

 @Test
 public void testParseFragmentWithXMLDeclaration() throws IOException {
 String xmlFragment = "<?xml version='1.0' encoding='UTF-8'?><root><child1>Hello World</child1><child2/></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errors);
 Document doc = nodes.get(0);
 assertEquals("root", doc.tagName());
 Element root = (Element) doc.childNodes().get(0);
 assertEquals("child1", root.tagName());
 assertEquals("Hello World", root.childNodes().get(0).nodeValue());
 Element child2 = (Element) root.childNodes().get(1);
 assertTrue(child2.tagName().isEmpty());
 // Check xml declaration was parsed
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.nodeName());
 assertEquals("1.0", doctypeNode.getPublicIdentifier().toString());
 assertEquals("UTF-8", doctypeNode.getSystemIdentifier().toString());
 }

 @Test
 public void testParseFragmentWithSelfClosingTag() throws IOException {
 String xmlFragment = "<root><child1><child2/></child1></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errors);
 Document doc = nodes.get(0);
 Element root = (Element) doc.childNodes().get(0);
 Element child1 = (Element) root.childNodes().get(0);
 Element child2 = (Element) child1.childNodes().get(0);
 assertEquals("child2", child2.tagName());
 assertTrue(child2.isSelfClosing());
 }

 @Test
 public void testParseFragmentWithEmptyXML() throws IOException {
 String xmlFragment = "<root></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errors);
 Document doc = nodes.get(0);
 assertEquals("root", doc.tagName());
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseFragmentWithErrorHandling() throws IOException {
 String xmlFragment = "<root><child1>Hello World</child1><child2><script>alert('Hello World')</script></child2></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errors);
 assertNotNull(nodes);
 int errorCount = errors.size();
 assertEquals(1, errorCount);
 assertEquals("Error on line 4: <script>", errors.getErrors().get(0).toString());
 }
}