package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.parser.ParseErrorList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test class for TreeBuilder class.
 *
 * @author John Doe
 */
public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private ParseErrorList errors;
 private Document doc;

 @Before
 public void setUp() {
 input = "test.html";
 baseUri = "http://example.com";
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // all methods are tested in this class, so we just need to process the token
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 }

 @After
 public void tearDown() {
 treeBuilder = null;
 doc = null;
 }

 @Test
 public void testParseWithValidInput() {
 doc = treeBuilder.parse("test.html", baseUri);
 assertNotNull(doc);
 assertNotNull(doc.body());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
 treeBuilder.parse(null, baseUri);
 fail("Expected NullPointerException, but got null document instead");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 treeBuilder.parse("test.html", null);
 fail("Expected NullPointerException, but got null baseUri instead");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 treeBuilder.parse("", baseUri);
 fail("Expected IllegalArgumentException, but got null document instead");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNoOpeningTags() {
 doc = treeBuilder.parse("<img src=\"image.png\" />", baseUri);
 assertEquals(0, doc.body().childNodes().size());
 }

 @Test
 public void testProcessStartTag() {
 String name = "div";
 treeBuilder.processStartTag(name);
 Element parent = currentElement();
 assertEquals(name, parent.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithEmptyName() {
 treeBuilder.processStartTag("");
 fail("Expected IllegalArgumentException, but got null element instead");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithNullName() {
 treeBuilder.processStartTag(null);
 fail("Expected IllegalArgumentException, but got null element instead");
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String name = "a";
 String attrName = "href";
 String attrValue = "http://example.com";
 Attributes attrs = new Attributes();
 attrs.put(attrName, attrValue);
 treeBuilder.processStartTag(name, attrs);
 Element parent = currentElement();
 assertEquals(name, parent.tagName());
 assertEquals(attrValue, parent.attr(attrName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithNullAttributes() {
 treeBuilder.processStartTag(name, null);
 fail("Expected IllegalArgumentException, but got null attributes instead");
 }

 @Test
 public void testProcessEndTag() {
 String name = "div";
 treeBuilder.processStartTag(name);
 treeBuilder.processEndTag(name);
 Element parent = currentElement();
 assertNull(parent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithEmptyName() {
 treeBuilder.processEndTag("");
 fail("Expected IllegalArgumentException, but got null element instead");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithNullName() {
 treeBuilder.processEndTag(null);
 fail("Expected IllegalArgumentException, but got null element instead");
 }
}