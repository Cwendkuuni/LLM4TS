package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 @Test
 public void testParse() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("Test Title", doc.title().text());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.getElementsByTag("h1").first());
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<body><h1>Test Heading</h1></body>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");
 assertNotNull(doc.head());
 assertNull(doc.title());
 assertNotNull(doc.body());
 assertNotNull(doc.getElementsByTag("h1").first());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String html = "<body><p><script>alert('Hello World')</script></p></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");
 assertNotNull(doc.head());
 assertNull(doc.title());
 assertNotNull(doc.body());
 Element script = doc.body().getElementsByTag("script").first();
 assertNotNull(script);
 assertNotNull(script.data());
 assertNotNull(script.childNodes().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser.parse("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", null);
 }
}