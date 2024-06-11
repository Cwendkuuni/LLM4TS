import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.nodes.Document;
import static org.jsoup.nodes.Jsoup.*;

public class QueryParserTest {

 private static final String BASIC_SELECTOR = "#id1 > .class1.class2 > div > p:containsOwn(Test)";
 private static final String BASIC_HTML = "<html><body><div id='id1' class='class1 class2'><p>Test</p></div></body></html>";

 @Test
 public void testBasicSelector() {
 Document doc = parse(BASIC_HTML);
 Evaluator evaluator = QueryParser.parse(BASIC_SELECTOR);
 List<Element> elements = evaluator.getAllMatches(doc);
 assertEquals(1, elements.size());
 }

 private static final String COMBINATOR_SELECTOR = "#id1 > #id2";
 private static final String COMBINATOR_HTML = "<html><body><div id='id1'><div id='id2'></div></div></body></html>";

 @Test(expected = Selector.SelectorParseException.class)
 public void testCombinatorError() {
 QueryParser.parse("#id1 #id2"); // should throw an error as this is not a valid combinator
 }

 @Test
 public void testCombinatorSelector() {
 Document doc = parse(COMBINATOR_HTML);
 Evaluator evaluator = QueryParser.parse(COMBINATOR_SELECTOR);
 List<Element> elements = evaluator.getAllMatches(doc);
 assertEquals(1, elements.size());
 }

 private static final String ID_SELECTOR = "#idThatDoesNotExist";
 private static final String ID_HTML = "<html><body><div id='id1'>Test</div></body></html>";

 @Test
 public void testIdSelector() {
 Document doc = parse(ID_HTML);
 Evaluator evaluator = QueryParser.parse(ID_SELECTOR);
 List<Element> elements = evaluator.getAllMatches(doc);
 assertTrue(elements.isEmpty());
 }

 private static final String CLASS_SELECTOR = ".classThatDoesNotExist";
 private static final String CLASS_HTML = "<html><body><div class='class1'>Test</div></body></html>";

 @Test
 public void testClassSelector() {
 Document doc = parse(CLASS_HTML);
 Evaluator evaluator = QueryParser.parse(CLASS_SELECTOR);
 List<Element> elements = evaluator.getAllMatches(doc);
 assertTrue(elements.isEmpty());
 }

 // Add more test cases for tag selectors, attribute selectors, pseudo classes, and other selectors as needed.
 // Also add test cases for error conditions such as missing quotes, invalid selectors, and other edge cases.

}