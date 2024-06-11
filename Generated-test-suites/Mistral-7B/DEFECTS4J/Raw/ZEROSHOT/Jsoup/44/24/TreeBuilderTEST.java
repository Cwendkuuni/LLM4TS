package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;

import java.util.ArrayList;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document expectedDocument;
 private String input;

 @Before
 public void setUp() {
 input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 expectedDocument = new Document("http://example.com");
 treeBuilder = new SimpleTreeBuilder(); // or any subclass of TreeBuilder
 }

 @Test
 public void testParse() {
 treeBuilder.parse(input, "http://example.com");
 assertEquals(expectedDocument, treeBuilder.doc);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 treeBuilder.parse(null, "http://example.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 treeBuilder.parse(input, null);
 }

 @Test
 public void testParseWithErrors() {
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.parse(input, "http://example.com", errors);
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testRunParser() {
 treeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking());
 treeBuilder.runParser();
 assertEquals(expectedDocument.numChildren(), treeBuilder.stack.size());
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking());
 StartTag startTag = new StartTag("html");
 treeBuilder.processStartTag(startTag);
 Element html = treeBuilder.currentElement();
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());
 stack.clear();
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 StartTag startTag = new StartTag("title", attributes);
 treeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking());
 treeBuilder.processStartTag(startTag);
 Element title = treeBuilder.currentElement();
 assertEquals("title", title.tagName());
 assertEquals("test", title.attributes().get("id"));
 stack.clear();
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking());
 EndTag endTag = new EndTag("html");
 treeBuilder.processEndTag(endTag.name());
 assertNull(treeBuilder.currentElement());
 stack.clear();
 }

 @Test
 public void testProcessStartTagAndEndTag() {
 treeBuilder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking());
 StartTag htmlStartTag = new StartTag("html");
 EndTag htmlEndTag = new EndTag("html");
 treeBuilder.processStartTag(htmlStartTag);
 treeBuilder.processEndTag(htmlEndTag.name());
 assertNull(treeBuilder.currentElement());
 stack.clear();
 }

 @Test
 public void testProcessInvalidToken() {
 treeBuilder.initialiseParse("", "http://example.com", ParseErrorList.noTracking());
 Token invalidToken = new Token();
 invalidToken.type = Token.TokenType.EOF;
 treeBuilder.process(invalidToken);
 assertNull(treeBuilder.doc);
 }
}

 class SimpleTreeBuilder extends TreeBuilder {
 @Override protected boolean process(Token token) {
 if (token.type == Token.TokenType.EOF) {
 doc.body().appendElement("eof");
 return false;
 }
 return super.process(token);
 }
}