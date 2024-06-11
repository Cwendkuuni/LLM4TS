package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";

 @Before
 public void setUp() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 doc = treeBuilder.parse(input, "http://test.com");
 }

 @After
 public void tearDown() {
 treeBuilder = null;
 doc = null;
 }

 @Test
 public void testParse() {
 Validate.notNull(doc, "Document should not be null");
 assertEquals("http://test.com", doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.title());
 assertEquals("Test", doc.title().text());
 assertNotNull(doc.body());
 assertNotNull(doc.getElementsByTag("h1").first());
 assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 treeBuilder = new TreeBuilder();
 treeBuilder.parse(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 treeBuilder = new TreeBuilder();
 treeBuilder.parse(" <html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", null);
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean processStartTag(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name, Attributes attrs) {
 Element element = new Element(name);
 stack.add(element);
 return true;
 }
 };

 Token token = new Token("html", 0);
 treeBuilder.processStartTag(token);
 assertEquals("html", treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean processStartTag(Token token) {
 return true;
 }

 @Override
 protected boolean processEndTag(Token token) {
 if (stack.size() > 0) {
 stack.remove(stack.size() - 1);
 }
 return true;
 }
 };

 Token startToken = new Token("html", 0);
 treeBuilder.processStartTag(startToken);
 Token endToken = new Token("html", 1);
 treeBuilder.processEndTag(endToken);
 assertEquals(0, stack.size());
 }

 @Test
 public void testProcessInvalidToken() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };

 Token invalidToken = new Token("-", 0);
 treeBuilder.process(invalidToken);
 assertFalse(treeBuilder.errors.hasErrors());
 }

 @Test
 public void testProcessErrors() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };

 treeBuilder.errors = new ParseErrorList();
 Token invalidToken1 = new Token("html", 0);
 treeBuilder.process(invalidToken1);
 treeBuilder.errors.add(new ParseError("Error 1", invalidToken1.getLine(), invalidToken1.getColumn()));

 Token invalidToken2 = new Token("body", 0);
 treeBuilder.process(invalidToken2);
 treeBuilder.errors.add(new ParseError("Error 2", invalidToken2.getLine(), invalidToken2.getColumn()));

 assertEquals(2, treeBuilder.errors.getErrorCount());
 }
}