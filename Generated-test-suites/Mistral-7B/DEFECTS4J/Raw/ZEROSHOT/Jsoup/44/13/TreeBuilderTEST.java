package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private String input;
 private String baseUri;

 @Before
 public void setUp() {
 input = " <html><head><title>Test</title></head><body><h1 id='test'>Test</h1></body></html>";
 baseUri = "http://test.com";
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // Implement your assertions here for each token
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 addElementToStack(name);
 return true;
 }

 @Override
 protected boolean processEndTag(String name) {
 removeLastElementFromStack(name);
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
 }

 @After
 public void tearDown() {
 doc = null;
 treeBuilder = null;
 }

 @Test
 public void testParse() {
 doc = treeBuilder.parse(input, baseUri);
 assertEquals("html", doc.tagName());
 assertNotNull(doc.head());
 assertNotNull(doc.head().title());
 assertNotNull(doc.body());
 assertNotNull(doc.body().getElementsByTag("h1"));
 Element h1 = doc.body().getElementsByTag("h1").first();
 assertEquals("Test", h1.text());
 assertEquals("test", h1.id());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 treeBuilder.parse(null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 treeBuilder.parse(input, null);
 }

 @Test
 public void testProcessStartTag() {
 StartTag startTag = new StartTag("html");
 treeBuilder.processStartTag(startTag.name());
 assertEquals(startTag.name(), treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessEndTag() {
 EndTag endTag = new EndTag("html");
 treeBuilder.processEndTag(endTag.name());
 assertNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTagNoMatchingStartTag() {
 EndTag endTag = new EndTag("body");
 treeBuilder.processEndTag(endTag.name());
 assertNotNull(treeBuilder.currentElement());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 StartTag startTag = new StartTag("a", new Attributes().addClass("link"));
 treeBuilder.processStartTag(startTag.name(), startTag.attributes());
 Element aElement = treeBuilder.currentElement();
 assertEquals("a", aElement.tagName());
 assertTrue(aElement.hasClass("link"));
 }

 private void addElementToStack(String name) {
 Element element = new Element(name);
 doc.appendElement(element);
 stack.add(element);
 }

 private void removeLastElementFromStack(String name) {
 assertEquals(name, treeBuilder.currentElement().tagName());
 stack.remove(stack.size() - 1);
 }
}