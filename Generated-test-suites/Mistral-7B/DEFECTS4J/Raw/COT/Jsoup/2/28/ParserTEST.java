package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test Parser class.
 */
public class ParserTest {

 private static final String HTML_TEMPLATE = "<html><head></head><body><h1>Test</h1></body></html>";

 @Test
 public void testParse() {
 Parser parser = new Parser(HTML_TEMPLATE, "http://example.com/");
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text());
 assertEquals(1, doc.body().children().size());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Test</h1>", "http://example.com/");
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text()); // title is null for body fragment
 assertEquals(1, doc.body().children().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 }

 @Test
 public void testParseEmptyString() {
 Parser parser = new Parser("", "http://example.com/");
 Document doc = parser.parse();
 assertTrue(doc.isEmpty());
 }

 @Test
 public void testParseInvalidTagSequence() {
 Parser parser = new Parser("</html><body>", "http://example.com/");
 assertNull(parser.parse());
 }

 @Test
 public void testParseInvalidTagOrder() {
 Parser parser = new Parser("<body><html></html>", "http://example.com/");
 assertNull(parser.parse());
 }

 @Test
 public void testParseEmptyTag() {
 Parser parser = new Parser("<html></html>", "http://example.com/");
 Document doc = parser.parse();
 assertEquals(0, doc.body().children().size());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com/");
 Document doc = parser.parse();
 Element comment = doc.body().child(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 Parser parser = new Parser("<html xmlns:foo=\"http://example.com/foo\"><!-- Test --></html>", "http://example.com/");
 Document doc = parser.parse();
 assertTrue(doc.getXmlDeclaration().isStandalone());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><h1>Test</h1></body></html>", "http://example.com/");
 Document doc = parser.parse();
 assertEquals(1, doc.body().children().size());
 }

 @Test
 public void testParseStartTagWithAttributes() {
 Parser parser = new Parser("<html><body><a href=\"http://example.com/link\" class=\"linkClass\" id=\"linkId\">Link</a></body></html>", "http://example.com/");
 Document doc = parser.parse();
 Element link = doc.body().child(1);
 assertEquals("Link", link.text());
 assertEquals("http://example.com/link", link.absUrl("href"));
 assertEquals("linkClass", link.className());
 assertEquals("linkId", link.id());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>Test</p></body></html>", "http://example.com/");
 Document doc = parser.parse();
 Element p = doc.body().child(0);
 assertEquals("Test", p.text().toString());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><body><![CDATA[Test]]></body></html>", "http://example.com/");
 Document doc = parser.parse();
 Element cdata = doc.body().child(0);
 assertEquals("Test", cdata.text().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTagName() {
 Parser parser = new Parser("<html><body><x>Test</x></body></html>", "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownTag() {
 Parser parser = new Parser("<html><body><zoo></zoo></body></html>", "http://example.com/");
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser("<html><body><base href=\"http://example.com/newBase\"><h1>Test</h1></body></html>", "http://example.com/");
 Document doc = parser.parse();
 assertEquals("http://example.com/newBase", doc.baseUri());
 }

 @Test
 public void testParseBaseTagWithEmptyValue() {
 Parser parser = new Parser("<html><body><base href=\"\"><h1>Test</h1></body></html>", "http://example.com/");
 Document doc = parser.parse();
 assertNull(doc.baseUri());
 }
}