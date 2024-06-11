import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

 private QueryParser queryParser;

 @Before
 public void setUp() {
 queryParser = new QueryParser("#test");
 }

 @Test
 public void testParse() {
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidQuery() {
 queryParser = new QueryParser("*"); // intentionally invalid query
 try {
 queryParser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // passing
 }
 }

 @Test
 public void testCombinatorGreaterThan() {
 queryParser = new QueryParser("div > p");
 Evaluator rootEval = queryParser.parse().evals.get(0);
 assertTrue(rootEval instanceof StructuralEvaluator.ImmediateParent);
 }

 @Test
 public void testCombinatorLessThan() {
 queryParser = new QueryParser("div < p");
 Evaluator rootEval = queryParser.parse().evals.get(0);
 assertTrue(rootEval instanceof StructuralEvaluator.ImmediatePreviousSibling);
 }

 @Test
 public void testCombinatorSpace() {
 queryParser = new QueryParser("div p");
 Evaluator rootEval = queryParser.parse().evals.get(0);
 assertTrue(rootEval instanceof StructuralEvaluator.Parent);
 }

 @Test
 public void testCombinatorPlus() {
 queryParser = new QueryParser("div + p");
 Evaluator rootEval = queryParser.parse().evals.get(0);
 assertTrue(rootEval instanceof StructuralEvaluator.ImmediatePreviousSibling);
 }

 @Test
 public void testCombinatorTilde() {
 queryParser = new QueryParser("div ~ p");
 Evaluator rootEval = queryParser.parse().evals.get(0);
 assertTrue(rootEval instanceof StructuralEvaluator.PreviousSibling);
 }

 @Test
 public void testCombinatorComma() {
 queryParser = new QueryParser("div, p");
 Evaluator rootEval = queryParser.parse().evals.get(0);
 assertTrue(rootEval instanceof CombiningEvaluator.Or);
 }

 @Test
 public void testFindElementsById() {
 queryParser = new QueryParser("#testId");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Id);
 assertEquals("testId", ((Evaluator.Id) evaluator).getId());
 }

 @Test
 public void testFindElementsByClass() {
 queryParser = new QueryParser(".testClass");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Class);
 assertEquals("testClass", ((Evaluator.Class) evaluator).getClassname());
 }

 @Test
 public void testFindElementsByTag() {
 queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("div", ((Evaluator.Tag) evaluator).getTagName());
 }

 @Test
 public void testFindElementsByTagWithNamespace() {
 queryParser = new QueryParser("html div");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("div", ((Evaluator.Tag) evaluator).getTagName());
 assertTrue(evaluator.toString().startsWith("html>div"));
 }

 @Test
 public void testFindElementsByAttribute() {
 queryParser = new QueryParser("[class='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("class", ((Evaluator.Attribute) evaluator).getAttributeName());
 assertEquals("test", ((Evaluator.Attribute) evaluator).getValue());
 }

 @Test
 public void testFindElementsByAttributeEquals() {
 queryParser = new QueryParser("[class='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("class", ((Evaluator.Attribute) evaluator).getAttributeName());
 assertEquals("test", ((Evaluator.Attribute) evaluator).getValue());
 }

 @Test
 public void testFindElementsByAttributeNotEquals() {
 queryParser = new QueryParser("[class!='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("class", ((Evaluator.Attribute) evaluator).getAttributeName());
 assertFalse(((Evaluator.Attribute) evaluator).getValue().equals("test"));
 }

 @Test
 public void testFindElementsByAttributeStartsWith() {
 queryParser = new QueryParser("[class^='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.AttributeStarting);
 assertEquals("class", ((Evaluator.AttributeStarting) evaluator).getAttributeName());
 assertTrue(((Evaluator.AttributeStarting) evaluator).getValue().startsWith("test"));
 }

 @Test
 public void testFindElementsByAttributeEndsWith() {
 queryParser = new QueryParser("[class$='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
 assertEquals("class", ((Evaluator.AttributeWithValueEnding) evaluator).getAttributeName());
 assertTrue(((Evaluator.AttributeWithValueEnding) evaluator).getValue().endsWith("test"));
 }

 @Test
 public void testFindElementsByAttributeContains() {
 queryParser = new QueryParser("[class*='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
 assertEquals("class", ((Evaluator.AttributeWithValueContaining) evaluator).getAttributeName());
 assertTrue(((Evaluator.AttributeWithValueContaining) evaluator).getValue().contains("test"));
 }

 @Test
 public void testFindElementsByAttributeMatching() {
 queryParser = new QueryParser("[class~='test']");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
 assertEquals("class", ((Evaluator.AttributeWithValueMatching) evaluator).getAttributeName());
 assertTrue(((Evaluator.AttributeWithValueMatching) evaluator).getPattern().matcher("testClass").matches());
 }

 @Test
 public void testFindAllElements() {
 queryParser = new QueryParser("*");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @Test
 public void testFindElementByIndexLessThan() {
 queryParser = new QueryParser(":lt(2)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IndexLessThan);
 assertEquals(2, ((Evaluator.IndexLessThan) evaluator).getIndex());
 }

 @Test
 public void testFindElementByIndexGreaterThan() {
 queryParser = new QueryParser(":gt(2)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);
 assertEquals(2, ((Evaluator.IndexGreaterThan) evaluator).getIndex());
 }

 @Test
 public void testFindElementByIndexEquals() {
 queryParser = new QueryParser(":eq(2)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IndexEquals);
 assertEquals(2, ((Evaluator.IndexEquals) evaluator).getIndex());
 }

 @Test
 public void testFindElementByHas() {
 queryParser = new QueryParser(":has(p)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testFindElementByContainsText() {
 queryParser = new QueryParser(":contains(text)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.ContainsText);
 }

 @Test
 public void testFindElementByContainsOwnText() {
 queryParser = new QueryParser(":containsOwn(text)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
 }

 @Test
 public void testFindElementByContainsData() {
 queryParser = new QueryParser(":containsData(data)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.ContainsData);
 }

 @Test
 public void testFindElementByMatches() {
 queryParser = new QueryParser(":matches(regex)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.Matches);
 }

 @Test
 public void testFindElementByMatchesOwn() {
 queryParser = new QueryParser(":matchesOwn(regex)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.MatchesOwn);
 }

 @Test
 public void testFindElementByNot() {
 queryParser = new QueryParser(":not(selector)");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof StructuralEvaluator.Not);
 }

 @Test
 public void testFindElementByNthChild() {
 queryParser = new QueryParser(":nth-child(1) p");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsNthChild);
 assertEquals(1, ((Evaluator.IsNthChild) evaluator).getIndex());
 assertEquals("p", ((Evaluator.IsNthChild) evaluator).getTagName());
 }

 @Test
 public void testFindElementByNthLastChild() {
 queryParser = new QueryParser(":nth-last-child(1) p");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
 assertEquals(1, ((Evaluator.IsNthLastChild) evaluator).getIndex());
 assertEquals("p", ((Evaluator.IsNthLastChild) evaluator).getTagName());
 }

 @Test
 public void testFindElementByNthOfType() {
 queryParser = new QueryParser(":nth-of-type(1) p");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsNthOfType);
 assertEquals(1, ((Evaluator.IsNthOfType) evaluator).getIndex());
 assertEquals("p", ((Evaluator.IsNthOfType) evaluator).getTagName());
 }

 @Test
 public void testFindElementByNthLastOfType() {
 queryParser = new QueryParser(":nth-last-of-type(1) p");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
 assertEquals(1, ((Evaluator.IsNthLastOfType) evaluator).getIndex());
 assertEquals("p", ((Evaluator.IsNthLastOfType) evaluator).getTagName());
 }

 @Test
 public void testFindElementByFirstChild() {
 queryParser = new QueryParser(":first-child");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsFirstChild);
 }

 @Test
 public void testFindElementByLastChild() {
 queryParser = new QueryParser(":last-child");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsLastChild);
 }

 @Test
 public void testFindElementByFirstOfType() {
 queryParser = new QueryParser(":first-of-type");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsFirstOfType);
 }

 @Test
 public void testFindElementByLastOfType() {
 queryParser = new QueryParser(":last-of-type");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsLastOfType);
 }

 @Test
 public void testFindElementByEmpty() {
 queryParser = new QueryParser(":empty");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsEmpty);
 }

 @Test
 public void testFindElementByRoot() {
 queryParser = new QueryParser(":root");
 Evaluator evaluator = queryParser.parse().evals.get(0);
 assertTrue(evaluator instanceof Evaluator.IsRoot);
 }
}