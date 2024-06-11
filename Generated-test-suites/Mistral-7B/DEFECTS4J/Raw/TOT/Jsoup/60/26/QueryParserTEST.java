import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseSimpleQuery() {
 String query = ".class1";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(2, elements.size()); // assuming there are 2 elements with class "class1" in the document
 }

 @Test
 public void testParseQueryWithId() {
 String query = "#id1";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 Element element = selector.select(document).get(0);
 assertEquals("id1", element.id());
 }

 @Test
 public void testParseQueryWithClassAndTag() {
 String query = "div.class1";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(3, elements.size()); // assuming there are 3 div elements with class "class1" in the document
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseQueryWithInvalidCombinator() {
 String query = "div+-class1"; // '+' is not a valid combinator
 Evaluator evaluator = QueryParser.parse(query);
 fail("Expected Selector.SelectorParseException but test passed instead.");
 }

 @Test
 public void testParseQueryWithAttribute() {
 String query = "input[type=text]";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(4, elements.size()); // assuming there are 4 input elements with type="text" in the document
 }

 @Test
 public void testParseQueryWithAttributeValue() {
 String query = "input[type=text][value=username]";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 Element element = selector.select(document).get(0);
 assertEquals("username", element.value());
 }

 @Test
 public void testParseQueryWithNegatedAttribute() {
 String query = "input:not[type=text]";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(10, elements.size()); // assuming there are 10 input elements in the document, only 2 of which have type="text"
 }

 @Test
 public void testParseQueryWithMultipleAttributes() {
 String query = "div[class=class1][data-custom-attr=value1]";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(2, elements.size()); // assuming there are 2 div elements with class="class1" and data-custom-attr="value1" in the document
 }

 @Test
 public void testParseQueryWithPseudoClass() {
 String query = "div:first-child";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(3, elements.size()); // assuming there are 3 div elements as first children of their parents in the document
 }

 @Test
 public void testParseQueryWithCombinators() {
 String query = "div > ul > li:first-child";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(document);
 assertEquals(2, elements.size()); // assuming there are 2 li elements that are the first children of ul elements inside div elements
 }

 @Test
 public void testParseQueryWithComplexSelectors() {
 String query = "#id1 > div > ul > li.class1:first-child:has(input[type=text])";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 Element element = selector.select(document).get(0);
 assertTrue(element.hasClass("class1")); // assuming the first li element inside a div element with id="id1" contains a text input element
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseQueryWithInvalidSelector() {
 String query = "div##invalidSelector"; // '#' should not precede an ID
 Evaluator evaluator = QueryParser.parse(query);
 fail("Expected Selector.SelectorParseException but test passed instead.");
 }
}