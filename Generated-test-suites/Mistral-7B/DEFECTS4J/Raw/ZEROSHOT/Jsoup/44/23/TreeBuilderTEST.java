package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private String input = " <html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";

 @Test
 public void testParse() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
 doc = treeBuilder.parse(input, "http://test.com");

 assertEquals("http://test.com", doc.baseUri());
 assertEquals("html", doc.tagName());

 Element html = doc.body();
 assertEquals("body", html.tagName());

 Element title = html.head();
 assertEquals("head", title.tagName());
 Element h1 = html.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello", h1.text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.initialiseParse(null, "http://test.com", ParseErrorList.noTracking());
 treeBuilder.parse(input, "http://test.com");
 fail("Should have thrown a NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.initialiseParse(input, null, ParseErrorList.noTracking());
 treeBuilder.parse(input, "http://test.com");
 fail("Should have thrown a NullPointerException");
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return token.type == Token.TokenType.START_TAG;
 }

 @Override
 protected boolean processStartTag(String name) {
 return name.equals("html");
 }

 @Override
 protected Element currentElement() {
 return null;
 }
 };
 treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
 doc = treeBuilder.parse(input, "http://test.com");

 assertFalse(treeBuilder.processStartTag("head"));
 assertTrue(treeBuilder.processStartTag("html"));
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return token.type == Token.TokenType.START_TAG;
 }

 @Override
 protected Element currentElement() {
 return null;
 }

 @Override
 protected boolean processStartTag(String name, Attributes attrs) {
 return name.equals("title") && attrs.value("title").equals("Test");
 }
 };
 treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
 doc = treeBuilder.parse(input, "http://test.com");

 Element title = doc.head();
 assertTrue(treeBuilder.processStartTag(title.tagName(), title.attributes()));
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return token.type == Token.TokenType.END_TAG;
 }

 @Override
 protected Element currentElement() {
 return null;
 }

 @Override
 protected boolean processEndTag(String name) {
 return name.equals("html");
 }
 };
 treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
 doc = treeBuilder.parse(input, "http://test.com");

 assertFalse(treeBuilder.processEndTag("head"));
 assertTrue(treeBuilder.processEndTag("html"));
 }
}