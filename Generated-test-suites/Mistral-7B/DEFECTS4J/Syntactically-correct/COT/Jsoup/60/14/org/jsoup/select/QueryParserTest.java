import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private String query;
 private QueryParser queryParser;
 private Evaluator evaluator;

 @Test
 public void testParse() {
 query = "div.class1.class2";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.class, evaluator.getClass());
 // add more assertions here based on the structure of the Evaluator tree
 }

 @Test
 public void testParseWithCombinator() {
 query = "div > p";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", CombiningEvaluator.class, evaluator.getClass());
 assertNotNull("expected rightmost evaluator to be present", evaluator.rightMostEvaluator());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseWithUnknownCombinator() {
 query = "div ~~ p";
 queryParser = new QueryParser(query);
 queryParser.parse();
 }

 @Test
 public void testConsumeSubQuery() {
 query = "div > .class1 > .class2";
 queryParser = new QueryParser(query);
 String subQuery = queryParser.consumeSubQuery();

 assertEquals("expected subQuery to be '>.class1>.class2'", ".class1>.class2", subQuery);
 }

 @Test
 public void testFindElementsByTag() {
 query = "div";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.Tag.class, evaluator.getClass());
 assertEquals("expected tag name to be 'div'", "div", evaluator.toString());
 }

 @Test
 public void testFindElementsById() {
 query = "#id";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.Id.class, evaluator.getClass());
 assertEquals("expected id to be 'id'", "id", evaluator.toString());
 }

 @Test
 public void testFindElementsByClass() {
 query = ".class1.class2";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.Class.class, evaluator.getClass());
 assertEquals("expected class names to be ['class1', 'class2']", new String[]{"class1", "class2"}, evaluator.toString().split(" "));
 }

 @Test
 public void testFindElementsByAttribute() {
 query = "[attr='value']";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.Attribute.class, evaluator.getClass());
 assertEquals("expected attribute name to be 'attr'", "attr", evaluator.toString());
 assertEquals("expected attribute value to be 'value'", "value", evaluator.toString());
 }

 @Test
 public void testFindElementsByAttributeWithValueStarting() {
 query = "[data-start='abc']";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 assertEquals("expected attribute name to be 'data-start'", "data-start", evaluator.toString());
 assertEquals("expected attribute value starting with 'abc'", "abc", evaluator.toString());
 }

 @Test
 public void testFindElementsByAttributeWithValueEnding() {
 query = "[data-end='abc']";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 assertEquals("expected attribute name to be 'data-end'", "data-end", evaluator.toString());
 assertEquals("expected attribute value ending with 'abc'", "abc", evaluator.toString());
 }

 @Test
 public void testFindElementsByAttributeWithValueContaining() {
 query = "[data*='abc']";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
 assertEquals("expected attribute name to be 'data'", "data", evaluator.toString());
 assertEquals("expected attribute value containing 'abc'", "abc", evaluator.toString());
 }

 @Test
 public void testFindElementsByAttributeWithValueMatching() {
 query = "[data~='abc']";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 assertEquals("expected attribute name to be 'data'", "data", evaluator.toString());
 assertEquals("expected attribute value matching regex 'abc'", Pattern.compile("abc").matcher("abc").matches(), evaluator.toString().equals("[data~='abc']"));
 }

 @Test
 public void testFindAllElements() {
 query = "*";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.AllElements.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByIndexLessThan() {
 query = "div:lt(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IndexLessThan.class, evaluator.getClass());
 assertEquals("expected index to be 2", 2, evaluator.index());
 }

 @Test
 public void testFindElementsByIndexGreaterThan() {
 query = "div:gt(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IndexGreaterThan.class, evaluator.getClass());
 assertEquals("expected index to be 3", 3, evaluator.index());
 }

 @Test
 public void testFindElementsByIndexEquals() {
 query = "div:eq(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IndexEquals.class, evaluator.getClass());
 assertEquals("expected index to be 2", 2, evaluator.index());
 }

 @Test
 public void testFindElementsByPseudoClassHas() {
 query = "div:has(p)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", StructuralEvaluator.Has.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassContains() {
 query = "div:contains(text)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.ContainsText.class, evaluator.getClass());
 assertEquals("expected text to be 'text'", "text", evaluator.toString());
 }

 @Test
 public void testFindElementsByPseudoClassContainsOwn() {
 query = "div:containsOwn(text)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.ContainsOwnText.class, evaluator.getClass());
 assertEquals("expected text to be 'text'", "text", evaluator.toString());
 }

 @Test
 public void testFindElementsByPseudoClassContainsData() {
 query = "div:containsData(data)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.ContainsData.class, evaluator.getClass());
 assertEquals("expected data to be 'data'", "data", evaluator.toString());
 }

 @Test
 public void testFindElementsByPseudoClassMatches() {
 query = "div:matches(regex)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.Matches.class, evaluator.getClass());
 assertEquals("expected regex to match div elements", Pattern.compile(queryParser.consumeSubQuery()).matcher(new String("div")).matches(), evaluator.toString().equals("[matches='regex']"));
 }

 @Test
 public void testFindElementsByPseudoClassMatchesOwn() {
 query = "div:matchesOwn(regex)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.MatchesOwn.class, evaluator.getClass());
 assertEquals("expected regex to match div elements", Pattern.compile(queryParser.consumeSubQuery()).matcher(new String("div")).matches(), evaluator.toString().equals("[matchesOwn='regex']"));
 }

 @Test
 public void testFindElementsByPseudoClassNot() {
 query = "div:not(p)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", StructuralEvaluator.Not.class, evaluator.getClass());
 assertEquals("expected subselect to be 'p'", "p", evaluator.toString());
 }

 @Test
 public void testFindElementsByPseudoClassNthChild() {
 query = "div:nth-child(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsNthChild.class, evaluator.getClass());
 assertEquals("expected index to be 2", 2, evaluator.index());
 }

 @Test
 public void testFindElementsByPseudoClassNthLastChild() {
 query = "div:nth-last-child(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsNthLastChild.class, evaluator.getClass());
 assertEquals("expected index to be 2", 2, evaluator.index());
 }

 @Test
 public void testFindElementsByPseudoClassNthOfType() {
 query = "div:nth-of-type(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsNthOfType.class, evaluator.getClass());
 assertEquals("expected index to be 2", 2, evaluator.index());
 }

 @Test
 public void testFindElementsByPseudoClassNthLastOfType() {
 query = "div:nth-last-of-type(2)";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsNthLastOfType.class, evaluator.getClass());
 assertEquals("expected index to be 2", 2, evaluator.index());
 }

 @Test
 public void testFindElementsByPseudoClassFirstChild() {
 query = "div:first-child";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsFirstChild.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassLastChild() {
 query = "div:last-child";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsLastChild.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassFirstOfType() {
 query = "div:first-of-type";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsFirstOfType.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassLastOfType() {
 query = "div:last-of-type";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsLastOfType.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassOnlyChild() {
 query = "div:only-child";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsOnlyChild.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassOnlyOfType() {
 query = "div:only-of-type";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsOnlyOfType.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassEmpty() {
 query = "div:empty";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsEmpty.class, evaluator.getClass());
 }

 @Test
 public void testFindElementsByPseudoClassRoot() {
 query = "div:root";
 queryParser = new QueryParser(query);
 evaluator = queryParser.parse();

 assertEquals("expected Evaluator class", Evaluator.IsRoot.class, evaluator.getClass());
 }

}