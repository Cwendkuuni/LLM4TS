import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for QueryParser.
 */
public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.class1";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("div.class1", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithIdAndClass() {
 String query = "#id1.class1.class2";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("#id1.class1.class2", evaluator.toString());
 }

 @Test
 public void testParseCombinedSelectors() {
 String query = "div > p + ul";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("div > p + ul", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelector() {
 String query = "input[type='text']";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("input[type='text']", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValue() {
 String query = "input[type='text'][value='abc']";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("input[type='text'][value='abc']", evaluator.toString());
 }

 @Test
 public void testParsePseudoClassSelector() {
 String query = "input:disabled";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("input:disabled", evaluator.toString());
 }

 @Test
 public void testParsePseudoClassSelectorWithNegation() {
 String query = "input:not(:disabled)";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("input:not(:disabled)", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithNegation() {
 String query = "div:not(.class1) > p";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("div:not(.class1) > p", evaluator.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidSelector() {
 String query = "div>p>#invalidSelector";
 QueryParser.parse(query); // this should throw an exception
 }
}