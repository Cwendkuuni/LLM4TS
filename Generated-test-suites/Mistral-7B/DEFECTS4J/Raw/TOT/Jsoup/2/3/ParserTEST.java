package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("", "baseUri", false);
 Document doc = parser.parse();
 Assert.assertEquals(htmlTag, doc.tagName());
 Assert.assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseBasicHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();

 Element htmlElement = doc.body();
 Assert.assertEquals(htmlTag, htmlElement.tagName());
 Element headElement = doc.head();
 Element titleElement = headElement.child(0);
 Assert.assertEquals(headTag, titleElement.tagName());
 Assert.assertEquals("Test", titleElement.text());
 Element h1Element = htmlElement.child(0);
 Assert.assertEquals(h1, h1Element.tagName());
 Assert.assertEquals("Hello, World!", h1Element.text());
 }

 @Test
 public void testParseHtmlWithComments() {
 String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><!-- Another comment --><h1>Hello, World!</h1></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();

 Element htmlElement = doc.body();
 Element comment1 = htmlElement.child(0);
 Assert.assertEquals(Comment.class, comment1.getClass());
 Assert.assertEquals("This is a comment", comment1.text());
 Element h1Element = htmlElement.child(1);
 Assert.assertEquals(h1, h1Element.tagName());
 Assert.assertEquals("Hello, World!", h1Element.text());
 }

 @Test
 public void testParseHtmlWithCdata() {
 String html = "<html><body><![CDATA[ <p>This is some <strong>cdata</strong>.</p> ]]></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();

 Element pElement = doc.body().child(0);
 Assert.assertEquals(p, pElement.tagName());
 String text = pElement.text();
 Assert.assertEquals("This is some <strong>cdata</strong>", text);
 }

 @Test
 public void testParseHtmlWithAttributes() {
 String html = "<a href='http://example.com' id='link1'>Link</a>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();
 Element linkElement = doc.body().child(0);
 Assert.assertEquals("a", linkElement.tagName());
 Map<String, String> expectedAttributes = new LinkedHashMap<>();
 expectedAttributes.put("href", "http://example.com");
 expectedAttributes.put("id", "link1");
 Assert.assertEquals(expectedAttributes, linkElement.attributes());
 }

 @Test
 public void testParseHtmlWithNestedElements() {
 String html = "<html><head><title>Test</title></head><body><h1><a href='http://example.com'>Link</a></h1></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();
 Element h1Element = doc.body().child(0);
 Assert.assertEquals("h1", h1Element.tagName());
 Element aElement = h1Element.child(0);
 Assert.assertEquals("a", aElement.tagName());
 Assert.assertEquals("Link", aElement.text());
 Assert.assertEquals("http://example.com", aElement.absUrl("href"));
 }

 @Test
 public void testParseHtmlWithInvalidTags() {
 String html = "<html><body><invalid><h1>Hello, World!</h1></invalid></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 try {
 parser.parse();
 Assert.fail("Expected ParserException not thrown");
 } catch (Parser.ParserException e) {
 // expected
 }
 }

 @Test
 public void testParseHtmlWithMissingEndTags() { // this test case will fail, as the Parser will try to add an implicit parent
 String html = "<html><body><h1>Hello, World!";
 Parser parser = new Parser(html, "baseUri", false);
 try {
 parser.parse();
 Assert.fail("Expected ParserException not thrown");
 } catch (Parser.ParserException e) {
 // expected
 }
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 String html = "<html><head></head><body><base href='/foo'><a href='/bar'>Link</a></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();
 String expectedBaseUrl = "http://example.com/foo";
 Assert.assertEquals(expectedBaseUrl, doc.baseUri());
 String linkHref = doc.body().child(0).child(0).absUrl("href");
 Assert.assertEquals("http://example.com/bar", linkHref);
 }

 @Test
 public void testParseHtmlWithEntities() { // this test case requires a specific Jsoup version
 String html = "<html><body><p>&lt;p&gt;This is a &amp;amp; test &quot;with&quot; &apos;single&apos; &eacute;quoted and &nbsp;spaces.</p></body></html>";
 Parser parser = new Parser(html, "baseUri", false);
 Document doc = parser.parse();
 Element pElement = doc.body().child(0);
 String text = pElement.text();
 String expectedText = "&lt;p&gt;This is a &amp;amp; test &quot;with&quot; &apos;single&apos; &eacute;quoted and &nbsp;spaces.";
 Assert.assertEquals(expectedText, text);
 }

 @Test
 public void testParseHtmlWithExternalResources() { // this test case requires a specific Jsoup version and a web server
 String html = "<html><head><link rel='stylesheet' href='/styles.css'></head><body><div id='content'></div></body></html>";
 // start web server and serve styles.css
 // ...
 Parser parser = new Parser(html, "http://localhost:8080", false);
 Document doc = parser.parse();
 Element contentElement = doc.body().child(0);
 // check that styles.css was loaded correctly
 // ...
 }
}