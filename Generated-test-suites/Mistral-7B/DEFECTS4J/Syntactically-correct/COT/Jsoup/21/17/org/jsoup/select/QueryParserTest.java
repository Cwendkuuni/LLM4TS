import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 private static final String[] testQueries = {
 "div > p",
 "div p",
 "div + p",
 "div ~ p",
 "div#id",
 "div.class",
 "div.class1.class2",
 "div.class1",
 "div.class2",
 "div:not(.class)",
 "div:not([id])",
 "div:lt(3)",
 "div:gt(3)",
 "div:eq(3)",
 "div:has(p)",
 "div:contains(text)",
 "div:containsOwn(text)",
 "div:matches(regex)",
 "div:matchesOwn(regex)",
 "div[attr='value']",
 "div[attr='value1'|'value2']",
 "div[attr^='value']",
 "div[attr$='value']",
 "div[attr*='value']"
 };

 @org.junit.Test
 public void testParse() {
 for (String query : testQueries) {
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 // Add more assertions based on the structure of Evaluator here
 }
 }

 @org.junit.Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("invalid query");
 parser.parse();
 }

 @org.junit.Test
 public void testCombinator() {
 QueryParser parser = new QueryParser("div > p");
 parser.combinator('>');
 List<Evaluator> evals = parser.evals;
 assertTrue(evals.size() == 2);
 Evaluator parent = evals.get(1);
 assertTrue(parent instanceof StructuralEvaluator.ImmediateParent);
 }

 @org.junit.Test
 public void testConsumeSubQuery() {
 QueryParser parser = new QueryParser("div > p > q");
 StringBuilder sq = new StringBuilder();
 parser.consumeSubQuery().forEachRemaining(sq::append);
 assertEquals("p > q", sq.toString());
 }

 @org.junit.Test
 public void testFindElementsById() {
 QueryParser parser = new QueryParser("div#id");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.Id);
 }

 @org.junit.Test
 public void testFindElementsByClass() {
 QueryParser parser = new QueryParser("div.class");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.Class);
 }

 @org.junit.Test
 public void testFindElementsByTag() {
 QueryParser parser = new QueryParser("div");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.Tag);
 }

 @org.junit.Test
 public void testFindElementsByAttribute() {
 QueryParser parser = new QueryParser("div[attr='value']");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.Attribute);
 }

 @org.junit.Test
 public void testFindElementsAllElements() {
 QueryParser parser = new QueryParser("div:all");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.AllElements);
 }

 @org.junit.Test
 public void testFindElementsIndexLessThan() {
 QueryParser parser = new QueryParser("div:lt(2)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.IndexLessThan);
 }

 @org.junit.Test
 public void testFindElementsIndexGreaterThan() {
 QueryParser parser = new QueryParser("div:gt(2)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.IndexGreaterThan);
 }

 @org.junit.Test
 public void testFindElementsIndexEquals() {
 QueryParser parser = new QueryParser("div:eq(2)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.IndexEquals);
 }

 @org.junit.Test
 public void testFindElementsHas() {
 QueryParser parser = new QueryParser("div:has(p)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof StructuralEvaluator.Has);
 }

 @org.junit.Test
 public void testFindElementsContains() {
 QueryParser parser = new QueryParser("div:contains(text)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsText);
 }

 @org.junit.Test
 public void testFindElementsContainsOwn() {
 QueryParser parser = new QueryParser("div:containsOwn(text)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsOwnText);
 }

 @org.junit.Test
 public void testFindElementsMatches() {
 QueryParser parser = new QueryParser("div:matches(regex)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.Matches);
 }

 @org.junit.Test
 public void testFindElementsMatchesOwn() {
 QueryParser parser = new QueryParser("div:matchesOwn(regex)");
 parser.findElements();
 assertTrue(parser.evals.size() == 1);
 assertTrue(parser.evals.get(0) instanceof Evaluator.MatchesOwn);
 }

 @org.junit.Test(expected = Selector.SelectorParseException.class)
 public void testFindElementsInvalidQuery() {
 QueryParser parser = new QueryParser("div:invalid");
 parser.findElements();
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFindElementsNullQuery() {
 QueryParser parser = new QueryParser(null);
 parser.findElements();
 }
}