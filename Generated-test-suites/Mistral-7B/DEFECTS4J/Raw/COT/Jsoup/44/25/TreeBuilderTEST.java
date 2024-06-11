package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Tokeniser tokeniser;
 private CharacterReader characterReader;
 private Document document;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 characterReader = mock(CharacterReader.class);
 tokeniser = mock(Tokeniser.class);
 errors = mock(ParseErrorList.class);
 document = mock(Document.class);
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
 treeBuilder.initialiseParse("input", "baseUri", errors);
 when(characterReader.read()).thenCallRealMethod();
 when(tokeniser.read()).thenCallRealMethod();
 }

 @After
 public void tearDown() {
 treeBuilder = null;
 characterReader = null;
 tokeniser = null;
 document = null;
 errors = null;
 }

 @Test
 public void testParse() {
 String input = "input";
 String baseUri = "baseUri";
 Document parsedDocument = treeBuilder.parse(input, baseUri);
 assertEquals(baseUri, parsedDocument.baseURI());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
 treeBuilder.parse(null, "baseUri");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 treeBuilder.parse("input", null);
 }

 @Test
 public void testParseWithErrors() {
 String input = "input";
 ParseErrorList errors = mock(ParseErrorList.class);
 Document parsedDocument = treeBuilder.parse(input, "baseUri", errors);
 assertEquals(parsedDocument, treeBuilder.doc);
 // check that errors were recorded
 // add assertions based on the expected behavior of the TreeBuilder class
 }

 @Test
 public void testProcessStartTag() {
 String name = "name";
 Attributes attrs = new Attributes();
 attrs.put("key", "value");
 treeBuilder.processStartTag(name, attrs);
 assertSame(name, treeBuilder.start.name());
 assertEquals("value", treeBuilder.start.attr("key"));
 }

 @Test(expected = NullPointerException.class)
 public void testProcessStartTagWithNullName() {
 treeBuilder.processStartTag(null, new Attributes());
 }

 @Test
 public void testProcessEndTag() {
 String name = "name";
 treeBuilder.processEndTag(name);
 assertSame(name, treeBuilder.end.name());
 }

 @Test(expected = NullPointerException.class)
 public void testProcessEndTagWithNullName() {
 treeBuilder.processEndTag(null);
 }

 @Test
 public void testCurrentElement() {
 Element element = mock(Element.class);
 stack.add(element);
 assertSame(element, treeBuilder.currentElement());
 }

 @Test
 public void testCurrentElementWhenStackIsEmpty() {
 assertNull(treeBuilder.currentElement());
 }
}