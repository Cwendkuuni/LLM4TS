package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse_ValidHtml() {
 String html = "<html><head><title>Test</title></head><body>" +
 " <h1>Hello World</h1>" +
 " <p>This is a test.</p>" +
 " <ul>" +
 " <li>Item 1</li>" +
 " <li>Item 2</li>" +
 " </ul>" +
 " <img src=\"image.jpg\" alt=\"Image\"></img>" +
 " </body></html>";

 Document doc = Parser.parse(html, "http://example.com");
 Element root = doc.body();

 assertEquals("html", root.tagName());
 assertEquals(1, root.childNodeSize());

 Element h1 = (Element) root.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());

 Element p = (Element) h1.nextSibling();
 assertEquals("p", p.tagName());
 assertEquals("This is a test.", p.text());

 Element ul = (Element) p.nextSibling();
 assertEquals("ul", ul.tagName());
 assertEquals(2, ul.childNodeSize());

 Element li1 = (Element) ul.child(0);
 assertEquals("li", li1.tagName());
 assertEquals("Item 1", li1.text());

 Element li2 = (Element) ul.child(1);
 assertEquals("li", li2.tagName());
 assertEquals("Item 2", li2.text());

 Element img = (Element) ul.nextSibling();
 assertEquals("img", img.tagName());
 assertEquals("image.jpg", img.attr("src"));
 assertEquals("Image", img.attr("alt"));
 }

 @Test
 public void testParse_BodyFragment() {
 String html = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");
 Element root = doc;

 assertEquals("html", root.tagName());
 assertEquals(1, root.childNodeSize());

 Element h1 = (Element) root.child(0);
 assertEquals("body", h1.tagName());
 assertEquals("h1", h1.child(0).tagName());
 assertEquals("Hello World", h1.child(0).text());
 }

 @Test
 public void testParse_BodyFragmentRelaxed() {
 String html = "<body><unknown-tag data=\"test\"></unknown-tag>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");
 Element root = doc;

 assertEquals("html", root.tagName());
 assertEquals(1, root.childNodeSize());

 Element body = (Element) root.child(0);
 assertEquals("body", body.tagName());
 assertEquals(1, body.childNodeSize());

 Element unknownTag = (Element) body.child(0);
 assertEquals("unknown-tag", unknownTag.tagName());
 assertEquals("test", unknownTag.attr("data"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_NullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParse_NullBaseUri() {
 Parser.parse("<html></html>", null);
 }
}