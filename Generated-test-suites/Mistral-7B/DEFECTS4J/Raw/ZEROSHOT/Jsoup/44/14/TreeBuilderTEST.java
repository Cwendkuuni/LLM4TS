import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;

 @Rule
 public ExpectedException expectedException = ExpectedException.none();

 @Before
 public void setup() {
 String input = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 ArrayList<Element> stack = new ArrayList<>();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }

 @Override
 protected boolean processStartTag(String name) {
 stack.add(new Element(name));
 return true;
 }

 @Override
 protected boolean processEndTag(String name) {
 stack.remove(stack.size() - 1);
 return true;
 }

 @Override
 public void initialiseParse(String input, String baseUri, ParseErrorList errors) {
 super.initialiseParse(input, baseUri, errors);
 doc = new Document(baseUri);
 }

 @Override
 public Document parse(String input, String baseUri) {
 return parse(input, baseUri, ParseErrorList.noTracking());
 }

 @Override
 public Document parse(String input, String baseUri, ParseErrorList errors) {
 super.initialiseParse(input, baseUri, errors);
 super.runParser();
 return doc;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, null);
 }

 @org.junit.Test
 public void testParse() {
 Document doc = treeBuilder.parse("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Validate.notNull(doc, "Document should not be null");
 Validate.assertEquals("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", doc.body().html());
 }

 @org.junit.Test
 public void testProcessStartTag() {
 treeBuilder.processStartTag("html");
 Element html = treeBuilder.currentElement();
 Validate.notNull(html, "Current element should not be null");
 Validate.assertEquals("html", html.tagName());
 }

 @org.junit.Test
 public void testProcessStartTagWithAttributes() {
 String input = "<html lang='en'><head><title>Test Page</title></head><body><h1 id='title' class='title'>Hello World</h1></body></html>";
 treeBuilder.initialiseParse(input, "http://example.com", null);
 treeBuilder.processStartTag("html", new Attributes() {{
 put("lang", "en");
 }});
 Element html = treeBuilder.currentElement();
 Validate.notNull(html, "Current element should not be null");
 Validate.assertEquals("html", html.tagName());
 Element title = treeBuilder.currentElement();
 Validate.notNull(title, "Current element should not be null");
 Validate.assertEquals("title", title.tagName());
 Validate.assertEquals("en", title.attr("lang"));
 Validate.assertEquals("title", title.attr("id"));
 Validate.assertEquals("title", title.attr("class"));
 }

 @org.junit.Test
 public void testProcessEndTag() {
 treeBuilder.processStartTag("html");
 treeBuilder.processStartTag("head");
 treeBuilder.processEndTag("head");
 Element html = treeBuilder.currentElement();
 Validate.notNull(html, "Current element should not be null");
 Validate.assertTrue(html.childNodes().size() == 0);
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testNullInput() {
 expectedException.expect(NullPointerException.class);
 treeBuilder.parse(null, "http://example.com");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 expectedException.expect(NullPointerException.class);
 treeBuilder.parse("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", null);
 }
}