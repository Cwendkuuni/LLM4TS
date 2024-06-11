package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

public class ParserTest {

 private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --></html>";
 private static final String HTML_WITH_XML_DECL = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\">";
 private static final String HTML_WITHOUT_END_TAG = "<html><body><div";
 private static final String HTML_WITH_EMPTY_ELEMENT = "<html><body><img src=\"image.png\"/></body></html>";
 private static final String HTML_WITH_DATA_TAG = "<html><head><title>Test Title</title></head><body><textarea>Test Data</textarea></body></html>";
 private static final String HTML_WITH_BASE_TAG = "<html><head><base href=\"/test/\"/><body><a href=\"link.html\">Link</a></body></html>";
 private static final String HTML_WITH_SELF_CLOSED_TAG = "<html><body><div/></body></html>";

 @Test
 public void testParse() {
 Document doc = Parser.parse(HTML_WITH_COMMENT, "http://example.com");
 assertTrue(doc.childNodes().get(0) instanceof Comment);
 }

 @Test
 public void testParseXmlDecl() {
 Document doc = Parser.parse(HTML_WITH_XML_DECL, "http://example.com");
 assertEquals("http://www.w3.org/1999/xhtml", doc.xmlVersion());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 Parser parser = new Parser("");
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBaseUri() {
 Parser parser = new Parser(HTML_WITH_COMMENT, null, false);
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyBaseUri() {
 Parser parser = new Parser(HTML_WITH_COMMENT, "", false);
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<body><p>Test</p></body>", "http://example.com");
 assertEquals(1, doc.body().childNodes().size());
 assertEquals("Test", doc.body().childNodes().get(0).text());
 }

 @Test
 public void testParseWithEmptyElement() {
 Document doc = Parser.parse(HTML_WITH_EMPTY_ELEMENT, "http://example.com");
 assertEquals(2, doc.body().childNodes().size());
 assertTrue(doc.body().childNodes().get(0) instanceof Element);
 assertTrue(((Element) doc.body().childNodes().get(0)).hasAttrs());
 assertTrue(((Element) doc.body().childNodes().get(1)) instanceof TextNode);
 }

 @Test
 public void testParseWithDataTag() {
 Document doc = Parser.parse(HTML_WITH_DATA_TAG, "http://example.com");
 assertEquals(2, doc.body().childNodes().size());
 assertEquals("Test Data", ((DataNode) doc.body().childNodes().get(1)).text());
 }

 @Test
 public void testParseWithBaseTag() {
 Document doc = Parser.parse(HTML_WITH_BASE_TAG, "http://example.com");
 assertEquals("http://example.com/test/link.html", doc.baseUri());
 assertEquals("link.html", doc.getElementById("link").absUrl("href"));
 }

 @Test
 public void testParseWithSelfClosedTag() {
 Document doc = Parser.parse(HTML_WITH_SELF_CLOSED_TAG, "http://example.com");
 assertEquals(1, doc.body().childNodes().size());
 assertTrue(doc.body().childNodes().get(0) instanceof Element);
 assertFalse(((Element) doc.body().childNodes().get(0)).hasChildren());
 }

 @Test
 public void testParseWithMissingEndTag() {
 try {
 Parser parser = new Parser(HTML_WITHOUT_END_TAG, "http://example.com");
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}