package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 initialiseTest();
 }

 private void initialiseTest() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 doc = treeBuilder.parse("", "", ParseErrorList.noTracking());
 }

 @Test
 public void testParseNullInput() {
 treeBuilder = new TreeBuilder();
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse(null, "", errors);
 assertEquals(1, errors.size());
 assertEquals("Error: String input must not be null", errors.get(0).getMessage());
 assertNull(doc);
 }

 @Test
 public void testParseNullBaseUri() {
 treeBuilder = new TreeBuilder();
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse("", null, errors);
 assertEquals(1, errors.size());
 assertEquals("Error: BaseURI must not be null", errors.get(0).getMessage());
 assertNull(doc);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<html><head><title>Test</title></head><body>";
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return token.type == Token.TokenType.START_TAG;
 }

 @Override
 protected boolean processStartTag(Token startTag) {
 if (startTag.name.equals("html")) {
 doc.setDocType(doc.createDocType("html", "", ""));
 }
 return true;
 }
 };
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, "", errors);
 Element html = doc.head();
 assertEquals("html", html.tagName());
 assertFalse(html.hasAttr("id"));
 Element title = html.child(0);
 assertEquals("head", title.tagName());
 assertFalse(title.hasAttr("id"));
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String input = "<html lang='en' xmlns:html='http://www.w3.org/1999/xhtml'><head><title id='title'>Test</title></head><body>";
 treeBuilder = new TreeBuilder();
 treeBuilder.setProcessor(new TestProcessor());
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, "", errors);
 Element html = doc.head();
 assertEquals("html", html.tagName());
 assertTrue(html.hasAttr("lang"));
 assertEquals("en", html.attr("lang"));
 assertTrue(html.hasAttr("xmlns:html"));
 assertEquals("http://www.w3.org/1999/xhtml", html.attr("xmlns:html"));
 Element title = html.child(0);
 assertEquals("head", title.tagName());
 assertTrue(title.hasAttr("id"));
 assertEquals("title", title.child(0).tagName());
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return token.type == Token.TokenType.END_TAG;
 }

 @Override
 protected boolean processEndTag(Token endTag) {
 if (endTag.name.equals("html")) {
 doc.remove();
 }
 return true;
 }
 };
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse("<html><head><title>Test</title></head></html>", "", errors);
 assertTrue(doc.body() == null);
 assertNull(doc.head().nextSibling());
 }

 @Test
 public void testProcessInvalidStartTag() {
 treeBuilder = new TreeBuilder();
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse("<invalid>", "", errors);
 assertEquals(1, errors.size());
 assertEquals("Error: <invalid> : unrecognized tag name", errors.get(0).getMessage());
 assertNull(doc);
 }

 @Test
 public void testProcessInvalidEndTag() {
 treeBuilder = new TreeBuilder();
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse("<html><head><title>Test</title></body></html>", "", errors); // mismatched end tag
 assertEquals(1, errors.size());
 assertEquals("Error: Mismatched end tag end-tag name : body", errors.get(0).getMessage());
 assertNull(doc);
 }

 @Test
 public void testProcessEOF() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return token.type == Token.TokenType.EOF;
 }
 };
 ParseErrorList errors = new ParseErrorList();
 String input = "<html><head><title>Test</title></head>";
 doc = treeBuilder.parse(input, "", errors);
 doc.body().appendElement("bodyContent");
 doc.body().appendElement("bodyContent2");
 doc.body().appendElement("bodyContent3");
 String output = doc.body().html();
 assertEquals("<html><head><title>Test</title></head><body><bodyContent><bodyContent2><bodyContent3>", output);
 }
}

class TestProcessor implements TreeBuilder {
 @Override
 protected boolean process(Token token) {
 return false;
 }
}