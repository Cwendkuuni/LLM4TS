package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 assertNotNull(xmlTreeBuilder.defaultSettings());
 }

 @Test
 public void testInitialiseParse() {
 StringReader input = new StringReader("<xml><root/></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 assertNotNull(doc);
 }

 @Test
 public void testProcessStartTag() {
 StringReader input = new StringReader("<xml><root><child/></root></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 Element root = doc.select("root").first();
 Element child = root.select("child").first();
 assertTrue(root.childNodeSize() == 1);
 assertTrue(child.nodeName().equals("child"));
 }

 @Test
 public void testProcessEndTag() {
 StringReader input = new StringReader("<xml><root><child/></root></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 Element root = doc.select("root").first();
 root.childNode(0).remove(); // remove child
 List<Node> nodes = doc.childNodes();
 assertTrue(nodes.get(1).nodeName().equals("root"));
 }

 @Test
 public void testProcessComment() {
 StringReader input = new StringReader("<xml><!-- comment --></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 List<Node> nodes = doc.childNodes();
 assertTrue(nodes.get(0).nodeName().equals("#comment"));
 }

 @Test
 public void testProcessCharacter() {
 StringReader input = new StringReader("<xml>&#x123;<text>char</text></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 List<Node> nodes = doc.childNodes();
 assertTrue(nodes.get(0).nodeName().equals("#text"));
 assertTrue(nodes.get(0).text().equals("&#x123;char"));
 }

 @Test
 public void testProcessDoctype() {
 StringReader input = new StringReader("<!DOCTYPE xml SYSTEM \"xml.dtd\"[>\n<xml></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 List<Node> nodes = doc.childNodes();
 assertTrue(nodes.get(0).nodeName().equals("!DOCTYPE"));
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 StringReader input = new StringReader("<xml><root><child><!-- comment --></child></root></xml>");
 Document doc = xmlTreeBuilder.parse(input, "http://example.com");
 }

 @Test
 public void testInsertNode() {
 Document doc = new Document();
 Element root = new Element("root");
 doc.appendChild(root);
 xmlTreeBuilder.insertNode(root);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNode() {
 Document doc = new Document();
 xmlTreeBuilder.insertNode(null);
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<xml><root><child/></root></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", new Parser(xmlTreeBuilder));
 Element root = (Element) nodes.get(0);
 assertTrue(root.childNodeSize() == 1);
 Element child = (Element) root.childNodes().get(0);
 assertTrue(child.nodeName().equals("child"));
 }

 @Test
 public void testParseFragmentWithContext() {
 String inputFragment = "<xml><root><child/></root></xml>";
 Element context = new Element("context");
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, "http://example.com", new Parser(xmlTreeBuilder));
 assertTrue(nodes.get(0).parentNode() == context);
 }

}