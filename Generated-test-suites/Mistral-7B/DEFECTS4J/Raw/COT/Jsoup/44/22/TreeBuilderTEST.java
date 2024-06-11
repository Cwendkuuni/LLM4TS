package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

/**
 * Test cases for TreeBuilder class
 */
public class TreeBuilderTest {

 private TreeBuilder treeBuilder;

 @Test
 public void testInitializeParse() {
     String input = "test input";
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     assertNotNull(treeBuilder.doc);
     assertNotNull(treeBuilder.reader);
     assertNotNull(treeBuilder.tokeniser);
     assertNotNull(treeBuilder.stack);
     assertEquals(baseUri, treeBuilder.baseUri);
     assertNull(treeBuilder.currentToken);
     assertNull(treeBuilder.errors);
 }

 @Test
 public void testParse() {
     String input = "test input";
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             return true;
         }

         @Override
         protected boolean processEndTag(Token token) {
             return true;
         }

         @Override
         protected Document parse(String input, String baseUri, ParseErrorList errors) {
             return super.parse(input, baseUri, errors);
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.runParser();
     assertNotNull(treeBuilder.doc.body());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected Document parse(String input, String baseUri, ParseErrorList errors) {
             return super.parse(input, baseUri, errors);
         }
     };
     treeBuilder.initialiseParse(null, baseUri, null);
     treeBuilder.runParser();
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
     String input = "test input";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected Document parse(String input, String baseUri, ParseErrorList errors) {
             return super.parse(input, baseUri, errors);
         }
     };
     treeBuilder.initialiseParse(input, null, null);
     treeBuilder.runParser();
 }

 @Test
 public void testProcessStartTag() {
     String input = "<div><p>Test</p></div>";
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             return true;
         }

         @Override
         protected boolean processEndTag(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.runParser();
     Element currentElement = treeBuilder.currentElement();
     assertNotNull(currentElement);
     assertEquals("div", currentElement.tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
     String input = "<div id='test' class='test'><p>Test</p></div>";
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             return true;
         }

         @Override
         protected boolean processEndTag(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.runParser();
     Element currentElement = treeBuilder.currentElement();
     assertNotNull(currentElement);
     assertEquals("div", currentElement.tagName());
     Attributes attributes = currentElement.attributes();
     assertEquals("test", attributes.get("id"));
     assertEquals("test", attributes.get("class"));
 }

 @Test
 public void testProcessEndTag() {
     String input = "<div><p>Test</p></div>";
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             return true;
         }

         @Override
         protected boolean processEndTag(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.runParser();
     Element currentElement = treeBuilder.currentElement();
     assertNotNull(currentElement);
     assertEquals("div", currentElement.tagName());
     treeBuilder.processEndTag("p");
     assertNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTagWithNoMatchingStartTag() {
     String input = "<div><p>Test</p></div>";
     String baseUri = "https://example.com";
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             return true;
         }

         @Override
         protected boolean processEndTag(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.runParser();
     Element currentElement = treeBuilder.currentElement();
     assertNotNull(currentElement);
     assertEquals("div", currentElement.tagName());
     treeBuilder.processEndTag("span");
     assertNull(treeBuilder.currentElement());
 }
}