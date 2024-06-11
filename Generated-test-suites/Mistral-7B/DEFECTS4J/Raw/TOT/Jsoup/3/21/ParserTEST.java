package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserTest {

 private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --><body></body></html>";
 private static final String HTML_WITH_CDATA = "<html><![CDATA[This is CDATA]]></html>";
 private static final String HTML_WITH_XML_DECL = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
 private static final String HTML_WITH_BASE_TAG = "<html><base href=\"http://example.com\"><body></body></html>";
 private static final String HTML_WITH_MISSING_END_TAG = "<html><body><p><div";
 private static final String HTML_WITH_EMPTY_ELEMENT = "<html><body><img src=\"image.png\"></body></html>";
 private static final String HTML_WITH_SELF_CLOSED_TAG = "<html><body><input type=\"text\"></body></html>";
 private static final String HTML_WITH_DATA_TAG = "<html><body><title>Test Title</title><script type=\"text/javascript\">function test(){}</script></body></html>";
 private static final String HTML_WITH_MIXED_CONTENT = "<html><body><p>This is <b>some</b> <img src=\"image.png\"></p></body></html>";
 private static final String HTML_WITH_TAG_MISMATCH = "<html><body><p><span>This is a <b>span</b></p></body></html>";

 @Test
 public void testParseWithComment() {
 Parser parser = new Parser(HTML_WITH_COMMENT, null, false);
 Document doc = parser.parse();
 Element comment = doc.body().child(0);
 String commentText = comment.text();
 Assert.assertEquals("This is a comment", commentText);
 }

 @Test
 public void testParseWithCdata() {
 Parser parser = new Parser(HTML_WITH_CDATA, null, false);
 Document doc = parser.parse();
 Element cdata = doc.body().child(0);
 String cdataText = cdata.text();
 Assert.assertEquals("This is CDATA", cdataText);
 }

 @Test
 public void testParseWithXmlDecl() {
 Parser parser = new Parser(HTML_WITH_XML_DECL, null, false);
 Document doc = parser.parse();
 Assert.assertEquals("http://www.w3.org/1999/xhtml", doc.xmlNsPrefix("html"));
 Assert.assertEquals("en", doc.xmlNsAttribute("xml:lang"));
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser(HTML_WITH_BASE_TAG, null, false);
 Document doc = parser.parse();
 String baseUrl = doc.baseUri();
 Assert.assertEquals("http://example.com", baseUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithMissingEndTag() {
 Parser parser = new Parser(HTML_WITH_MISSING_END_TAG, null, false);
 Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse());
 }

 @Test
 public void testParseWithEmptyElement() {
 Parser parser = new Parser(HTML_WITH_EMPTY_ELEMENT, null, false);
 Document doc = parser.parse();
 Element img = doc.body().child(0).child(0);
 String imgSrc = img.attr("src");
 Assert.assertEquals("image.png", imgSrc);
 }

 @Test
 public void testParseWithSelfClosedTag() {
 Parser parser = new Parser(HTML_WITH_SELF_CLOSED_TAG, null, false);
 Document doc = parser.parse();
 Element input = doc.body().child(0).child(0);
 String inputType = input.tagName();
 Assert.assertEquals("input", inputType);
 }

 @Test
 public void testParseWithDataTag() {
 Parser parser = new Parser(HTML_WITH_DATA_TAG, null, false);
 Document doc = parser.parse();
 Element title = doc.title();
 String titleText = title.text();
 Assert.assertEquals("Test Title", titleText);
 Element script = doc.body().child(0).child(1);
 String scriptText = script.text();
 Assert.assertTrue(StringUtils.isNotBlank(scriptText));
 }

 @Test
 public void testParseWithMixedContent() {
 Parser parser = new Parser(HTML_WITH_MIXED_CONTENT, null, false);
 Document doc = parser.parse();
 Element p = doc.body().child(0);
 String pText = p.text();
 Assert.assertEquals("This is some", pText);
 Element img = p.child(1);
 String imgSrc = img.attr("src");
 Assert.assertEquals("image.png", imgSrc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithTagMismatch() {
 Parser parser = new Parser(HTML_WITH_TAG_MISMATCH, null, false);
 Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser(HTML_WITH_EMPTY_ELEMENT, null, true);
 Document doc = parser.parse();
 Element body = doc;
 Assert.assertEquals(2, body.childCount());
 Element img = body.child(0);
 String imgSrc = img.attr("src");
 Assert.assertEquals("image.png", imgSrc);
 }

 @Test
 public void testParseBodyFragmentWithDataTag() {
 Parser parser = new Parser(HTML_WITH_DATA_TAG, null, true);
 Document doc = parser.parse();
 Element body = doc;
 Assert.assertEquals(2, body.childCount());
 Element title = body.child(0);
 String titleText = title.text();
 Assert.assertEquals("Test Title", titleText);
 Element script = body.child(1);
 String scriptText = script.text();
 Assert.assertTrue(StringUtils.isNotBlank(scriptText));
 }

 @Test
 public void testParseBodyFragmentWithMissingEndTag() {
 Parser parser = new Parser(HTML_WITH_MISSING_END_TAG, null, true);
 Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse());
 }

 @Test
 public void testParseBodyFragmentWithTagMismatch() {
 Parser parser = new Parser(HTML_WITH_TAG_MISMATCH, null, true);
 Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse());
 }
}