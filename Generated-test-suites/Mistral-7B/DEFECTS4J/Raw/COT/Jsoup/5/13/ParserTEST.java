package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("empty", "baseUrl");
 Document doc = parser.parse();
 assertNotNull(doc);
 assertEquals("baseUrl", doc.baseUri());
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<html><body><h1>Hello World</h1></body></html>", "baseUrl");
 Document doc = parser.parse();
 Element root = doc.getRootElement();
 assertEquals(htmlTag, root.tag());
 assertEquals(1, doc.childNodes().size());
 assertEquals("body", doc.getElementsByTag("body").get(0).tagName());
 assertEquals("Hello World", doc.getElementsByTag("h1").text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<html><body><h1>Hello World</h1></body></html>", "baseUrl");
 Document doc = parser.parseBodyFragment("<html><p>Relaxed Parser Test</p></html>", "baseUrl");
 Element root = doc.getRootElement();
 assertEquals(htmlTag, root.tag());
 assertEquals(1, doc.childNodes().size());
 assertEquals("body", doc.getElementsByTag("body").get(0).tagName());
 assertEquals("Relaxed Parser Test", doc.getElementsByTag("p").text());
 }

 @Test
 public void testParseWithBaseUri() {
 Parser parser = new Parser("<html><head><base href=\"/test\"/><body><a href=\"test.html\"></a></body></html>", "http://example.com/testpage.html");
 Document doc = parser.parse();
 assertEquals("http://example.com/testpage.html/test/test.html", doc.baseUri());
 assertEquals("/test/test.html", doc.getElementsByTag("a").get(0).absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "baseUrl");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("empty", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyParse() {
 Parser parser = new Parser("", "baseUrl");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Parser parser = new Parser("<html><badTag></badTag></html>", "baseUrl");
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "baseUrl");
 Document doc = parser.parse();
 Element root = doc.getRootElement();
 assertEquals(1, doc.childNodes().size());
 Element comment = root.childNodes().get(0);
 assertEquals(Comment.class, comment.getClass());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns:html=\"http://example.com/ns/html\" xml:lang=\"en\"><head/><body/></html>", "baseUrl");
 Document doc = parser.parse();
 Element root = doc.getRootElement();
 assertEquals(1, doc.childNodes().size());
 Element xmlDecl = root.childNodes().get(0);
 assertEquals(XmlDeclaration.class, xmlDecl.getClass());
 assertEquals("http://example.com/ns/html", xmlDecl.xmlNamespace());
 assertEquals("en", xmlDecl.xmlLang());
 }

}