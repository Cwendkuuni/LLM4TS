package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.LinkedList;

public class ParserTest {

 private Parser createParser(String html, String baseUri) {
 Validate.notNull(html);
 Validate.notNull(baseUri);

 Parser parser = new Parser(html, baseUri, false);
 return parser;
 }

 @Test
 public void testParse() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(headTag, doc.head().parent());
 assertEquals(titleTag, doc.head().child(0).tag());
 assertEquals("Test", doc.head().child(0).text());
 assertEquals("Hello World", doc.body().child(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(bodyTag, doc.body());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(bodyTag, doc.documentElementTag());
 assertEquals("Hello World", doc.body().child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyParser() {
 Parser parser = new Parser("", "http://example.com", false);
 }

 @Test
 public void testParseEmptyBodyFragment() {
 String bodyHtml = "<body></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(bodyTag, doc.body());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(bodyTag, doc.documentElementTag());
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseInvalidStartTag() {
 Parser parser = createParser("<html<foo", "http://example.com");

 assertEquals(htmlTag, parser.parse().documentElementTag());
 assertFalse(parser.stack().getLast().tagName().equals("html"));
 }

 @Test
 public void testParseMissingEndTag() {
 Parser parser = createParser("<html><body><h1>Hello World</h1><script", "http://example.com");

 assertNull(parser.parse().body().child(1).nextSibling());
 }

 @Test
 public void testParseNestedTags() {
 Parser parser = createParser("<html><head><title>Outer</title><title>Inner</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(headTag, doc.head().child(0));
 assertEquals("Outer", doc.head().child(0).text());
 assertEquals(titleTag, doc.head().child(1));
 assertEquals("Inner", doc.head().child(1).text());
 assertEquals(bodyTag, doc.body());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(h1Tag, doc.body().child(0).tag());
 assertEquals("Hello World", doc.body().child(0).text());
 }

 @Test
 public void testParseMixedTags() {
 Parser parser = createParser("<html><body><h1>Hello World</h1><img src=\"image.png\"></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(bodyTag, doc.body());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(h1Tag, doc.body().child(0).tag());
 assertEquals("Hello World", doc.body().child(0).text());
 assertEquals(imgTag, doc.body().child(1).tag());
 assertEquals("image.png", doc.body().child(1).attr("src"));
 }

 @Test
 public void testParseComments() {
 Parser parser = createParser("<html><!-- This is a comment -->\n<body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(bodyTag, doc.body());
 assertEquals(h1Tag, doc.body().child(0).tag());
 assertEquals("Hello World", doc.body().child(0).text());
 assertEquals(Comment.class, doc.body().child(0).child(0).getClass());
 assertEquals("This is a comment", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseXmlDeclaration() {
 Parser parser = createParser("<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\"><body></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(xmlDeclarationTag, doc.documentElementTag());
 assertEquals("en", doc.documentElement().xmlLang());
 assertEquals(xmlnsDeclarationTag, doc.documentElement().child(0).tag());
 assertEquals("http://example.com/foo", doc.documentElement().child(0).attr("prefix"));
 }

 @Test
 public void testParseCdata() {
 Parser parser = createParser("<html><![CDATA[This is some CDATA]]><body></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(htmlTag, doc.body().parent());
 assertEquals(bodyTag, doc.body());
 assertEquals("", doc.body().text());
 assertEquals(TextNode.class, doc.body().child(0).getClass());
 assertEquals("This is some CDATA", doc.body().child(0).text());
 }

 @Test
 public void testParseInvalidXmlDeclaration() {
 Parser parser = createParser("<html><![CDATA[<html>XML Declaration", "http://example.com");

 assertFalse(parser.parse().documentElementTag() instanceof XmlDeclaration);
 }

 @Test
 public void testParseEmptyDocument() {
 Parser parser = createParser("<html></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.documentElementTag());
 assertEquals(htmlTag, doc.document());
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = createParser("<html><base href=\"/subdir\"><body></body></html>", "http://example.com/page.html");
 Document doc = parser.parse();

 assertEquals("http://example.com/page.html", doc.baseUri());
 assertEquals("http://example.com/subdir/page.html", doc.baseHref());
 }

 @Test
 public void testParseInvalidBaseTag() {
 Parser parser = createParser("<html><base target=\"_blank\" href=\"/subdir\"><body></body></html>", "http://example.com/page.html");

 assertEquals("http://example.com/page.html", doc.baseUri());
 assertNull(doc.baseHref());
 }

 private LinkedList<Element> stack(Parser parser) {
 return parser.stack();
 }
}