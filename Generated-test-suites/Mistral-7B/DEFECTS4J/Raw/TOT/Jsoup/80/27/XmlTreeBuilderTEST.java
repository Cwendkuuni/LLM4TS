package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXmlWithValidInput() {
 String xmlInput = "<root><element>Hello</element><element2 attr=\"value\">World</element2></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = xmlDoc.rootElement();
 Element element = root.selectFirst("element");
 Element element2 = root.selectFirst("element2");

 assertEquals("Hello", element.text());
 assertEquals("World", element2.text());
 assertEquals("value", element2.attr("attr"));
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlInput = "<root><element /> </root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = xmlDoc.rootElement();
 Element element = root.selectFirst("element");

 assertEquals(0, element.childNodeSize());
 }

 @Test(expected = ParseError.class)
 public void testParseXmlWithInvalidInput() {
 String xmlInput = "<root><element<tag";
 Document xmlDoc = null;
 try {
 xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 } catch (ParseError e) {
 // expected
 }

 ParseErrorList errors = xmlTreeBuilder.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected end of document", error.getMessage());
 assertEquals(2, error.line());
 assertEquals(4, error.column());
 }

 @Test
 public void testParseXmlWithBogusComment() {
 String xmlInput = "<root><!--!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = xmlDoc.rootElement();
 Element declaration = root.selectFirst("!--");

 assertEquals("xml-declaration", declaration.nodeName());
 assertTrue(declaration.hasAttr("xmlns:xml"));
 assertEquals("http://www.w3.org/XML/1998/namespace", declaration.attr("xmlns:xml"));
 }

 @Test
 public void testParseXmlWithCDataSection() {
 String xmlInput = "<root><![CDATA[Hello < tag > World]]></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = xmlDoc.rootElement();
 Element cdata = root.selectFirst("[CDATA[");

 assertEquals("Hello < tag > World", cdata.text());
 }

 @Test
 public void testParseXmlWithEmptyDocument() {
 String xmlInput = "<root></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://test.com");
 Element root = xmlDoc.rootElement();

 assertEquals(0, root.childNodeSize());
 }

 @Test
 public void testParseXmlWithFragment() {
 String xmlInput = "<element1><element2>Hello</element2></element1>";
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Element element1 = (Element) fragment.get(0);
 Element element2 = (Element) fragment.get(1);

 assertEquals("element1", element1.nodeName());
 assertEquals("element2", element2.nodeName());
 assertEquals("Hello", element2.text());
 }
}