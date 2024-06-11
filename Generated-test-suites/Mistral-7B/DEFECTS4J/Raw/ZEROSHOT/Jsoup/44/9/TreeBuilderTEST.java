package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private CharacterReader characterReader;
 private Tokeniser tokeniser;
 private Document doc;
 private ParseErrorList errors;
 private Element rootElement;

 @Before
 public void setup() {
 baseSetup();
 treeBuilder = new HtmlTreeBuilder(); // use HtmlTreeBuilder as an example
 }

 private void baseSetup() {
 characterReader = mock(CharacterReader.class);
 tokeniser = mock(Tokeniser.class);
 doc = mock(Document.class);
 errors = mock(ParseErrorList.class);
 rootElement = Mockito.spy(new Element("html"));
 when(doc.createElement("html")).thenReturn(rootElement);
 when(errors.noTracking()).thenReturn(errors);
 when(tokeniser.read()).thenReturn(getTestTokens());
 }

 private List<Token> getTestTokens() {
 return Arrays.asList(
 new Token.StartTag(),
 new Token.StartTag("html"),
 new Token.StartTag("head"),
 new Token.EndTag("head"),
 new Token.StartTag("body"),
 new Token.EndTag("body"),
 new Token.EndTag("html")
 );
 }

 @Test
 public void testParse() {
 treeBuilder.initialiseParse("testInput", "testBaseUri", errors);
 treeBuilder.parse("testInput", "testBaseUri");
 assertNotNull(doc);
 assertEquals(rootElement, doc.body());
 assertEquals(rootElement, doc.head());
 assertEquals(doc, treeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.initialiseParse("testInput", "testBaseUri", errors);
 treeBuilder.runParser();
 treeBuilder.processStartTag("html");
 treeBuilder.processEndTag("html");
 assertNotNull(doc);
 assertEquals(rootElement, doc.body());
 assertEquals(rootElement, doc.head());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("testInput", "testBaseUri", errors);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.processStartTag("html", new Attributes() {{ setAttribute("xmlns", "http://www.w3.org/1999/xhtml"); }});
 xmlTreeBuilder.processEndTag("html");
 Element htmlElement = xmlTreeBuilder.currentElement();
 assertEquals("html", htmlElement.tagName());
 assertNotNull(htmlElement.attributes());
 assertEquals("http://www.w3.org/1999/xhtml", htmlElement.xmlns());
 }

 @Test(expected = AssertionError.class)
 public void testProcessInvalidToken() {
 treeBuilder.initialiseParse("testInput", "testBaseUri", errors);
 treeBuilder.runParser();
 treeBuilder.process(new Token.TextNode("InvalidToken"));
 }
}