package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <root>" +
 " <child1 attr1=\"value1\">Content 1</child1>" +
 " <child2>" +
 " <grandChild1>Content 1.1</grandChild1>" +
 " <grandChild2>Content 1.2</grandChild2>" +
 " </child2>" +
 " </root>" +
 "</xml>";

 Document doc = xmlTreeBuilder.parse(xmlInput, "");
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(2, children.size());

 Element child1 = (Element) children.get(0);
 assertEquals("child1", child1.tagName());
 List<Attribute> attributes = child1.attributes();
 assertEquals(1, attributes.size());
 assertEquals("value1", attributes.get(0).getValue());
 assertEquals("Content 1", child1.text());

 Element child2 = (Element) children.get(1);
 assertEquals("child2", child2.tagName());
 List<Node> grandChildren = child2.children();
 assertEquals(2, grandChildren.size());

 Element grandChild1 = (Element) grandChildren.get(0);
 assertEquals("grandChild1", grandChild1.tagName());
 assertEquals("Content 1.1", grandChild1.text());

 Element grandChild2 = (Element) grandChildren.get(1);
 assertEquals("grandChild2", grandChild2.tagName());
 assertEquals("Content 1.2", grandChild2.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child tag=\"test\"/></root>";
 List<Node> fragments = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Document doc = new Document();
 doc.setRootElement(fragments.get(0));
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(1, children.size());
 Element child = (Element) children.get(0);
 assertEquals("child", child.tagName());
 assertEquals("test", child.attributes().get("tag").getValue());
 }

 @Test
 public void testInvalidInput() {
 String xmlInvalidInput = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <root>"; // missing closing tag
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(xmlInvalidInput, "");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(doc);
 }
}