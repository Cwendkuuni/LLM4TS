package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

 @Test
 public void testParseHtmlDocument() {
 Document doc = Parser.parse("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://example.com");

 Element html = doc.elementSibling(0);
 Element head = html.child(0);
 Element title = head.child(0);
 Element body = html.child(1);
 Element heading = body.child(0);

 Assert.assertEquals("Test Title", title.text());
 Assert.assertEquals("h1", heading.tagName());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<body><p>Test Paragraph</p></body>", "http://example.com");

 Element body = doc.body();
 Element p = body.child(0);

 Assert.assertEquals("Test Paragraph", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragment("<div><script src='script.js'></script><p>Test Paragraph</p></div>", "http://example.com");

 Element div = doc.body().child(0);
 Element script = div.child(0);
 Element p = div.child(1);

 Assert.assertNotNull(script.attr("src"));
 Assert.assertEquals("p", p.tagName());
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Document doc = Parser.parseBodyFragment("", "http://example.com");

 Assert.assertNotNull(doc);
 Assert.assertTrue(doc.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser.parse("<html></html>", null);
 }
}