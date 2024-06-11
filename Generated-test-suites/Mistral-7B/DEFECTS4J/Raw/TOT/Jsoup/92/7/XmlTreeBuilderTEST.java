package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <root>\n" +
 " <element tag=\"testTag\" attr1=\"testValue1\" attr2=\"testValue2\">Content</element>\n" +
 " <comment><![CDATA[This is a CDATA comment]]></comment>\n" +
 " </root>", null);
 }

 @Test
 public void testParseXmlDocument() {
 // Test empty document
 Document emptyDocument = xmlTreeBuilder.parse("", null);
 assertTrue(emptyDocument.childNodes().isEmpty());

 // Test simple XML document
 List<Node> nodes = document.childNodes();
 assertEquals(2, nodes.size());

 // Test element with attributes and content
 Element element = (Element) nodes.get(0);
 assertEquals("root", element.nodeName());
 List<Node> children = element.childNodes();
 assertEquals(2, children.size());

 Element testElement = (Element) children.get(0);
 assertEquals("element", testElement.nodeName());
 assertEquals("testTag", testElement.tagName());
 assertEquals("testValue1", testElement.attr("attr1"));
 assertEquals("testValue2", testElement.attr("attr2"));
 assertEquals("Content", testElement.text());

 // Test CDATA comment
 Node commentNode = children.get(1);
 assertTrue(commentNode instanceof CDataNode);
 String commentText = ((CDataNode) commentNode).getData();
 assertEquals("This is a CDATA comment", commentText);
 }

 @Test
 public void testParseInvalidXmlDocument() {
 String invalidXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root<tag>";
 Document invalidDocument = xmlTreeBuilder.parse(new StringReader(invalidXml), null);
 assertTrue(invalidDocument.childNodes().isEmpty());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<tag attr1=\"testValue1\">Content</tag>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(inputFragment, null, null);
 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("tag", fragmentElement.tagName());
 assertEquals("testValue1", fragmentElement.attr("attr1"));
 assertEquals("Content", fragmentElement.text());
 }

 @Test
 public void testParseFragmentWithContext() {
 Element context = new Element("context");
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(
 "<tag attr1=\"testValue1\">Content</tag>", context, null, null);
 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("tag", fragmentElement.tagName());
 assertEquals("testValue1", fragmentElement.attr("attr1"));
 assertEquals("Content", fragmentElement.text());
 context.appendChild(fragmentElement);
 assertEquals(1, context.childNodes().size());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <root>\n" +
 " <tag invalidTagName>\n" +
 " </root>", null);
 }
}