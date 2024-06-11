package org.jsoup.parser;

import org.jsoup.Helper;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private static final String XML_STRING = "<root><child1>Hello</child1><child2><subChild1/><subChild2/></child2></root>";

 @Test
 public void testXmlTreeBuilder() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = Helper.parseFragment(XML_STRING, "", xmlTreeBuilder.parseFragment(XML_STRING, "", new ParseErrorList()));

 // Test root element
 Assert.assertEquals("root", xmlDoc.getRootElement().nodeName());

 // Test child elements
 List<Node> childNodes = xmlDoc.childNodes();
 Assert.assertEquals(2, childNodes.size());

 // Test child1 element
 Element child1 = (Element) childNodes.get(0);
 Assert.assertEquals("child1", child1.nodeName());

 // Test Hello text node
 Text textNode = (Text) child1.childNodes().get(0);
 Assert.assertEquals("Hello", textNode.text());

 // Test child2 element
 Element child2 = (Element) childNodes.get(1);
 Assert.assertEquals("child2", child2.nodeName());

 // Test subChild1 element
 Element subChild1 = (Element) child2.childNodes().get(0);
 Assert.assertEquals("subChild1", subChild1.nodeName());

 // Test empty subChild2 element
 Element subChild2 = (Element) child2.childNodes().get(1);
 Assert.assertEquals("", subChild2.outerHtml());
 }
}