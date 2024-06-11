import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 @Test
 public void testInitialiseParse() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = "some input";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 treeBuilder.initialiseParse(input, baseUri, errors);

 assertEquals("http://example.com", treeBuilder.baseUri);
 assertNotNull(treeBuilder.doc);
 assertNotNull(treeBuilder.tokeniser);
 assertNull(treeBuilder.currentToken);
 assertNull(treeBuilder.errors);
 }

 @Test
 public void testParseWithNoErrors() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 Document doc = treeBuilder.parse(input, baseUri);

 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.body());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseWithErrors() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = "<html><head><title>Test</title><script>alert('XSS')</script></head><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 Document doc = treeBuilder.parse(input, baseUri);

 assertNotNull(doc);
 assertFalse(doc.body().hasText());
 assertFalse(doc.body().hasChildren());
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testProcessStartTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(Token token) {
 System.out.println(token.name());
 return true;
 }
 };
 String input = "<html>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 treeBuilder.parse(input, baseUri);

 assertTrue(treeBuilder.stack.size() > 0);
 assertEquals("html", treeBuilder.stack.get(treeBuilder.stack.size() - 1).tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(String name, Attributes attrs) {
 System.out.println("Processing start tag: " + name);
 for (String key : attrs.keySet()) {
 System.out.println("Attribute " + key + "=" + attrs.get(key));
 }
 return true;
 }
 };
 String input = "<html lang='en'>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 treeBuilder.parse(input, baseUri);

 assertEquals(1, treeBuilder.stack.size());
 assertEquals("html", treeBuilder.stack.get(0).tagName());
 System.out.println(treeBuilder.stack.get(0).attributes().get("lang"));
 }

 @Test
 public void testProcessEndTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processEndTag(Token token) {
 System.out.println(token.name());
 return true;
 }
 };
 String input = "<html><body></body></html>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 treeBuilder.parse(input, baseUri);

 assertEquals(2, treeBuilder.stack.size());
 assertEquals("body", treeBuilder.stack.get(treeBuilder.stack.size() - 1).tagName());
 assertEquals("html", treeBuilder.stack.get(treeBuilder.stack.size() - 2).tagName());
 assertEquals("html", treeBuilder.stack.get(treeBuilder.stack.size() - 1).parentTagName());
 System.out.println(treeBuilder.stack.get(treeBuilder.stack.size() - 1).tagName());
 }
}