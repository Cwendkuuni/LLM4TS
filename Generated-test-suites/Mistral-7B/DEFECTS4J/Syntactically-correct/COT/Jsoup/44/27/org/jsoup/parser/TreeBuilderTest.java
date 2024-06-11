package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setup() {
 initialiseTreeBuilder();
 doc = new Document("");
 }

 @BeforeClass
 public static void setupClass() {
 Validate.notNull(new TreeBuilder(), "TreeBuilder should not be null");
 }

 @Test
 public void testInitialiseParse() {
 String input = "test input";
 String baseUri = "test base uri";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder = new TreeBuilder();
 treeBuilder.initialiseParse(input, baseUri, errors);
 assertTrue(treeBuilder.reader != null);
 assertTrue(treeBuilder.tokeniser != null);
 assertTrue(treeBuilder.doc != null);
 assertTrue(treeBuilder.errors == errors);
 assertTrue(treeBuilder.stack.isEmpty());
 assertTrue(treeBuilder.baseUri.equals(baseUri));
 }

 @Test
 public void testParse() {
 String input = "test input";
 String baseUri = "test base uri";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testParseWithErrors() {
 String input = "test input";
 String baseUri = "test base uri";
 ParseErrorList errors = new ParseErrorList();
 doc = treeBuilder.parse(input, baseUri, errors);
 assertNotNull(doc);
 assertTrue(errors.size() == 0);
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
 String baseUri = "test base uri";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.parse(null, baseUri, errors);
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 String input = "test input";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.parse(input, null, errors);
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullErrors() {
 String input = "test input";
 String baseUri = "test base uri";
 treeBuilder.parse(input, baseUri, null);
 }

 @Test
 public void testRunParser() {
 String input = "<html><head/><body></body></html>";
 String baseUri = "test base uri";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);
 treeBuilder.runParser();
 assertTrue(treeBuilder.doc.body() != null);
 }

 @Test
 public void testProcessStartTag() {
 String name = "html";
 treeBuilder.processStartTag(name);
 assertFalse(treeBuilder.doc.body() != null);
 assertEquals(name, treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String name = "div";
 Attributes attrs = new Attributes();
 attrs.put("id", "testDiv");
 treeBuilder.processStartTag(name, attrs);
 assertFalse(treeBuilder.doc.body() != null);
 assertEquals(name, treeBuilder.currentElement().tagName());
 assertEquals("testDiv", treeBuilder.currentElement().attribute("id"));
 }

 @Test
 public void testProcessEndTag() {
 String name = "html";
 treeBuilder.processStartTag(name);
 treeBuilder.processEndTag(name);
 assertNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTagWithEmptyStack() {
 treeBuilder.processEndTag("html");
 assertNull(treeBuilder.currentElement());
 }

}