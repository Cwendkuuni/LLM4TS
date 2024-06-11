package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private Parser parser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testParseEmptyXml() {
 String xml = "<root/>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 assertEquals("root", doc.tagName());
 assertNotNull(doc.childNodes());
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseXmlWithElements() {
 String xml = "<root><child1>1</child1><child2><subchild>2</subchild></child2></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = doc.child(0);
 assertEquals("root", root.tagName());

 Element child1 = root.child(0);
 assertEquals("child1", child1.tagName());
 assertEquals("1", child1.child(0).text());

 Element child2 = root.child(1);
 assertEquals("child2", child2.tagName());

 Element subchild = child2.child(0);
 assertEquals("subchild", subchild.tagName());
 assertEquals("2", subchild.child(0).text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<root><img src='image.jpg'/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = doc.child(0);
 assertEquals("root", root.tagName());
 assertNotNull(root.childNodes());
 assertEquals(1, root.childNodes().size());

 Element img = root.child(0);
 assertEquals("img", img.tagName());
 assertNotNull(img.attr("src"));
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseXmlWithComment() {
 String xml = "<root><!-- This is a comment --></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 assertNotNull(doc.childNodes());
 assertEquals(1, doc.childNodes().size());

 Element root = doc.child(0);
 Node commentNode = root.child(0);
 assertEquals(Node.COMMENT_NODE, commentNode.nodeType());
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE root SYSTEM 'About:legacy-compat'>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 assertNotNull(doc.childNodes());
 assertEquals(1, doc.childNodes().size());

 Element doctypeNode = (Element) doc.child(0);
 assertEquals("root", doctypeNode.tagName());
 assertNotNull(doctypeNode.attributes());
 assertEquals("About:legacy-compat", doctypeNode.attr("SYSTEM"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidToken() {
 String xml = "<root><!--<invalid-->"; // missing '>' in comment
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");
 // Test should fail with IllegalArgumentException
 }
}