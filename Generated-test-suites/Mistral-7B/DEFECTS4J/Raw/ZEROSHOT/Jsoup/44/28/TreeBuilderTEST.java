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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private ParseErrorList errors;

 @Before
 public void setup() {
 validateInput("test.html", "http://test.com/", errors -> {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse("test.html", "http://test.com/", errors);
 doc = treeBuilder.parse("test.html", "http://test.com/");
 });
 }

 @Test
 public void testParse() {
 assertEquals("http://test.com/", doc.baseUri());
 }

 @Test
 public void testProcessStartTag() {
 String name = "html";
 assertTrue(treeBuilder.processStartTag(name));
 assertEquals("html", currentElement().tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String name = "html";
 Attributes attrs = new Attributes();
 attrs.put("id", "test");
 assertTrue(treeBuilder.processStartTag(name, attrs));
 assertEquals("html", currentElement().tagName());
 assertEquals("test", currentElement().attributes().get("id"));
 }

 @Test
 public void testProcessEndTag() {
 String name = "html";
 treeBuilder.processStartTag(name);
 assertTrue(treeBuilder.processEndTag(name));
 assertNull(currentElement());
 }

 @Test(expected = AssertionError.class)
 public void testProcessNonTagToken() {
 Token token = new Token("&lt;script&gt;");
 treeBuilder.process(token);
 }

 @Test(expected = AssertionError.class)
 public void testProcessInvalidStartTag() {
 start.name("invalidTag");
 treeBuilder.process(start);
 }

 @Test
 public void testProcessInvalidEndTag() {
 end.name("div");
 treeBuilder.process(end);
 }

 @Test
 public void testRunParser() {
 validateInput("test.html", "http://test.com/", errors -> {
 treeBuilder = new HtmlTreeBuilder();
 treeBuilder.initialiseParse("test.html", "http://test.com/", errors);
 treeBuilder.runParser();
 });
 assertEquals(0, stack.size());
 }

 private void validateInput(String input, String baseUri, Consumer<ParseErrorList> errorsConsumer) {
 errors = new ParseErrorList();
 Consumer<ParseErrorList> consumer = errorsConsumer;
 treeBuilder.parse(input, baseUri, errors -> {
 consumer.accept(errors);
 });
 }
}