package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.LinkedList;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

/**
 * Test for {@link Parser} class.
 * @author asad
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().get(0).tagName());
 assertEquals(1, html.childNodes().size());

 Element head = html.childNodes().get(0);
 assertEquals(titleTag, head.childNodes().get(0).tagName());
 assertEquals("Test", head.childNodes().get(0).text());
 assertEquals(1, head.childNodes().size());

 Element body = html.childNodes().get(1);
 assertEquals(h1Tag, body.childNodes().get(0).tagName());
 assertEquals("Hello World", body.childNodes().get(0).text());
 assertEquals(1, body.childNodes().size());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<h1>Hello World</h1>", "http://example.com");
 Document doc = parser.parseBodyFragment("", "http://example.com");

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(bodyTag, html.childNodes().get(0).tagName());
 assertEquals("Hello World", html.childNodes().get(0).text());
 assertEquals(1, html.childNodes().size());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<h1>Hello World</h1>", "http://example.com");
 Document doc = parser.parseBodyFragment("", "http://example.com", true);

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(bodyTag, html.tagName()); // no need for explicit check for bodyTag here since it is the only child of html
 assertEquals("Hello World", html.text());
 }

 @Test
 public void testParseEmpty() {
 Parser parser = new Parser("<html></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(1, html.childNodes().size());
 assertNull(html.childNodes().get(0).text());
 }

 @Test
 public void testParseWithAttributes() {
 Parser parser = new Parser("<h1 id='hello' class='foo bar' title='Test'>Hello World</h1>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().size(), 0);
 assertEquals(bodyTag, html.tagName());

 Element h1 = html.childNodes().get(0);
 assertEquals(1, h1.childNodes().size());
 assertEquals("Hello World", h1.text());

 Attributes attributes = h1.attributes();
 assertEquals(3, attributes.size());
 assertEquals("hello", attributes.get("id"));
 assertEquals("foo bar", attributes.get("class"));
 assertEquals("Test", attributes.get("title"));
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser("<html><base href='/foo'><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().get(0).tagName());
 assertEquals(1, html.childNodes().size());

 Element head = html.childNodes().get(0);
 assertEquals(titleTag, head.childNodes().get(0).tagName());
 assertEquals("Test", head.childNodes().get(0).text());
 assertEquals(1, head.childNodes().size());

 Element body = html.childNodes().get(1);
 assertEquals(h1Tag, body.childNodes().get(0).tagName());
 assertEquals("Hello World", body.childNodes().get(0).text());
 assertEquals(1, body.childNodes().size());

 assertEquals("/foo", doc.baseUri());
 }

 @Test
 public void testParseWithScriptTag() {
 Parser parser = new Parser("<html><head><script type='text/javascript' src='script.js'></script></head></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().size(), 1);

 Element head = html.childNodes().get(0);
 Element script = head.childNodes().get(0);
 assertEquals(scriptTag, script.tagName());
 assertEquals("script.js", script.absUrl("src"));
 assertEquals(0, script.childNodes().size());
 }

 @Test
 public void testParseWithDataTag() {
 Parser parser = new Parser("<html><body><textarea name='foo'>Hello World</textarea></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().size(), 0);
 assertEquals(bodyTag, html.tagName());

 Element body = html.childNodes().get(0);
 Element textarea = body.childNodes().get(0);
 assertEquals(textareaTag, textarea.tagName());
 assertEquals("Hello World", textarea.text());
 assertEquals(0, textarea.childNodes().size());
 }

 @Test
 public void testParseWithCommentTag() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().size(), 0);
 assertEquals(bodyTag, html.tagName());

 Element comment = html.childNodes().get(0);
 assertEquals(commentTag, comment.tagName());
 assertEquals("This is a comment", comment.text());
 assertEquals(0, comment.childNodes().size());
 }

 @Test
 public void testParseWithXMLDeclarationTag() {
 Parser parser = new Parser("<html xmlns='http://www.w3.org/1999/xhtml'><head></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodes().size());

 Element html = doc.childNodes().get(0);
 assertEquals(headTag, html.childNodes().size(), 0);
 assertEquals(bodyTag, html.tagName());

 XmlDeclaration xmlDeclaration = html.childNodes().get(0);
 assertEquals(xmlDeclarationTag, xmlDeclaration.tagName());
 assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlns());
 assertEquals("", xmlDeclaration.doctype());
 assertEquals(false, xmlDeclaration.isStandalone());
 assertEquals("", xmlDeclaration.encoding());
 }
}