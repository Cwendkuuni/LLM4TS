import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private ParseErrorList errors;

 @Before
 public void setup() {
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // Override process method here for testing purposes
 return true;
 }
 };
 treeBuilder.initialiseParse("input", "baseUri", errors);
 }

 @Test
 public void testParse() {
 Document doc = treeBuilder.parse("input", "baseUri");
 assertNotNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
 treeBuilder.parse(null, "baseUri");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 treeBuilder.parse("input", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 treeBuilder.parse("", "baseUri");
 }

 @Test
 public void testProcessStartTag() {
 String name = "div";
 Element element = new Element("div");
 treeBuilder.stack.add(element);
 boolean result = treeBuilder.processStartTag(name);
 assertEquals(true, result);
 assertEquals(element, treeBuilder.currentElement());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 String name = "p";
 Attributes attrs = new Attributes();
 attrs.put("class", "someClass");
 attrs.put("id", "someId");
 Element element = new Element("p");
 element.attributes().putAll(attrs);
 treeBuilder.stack.add(element);
 boolean result = treeBuilder.processStartTag(name, attrs);
 assertEquals(true, result);
 assertEquals(element, treeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTag() {
 String name = "div";
 boolean result = treeBuilder.processEndTag(name);
 assertEquals(true, result);
 assertEquals(treeBuilder.stack.size() - 1, treeBuilder.stack.size());
 assertEquals(treeBuilder.stack.get(treeBuilder.stack.size() - 1), treeBuilder.currentElement());
 treeBuilder.currentElement().remove();
 }

 @Test
 public void testProcessEndTagWithWrongName() {
 String name = "span";
 boolean result = treeBuilder.processEndTag(name);
 assertEquals(false, result);
 }

 @Test
 public void testCurrentElement() {
 String name = "div";
 Element element = new Element("div");
 treeBuilder.stack.add(element);
 boolean result = treeBuilder.processStartTag(name);
 assertEquals(true, result);
 Element currentElement = treeBuilder.currentElement();
 assertEquals(element, currentElement);
 }

 @Test
 public void testCurrentElementWhenNoElementOnStack() {
 boolean result = treeBuilder.processStartTag("p");
 assertEquals(true, result);
 assertEquals(null, treeBuilder.currentElement());
 }
}