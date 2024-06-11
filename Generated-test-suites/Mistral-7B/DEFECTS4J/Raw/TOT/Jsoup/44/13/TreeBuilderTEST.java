package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Tokeniser tokeniser;
 private Document doc;

 @Before
 public void setUp() {
 initialiseTest();
 }

 private void initialiseTest() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false; // We don't process tokens in our test
 }
 };
 tokeniser = new Tokeniser(new CharacterReader(""), treeBuilder.errors);
 doc = treeBuilder.doc;
 }

 @Test
 public void testParseNone() {
 treeBuilder.parse("", "", ParseErrorList.noTracking());
 assertEquals(0, doc.body().children().size());
 }

 @Test
 public void testParseWithBaseURI() {
 treeBuilder.initialiseParse("<html><body><div id='test'></div></body></html>", "http://test.com", ParseErrorList.noTracking());
 treeBuilder.parse("", "http://test.com");

 Element div = doc.body().getElementById("test");
 assertNotNull(div);
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.initialiseParse("<html><body><div id='test'></div></body></html>", "http://test.com", ParseErrorList.noTracking());
 treeBuilder.parse("", "http://test.com");

 Element div = doc.body().getElementById("test");
 assertNotNull(div);

 TreeBuilder spyTreeBuilder = new SpyTreeBuilder(treeBuilder);
 spyTreeBuilder.processStartTag("div");
 assertEquals(1, spyTreeBuilder.stack.size());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 treeBuilder.initialiseParse("<html><body><div id='test' class='testClass'></div></body></html>", "http://test.com", ParseErrorList.noTracking());
 treeBuilder.parse("", "http://test.com");

 Element div = doc.body().getElementById("test");
 assertNotNull(div);

 Attributes attributes = div.attributes();
 assertEquals("testClass", attributes.get("class"));

 TreeBuilder spyTreeBuilder = new SpyTreeBuilder(treeBuilder);
 spyTreeBuilder.processStartTag("div", new Attributes() {{
 set("class", "testClass");
 }});
 assertEquals(1, spyTreeBuilder.stack.size());
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder.initialiseParse("<html><body><div id='test'><p>Test</p></div></body></html>", "http://test.com", ParseErrorList.noTracking());
 treeBuilder.parse("", "http://test.com");

 Element div = doc.body().getElementById("test");
 Element p = div.child(0);
 assertNotNull(div);
 assertNotNull(p);

 TreeBuilder spyTreeBuilder = new SpyTreeBuilder(treeBuilder);
 spyTreeBuilder.processEndTag("div");
 assertEquals(0, spyTreeBuilder.stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 treeBuilder.parse(null, "", ParseErrorList.noTracking());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseURI() {
 treeBuilder.parse("", null, ParseErrorList.noTracking());
 }
}

class SpyTreeBuilder extends TreeBuilder {
 private TreeBuilder treeBuilder;

 public SpyTreeBuilder(TreeBuilder treeBuilder) {
 this.treeBuilder = treeBuilder;
 }

 @Override
 protected boolean process(Token token) {
 super.process(token);
 return false; // We don't process tokens in our test
 }

 @Override
 protected List<Element> stack = new ArrayList<>();
}