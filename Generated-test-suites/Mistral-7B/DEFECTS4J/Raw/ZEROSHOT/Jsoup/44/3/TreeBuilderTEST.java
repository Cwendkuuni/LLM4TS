package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private ArrayList<Element> stack;

 @Before
 public void setUp() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 doc = null;
 stack = new ArrayList<>();
 }

 @After
 public void tearDown() {
 if (doc != null) {
 doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 System.out.println(doc.body().html());
 doc = null;
 }
 }

 @Test
 public void testParse() {
 String input = "<html><body><h1>Test</h1></body></html>";
 treeBuilder.initialiseParse(input, "", ParseErrorList.noTracking());
 treeBuilder.parse(input, "");
 assertNotNull(doc);
 assertEquals("html", doc.tagName());
 assertNotNull(doc.body());
 assertEquals("h1", doc.body().child(0).tagName());
 assertEquals("Test", doc.body().child(0).text());
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.initialiseParse("<html><h1 id=\"test\">Test</h1>", "", ParseErrorList.noTracking());
 assertFalse(treeBuilder.processStartTag("html"));
 assertTrue(treeBuilder.processStartTag("h1"));
 Element h1 = treeBuilder.currentElement();
 assertEquals("h1", h1.tagName());
 assertEquals("test", h1.id());
 assertNull(treeBuilder.currentElement().attributes());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 treeBuilder.initialiseParse("<html><h1 id=\"test\" class=\"testClass\">Test</h1>", "", ParseErrorList.noTracking());
 Element h1 = (Element) treeBuilder.processStartTag("h1", new Attributes() {{
 put("id", "test2");
 put("class", "testClass2");
 }});
 assertEquals("h1", h1.tagName());
 assertEquals("test2", h1.id());
 assertEquals("testClass2", h1.classes().toString());
 }

 @Test
 public void testProcessEndTag() {
 treeBuilder.initialiseParse("<html><body><h1>Test</h1></body></html>", "", ParseErrorList.noTracking());
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("body");
 treeBuilder.processStartTag("h1");
 treeBuilder.processText("Test");
 treeBuilder.processEndTag("h1");
 treeBuilder.processEndTag("body");
 treeBuilder.processEndTag("html");
 assertNull(treeBuilder.currentElement());
 }
}