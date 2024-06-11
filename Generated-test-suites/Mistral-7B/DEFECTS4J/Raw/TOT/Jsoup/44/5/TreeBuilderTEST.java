package org.jsoup.parser.test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private String input;
 private String baseUri;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 input = "<html><head><title>Test Page</title></head>" +
 "<body><h1 id='test'>Hello, World!</h1></body></html>";
 baseUri = "http://test.com";
 errors = ParseErrorList.noTracking();
 }

 @Test
 public void testParse() {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse(input, baseUri, errors);
 Document doc = treeBuilder.parse(input, baseUri);
 assertEquals("Test Page", doc.title().text());
 assertEquals("Hello, World!", doc.getElementById("test").text());
 assertTrue(doc.body().hasText());
 }

 @Test
 public void testParseWithErrors() {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse("<html><title<h1", baseUri, errors);
 assertNotNull(treeBuilder.errors);
 assertTrue(treeBuilder.errors.size() > 0);
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse(input, baseUri, errors);
 Token token = new Token.StartTag();
 token.name("html");
 assertTrue(treeBuilder.processStartTag(token.name()));
 token = new Token.StartTag();
 token.name("head");
 assertTrue(treeBuilder.processStartTag(token.name()));
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse(input, baseUri, errors);
 Attributes attrs = new Attributes();
 attrs.put("id", "title");
 Token token = new Token.StartTag();
 token.name("title").attributes(attrs);
 assertTrue(treeBuilder.processStartTag(token.name(), attrs));
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse("<html><head><title>Test Page</title></head>" +
 "<body><h1 id='test'>Hello, World!</h1></body></html>", baseUri, errors);
 Element currentElement = treeBuilder.currentElement();
 Token token = new Token.EndTag();
 token.name("body");
 assertTrue(treeBuilder.processEndTag(token.name()));
 assertEquals(currentElement, treeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTagWrongName() {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse("<html><head><title>Test Page</title></head>" +
 "<body><h1 id='test'>Hello, World!</h1></body></html>", baseUri, errors);
 Element currentElement = treeBuilder.currentElement();
 Token token = new Token.EndTag();
 token.name("head");
 assertFalse(treeBuilder.processEndTag(token.name()));
 }
}