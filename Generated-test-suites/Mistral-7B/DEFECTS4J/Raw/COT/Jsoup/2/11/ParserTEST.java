package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;

public class ParserTest {

 private static final String HTML_WITH_COMMENT = "<!-- comment --> <html><body></body></html>";
 private static final String HTML_WITH_XML_DECL = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 private static final String HTML_WITH_SCRIPT_TAG = "<html><head><script>function test(){}</script></head><body></body></html>";
 private static final String HTML_WITH_EMPTY_TAG = "<html><body><img src=\"image.jpg\"></body></html>";
 private static final String HTML_WITH_TEXTAREA_TAG = "<html><body><textarea></textarea></body></html>";
 private static final String HTML_WITH_BASE_TAG = "<html><head><base href=\"base.html\"></base></html><body></body></html>";

 @Test
 public void testParse() {
 Parser parser = new Parser(HTML_WITH_COMMENT, null, false);
 Document doc = parser.parse();
 assertEquals("comment", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser(HTML_WITH_XML_DECL, null, true);
 Document doc = parser.parse();
 assertTrue(doc.xmlDeclaration().standalone);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("html", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyHtml() {
 Parser parser = new Parser("", null, false);
 }

 @Test
 public void testParseWithScriptTag() {
 Parser parser = new Parser(HTML_WITH_SCRIPT_Tag, null, false);
 Document doc = parser.parse();
 assertEquals(1, doc.getElementsByTag("script").size());
 }

 @Test
 public void testParseWithEmptyTag() {
 Parser parser = new Parser(HTML_WITH_EMPTY_TAG, null, false);
 Document doc = parser.parse();
 assertEquals(1, doc.getElementsByTag("img").size());
 }

 @Test
 public void testParseWithTextareaTag() {
 Parser parser = new Parser(HTML_WITH_TEXTAREA_TAG, null, false);
 Document doc = parser.parse();
 assertEquals(1, doc.getElementsByTag("textarea").size());
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser(HTML_WITH_BASE_TAG, null, false);
 Document doc = parser.parse();
 assertEquals("base.html", doc.baseUri());
 }

 @Test
 public void testParseWithInvalidStartTag() {
 Parser parser = new Parser("<invalidTag attr=\"value\">", null, false);
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithInvalidEndTag() {
 Parser parser = new Parser("<html><body><div></div></foo>", null, false);
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithMismatchedTags() {
 Parser parser = new Parser("<html><body><div><div></div></div></html>", null, false);
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseWithNestedTags() {
 Parser parser = new Parser("<html><body><div><p>Text inside div</p></div></body></html>", null, false);
 Document doc = parser.parse();
 Element div = doc.body().child(0).child(0);
 Element p = div.child(0);
 assertEquals("Text inside div", p.text());
 }
}