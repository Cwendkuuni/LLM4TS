package org.jsoup.parser;

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
 private Parser parser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testParseXMLDocument() {
 String xml = "<root><child element='value'/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 assertEquals("<root><child element='value'/></root>", doc.html());
 }

 @Test
 public void testParseXMLWithEmptyElement() {
 String xml = "<root><element/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 assertEquals("<root><element/></root>", doc.html());
 }

 @Test
 public void testParseXMLWithSelfClosingElement() {
 String xml = "<root><img src='image.jpg'/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 Element img = doc.select("img").first();
 assertNotNull(img);
 assertEquals("img", img.tagName());
 }

 @Test
 public void testParseXMLWithComment() {
 String xml = "<root><!-- This is a comment --></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 List<Node> comments = doc.childNodes();
 assertEquals(1, comments.size());
 Node comment = comments.get(0);
 assertEquals(Node.COMMENT_NODE, comment.nodeType());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXMLWithCDATA() {
 String xml = "<root><![CDATA[ <script type='text/javascript'>function test() { alert('Hello World'); } ]]></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 List<Node> cdtaNodes = doc.childNodes();
 assertEquals(1, cdtaNodes.size());
 Node cdataNode = cdtaNodes.get(0);
 assertEquals(Node.CDATA_SECTION_NODE, cdataNode.nodeType());
 String cdataText = ((CDataNode) cdataNode).getData();
 assertEquals("<script type='text/javascript'>function test() { alert('Hello World'); } </script>", cdataText);
 }

 @Test
 public void testParseXMLWithEmptyDocument() {
 Document doc = xmlTreeBuilder.parse(new StringReader(""), null);
 assertNotNull(doc);
 assertEquals("", doc.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 xmlTreeBuilder.parse(null, null, parser);
 }

 @Test
 public void testParseXMLFragment() {
 String xmlFragment = "<root><child element='value'/></root>";
 List<Node> expectedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, parser);
 Document doc = Jsoup.parse(expectedNodes.toString(), null);
 assertNotNull(doc);
 assertEquals("<root><child element='value'/></root>", doc.html());
 }

 @Test
 public void testParseXMLFragmentWithContext() {
 Element context = Jsoup.parse("<html><body><div id='root'></div></body></html>").body().getElementById("root");
 String xmlFragment = "<child element='value'/>";
 List<Node> expectedNodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, parser);
 context.appendChild(expectedNodes);
 Document doc = Jsoup.parse(context.html(), null);
 assertNotNull(doc);
 assertEquals("<html><body><div id='root'><child element='value'/></div></body></html>", doc.html());
 }

 @Test
 public void testParseXMLWithDoctype() {
 String xml = "<!DOCTYPE root [ <!ATTLIST foo bar CDATA #REQUIRED> ]><root></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 DocumentType doctype = doc.doctype();
 assertNotNull(doctype);
 assertEquals("root", doctype.getName());
 assertNotNull(doctype.getPublicIdentifier());
 assertNotNull(doctype.getSystemIdentifier());
 }

 @Test
 public void testParseXMLWithInvalidDoctype() {
 String xml = "<!DOCTYPE root [ <!ATTLIST foo bar CDATA #REQUIRED> <html>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(doc);
 assertEquals("<!DOCTYPE root [ <!ATTLIST foo bar CDATA #REQUIRED> <html>", doc.html());
 }
}