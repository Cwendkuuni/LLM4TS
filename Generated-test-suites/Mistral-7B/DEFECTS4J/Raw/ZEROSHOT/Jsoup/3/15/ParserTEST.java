package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser.java.
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element head = html.child(0);
 assertEquals("head", head.tagName());

 Element title = head.child(0);
 assertEquals("title", title.tagName());
 assertEquals("Test", title.text());

 Element body = html.child(1);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com");
 Document doc = parser.parseBodyFragment();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element body = html.child(0);
 assertEquals("body", body.tagName());

 Element p = body.child(0);
 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser("<html><base href=\"/test\"><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("/test", html.baseUri());

 Element head = html.child(0);
 assertEquals("head", head.tagName());

 Element title = head.child(0);
 assertEquals("title", title.tagName());
 assertEquals("Test", title.text());

 Element body = html.child(1);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseEmpty() {
 Parser parser = new Parser("", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 assertTrue(doc.isEmpty());
 }

 @Test
 public void testParseWithInvalidHtml() {
 Parser parser = new Parser("</html>", "http://example.com");
 try {
 parser.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithMissingEndTag() {
 Parser parser = new Parser("<p>Hello World<script>alert(\"Hello\");</p>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element body = html.child(0);
 assertEquals("body", body.tagName());

 Element p = body.child(0);
 assertEquals("p", p.tagName());
 assertEquals("Hello World<script>alert(\"Hello\");", p.text()); // should not have stripped script tag

 Element script = p.child(0);
 assertNull(script); // should not have created script tag
 }

 @Test
 public void testParseWithCdata() {
 Parser parser = new Parser("<html><![CDATA[ <p>Hello World</p> ]]> </html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element body = html.child(0);
 assertEquals("body", body.tagName());

 Element p = body.child(0);
 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseWithTextarea() {
 Parser parser = new Parser("<html><body><textarea>Hello World</textarea></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element body = html.child(0);
 assertEquals("body", body.tagName());

 Element textarea = body.child(0);
 assertEquals("textarea", textarea.tagName());
 assertEquals("Hello World", textarea.text());
 }

 @Test
 public void testParseWithComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 assertTrue(html.child(0).toString().startsWith("<!-- This is a comment -->"));
 }
}