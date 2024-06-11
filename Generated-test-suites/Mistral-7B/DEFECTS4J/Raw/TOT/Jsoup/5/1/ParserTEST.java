package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.*;

public class ParserTest {

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element root = doc.body();

 assertEquals("Test Title", doc.title().text());
 assertEquals("Test Heading", root.tagName(), Element.TAG_H1);
 assertEquals("Test Paragraph", root.child(0).text());
 }

 @Test
 public void testParseEmptyHtml() {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element root = doc.body();
 assertTrue(root.children().isEmpty());
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html><title>Test Title</title></html<body>"; // missing closing tag
 Document doc = Parser.parse(html, "http://test.com");
 assertNotNull(doc);
 Element root = doc.body();
 assertEquals(0, root.children().size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test Paragraph</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 Element root = doc.body();
 assertEquals("Test Paragraph", root.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Test Paragraph</p>"; // missing html tag
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
 Element root = doc.body();
 assertEquals("Test Paragraph", root.text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
 String html = null;
 try {
 Parser.parse(html, "http://test.com");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBaseUri() {
 String html = "<html><base href='javascript:alert(\"Hello\")'><body></body></html>";
 try {
 Parser.parse(html, null);
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}