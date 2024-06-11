package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.junit.Test;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private ParseErrorList errors;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 InputStream inputStream = new ByteArrayInputStream("Test input".getBytes());
 String input = new String(inputStream.readAllBytes());
 String baseUri = "http://test.com";
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 doc = treeBuilder.parse(input, baseUri);
 }

 @Test
 public void testParse() {
 Validate.notNull(doc, "parse should return a non-null Document");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 treeBuilder.initialiseParse(null, "", errors);
 treeBuilder.parse("input", "");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 treeBuilder.initialiseParse("", baseUri, errors);
 treeBuilder.parse(null, baseUri);
 }

 @Test
 public void testProcessStartTag() {
 Token startToken = new Token(Token.TokenType.STARTTAG, "div", new Attributes());
 assertTrue(treeBuilder.processStartTag(startToken.name));
 Element currentElement = treeBuilder.currentElement();
 assertEquals("div", currentElement.tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 Token startToken = new Token(Token.TokenType.STARTTAG, "span", new Attributes().put("id", "testId"));
 startToken.attributes().put("class", "testClass");
 assertTrue(treeBuilder.processStartTag(startToken.name, startToken.attributes()));
 Element currentElement = treeBuilder.currentElement();
 assertEquals("span", currentElement.tagName());
 assertEquals("testId", currentElement.id());
 assertEquals("testClass", currentElement.classes().get(0));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.TokenType.ENDTAG, "div");
 assertTrue(treeBuilder.processEndTag(endTagToken.name));
 Element currentElement = treeBuilder.currentElement();
 assertSame(stack.get(stack.size() - 2), currentElement);
 }

 @Test
 public void testProcessInvalidToken() {
 Token invalidToken = new Token(Token.TokenType.INVALID, "InvalidToken");
 assertFalse(treeBuilder.process(invalidToken));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithNullName() {
 treeBuilder.processStartTag(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithNullAttributes() {
 treeBuilder.processStartTag(null, new Attributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCurrentElementWhenStackIsEmpty() {
 assertNull(treeBuilder.currentElement());
 }
}