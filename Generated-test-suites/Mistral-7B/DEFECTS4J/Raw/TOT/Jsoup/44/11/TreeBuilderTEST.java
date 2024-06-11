import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private ParseErrorList errors;
 private Document doc;

 @Before
 public void setup() {
 initialiseTestBuilder();
 }

 private void initialiseTestBuilder() {
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true; // let the test case cover all methods
 }
 };
 errors = new ParseErrorList();
 }

 @Test
 public void testParseEmptyString() {
 treeBuilder.parse("", "", errors);
 assertNotNull(treeBuilder.doc);
 assertNotNull(treeBuilder.doc.body());
 assertNotNull(treeBuilder.doc.body().children());
 assertEmpty(treeBuilder.doc.body().children());
 }

 @Test
 public void testParseValidInput() {
 treeBuilder.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "", errors);
 assertNotNull(treeBuilder.doc);
 assertNotNull(treeBuilder.doc.head());
 assertNotNull(treeBuilder.doc.title());
 assertNotNull(treeBuilder.doc.body());
 assertNotNull(treeBuilder.doc.body().children());
 assertEquals(1, treeBuilder.doc.body().children().size());
 assertNotNull(treeBuilder.doc.body().children().get(0).text());
 assertEquals("Hello World", treeBuilder.doc.body().children().get(0).text());
 assertNotNull(treeBuilder.doc.body().children().get(0).tagName());
 assertEquals("h1", treeBuilder.doc.body().children().get(0).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 treeBuilder.parse(null, "", errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 treeBuilder.parse("<html></html>", null, errors);
 }

 @Test(expected = ParseErrorList.MalformedElementException.class)
 public void testMalformedStartTag() {
 treeBuilder.parse("<html<head></html>", "", errors);
 }

 @Test(expected = ParseErrorList.MalformedElementException.class)
 public void testMalformedEndTag() {
 treeBuilder.parse("<html><body></body></html>", "", errors);
 }

 @Test(expected = ParseErrorList.MalformedElementException.class)
 public void testMissingEndTag() {
 treeBuilder.parse("<html><body></html><h1>Hello</h1>", "", errors);
 }

 @Test
 public void testProcessStartTag() {
 treeBuilder.processStartTag("html", new Attributes());
 assertNotNull(treeBuilder.currentElement());
 assertNotNull(treeBuilder.currentElement().tagName());
 assertEquals("html", treeBuilder.currentElement().tagName());
 }

 @Test
 public void testProcessEndTag() {
 Element current = treeBuilder.processStartTag("html", new Attributes());
 treeBuilder.processEndTag("html");
 assertNull(treeBuilder.currentElement());
 }

 private void assertEmpty(Iterable<?> iterable) {
 assertNotNull(iterable);
 assertFalse(iterable.iterator().hasNext());
 }
}