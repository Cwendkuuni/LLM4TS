package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 @Test
 public void testParseEmptyXml() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(""), "");
 assertEquals(XmlTreeBuilder.class, xmlDoc.parser().getClass());
 assertEquals(0, xmlDoc.childNodes().size());
 }

 @Test
 public void testParseSimpleXml() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><name>John</name></root>"), "");
 assertEquals(XmlTreeBuilder.class, xmlDoc.parser().getClass());
 Element root = xmlDoc.child(0);
 assertEquals("root", root.tagName());
 Element name = root.child(0);
 assertEquals("name", name.tagName());
 assertEquals("John", name.text());
 }

 @Test
 public void testParseXmlWithComment() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><!-- comment --><name>John</name></root>"), "");
 assertEquals(XmlTreeBuilder.class, xmlDoc.parser().getClass());
 Element root = xmlDoc.child(0);
 Comment comment = root.child(0);
 assertEquals("comment", comment.data());
 Element name = root.child(1);
 assertEquals("name", name.tagName());
 assertEquals("John", name.text());
 }

 @Test
 public void testParseXmlWithCdata() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><![CDATA[<script>alert(\"Hello World\");]]]><name>John</name></root>"), "");
 assertEquals(XmlTreeBuilder.class, xmlDoc.parser().getClass());
 Element root = xmlDoc.child(0);
 Node cdata = root.child(0);
 assertEquals(CDataNode.class, cdata.getClass());
 assertEquals("<script>alert(\"Hello World\");</script>", cdata.text());
 Element name = root.child(1);
 assertEquals("name", name.tagName());
 assertEquals("John", name.text());
 }

 @Test
 public void testParseXmlWithDoctype() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST name attr CDATA #REQUIRED> ]><root><name>John</name></root>"), "");
 assertEquals(XmlTreeBuilder.class, xmlDoc.parser().getClass());
 DocumentType doctype = xmlDoc.child(0);
 assertEquals("root", doctype.nodeName());
 assertEquals("", doctype.data());
 Element root = xmlDoc.child(1);
 Element name = root.child(0);
 assertEquals("name", name.tagName());
 assertEquals("John", name.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><img src=\"image.png\" /> </root>"), "");
 assertEquals(XmlTreeBuilder.class, xmlDoc.parser().getClass());
 Element root = xmlDoc.child(0);
 Element img = root.child(0);
 assertEquals("img", img.tagName());
 assertNotNull(img.attributes());
 assertEquals("src", img.tagName());
 assertEquals("image.png", img.attributes().get("src"));
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><name>John</name></root>";
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", "");
 Document doc = new Document();
 doc.appendChild(nodes.get(0));
 assertEquals(1, doc.childNodes().size());
 Element root = doc.child(0);
 Element name = root.child(0);
 assertEquals("name", name.tagName());
 assertEquals("John", name.text());
 }

 @Test
 public void testParseFragmentWithContext() {
 String xmlFragment = "<root><name>John</name></root>";
 Element context = new Element("root");
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, context, "", "");
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 Element name = root.child(0);
 assertEquals("name", name.tagName());
 assertEquals("John", name.text());
 context.appendChild(root);
 Assert.assertEquals(1, context.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXml() {
 Validate.expect(IllegalArgumentException.class);
 XmlTreeBuilder.parse(new StringReader("<root></root><name>John"), "");
 }

}