package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXml() throws IOException {
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(new byte[0]), "baseUrl");
 assertNotNull(doc);
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseSimpleXml() throws IOException {
 String xml = "<root><element attr=\"value\">Hello World</element></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "baseUrl");
 Element root = (Element) doc.child(0);
 Element element = (Element) root.child(0);
 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attributes().get("attr").toString());
 assertEquals("Hello World", element.child(0).text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() throws IOException {
 String xml = "<root><img src=\"image.jpg\" /> </root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "baseUrl");
 Element root = (Element) doc.child(0);
 assertNull(root.child(1));
 }

 @Test
 public void testParseXmlWithComment() throws IOException {
 String xml = "<root><!-- This is a comment --></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "baseUrl");
 Element root = (Element) doc.child(0);
 assertEquals(1, root.childNodes().size());
 Node commentNode = root.child(0);
 assertNotNull(commentNode);
 assertTrue(commentNode instanceof Comment);
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testParseXmlWithCDataSection() throws IOException {
 String xml = "<root><![CDATA[ <script language=\"javascript\"> alert('Hello World'); </script> ]]></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "baseUrl");
 Element root = (Element) doc.child(0);
 assertEquals(1, root.childNodes().size());
 Node cdataNode = root.child(0);
 assertNotNull(cdataNode);
 assertTrue(cdataNode instanceof CDataNode);
 assertEquals("<script language=\"javascript\"> alert('Hello World'); </script>", cdataNode.text());
 }

 @Test
 public void testParseXmlWithDoctype() throws IOException {
 String xml = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><element/></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "baseUrl");
 DocumentType doctype = (DocumentType) doc.child(0);
 assertEquals("root", doctype.getName());
 assertEquals("root.dtd", doctype.getSystemIdentifier());
 }

 @Test
 public void testParseXmlFragment() throws IOException {
 String xmlFragment = "<root><element attr=\"value\">Hello World</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attributes().get("attr").toString());
 assertEquals("Hello World", element.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXml() throws IOException {
 String xml = "<root><!-- This is not XML -->";
 try {
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "baseUrl");
 } catch (IOException e) {
 // Expected
 }
 Validate.isTrue(xmlTreeBuilder.getParseErrors().size() > 0);
 }
}