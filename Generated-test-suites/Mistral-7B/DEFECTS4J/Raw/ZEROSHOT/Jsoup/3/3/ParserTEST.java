package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello World</h1>" +
 "<p>This is a <b>test</b> paragraph.</p>" +
 "</body></html>";

 parser = new Parser(html, "http://example.com");
 doc = parser.parse();
 }

 @Test
 public void testParse() {
 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.rootElement().tag());
 assertEquals(1, doc.childNodes().size());
 assertEquals(htmlTag, doc.childNodes().get(0).tag());
 assertEquals(1, doc.childNodes().get(0).childNodes().size());
 assertEquals(headTag, doc.childNodes().get(0).childNodes().get(0).tag());
 assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
 assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tag());
 assertEquals("Test Page", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());

 assertEquals(1, doc.body().childNodes().size());
 assertEquals(h1Tag, doc.body().childNodes().get(0).tag());
 assertEquals("Hello World", doc.body().childNodes().get(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>This is a test paragraph.</p>";
 Document bodyDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("http://example.com", bodyDoc.baseUri());
 assertEquals(1, bodyDoc.childNodes().size());
 assertEquals(bodyTag, bodyDoc.childNodes().get(0).tag());
 assertEquals(1, bodyDoc.childNodes().get(0).childNodes().size());
 assertEquals(pTag, bodyDoc.childNodes().get(0).childNodes().get(0).tag());
 assertEquals("This is a test paragraph.", bodyDoc.childNodes().get(0).childNodes().get(0).text());
 }

 @Test
 public void testParseXmlDecl() {
 String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
 "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello World</h1>" +
 "</body></html>";

 parser = new Parser(xmlDeclHtml, "http://example.com");
 doc = parser.parse();
 XmlDeclaration xmlDecl = doc.getElementsByTag("xml-declaration").get(0);
 assertEquals("UTF-8", xmlDecl.encoding());
 }

 @Test
 public void testParseComment() {
 String commentHtml = "<!-- This is a comment -->";

 parser = new Parser(commentHtml, "http://example.com");
 doc = parser.parse();
 Comment comment = doc.getElementsByTag("comment").get(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseEndTag() {
 String endTagHtml = "<p><b>Hello</b></p>";

 parser = new Parser(endTagHtml, "http://example.com");
 doc = parser.parse();
 Element p = doc.getElementsByTag("p").get(0);
 Element b = p.childNodes().get(0);
 assertEquals("b", b.tag().name());
 assertEquals(1, p.childNodes().size());
 assertTrue(b.parentNode() == p);
 }

 @Test
 public void testParseStartTag() {
 String startTagHtml = "<h1>Hello World</h1>";

 parser = new Parser(startTagHtml, "http://example.com");
 doc = parser.parse();
 Element h1 = doc.getElementsByTag("h1").get(0);
 assertEquals("h1", h1.tag().name());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseAttribute() {
 String attributeHtml = "<p id=\"test\" class=\"test\" title=\"Test Title\">Test</p>";
 parser = new Parser(attributeHtml, "http://example.com");
 doc = parser.parse();
 Element p = doc.getElementsByTag("p").get(0);
 Map<String, String> attributes = p.attributes();
 assertEquals(3, attributes.size());
 assertEquals("test", attributes.get("id"));
 assertEquals("Test Title", attributes.get("title"));
 assertEquals("class", attributes.keySet().iterator().next());
 }

 @Test
 public void testParseTextNode() {
 String textNodeHtml = "Hello World";
 parser = new Parser(textNodeHtml, "http://example.com");
 doc = parser.parse();
 Element p = new Element(pTag, "http://example.com");
 p.appendChild(new TextNode("Hello World", "http://example.com"));
 assertEquals("Hello World", doc.body().text());
 assertEquals(1, doc.childNodes().size());
 assertEquals(bodyTag, doc.childNodes().get(0).tag());
 }

 @Test
 public void testParseCdata() {
 String cdataHtml = "<![CDATA[Hello World]]>";
 parser = new Parser(cdataHtml, "http://example.com");
 doc = parser.parse();
 Element p = new Element(pTag, "http://example.com");
 p.appendChild(new TextNode("<![CDATA[Hello World]]>", "http://example.com"));
 assertEquals("Hello World", doc.body().text());
 assertEquals(1, doc.childNodes().size());
 assertEquals(bodyTag, doc.childNodes().get(0).tag());
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidHtml() {
 String invalidHtml = "<html><body><!"; // missing > in comment
 parser = new Parser(invalidHtml, "http://example.com");
 doc = parser.parse();
 fail();
 }
}