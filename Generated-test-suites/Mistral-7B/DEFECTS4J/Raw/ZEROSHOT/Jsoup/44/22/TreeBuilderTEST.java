import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // Override the process method to test each token individually
 System.out.println("Process Token: " + token.toString());
 return true;
 }
 };
 treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
 }

 @After
 public void tearDown() {
 doc = null;
 treeBuilder = null;
 }

 @Test
 public void testParse() {
 Document parsedDoc = treeBuilder.parse("Test Input", "http://test.com");
 assertNotNull(parsedDoc);
 assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", parsedDoc.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.parse(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.parse("Test Input", null);
 }

 @Test
 public void testProcessStartTag() {
 String name = "html";
 Attributes attrs = new Attributes();
 attrs.put("id", "test");
 treeBuilder.processStartTag(name, attrs);
 Element currentElement = treeBuilder.currentElement();
 assertNotNull(currentElement);
 assertEquals("html", currentElement.tagName());
 assertEquals("test", currentElement.attributes().get("id"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagNullName() {
 treeBuilder.processStartTag(null, new Attributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagNullAttrs() {
 treeBuilder.processStartTag("html", null);
 }

 @Test
 public void testProcessStartTagWithoutAttrs() {
 treeBuilder.processStartTag("html");
 Element currentElement = treeBuilder.currentElement();
 assertNotNull(currentElement);
 assertEquals("html", currentElement.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String name = "html";
 treeBuilder.processEndTag(name);
 Element currentElement = treeBuilder.currentElement();
 assertNull(currentElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagNullName() {
 treeBuilder.processEndTag(null);
 }

 @Test
 public void testCurrentElement() {
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("head");
 treeBuilder.processStartTag("title");
 Element titleElement = treeBuilder.currentElement();
 assertNotNull(titleElement);
 assertEquals("title", titleElement.tagName());
 treeBuilder.processEndTag("title");
 treeBuilder.processEndTag("head");
 treeBuilder.processEndTag("html");
 assertNull(treeBuilder.currentElement());
 }
}