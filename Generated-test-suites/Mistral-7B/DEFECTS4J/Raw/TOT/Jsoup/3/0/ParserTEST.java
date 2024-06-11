package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello, World!</h1><p>This is a test.</p>" +
 "<script src=\"script.js\"></script></body></html>";
 String baseUri = "https://example.com";
 parser = new Parser(html, baseUri, false);
 doc = parser.parse();
 }

 @Test
 public void testParseHtml() {
 // Test for basic HTML parsing
 Element htmlElement = doc.elementAt(0);
 assertEquals("html", htmlElement.tagName());

 Element headElement = doc.head();
 assertEquals("head", headElement.tagName());

 Element titleElement = headElement.child(0);
 assertEquals("title", titleElement.tagName());
 assertEquals("Test Page", titleElement.text());

 Element bodyElement = doc.body();
 assertEquals("body", bodyElement.tagName());

 Element h1Element = bodyElement.child(0);
 assertEquals("h1", h1Element.tagName());
 assertEquals("Hello, World!", h1Element.text());

 Element pElement = bodyElement.child(1);
 assertEquals("p", pElement.tagName());
 assertEquals("This is a test.", pElement.text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test for parsing a body fragment
 String bodyHtml = "<p>This is a test.</p>";
 doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 Element bodyElement = doc.body();
 Element pElement = bodyElement.child(0);
 assertEquals("p", pElement.tagName());
 assertEquals("This is a test.", pElement.text());
 }

 @Test
 public void testParseInvalidHtml() {
 // Test for parsing invalid HTML
 String invalidHtml = "<html><body><script>alert('Hello, World!');</script></html>";
 doc = Parser.parse(invalidHtml, baseUri);
 assertTrue(doc.body().childSize() == 0);
 }

 @Test
 public void testParseWithBaseUri() {
 // Test for parsing with base URI
 String htmlWithLink = "<html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><p>This is a test.</p></body></html>";
 doc = Parser.parse(htmlWithLink, baseUri + "/test.html");
 Element linkElement = doc.head().child(0);
 assertEquals("link", linkElement.tagName());
 assertEquals("style.css", linkElement.attr("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 // Test for passing null HTML
 String html = null;
 parser = new Parser(html, baseUri, false);
 assertNull(parser);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 // Test for passing null base URI
 String html = "<html><body><p>This is a test.</p></body></html>";
 parser = new Parser(html, null, false);
 assertNull(parser);
 }
}