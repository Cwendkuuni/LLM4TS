package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParserUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setup() {
 doc = Document.createEmpty();
 parser = new Parser("", "", false);
 }

 @Test
 public void testParseEmpty() {
 Document parsedDoc = Parser.parse("", "");
 assertEquals(doc, parsedDoc);
 }

 @Test
 public void testParseValidHtml() {
 String html = ParserUtils.parseToEncodedString(
 "<html>\n<head>\n<title>My Title</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>");
 Document parsedDoc = Parser.parse(html, "");
 assertEquals(html, parsedDoc.html());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div>Hello</div>";
 Document parsedDoc = Parser.parseBodyFragment(bodyHtml, "");
 assertEquals("<body>" + bodyHtml + "</body>", parsedDoc.html());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<div>Hello</div>";
 Document parsedDoc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");
 Element body = parsedDoc.body();
 assertNotNull(body);
 assertEquals(bodyHtml, body.html());
 }

 @Test
 public void testParseFragmentWithBaseTag() {
 String bodyHtml = "<html><base href=\"/test\"><body><a href=\"test.html\">Test</a></body></html>";
 Document parsedDoc = Parser.parseBodyFragment(bodyHtml, "");
 String baseUri = parsedDoc.baseUri();
 assertEquals("/test", baseUri);
 assertEquals("<body><a href=\"test.html\">Test</a></body>", parsedDoc.body().html());
 }

 @Test
 public void testParseValidAttributes() {
 String html = "<p id='p1' class='class1 class2' title='Title'><a href='http://example.com'>Link</a></p>";
 Document parsedDoc = Parser.parse(html, "");
 Element p = parsedDoc.selectFirst("p");
 assertNotNull(p);
 assertEquals("p1", p.id());
 assertEquals("class1 class2", p.classes().toString());
 assertEquals("Title", p.attr("title"));
 assertEquals("http://example.com", p.selectFirst("a").attr("href"));
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html><body><script>alert('Hello!')</script></body></html>";
 try {
 Parser.parse(html, "");
 fail("Expected ParserException");
 } catch (Parser.ParserException e) {
 // expected
 }
 }

 @Test
 public void testParseSelfClosingTags() {
 String html = "<img src=\"image.jpg\" alt=\"Image\" />";
 Document parsedDoc = Parser.parse(html, "");
 Element img = parsedDoc.selectFirst("img");
 assertNotNull(img);
 assertTrue(img.tag().isSelfClosing());
 }

 @Test
 public void testParseEmptyTag() {
 String html = "<img src=\"image.jpg\" alt=\"Image\" />";
 Document parsedDoc = Parser.parse(html, "");
 Element img = parsedDoc.selectFirst("img");
 assertNotNull(img);
 assertEquals(0, img.childNodes().size());
 }

 @Test
 public void testParseDataTags() {
 String html = "<textarea>This is textarea content</textarea>";
 Document parsedDoc = Parser.parse(html, "");
 Element textarea = parsedDoc.selectFirst("textarea");
 assertNotNull(textarea);
 TextNode text = (TextNode) textarea.childNodes().get(0);
 assertEquals("This is textarea content", text.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<![CDATA[This is CDATA]]>";
 Document parsedDoc = Parser.parse(html, "");
 TextNode cdata = (TextNode) parsedDoc.selectFirst("html body").childNodes().get(0);
 assertEquals("This is CDATA", cdata.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTagName() {
 String html = "<invalid>";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeName() {
 String html = "<p foo='bar'></p>";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("foo", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeValue() {
 String html = "<p id='p1' class='class1 class2' title='Title'><a href='<script>alert('Hello!')</script>'>Link</a></p>";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("<script>alert('Hello!')</script>", e.getMessage());
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 String html = "<p><a href='test.html'>Test</a></p></p>";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("</p>", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlDecl() {
 String html = "<!DOCTYPE html SYSTEM \"<html>\">";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("<html>", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidComment() {
 String html = "<!--<html>\n-->";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("<!--<html>", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndComment() {
 String html = "--><html>";
 try {
 Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 } catch (Parser.ParserException e) {
 // ignore
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("-->", e.getMessage());
 }
 }

 @Test
 public void testParseRelativeUrls() {
 String html = "<html><head><title>My Title</title><link rel='stylesheet' href='styles.css'></head><body><img src='image.jpg'></body></html>";
 Document parsedDoc = Parser.parse(html, "http://example.com");
 assertEquals("http://example.com/styles.css", parsedDoc.selectFirst("link").attr("href"));
 assertEquals("http://example.com/image.jpg", parsedDoc.selectFirst("img").absUrl("src"));
 }

 @Test
 public void testParseRelativeUrlsWithBaseTag() {
 String html = "<html><base href='/test'><head><title>My Title</title><link rel='stylesheet' href='styles.css'></head><body><img src='image.jpg'></body></html>";
 Document parsedDoc = Parser.parse(html, "");
 assertEquals("/test/styles.css", parsedDoc.selectFirst("link").absUrl("href"));
 assertEquals("/test/image.jpg", parsedDoc.selectFirst("img").absUrl("src"));
 }

 @Test
 public void testParseInvalidRelativeUrlWithBaseTag() {
 String html = "<html><base href='/test'><head><title>My Title</title><link rel='stylesheet' href='../styles.css'></head><body><img src='image.jpg'></body></html>";
 Document parsedDoc = Parser.parse(html, "");
 assertEquals("/test/../styles.css", parsedDoc.selectFirst("link").absUrl("href"));
 assertEquals("/test/image.jpg", parsedDoc.selectFirst("img").absUrl("src"));
 }

 @Test
 public void testParseInvalidRelativeUrlWithoutBaseTag() {
 String html = "<html><head><title>My Title</title><link rel='stylesheet' href='../styles.css'></head><body><img src='image.jpg'></body></html>";
 Document parsedDoc = Parser.parse(html, "");
 assertEquals("", parsedDoc.selectFirst("link").absUrl("href"));
 assertEquals("image.jpg", parsedDoc.selectFirst("img").absUrl("src"));
 }
}