import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 input = "test.html";
 baseUri = "http://test.com";
 errors = ParseErrorList.noTracking();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 doc = treeBuilder.parse(input, baseUri);
 }

 @Test
 public void testInitialiseParse() {
 Validate.notNull(input, "String input must not be null");
 Validate.notNull(baseUri, "BaseURI must not be null");
 assertNotNull(treeBuilder.doc);
 assertNotNull(treeBuilder.tokeniser);
 assertNotNull(treeBuilder.stack);
 assertNotNull(treeBuilder.baseUri);
 assertNull(treeBuilder.currentToken);
 assertNull(treeBuilder.errors);
 }

 @Test
 public void testParse() {
 assertNotNull(doc);
 }

 @Test
 public void testParseWithErrors() {
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 throw new RuntimeException("Test error");
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 doc = treeBuilder.parse(input, baseUri);
 assertEquals(1, errors.size());
 }

 @Test
 public void testRunParser() {
 int tokenCount = 0;
 while (treeBuilder.tokeniser.hasNext()) {
 tokenCount++;
 treeBuilder.process(treeBuilder.tokeniser.next());
 }
 assertEquals(tokenCount, Token.TokenType.values().length * 2); // 2x StartTag and EndTag for each token
 }

 @Test
 public void testProcessStartTag() {
 String name = "test";
 treeBuilder.processStartTag(name);
 Element element = treeBuilder.currentElement();
 assertNotNull(element);
 assertEquals(name, element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String name = "test";
 treeBuilder.processStartTag(name);
 treeBuilder.processEndTag(name);
 Element element = treeBuilder.currentElement();
 assertNull(element);
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String name = "test";
 Attributes attributes = new Attributes();
 attributes.put("test", "test");
 treeBuilder.processStartTag(name, attributes);
 Element element = treeBuilder.currentElement();
 assertNotNull(element);
 assertEquals(name, element.tagName());
 assertEquals("test", element.attributes().get("test"));
 }

 private String input;
 private String baseUri;
 private ParseErrorList errors;
}