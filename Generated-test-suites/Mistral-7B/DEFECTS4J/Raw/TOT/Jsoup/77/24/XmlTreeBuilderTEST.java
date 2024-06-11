package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXml() {
 // Test parsing a valid XML document
 String xmlInput = "<root><element attr1='value1'>content</element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "/");

 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attribute(0).getValue());
 assertEquals("content", element.text());
 }

 @Test
 public void testParseInvalidXmlWithErrors() {
 // Test parsing an XML document with errors
 String invalidXmlInput = "<root><element<tag name='attr'>";
 ParseErrorList errors = new ParseErrorList();
 Document doc = xmlTreeBuilder.parse(new StringReader(invalidXmlInput), "/", errors, ParseSettings.preserveCase);

 assertEquals(1, errors.size());
 assertEquals("Error at line 1: Element 'element' does not have an end tag. Line number: 1", errors.get(0).getMessage());

 assertNull(doc);
 }

 @Test
 public void testParseXmlFragment() {
 // Test parsing an XML fragment
 String xmlFragment = "<element attr1='value1'>content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "/", ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attribute(0).getValue());
 assertEquals("content", element.text());
 }
}