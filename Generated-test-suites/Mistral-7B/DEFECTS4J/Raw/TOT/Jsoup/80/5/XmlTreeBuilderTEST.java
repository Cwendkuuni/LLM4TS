package org.jsoup.parser.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.ParseSettings;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private static XmlTreeBuilder xmlTreeBuilder;

 @BeforeClass
 public static void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseSimpleXml() {
 String xmlInput = "<root><element>Hello</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseURI");

 Element root = xmlDoc.selectRoot();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 Element element = root.select("element").first();
 assertNotNull(element);
 assertEquals("Hello", element.text());
 }

 @Test
 public void testParseSelfClosingTag() {
 String xmlInput = "<img src=\"image.png\" />";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseURI");
 Element img = xmlDoc.select("img").first();
 assertNotNull(img);
 assertEquals("img", img.tagName());
 assertEquals("image.png", img.attr("src"));
 }

 @Test
 public void testParseComment() {
 String xmlInput = "<!-- This is a comment -->";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseURI");
 List<Node> comments = xmlDoc.getElementsByTag("comment");
 assertNotNull(comments);
 assertEquals(1, comments.size());
 assertEquals("This is a comment", comments.get(0).text());
 }

 @Test(expected = RuntimeException.class)
 public void testParseInvalidXml() {
 String xmlInput = "<root><tag < attribute=\"value\""; // missing '>' in start tag
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseURI");
 }
}