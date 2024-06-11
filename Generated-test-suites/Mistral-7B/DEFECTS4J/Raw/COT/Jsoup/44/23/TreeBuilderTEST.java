package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

/**
 * Test for TreeBuilder class
 */
public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 private String baseUri = "http://www.example.com";
 private ParseErrorList errors = new ParseErrorList();

 @Test
 public void testInitialiseParse() {
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, errors);
     assertNotNull(treeBuilder.doc);
     assertNotNull(treeBuilder.tokeniser);
     assertNotNull(treeBuilder.stack);
     assertNotNull(treeBuilder.baseUri);
     assertNull(treeBuilder.currentToken);
     assertNull(treeBuilder.errors);
 }

 @Test
 public void testParse() {
     treeBuilder = new HtmlTreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }
     };
     Document doc = treeBuilder.parse(input, baseUri);
     assertNotNull(doc);
     assertEquals(1, doc.body().child(0).child(0).text().length());
     assertEquals("Hello", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseWithErrors() {
     treeBuilder = new HtmlTreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }
     };
     ParseErrorList errors = new ParseErrorList();
     Document doc = treeBuilder.parse(input, baseUri, errors);
     assertNotNull(doc);
     assertTrue(errors.size() > 0);
 }

 @Test
 public void testRunParser() {
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             System.out.println("Start tag : " + token.toString());
             return true;
         }

         @Override
         protected boolean processEndTag(Token token) {
             System.out.println("End tag : " + token.toString());
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.runParser();
 }

 @Test(expected = NullPointerException.class)
 public void testProcessStartTagNullName() {
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             return process(token);
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.processStartTag(null);
 }

 @Test(expected = NullPointerException.class)
 public void testProcessStartTagNullAttrs() {
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }

         @Override
         protected boolean processStartTag(Token token) {
             Attributes attrs = new Attributes();
             return process(token, attrs);
         }

         @Override
         protected boolean processStartTag(String name, Attributes attrs) {
             return process(token, attrs);
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.processStartTag(null, new Attributes());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
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
         protected boolean processStartTag(String name, Attributes attrs) {
             assertEquals("Test", attrs.get("title"));
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.processStartTag("title", new Attributes() {{
         put("title", "Test");
     }});
 }

 @Test
 public void testProcessEndTag() {
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
         protected boolean processEndTag(String name) {
             assertEquals("h1", name);
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.processStartTag("html", new Attributes());
     treeBuilder.processStartTag("head", new Attributes());
     treeBuilder.processStartTag("title", new Attributes());
     treeBuilder.processStartTag("body", new Attributes());
     treeBuilder.processStartTag("h1", new Attributes());
     treeBuilder.processEndTag("h1");
 }

 @Test
 public void testCurrentElement() {
     treeBuilder = new TreeBuilder() {
         @Override
         protected boolean process(Token token) {
             return true;
         }
     };
     treeBuilder.initialiseParse(input, baseUri, null);
     treeBuilder.processStartTag("html", new Attributes());
     treeBuilder.processStartTag("head", new Attributes());
     treeBuilder.processStartTag("title", new Attributes());
     Element title = treeBuilder.processStartTag("title", new Attributes());
     assertEquals("title", title.tagName());
     assertEquals("Test", title.text());
     treeBuilder.processEndTag("title");
     Element body = treeBuilder.currentElement();
     assertEquals("body", body.tagName());
 }

}