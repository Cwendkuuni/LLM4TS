package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.util.List;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element attr=\"value\">Content</element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals("root", document.tagName());
 assertNotNull(document.childNodes());
 assertEquals(1, document.childNodes().size());
 assertEquals("element", document.childNodes().get(0).tagName());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 assertEquals("element", nodes.get(0).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><element attr=\"value\">Content</element></root>"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnknownTag() {
 String xmlString = "<unknown/>";
 document = new Document();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessSelfClosingUnknownTag() {
 String xmlString = "<unknown/>";
 document = new Document();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 }

 @Test
 public void testProcessSelfClosingKnownTag() {
 String xmlString = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\"/>";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals(1, document.childNodes().size());
 assertEquals("html", document.childNodes().get(0).tagName());
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<!-- This is a comment -->";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals(1, document.childNodes().size());
 assertEquals("!--", document.childNodes().get(0).tagName());
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals(1, document.childNodes().size());
 assertEquals("!DOCTYPE", document.childNodes().get(0).tagName());
 }

 @Test
 public void testProcessEmptyElement() {
 String xmlString = "<element/>";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals(1, document.childNodes().size());
 assertEquals("element", document.childNodes().get(0).tagName());
 }

 @Test
 public void testPopStackToClose() {
 String xmlString = "<root><element><subElement/></element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals(3, document.childNodes().size());
 xmlTreeBuilder.popStackToClose(Tag.valueOf("subElement", ParseSettings.preserveCase).toToken());
 assertEquals(2, document.childNodes().size());
 }
}