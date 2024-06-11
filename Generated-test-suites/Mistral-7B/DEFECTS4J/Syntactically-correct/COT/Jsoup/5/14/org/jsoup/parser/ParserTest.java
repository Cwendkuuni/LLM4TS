package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.Parser.*;
import static org.jsoup.nodes.Tag.*;
import org.jsoup.nodes.*;

public class ParserTest {

 @Test
 public void testParse() {
 Document doc = parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://test.com");
 Element title = doc.head().title();
 Element h1 = doc.body().getElementsByTag("h1").first();

 assertEquals("Test", title.text());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = parseBodyFragment("<body><h1>Hello World</h1></body>", "http://test.com");
 Element h1 = doc.body().getElementsByTag("h1").first();

 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = parseBodyFragment("<body><unknownTag/>", "http://test.com");
 Element unknownTag = doc.body().getElementsByTag("unknownTag").first();

 assertNotNull(unknownTag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 try {
 parse(null, "http://test.com");
 Failure.fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 try {
 parse("<html></html>", null);
 Failure.fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithEmptyString() {
 Document doc = parse("", "http://test.com");
 assertTrue(doc.isEmpty());
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Document doc = parseBodyFragment("", "http://test.com");
 assertTrue(doc.isEmpty());
 }

 @Test
 public void testParseStartTagWithAttributes() {
 Document doc = parse("<img src=\"image.jpg\" alt=\"Image\" title=\"Title\"></img>", "http://test.com");
 Element img = doc.body().getElementsByTag("img").first();

 assertEquals("image.jpg", img.attr("src"));
 assertEquals("Image", img.attr("alt"));
 assertEquals("Title", img.attr("title"));
 }

 @Test
 public void testParseEmptyStartTag() {
 Document doc = parse("<input type=\"text\"></input>", "http://test.com");
 Element input = doc.body().getElementsByTag("input").first();

 assertTrue(input.isEmpty());
 }

 @Test
 public void testParseCdata() {
 Document doc = parse("<script><![CDATA[function test(){alert('Hello World');}]]></script>", "http://test.com");
 Element script = doc.body().getElementsByTag("script").first();

 assertEquals("[CDATA[\nfunction test(){\nalert('Hello World');}\n]]", script.child(0).outerHtml());
 }

 @Test
 public void testParseComment() {
 Document doc = parse("<!-- This is a comment -->", "http://test.com");
 Element comment = doc.getElementsByTag("comment").first();

 assertNull(comment);
 }

 @Test
 public void testParseXmlDecl() {
 Document doc = parse("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", "http://test.com");
 Element xmlDeclaration = doc.head().getElementsByTag("xml-decl").first();

 assertEquals("1.0", xmlDeclaration.attr("version"));
 assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
 }

 @Test
 public void testParseEndTag() {
 Document doc = parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://test.com");
 doc.body().remove();
 doc.head().remove();
 doc.remove();

 assertEquals(3, doc.childCount());
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidEndTag() {
 Document doc = parse("<html><head><title>Test</title></body></html>", "http://test.com");
 doc.body().remove();
 doc.head().remove();
 doc.remove();

 assertEquals(3, doc.childCount());

 try {
 doc.head().getElementsByTag("title").first().tagName();
 Failure.fail("AssertionError expected but not thrown.");
 } catch (AssertionError e) {
 // expected
 }
 }
}