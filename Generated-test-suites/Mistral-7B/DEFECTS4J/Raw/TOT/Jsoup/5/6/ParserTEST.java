package org.jsoup.parser;

import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.*;import static org.jsoup.helper.Utils.*;import static org.jsoup.parser.Parser.*;

import org.jsoup.nodes.*;

/**
 * ParserTest class for testing Parser methods.
 */
public class ParserTest {

 @Test
 public void testParseHtmlWithBaseUri() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Test Heading</h1><p>Test Paragraph</p><img src=\"image.jpg\" alt=\"Test Image\"></body></html>";
 Document doc = parse(html, "http://www.example.com");

 Element title = doc.title();
 assertEquals("Test Page", title.text());
 Element h1 = doc.select("h1").first();
 assertEquals("Test Heading", h1.text());
 Element p = doc.select("p").first();
 assertEquals("Test Paragraph", p.text());
 Element img = doc.select("img").first();
 assertEquals("http://www.example.com/image.jpg", img.absUrl("src"));
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String bodyHtml = "<body><h1>Test Heading</h1><p>Test Paragraph</p></body>";
 Document doc = parseBodyFragment(bodyHtml, "http://www.example.com");

 Element title = doc.head(); // head should be empty
 Assert.assertNull(title);
 Element h1 = doc.select("h1").first();
 assertEquals("Test Heading", h1.text());
 Element p = doc.select("p").first();
 assertEquals("Test Paragraph", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxedWithBaseUri() {
 String bodyHtml = "<body><p><img src=\"image.jpg\" alt=\"Test Image\"></p></body>";
 Document doc = parseBodyFragmentRelaxed(bodyHtml, "http://www.example.com");

 Element title = doc.head(); // head should be empty
 Assert.assertNull(title);
 Element p = doc.select("p").first();
 Element img = p.select("img").first();
 assertEquals("http://www.example.com/image.jpg", img.absUrl("src"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtmlOrBaseUri() {
 String nullHtml = null;
 String nullBaseUri = null;

 try {
 parse(nullHtml, nullBaseUri);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyHtml() {
 String emptyHtml = "";
 String baseUri = "http://www.example.com";

 try {
 parse(emptyHtml, baseUri);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyBaseUri() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
 String emptyBaseUri = "";

 try {
 parse(html, emptyBaseUri);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullDocument() {
 Document nullDoc = null;
 parse(html, "http://www.example.com", nullDoc);
 fail("NullPointerException expected but not thrown");
 }

}