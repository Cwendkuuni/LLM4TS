package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlTreeBuilderDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><child1 attr1=\"value1\"><child2 attr2=\"value2\"/></child1></root>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = doc.rootElement();
 Assert.assertEquals("root", root.tagName());

 Element child1 = root.child(0);
 Assert.assertEquals("child1", child1.tagName());
 Assert.assertEquals("value1", child1.attr("attr1"));

 Element child2 = child1.child(0);
 Assert.assertEquals("child2", child2.tagName());
 Assert.assertEquals("value2", child2.attr("attr2"));
 }

 @Test
 public void testXmlTreeBuilderSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><img src=\"image.jpg\" /> <child1 attr1=\"value1\"/></root>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = doc.rootElement();
 Assert.assertEquals("root", root.tagName());

 Element img = root.child(0);
 Assert.assertNotNull(img);
 Assert.assertEquals("img", img.tagName());
 Assert.assertNull(img.child(0));

 Element child1 = root.child(1);
 Assert.assertEquals("child1", child1.tagName());
 Assert.assertEquals("value1", child1.attr("attr1"));
 }

 @Test
 public void testXmlTreeBuilderComments() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><!-- comment1 --><child1 attr1=\"value1\"><!-- comment2 --><child2 attr2=\"value2\"/></child1></root>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = doc.rootElement();
 Assert.assertEquals("root", root.tagName());

 Element child1 = root.child(0);
 Assert.assertEquals("child1", child1.tagName());
 Assert.assertEquals("value1", child1.attr("attr1"));

 Comment comment1 = (Comment) child1.child(0);
 Assert.assertEquals("comment1", comment1.text());

 Element child2 = child1.child(1);
 Assert.assertEquals("child2", child2.tagName());
 Assert.assertEquals("value2", child2.attr("attr2"));

 Comment comment2 = (Comment) child2.child(0);
 Assert.assertEquals("comment2", comment2.text());
 }

 @Test
 public void testXmlTreeBuilderDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED> ]><root><child attr=\"value\"/></root>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "");

 DocumentType doctype = doc.doctype();
 Assert.assertEquals("root", doctype.getName());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());

 Element root = doc.rootElement();
 Element child = root.child(0);
 Assert.assertEquals("child", child.tagName());
 Assert.assertEquals("value", child.attr("attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlTreeBuilderInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<html>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "");
 }

 @Test
 public void testXmlTreeBuilderParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><child1><child2></child2></child1></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document doc = new Document();
 doc.setRootElement(nodes.get(0).asElement());

 Element root = doc.rootElement();
 Assert.assertEquals("root", root.tagName());

 Element child1 = root.child(0);
 Assert.assertEquals("child1", child1.tagName());

 Element child2 = child1.child(0);
 Assert.assertEquals("child2", child2.tagName());
 Assert.assertTrue(child2.childNodes().isEmpty());
 }

}