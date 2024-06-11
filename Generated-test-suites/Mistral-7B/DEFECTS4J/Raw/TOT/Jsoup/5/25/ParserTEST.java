package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseHtml() {
 Document doc = Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Element root = doc.body();
 assertEquals("Hello World", root.select("h1").text());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<p>Test</p>", "http://example.com");
 Element root = doc; // Document has no head, only body
 assertEquals("Test", root.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragment("<div></div>", "http://example.com"); // empty tag allowed
 Document relaxedDoc = Parser.parseBodyFragmentRelaxed("<div></div>", "http://example.com"); // empty tag not forced empty
 Element rootRelaxed = relaxedDoc;
 Element rootStrict = doc;

 assertTrue(rootStrict.tag().isSelfClosing());
 assertFalse(rootRelaxed.tag().isSelfClosing());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 try {
 Parser.parse(null, "http://example.com");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 try {
 Parser.parse("", "http://example.com");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseInvalidHtml() {
 try {
 Parser.parse("<html><body><script>//<![CDATA[alert('XSS');//]]></script></body></html>", "http://example.com");
 fail("Expected parsing exception");
 } catch (RuntimeException e) {
 // pass
 }
 }

 @Test
 public void testParseInvalidHtmlWithBase() {
 Document doc = Parser.parse("<html><base href=\"javascript:void(0)\" /><body><script>//<![CDATA[alert('XSS');//]]></script></body></html>", "http://example.com");
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseSelfClosingTag() {
 Document doc = Parser.parse("<html><head><title>Test</title></head><body><img src=\"image.jpg\" /></body></html>", "http://example.com");
 Element root = doc.body();
 assertNull(root.select("img").last().tag().children().get(0));
 }

 @Test
 public void testParseImplicitParent() {
 Document doc = Parser.parse("<html><body><p><span>Test</span></p></body></html>", "http://example.com");
 Element root = doc.body();
 Element span = root.select("span").get(0);
 assertEquals("Test", span.text());
 }

 @Test
 public void testParseInvalidParent() {
 try {
 Parser.parse("<html><head><title>Test</title></head><body><div></div><table></table></body></html>", "http://example.com");
 fail("Expected parsing exception");
 } catch (RuntimeException e) {
 // pass
 }
 }
}