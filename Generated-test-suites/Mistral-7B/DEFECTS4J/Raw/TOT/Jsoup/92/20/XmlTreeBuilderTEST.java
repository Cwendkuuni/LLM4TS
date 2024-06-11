import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXML() {
 String xml = "<>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");
 Validate.assertTrue(document.body() == null);
 }

 @Test
 public void testParseSimpleXML() {
 String xml = "<root><child>Hello World</child></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = document.getElementById("root");
 Element child = root.child(0);
 Validate.assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseXMLWithComments() {
 String xml = "<root><!-- This is a comment --><child>Hello World</child></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = document.getElementById("root");
 Element child = root.child(0);
 Validate.assertEquals("Hello World", child.text());

 List<Node> comments = document.getAllElements().stream()
 .filter(node -> node instanceof Comment)
 .collect(Collectors.toList());
 Validate.assertEquals(1, comments.size());
 }

 @Test
 public void testParseXMLWithDoctype() {
 String xml = "<!DOCTYPE root [ <!ELEMENT child (#PCDATA)> ]><root><child>Hello World</child></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = document.getElementById("root");
 Element child = root.child(0);
 Validate.assertEquals("Hello World", child.text());

 DocumentType doctype = document.doctype();
 Validate.assertEquals("root", doctype.getName());
 }

 @Test
 public void testParseXMLWithAttributes() {
 String xml = "<root id='test' attr='value'><child attr2='value2'>Hello World</child></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = document.getElementById("root");
 Element child = root.child(0);
 Validate.assertEquals("value", root.attr("id"));
 Validate.assertEquals("value2", child.attr("attr2"));
 }

 @Test
 public void testParseXMLWithSelfClosingTags() {
 String xml = "<root><img src='image.jpg' /> <link rel='stylesheet' href='style.css' /></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = document.getElementById("root");
 List<Element> images = root.select("img");
 Validate.assertEquals(1, images.size());
 List<Element> links = root.select("link");
 Validate.assertEquals(1, links.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidToken() {
 String xml = "<root<child>";
 document = xmlTreeBuilder.parse(new StringReader(xml), "");
 Validate.fail("Should not have reached this point");
 }
}