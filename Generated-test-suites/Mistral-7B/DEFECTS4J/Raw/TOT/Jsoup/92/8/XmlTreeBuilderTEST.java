import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

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
 public void testParseEmptyXml() {
 Document doc = xmlTreeBuilder.parse(new StringReader(""), "");
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseSimpleXml() {
 String xml = "<root><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = (Element) doc.childNodes().get(0);
 Element child = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("child", child.nodeName());
 assertEquals("Hello World", child.text().toString());
 }

 @Test
 public void testParseXmlWithComments() {
 String xml = "<!-- This is a comment --><root><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = (Element) doc.childNodes().get(0);
 List<Node> comments = root.childNodes();
 assertEquals(1, comments.size());
 assertEquals("<!-- This is a comment -->", comments.get(0).nodeName());
 }

 @Test
 public void testParseXmlWithCData() {
 String xml = "<root><child><![CDATA[This is some CDATA text]]></child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = (Element) doc.childNodes().get(0);
 Element child = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("child", child.nodeName());
 String cdataText = child.childNodes().get(0).text().toString();
 assertEquals("This is some CDATA text", cdataText);
 }

 @Test
 public void testParseXmlWithAttributes() {
 String xml = "<root attr1=\"value1\" attr2='value2'><child attr3=\"value3\"></child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = (Element) doc.childNodes().get(0);
 Element child = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("child", child.nodeName());

 assertEquals("value1", root.attr("attr1"));
 assertEquals("value2", root.attr("attr2"));
 assertEquals("value3", child.attr("attr3"));
 }

 @Test
 public void testParseXmlWithNestedElements() {
 String xml = "<root><child><grandchild>Hello Grandchild</grandchild></child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = (Element) doc.childNodes().get(0);
 Element child = (Element) root.childNodes().get(0);
 Element grandChild = (Element) child.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("child", child.nodeName());
 assertEquals("grandchild", grandChild.nodeName());
 assertEquals("Hello Grandchild", grandChild.text().toString());
 }

 @Test
 public void testParseXmlWithSelfClosingTags() {
 String xml = "<root><img src=\"image.png\" /> <link href=\"link.html\" rel=\"external\" /></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");

 Element root = (Element) doc.childNodes().get(0);
 List<Element> imgTags = root.select("img");
 assertEquals(1, imgTags.size());
 Element img = imgTags.get(0);
 assertEquals(0, img.childNodes().size());
 assertEquals("img", img.nodeName());

 List<Element> linkTags = root.select("link");
 assertEquals(1, linkTags.size());
 Element link = linkTags.get(0);
 assertEquals(0, link.childNodes().size());
 assertEquals("link", link.nodeName());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]><root></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");
 DocumentType doctype = (DocumentType) doc.childNodes().get(0);
 assertEquals("root", doctype.getName());
 assertEquals("[ <!ATTLIST root attr CDATA #REQUIRED > ]", doctype.getSystemIdentifier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidXml() {
 String xml = "<?xml version=\"1.0\"?><root>Invalid XML";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");
 // This should throw an IllegalArgumentException
 }
}