package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head></head><body></body></html>");
 }

 @Test
 public void testParseEmptyHtml() {
 assertEquals(HtmlDocument.class, doc.getClass());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertFalse(doc.body().text().isBlank());
 }

 @Test
 public void testParseHtmlWithContent() {
 parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "", false);
 doc = parser.parse();
 assertEquals("Hello World", doc.head().title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseHtmlWithScriptTag() {
 parser = new Parser("<html><head></head><body><script type=\"text/javascript\">function test(){alert('Hello World')}</script></body></html>", "", false);
 doc = parser.parse();
 assertNotNull(doc.body().getElementsByTag("script").first());
 }

 @Test
 public void testParseHtmlWithComment() {
 parser = new Parser("<html><!-- This is a comment --><head></head><body></body></html>", "", false);
 doc = parser.parse();
 assertTrue(StringUtils.isNotBlank(doc.body().select("html > comment()").text()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 parser = new Parser("</html>", "", false);
 parser.parse();
 fail("Expected IllegalArgumentException when parsing invalid HTML");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingBaseUri() {
 parser = new Parser("<html></html>", "", false);
 try {
 doc = parser.parse();
 fail("Expected IllegalArgumentException when parsing HTML without base URI");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseBodyFragment() {
 parser = new Parser("<p>Hello World</p>", "", true);
 doc = parser.parse();
 assertEquals("Hello World", doc.body().text());
 }
}