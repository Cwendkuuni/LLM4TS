package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class ParserTest {

 @Test
 public void testParseHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element title = doc.title();
 Element h1 = doc.body().getElementsByTag("h1").first();

 Assert.assertEquals("Test Page", title.text());
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element h1 = doc.body().getElementsByTag("h1").first();

 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div xmlns:foo=\"http://example.com/foo\">Hello World</div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Element div = doc.body().getElementsByTag("div").first();

 Assert.assertEquals("Hello World", div.text());
 }

 @Test
 public void testParseEmptyBodyFragmentRelaxed() {
 String bodyHtml = "<body></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Element body = doc.body();

 Assert.assertNull(body.text());
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello<script>alert('XSS')</script>World</body></html>";
 try {
 Document doc = Parser.parse(html, "http://example.com");
 Failure failure = Assert.fail("Expected parsing to fail");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseInvalidBodyFragment() {
 String bodyHtml = "<div<script>alert('XSS')</script>";
 try {
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Failure failure = Assert.fail("Expected parsing to fail");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}