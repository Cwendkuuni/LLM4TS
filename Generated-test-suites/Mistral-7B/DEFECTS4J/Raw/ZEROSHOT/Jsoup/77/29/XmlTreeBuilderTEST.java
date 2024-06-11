package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<root><child element='value'/></root>";
 String baseUri = "http://example.com";

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

 Element rootElement = xmlDoc.getRootElement();
 Element childElement = rootElement.child(0);

 assertEquals("root", rootElement.tagName());
 assertEquals("child", childElement.tagName());
 assertEquals("value", childElement.attribute("element"));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlFragment = "<child element='value'/>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = XmlTreeBuilder.defaultSettings();

 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, errors, settings);

 assertEquals(1, nodes.size());
 Element childElement = (Element) nodes.get(0);

 assertEquals("child", childElement.tagName());
 assertEquals("value", childElement.attribute("element"));
 }

 @Test
 public void testParseWithDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><child /></root>";
 String baseUri = "http://example.com";

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

 DocumentType rootDoctype = xmlDoc.doctype();
 assertEquals("root", rootDoctype.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<html>"; // this should fail as it's not valid xml
 String baseUri = "http://example.com";

 try {
 xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
 fail("Expected an IllegalArgumentException when parsing invalid xml input");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}