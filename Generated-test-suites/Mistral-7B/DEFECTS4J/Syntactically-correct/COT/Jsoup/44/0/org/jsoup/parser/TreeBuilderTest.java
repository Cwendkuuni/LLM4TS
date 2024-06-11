package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 initialiseTestTreeBuilder();
 doc = new Document();
 }

 private void initialiseTestTreeBuilder() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 }

 @Test
 public void testInitialiseParse() {
 String input = "test";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 assertTrue(treeBuilder.reader != null);
 assertTrue(treeBuilder.tokeniser != null);
 assertNotNull(treeBuilder.doc);
 assertEquals(baseUri, treeBuilder.baseUri);
 assertNull(treeBuilder.errors);
 }

 @Test(expected = NullPointerException.class)
 public void testInitialiseParseWithNullInput() {
 String input = null;
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 fail("Should throw NullPointerException for null input");
 }

 @Test(expected = NullPointerException.class)
 public void testInitialiseParseWithNullBaseUri() {
 String input = "test";
 String baseUri = null;
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 fail("Should throw NullPointerException for null baseUri");
 }

 @Test
 public void testParse() {
 String input = "<html><head></head><body></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 assertEquals("<html><head></head><body></body></html>", doc.html());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
 String input = null;
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 fail("Should throw NullPointerException for null input");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 String input = "<html></html>";
 String baseUri = null;
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 fail("Should throw NullPointerException for null baseUri");
 }

 @Test
 public void testRunParser() {
 String input = "<html><head></head><body><h1>Test</h1></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 treeBuilder.runParser();
 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processStartTag("html");
 assertEquals("<html>", doc.html());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String input = "<html id=\"test\" lang=\"en\">";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 attributes.put("lang", "en");
 treeBuilder.processStartTag("html", attributes);
 assertEquals("<html id=\"test\" lang=\"en\">", doc.html());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<html><body><p></p></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processEndTag("p");
 assertEquals("<html><body></body></html>", doc.html());
 }

 @Test
 public void testCurrentElement() {
 String input = "<html><body><p></p></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("body");
 treeBuilder.processStartTag("p");
 Element currentElement = treeBuilder.currentElement();
 assertEquals("p", currentElement.tagName());
 }

 @Test
 public void testProcessStartTagWithInvalidName() {
 String input = "<test>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processStartTag("test");
 assertEquals("<html><head></head><body></body></html>", doc.html());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessEndTagWithInvalidName() {
 String input = "<html><body><p></p></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processEndTag("head");
 assertEquals("<html><body><p></p></body></html>", doc.html());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessStartTagWithEmptyName() {
 String input = "<html><body><p></p></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processStartTag("");
 assertEquals("<html><body><p></p></body></html>", doc.html());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessStartTagTwice() {
 String input = "<html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("html");
 assertEquals("<html>", doc.html());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessEndTagTwice() {
 String input = "<html><body><p></p></body></html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processEndTag("body");
 treeBuilder.processEndTag("body");
 assertEquals("<html></html>", doc.html());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testProcessStartTagWithLowercaseName() {
 String input = "<html>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 treeBuilder.processStartTag("html");
 assertEquals("<html>", doc.html());
 treeBuilder.processStartTag("html"); // should not overwrite previous tag
 assertEquals("<html><html/>", doc.html());
 assertTrue(errors.size() > 0);
 }

}