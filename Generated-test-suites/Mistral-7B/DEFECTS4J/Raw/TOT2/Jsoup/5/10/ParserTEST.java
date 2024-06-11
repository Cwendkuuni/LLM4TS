package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * ParserTest.java - JUnit test file for Parser.java
 */
public class ParserTest {

 // Test case for parse method
 @Test
 public void testParse() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element htmlNode = doc.head();
 assertEquals("html", htmlNode.tagName());
 assertEquals("http://example.com", htmlNode.baseUri());

 Element titleNode = doc.head().title();
 assertEquals("Test", titleNode.text());

 Element h1Node = doc.body().getElementsByTag("h1").first();
 assertEquals("Hello World", h1Node.text());
 }

 // Test case for parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 Element h1Node = doc.body().getElementsByTag("h1").first();
 assertEquals("Hello World", h1Node.text());
 }

 // Test case for parseBodyFragmentRelaxed method
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div class=\"foo\"></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

 Element divNode = doc.body().getElementsByClass("foo").first();
 assertNotNull(divNode); // test for existence
 }

 // Test case for parse method with invalid HTML
 @Test(expected = RuntimeException.class)
 public void testParseInvalidHtml() {
 String html = "<html><body<h1>Hello World</h1>"; // missing closing tag
 Document doc = Parser.parse(html, "http://example.com");
 }

 // Test case for parse method with empty HTML
 @Test
 public void testParseEmptyHtml() {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals(1, doc.numberOfElements()); // check for empty document
 }
}