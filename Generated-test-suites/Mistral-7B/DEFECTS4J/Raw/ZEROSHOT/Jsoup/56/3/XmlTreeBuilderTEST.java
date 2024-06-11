package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private Document doc;
 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @After
 public void tearDown() {
 if (doc != null) {
 doc.delete();
 doc = null;
 }
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element name='test' /><element2 name='test2' /></root>";
 doc = xmlTreeBuilder.parse(xmlString, "");

 Element root = doc.getRootElement();
 Element element = root.getChild(0);
 Element element2 = root.getChild(1);

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("test", element.attr("name"));
 assertEquals("element2", element2.tagName());
 assertNull(element2.attr("name"));
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element name='test' /><element2 name='test2' />";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Element element = (Element) fragmentNodes.get(0);
 Element element2 = (Element) fragmentNodes.get(1);

 assertEquals("element", element.tagName());
 assertEquals("test", element.attr("name"));
 assertEquals("element2", element2.tagName());
 assertNull(element2.attr("name"));
 }

 @Test(expected = RuntimeException.class)
 public void testUnexpectedTokenType() {
 String input = "<html><body><xml</body></html>";
 xmlTreeBuilder.parse(input, "");
 }

 @Test
 public void testComment() {
 String xmlString = "<!-- This is a comment --><root><element name='test' /></root>";
 doc = xmlTreeBuilder.parse(xmlString, "");

 Comment comment = doc.getElementsByTag("comment").get(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testDoctype() {
 String xmlString = "<!DOCTYPE root [ <!ATTLIST element name CDATA #REQUIRED> ]><root><element name='test' /></root>";
 doc = xmlTreeBuilder.parse(xmlString, "");

 DocumentType doctype = (DocumentType) doc.childNodes().get(0);
 assertEquals("root", doctype.nodeName());
 assertEquals("root", doctype.getName());
 assertEquals("element", doctype.getQualifiedName());
 assertEquals("CDATA", doctype.getPublicIdentifier().scheme());
 assertNull(doctype.getPublicIdentifier().systemIdentifier());
 assertNull(doctype.getSystemIdentifier());
 }
}