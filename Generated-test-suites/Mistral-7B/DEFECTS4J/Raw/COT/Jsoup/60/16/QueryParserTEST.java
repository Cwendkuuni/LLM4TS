import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {

 private QueryParser queryParser;
 private Evaluator expectedEvaluator;

 @Test
 public void testParseEmptyQuery() {
 queryParser = new QueryParser("");
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals("Empty query should return empty Evaluator", 0, actualEvaluator.size());
 }

 @Test
 public void testParseSimpleTagSelector() {
 queryParser = new QueryParser("p");
 expectedEvaluator = new Evaluator.Tag("p");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Simple tag selector should match tag 'p'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseClassSelector() {
 queryParser = new QueryParser(".class");
 expectedEvaluator = new Evaluator.Class("class");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Class selector should match class 'class'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseIdSelector() {
 queryParser = new QueryParser("#id");
 expectedEvaluator = new Evaluator.Id("id");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Id selector should match id 'id'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseAttributeSelector() {
 queryParser = new QueryParser("[att='val']");
 expectedEvaluator = new Evaluator.AttributeWithValue("att", "val");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Attribute selector should match attribute 'att' with value 'val'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseCombinedSelectors() {
 queryParser = new QueryParser("#id > .class");
 expectedEvaluator = new CombiningEvaluator.And(
 new Evaluator.Id("id"),
 new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("class"))
 );
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Combined selectors '#id > .class' should match id 'id' and have a direct child with tag 'class'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseMultipleSelectorsWithCombinator() {
 queryParser = new QueryParser("p, #id, .class");
 expectedEvaluator = new CombiningEvaluator.Or(
 new Evaluator.Tag("p"),
 new Evaluator.Id("id"),
 new Evaluator.Class("class")
 );
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Multiple selectors with comma as combinator should match elements with tag 'p', id 'id', or class 'class'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseAttributeSelectorWithDifferentValues() {
 queryParser = new QueryParser("[att='val1' or att='val2']");
 expectedEvaluator = new CombiningEvaluator.Or(
 new Evaluator.AttributeWithValue("att", "val1"),
 new Evaluator.AttributeWithValue("att", "val2")
 );
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Attribute selector with 'or' should match elements with attribute 'att' having values 'val1' or 'val2'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseMultipleAttributes() {
 queryParser = new QueryParser("[att1='val1' and att2='val2']");
 expectedEvaluator = new Evaluator.And(
 new Evaluator.AttributeWithValue("att1", "val1"),
 new Evaluator.AttributeWithValue("att2", "val2")
 );
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Multiple attribute selectors should match elements with both attributes 'att1' with value 'val1' and 'att2' with value 'val2'", expectedEvaluator, actualEvaluator);
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 queryParser = new QueryParser("invalid:query");
 queryParser.parse(); // should throw Selector.SelectorParseException
 }

 @Test
 public void testParseNthChildSelector() {
 queryParser = new QueryParser("div:nth-child(3)");
 expectedEvaluator = new Evaluator.IsNthChild(3, 0);
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Nth child selector 'div:nth-child(3)' should match the third child element of its parent", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseNthOfTypeSelector() {
 queryParser = new QueryParser("li:nth-of-type(2)");
 expectedEvaluator = new Evaluator.IsNthOfType(1, 1);
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Nth of type selector 'li:nth-of-type(2)' should match the second 'li' element in the document", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseNthLastChildSelector() {
 queryParser = new QueryParser("li:nth-last-child(2)");
 expectedEvaluator = new Evaluator.IsNthLastChild(1, 1);
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Nth last child selector 'li:nth-last-child(2)' should match the second last 'li' element in the document", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseNthLastOfTypeSelector() {
 queryParser = new QueryParser("li:nth-last-of-type(2)");
 expectedEvaluator = new Evaluator.IsNthLastOfType(1, 1);
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Nth last of type selector 'li:nth-last-of-type(2)' should match the second last 'li' element in the document that is of the same type", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseContainsTextSelector() {
 queryParser = new QueryParser("p:contains(text)");
 expectedEvaluator = new Evaluator.ContainsText("text");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Contains text selector 'p:contains(text)' should match elements containing the text 'text'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseContainsOwnTextSelector() {
 queryParser = new QueryParser("p:containsOwn(text)");
 expectedEvaluator = new Evaluator.ContainsOwnText("text");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Contains own text selector 'p:containsOwn(text)' should match elements that contain the text 'text' as their own text", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseContainsDataSelector() {
 queryParser = new QueryParser("p:containsData(data)");
 expectedEvaluator = new Evaluator.ContainsData("data");
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Contains data selector 'p:containsData(data)' should match elements that contain the data 'data'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseMatchesSelector() {
 queryParser = new QueryParser("p:matches(regex)");
 expectedEvaluator = new Evaluator.Matches(Pattern.compile("regex"));
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Matches selector 'p:matches(regex)' should match elements that match the regular expression 'regex'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseMatchesOwnSelector() {
 queryParser = new QueryParser("p:matchesOwn(regex)");
 expectedEvaluator = new Evaluator.MatchesOwn(Pattern.compile("regex"));
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Matches own selector 'p:matchesOwn(regex)' should match elements that contain the sub-element matching the regular expression 'regex'", expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testParseNotSelector() {
 queryParser = new QueryParser("p:not(#id)");
 expectedEvaluator = new StructuralEvaluator.Not(new Evaluator.Id("id"));
 actualEvaluator = queryParser.parse().get(0);
 assertEquals("Not selector 'p:not(#id)' should match elements that are not 'p' elements with id 'id'", expectedEvaluator, actualEvaluator);
 }

}