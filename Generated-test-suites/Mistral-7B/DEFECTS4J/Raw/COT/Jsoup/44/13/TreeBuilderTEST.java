package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 private String baseUri = "http://example.com";

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
  treeBuilder = new DefaultTreeBuilder(); // assuming DefaultTreeBuilder is the concrete implementation of TreeBuilder
  treeBuilder.initialiseParse(input, baseUri, new ParseErrorList());
  doc = treeBuilder.parse(input, baseUri);
 }

 @Test
 public void testParse() {
  assertNotNull(doc);
 }

 @Test
 public void testParseWithErrors() {
  ParseErrorList errors = new ParseErrorList();
  Document doc = treeBuilder.parse(input, baseUri, errors);
  assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessStartTag() {
  StartTag startTag = new StartTag("html");
  assertTrue(treeBuilder.processStartTag(startTag.name()));
  Element html = doc.head();
  assertEquals("html", html.tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
  Attributes attrs = new Attributes();
  attrs.put("id", "test");
  StartTag startTag = new StartTag("div", attrs);
  Element div = doc.body().elementSibling();
  treeBuilder.processStartTag(startTag.name(), startTag.attributes());
  assertEquals("div", div.tagName());
  assertEquals("test", div.attr("id"));
 }

 @Test
 public void testProcessEndTag() {
  EndTag endTag = new EndTag("body");
  assertTrue(treeBuilder.processEndTag(endTag.name()));
  assertEquals(1, doc.body().childSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithNullName() {
  StartTag startTag = new StartTag(null);
  assertFalse(treeBuilder.processStartTag(startTag.name()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithEmptyName() {
  StartTag startTag = new StartTag("");
  assertFalse(treeBuilder.processStartTag(startTag.name()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithNullAttributes() {
  StartTag startTag = new StartTag("div", null);
  assertFalse(treeBuilder.processStartTag(startTag.name()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithEmptyAttributes() {
  StartTag startTag = new StartTag("div", new Attributes());
  assertFalse(treeBuilder.processStartTag(startTag.name()));
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithNullName() {
  EndTag endTag = new EndTag(null);
  assertFalse(treeBuilder.processEndTag(endTag.name()));
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithEmptyName() {
  EndTag endTag = new EndTag("");
  assertFalse(treeBuilder.processEndTag(endTag.name()));
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithNoMatchingStartTag() {
  EndTag endTag = new EndTag("body");
  assertFalse(treeBuilder.processEndTag(endTag.name()));
 }

 @Test
 public void testCurrentElement() {
  Element html = doc.head();
  assertEquals(html, treeBuilder.currentElement());
 }

 @Test
 public void testRunParser() {
  treeBuilder.runParser();
  assertEquals(15, doc.childSize());
 }

 @Test
 public void testProcessToken() {
  Token token = new Token("html");
  assertFalse(treeBuilder.process(token));
 }

 @Test
 public void testProcessTokenStartTag() {
  Token token = new StartTag("html");
  assertTrue(treeBuilder.process(token));
 }

 @Test
 public void testProcessTokenEndTag() {
  Token token = new EndTag("html");
  assertFalse(treeBuilder.process(token));
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessTokenInvalidToken() {
  Token token = new Token("invalid");
  assertFalse(treeBuilder.process(token));
 }

 @Test
 public void testProcessTokenWithIgnoredToken() {
  Token token = new CommentToken("// This is a comment");
  assertFalse(treeBuilder.process(token));
  assertEquals(15, doc.childSize());
 }

}