package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 doc = null;
 }

 @Test
 public void testParse() {
 String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <root attr=\"value\">" +
 " <element attr1=\"value1\">Content</element>" +
 " </root>" +
 "</xml>";

 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");

 Element root = doc.rootElement();
 Element element = root.child(0);

 assertEquals("root", root.tagName());
 assertEquals("value", root.attr("attr"));
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("Content", element.text());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <root attr=\"value\">" +
 " <element attr1=\"value1\">Content</element>" +
 " 1234"; // unexpected character

 try {
 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 } catch (Exception e) {
 Validate.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr1=\"value1\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);

 assertEquals("element", element.tagName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testSelfClosingTag() {
 String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <root>" +
 " <selfClosingTag attr=\"value\"/>" +
 " </root>" +
 "</xml>";

 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 Element root = doc.rootElement();
 Element selfClosingTag = root.child(0).child(0);

 assertEquals("selfClosingTag", selfClosingTag.tagName());
 assertEquals("value", selfClosingTag.attr("attr"));
 }

 @Test
 public void testParseDocumentWithDoctype() {
 String xmlString = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
 "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <root>" +
 " </root>" +
 "</xml>";

 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 DocumentType doctypeNode = doc.doctype();

 assertEquals("xml", doctypeNode.nodeName());
 assertNotNull(doctypeNode.getPublicIdentifier());
 assertNotNull(doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testParseComment() {
 String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <!-- This is a comment -->" +
 " <root>" +
 " </root>" +
 "</xml>";

 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 Comment comment = (Comment) doc.child(0);

 assertEquals("This is a comment", comment.getData());
 }
}