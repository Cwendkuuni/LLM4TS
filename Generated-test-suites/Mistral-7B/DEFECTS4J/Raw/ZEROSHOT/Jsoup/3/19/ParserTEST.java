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
 public void setup() {
 String html = "<html><head><title>Test Page</title></head><body>" +
 " <h1>Heading</h1>" +
 " <p>First paragraph.</p>" +
 " <p>Second paragraph.</p>" +
 " <ul>" +
 " <li>First item</li>" +
 " <li>Second item<ul>" +
 " <li>Nested item</li>" +
 " </ul></li>" +
 " </ul>" +
 " </body>" +
 "</html>";

 parser = new Parser(html, "http://example.com");
 doc = parser.parse();
 }

 @Test
 public void testParse() {
 // Test the resulting Document
 Element htmlNode = doc.body();
 assertEquals("html", htmlNode.tagName());
 assertEquals("http://example.com", doc.baseUri());

 Element headNode = doc.head();
 Assert.assertNotNull(headNode);
 Element titleNode = headNode.child(0);
 assertEquals("title", titleNode.tagName());
 assertEquals("Test Page", titleNode.text());

 Element bodyNode = doc.body();
 assertEquals("body", bodyNode.tagName());

 Element h1Node = bodyNode.child(0);
 assertEquals("h1", h1Node.tagName());
 assertEquals("Heading", h1Node.text());

 Element pNodes = bodyNode.children();
 List<Element> pList = new ArrayList<>(pNodes.size());
 for (Element pNode : pNodes) {
 pList.add(pNode);
 }
 assertEquals(2, pList.size());

 Element p1Node = pList.get(0);
 assertEquals("p", p1Node.tagName());
 assertEquals("First paragraph.", p1Node.text());

 Element p2Node = pList.get(1);
 assertEquals("p", p2Node.tagName());
 assertEquals("Second paragraph.", p2Node.text());

 // Test comments
 Element commentNode = bodyNode.nextSibling();
 assertEquals(Comment.class, commentNode.getClass());
 assertEquals("-->\n", commentNode.outerHtml());

 // Test CDATA
 Element cdataNode = bodyNode.nextSibling();
 assertEquals(DataNode.class, cdataNode.getClass());
 assertEquals("<![CDATA[", cdataNode.outerHtml().substring(0, 9));
 assertEquals("]]>", cdataNode.outerHtml().substring(cdataNode.outerHtml().length()-2));
 assertEquals("Test", cdataNode.text());

 // Test XML declaration
 Element xmlDeclNode = bodyNode.nextSibling();
 assertEquals(XmlDeclaration.class, xmlDeclNode.getClass());
 assertEquals("<xml version='1.0' encoding='UTF-8' standalone='yes'", xmlDeclNode.outerHtml());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test body fragment</p>";
 Parser parserBodyFragment = new Parser(bodyHtml, "http://example.com");
 doc = parserBodyFragment.parse();

 Element bodyNode = doc.body();
 assertEquals("body", bodyNode.tagName());
 Element pNode = bodyNode.child(0);
 assertEquals("p", pNode.tagName());
 assertEquals("Test body fragment", pNode.text());
 }
}