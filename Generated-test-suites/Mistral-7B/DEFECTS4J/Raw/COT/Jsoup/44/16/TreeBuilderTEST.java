package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TreeBuilder.
 */
public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private Element root;
 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 doc = mock(Document.class);
 root = mock(Element.class);
 treeBuilder = mock(TreeBuilder.class);
 tokeniser = mock(Tokeniser.class);
 errors = mock(ParseErrorList.class);
 characterReader = mock(CharacterReader.class);

 when(doc.createElement(anyString())).then((invocationOnMock) -> root);
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName", "tagName", "attributeName", "attributeValue", false, false));
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName2", null, null, null, false, false));
 when(tokeniser.read()).thenReturn(getTokenForTesting("EOF", null, null, null, false, true));
 }

 private Token getTokenForTesting(String tagName, String attributeName, String attributeValue, boolean isStartTag, boolean isSelfClosing) {
 Token token = new Token();
 token.type = isStartTag ? Token.TokenType.START_TAG : Token.TokenType.END_TAG;
 token.name = tagName;
 token.selfClosing = isSelfClosing;
 if (attributeName != null) {
 token.attributes = new Attributes();
 token.attributes.put(attributeName, attributeValue);
 }
 return token;
 }

 @Test
 public void testInitialiseParse() {
 Validate.notNull(treeBuilder, "TreeBuilder must not be null");
 treeBuilder.initialiseParse("input", "baseUri", errors);
 Validate.assertEquals("baseUri", treeBuilder.baseUri);
 }

 @Test
 public void testParse() {
 Document doc = treeBuilder.parse("input", "baseUri");
 Validate.assertNotNull(doc);
 Validate.assertSame(treeBuilder.doc, doc);
 }

 @Test
 public void testParseWithErrors() {
 Document doc = treeBuilder.parse("input", "baseUri", errors);
 Validate.assertNotNull(doc);
 Validate.assertSame(treeBuilder.doc, doc);
 validateErrors(errors, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 treeBuilder.parse(null, "baseUri");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 treeBuilder.parse("input", null);
 }

 @Test
 public void testRunParser() {
 treeBuilder.runParser();
 Validate.assertEquals(3, tokeniser.getTokensReadCount());
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.processStartTag("tagName");
 Validate.assertFalse(tokeniser.isAtStartOfInput());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 StartTag startTag = new StartTag();
 Attributes attributes = new Attributes();
 attributes.put("attributeName", "attributeValue");
 startTag.attributes = attributes;
 when(tokeniser.read()).thenReturn(startTag);
 treeBuilder.processStartTag("tagName", attributes);
 Validate.assertFalse(tokeniser.isAtStartOfInput());
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder.processEndTag("tagName");
 Validate.assertFalse(tokeniser.isAtStartOfInput());
 }

 @Test
 public void testProcessUnknownToken() {
 when(tokeniser.read()).thenReturn(getTokenForTesting("unknownToken", null, null, null, false, false));
 treeBuilder.process(getTokenForTesting("unknownToken", null, null, null, false, false));
 Validate.assertTrue(tokeniser.isAtStartOfInput());
 }

 @Test
 public void testProcessEOF() {
 Token eofToken = getTokenForTesting("EOF", null, null, null, false, true);
 treeBuilder.process(eofToken);
 Validate.assertTrue(tokeniser.isAtStartOfInput());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEOFAfterStartTag() {
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName", null, null, null, false, false));
 when(tokeniser.read()).thenReturn(getTokenForTesting("EOF", null, null, null, false, true));
 treeBuilder.processStartTag("tagName");
 treeBuilder.process(getTokenForTesting("EOF", null, null, null, false, true));
 }

 @Test
 public void testProcessSelfClosingTag() {
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName", null, null, null, true, false));
 treeBuilder.process(getTokenForTesting("tagName", null, null, null, true, false));
 Validate.assertTrue(tokeniser.isAtStartOfInput());
 }

 @Test
 public void testCurrentElement() {
 Element currentElement = treeBuilder.currentElement();
 Validate.assertNull(currentElement);
 }

 @Test
 public void testCurrentElementAfterStartTag() {
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName", null, null, null, false, false));
 treeBuilder.processStartTag("tagName");
 Element currentElement = treeBuilder.currentElement();
 Validate.assertSame(root, currentElement);
 }

 @Test
 public void testCurrentElementAfterEndTag() {
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName", null, null, null, false, false));
 treeBuilder.processStartTag("tagName");
 when(tokeniser.read()).thenReturn(getTokenForTesting("tagName", null, null, null, false, false));
 treeBuilder.processEndTag("tagName");
 Element currentElement = treeBuilder.currentElement();
 Validate.assertNull(currentElement);
 }

 private void validateErrors(ParseErrorList errors, int expectedNumberOfErrors) {
 if (errors != null) {
 Validate.assertEquals(expectedNumberOfErrors, errors.size());
 }
 }

}