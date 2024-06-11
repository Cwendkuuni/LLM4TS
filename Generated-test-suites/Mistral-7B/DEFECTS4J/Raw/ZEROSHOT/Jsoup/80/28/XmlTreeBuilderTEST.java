package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element attr=\"value\"><subElement/></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");

 assertEquals("root", doc.tagName());
 Element root = doc.getRootElement();
 List<Node> children = root.children();
 assertEquals(1, children.size());
 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());
 List<NameValuePair> attributes = element.attributes();
 assertEquals(1, attributes.size());
 assertEquals("value", attributes.get(0).getKey());
 assertEquals("value", attributes.get(0).getValue());
 List<Node> subElements = element.children();
 assertEquals(1, subElements.size());
 assertNull(subElements.get(0).childNodes().get(0)); // no children for subElement
 }

 @Test
 public void testParseFragment() {
 String xmlInputFragment = "<element attr=\"value\"><subElement/></element>";
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInputFragment, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element root = (Element) fragment.get(0);
 assertEquals("element", root.tagName());
 List<NameValuePair> attributes = root.attributes();
 assertEquals(1, attributes.size());
 assertEquals("value", attributes.get(0).getKey());
 assertEquals("value", attributes.get(0).getValue());
 List<Node> subElements = root.children();
 assertEquals(1, subElements.size());
 assertNull(subElements.get(0).childNodes().get(0)); // no children for subElement
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 // test with an invalid input
 String input = "invalid";
 xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse(new StringReader(input), "http://test.com");
 } catch (IllegalArgumentException e) {
 // expect exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 // test with a null input
 xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse((Reader) null, "http://test.com");
 } catch (IllegalArgumentException e) {
 // expect exception
 }
 }

 @Test
 public void testProcessStartTag() {
 String xmlInput = "<root><element attr=\"value\"><subElement/></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = doc.getRootElement();
 List<Node> children = root.children();
 assertEquals(1, children.size());
 Element element = (Element) children.get(0);
 Token startTag = element.child(0);
 xmlTreeBuilder.process(startTag);
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<root><element><subElement/></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = doc.getRootElement();
 Element element = (Element) root.children().get(0);
 List<Node> children = element.children();
 assertEquals(1, children.size());
 Element subElement = (Element) children.get(0);
 Token endTag = subElement.tag();
 xmlTreeBuilder.process(endTag);
 assertEquals(0, element.children().size());
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<!-- This is a comment -->";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 List<Node> children = doc.childNodes();
 assertEquals(1, children.size());
 Comment comment = (Comment) children.get(0);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<root>&#x27;Hello&#x27;</root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = (Element) doc.getRootElement();
 List<Node> children = root.children();
 assertEquals(1, children.size());
 Text text = (Text) children.get(0);
 assertEquals("Hello", text.text());
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 DocumentType doctype = doc.doctype();
 assertEquals("root", doctype.name());
 Attribute attribute = doctype.attributeNamed("element");
 assertEquals("attr", attribute.getName());
 assertEquals("CDATA", attribute.getValue());
 }

 @Test
 public void testProcessUnknownElement() {
 String xmlInput = "<root><unknown/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = (Element) doc.getRootElement();
 List<Node> children = root.children();
 assertEquals(1, children.size());
 Element unknown = (Element) children.get(0);
 assertEquals("unknown", unknown.tagName());
 }
}