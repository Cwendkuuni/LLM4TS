import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 @Test
 public void testParseValidInput() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://www.example.com/";
 ParseErrorList errors = ParseErrorList.noTracking();

 TreeBuilder builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 builder.initialiseParse(input, baseUri, errors);
 Document doc = builder.parse(input, baseUri);

 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.tagName());
 Element title = html.child(0);
 assertNotNull(title);
 assertEquals("head", title.parent().tagName());
 assertEquals("Test", title.text());

 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.parent().tagName());
 Element h1 = body.child(0);
 assertNotNull(h1);
 assertEquals("h1", h1.parent().tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseWithErrors() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = "https://www.example.com/";
 ParseErrorList errors = new ParseErrorList();

 TreeBuilder builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 builder.initialiseParse(input, baseUri, errors);
 Document doc = builder.parse(input, baseUri);

 assertNotNull(doc);
 assertFalse(doc.body().hasText());
 assertTrue(errors.hasErrors());
 }

 @Test
 public void testNestedStartTags() {
 String input = "<html><head><title>Test</title><title>Nested</title></head><body><h1>Hello <h2>World</h2></h1></body></html>";
 String baseUri = "https://www.example.com/";
 ParseErrorList errors = ParseErrorList.noTracking();

 TreeBuilder builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 builder.initialiseParse(input, baseUri, errors);
 Document doc = builder.parse(input, baseUri);

 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.tagName());
 Element title1 = html.child(0);
 assertNotNull(title1);
 assertEquals("title", title1.tagName());
 assertEquals("Test", title1.text());
 Element title2 = title1.sibling(1);
 assertNotNull(title2);
 assertEquals("title", title2.tagName());
 assertEquals("Nested", title2.text());

 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 Element h1 = body.child(0);
 assertNotNull(h1);
 assertEquals("h1", h1.tagName());
 Element h2 = h1.child(0);
 assertNotNull(h2);
 assertEquals("h2", h2.tagName());
 assertEquals("World", h2.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 String baseUri = "https://www.example.com/";
 ParseErrorList errors = ParseErrorList.noTracking();

 TreeBuilder builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 builder.initialiseParse(null, baseUri, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 ParseErrorList errors = ParseErrorList.noTracking();

 TreeBuilder builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 builder.initialiseParse(input, null, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullErrors() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://www.example.com/";

 TreeBuilder builder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };

 builder.initialiseParse(input, baseUri, null);
 }
}