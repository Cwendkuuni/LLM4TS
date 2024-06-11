import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 initialiseTestBuilder();
 }

 // Test for constructor with baseURI and no errors tracking
 @Test
 public void testParseWithBaseUriAndNoErrorTracking() {
 String input = "<html><head><title>Test</title></head><body>" +
         "<h1>Hello World</h1></body></html>";
 doc = treeBuilder.parse(input, "http://localhost:8080/");
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertEquals("Hello World", doc.body().text());
 }

 // Test for constructor with baseURI and errors tracking
 @Test
 public void testParseWithBaseUriAndErrorTracking() {
 ParseErrorList errors = new ParseErrorList();
 String input = "<html><head><title>Test</title></head><body>" +
         "<h1>Hello World</h1></body></html>";
 doc = treeBuilder.parse(input, "http://localhost:8080/", errors);
 assertNotNull(doc);
 assertNotNull(doc.body());
 assertEquals("Hello World", doc.body().text());
 assertTrue(errors.isEmpty());
 }

 // Test for empty input
 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 treeBuilder.parse("", "http://localhost:8080/", ParseErrorList.noTracking());
 }

 // Test for null baseURI
 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 treeBuilder.parse("<html></html>", null, ParseErrorList.noTracking());
 }

 // Test for processStartTag
 @Test
 public void testProcessStartTag() {
 treeBuilder.initialiseParse("<html><head><title>Test</title></head><body>" +
         "<h1>Hello World</h1></body></html>", "http://localhost:8080/", ParseErrorList.noTracking());
 assertTrue(treeBuilder.processStartTag("html"));
 assertNotNull(treeBuilder.currentElement());
 assertEquals("html", treeBuilder.currentElement().tagName());
 }

 // Test for processStartTag with attributes
 @Test
 public void testProcessStartTagWithAttributes() {
 treeBuilder.initialiseParse("<html lang=\"en\" xmlns:html=\"http://www.w3.org/1999/xhtml\"><head>" +
         "<title>Test</title></head><body>" +
         "<h1 id=\"title\" class=\"title\">Hello World</h1></body></html>", "http://localhost:8080/", ParseErrorList.noTracking());
 Element title = treeBuilder.currentElement();
 Attributes attrs = title.attributes();
 assertEquals("en", attrs.get("lang"));
 assertEquals("http://www.w3.org/1999/xhtml", attrs.get("xmlns:html"));
 assertTrue(treeBuilder.processStartTag("h1"));
 assertNotNull(treeBuilder.currentElement());
 assertEquals("h1", treeBuilder.currentElement().tagName());
 }

 // Test for processEndTag
 @Test
 public void testProcessEndTag() {
 treeBuilder.initialiseParse("<html><head><title>Test</title></head><body>" +
         "<h1>Hello World</h1></body></html>", "http://localhost:8080/", ParseErrorList.noTracking());
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("head");
 treeBuilder.processStartTag("title");
 treeBuilder.processEndTag("title");
 treeBuilder.processEndTag("head");
 treeBuilder.processEndTag("html");
 assertEquals(0, treeBuilder.stack.size());
 }

 // Clean up
 @After
 public void tearDown() {
 treeBuilder = null;
 doc = null;
 tokeniser = null;
 characterReader = null;
 }

 // Initialize test builder
 private void initialiseTestBuilder() {
 treeBuilder = new TreeBuilder() {
 @Override protected boolean process(Token token) {
 return true;
 }
 };
 characterReader = new CharacterReader("testInput");
 tokeniser = new Tokeniser(characterReader, new ParseErrorList());
 }
}