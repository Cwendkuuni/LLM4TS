import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class QueryParserTest {

 private static String query;
 private static QueryParser parser;
 private static Evaluator evaluator;
 private static TokenQueue tokenQueue;

 @Test
 public void testParse_EmptyQuery() {
 query = "";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNull(evaluator);
 }

 @Test
 public void testParse_SingleElement() {
 query = "div";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParse_SingleElementWithId() {
 query = "#foo";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 }

 @Test
 public void testParse_SingleElementWithClass() {
 query = ".bar";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test
 public void testParse_MultipleElements() {
 query = "div, p, .bar";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.Or);
 }

 @Test
 public void testParse_ElementsWithCombinators() {
 query = "div > p";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator parentEvaluator = ((CombiningEvaluator.And) evaluator).get(0);
 assertTrue(parentEvaluator instanceof StructuralEvaluator.ImmediateParent);
 }

 @Test
 public void testParse_ComplexQuery() {
 query = "div > p:containsOwn(test) ~ div.class";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator parentEvaluator = ((CombiningEvaluator.And) evaluator).get(0);
 assertTrue(parentEvaluator instanceof StructuralEvaluator.ImmediateParent);
 Evaluator containsEvaluator = ((CombiningEvaluator.And) evaluator).get(1);
 assertTrue(containsEvaluator instanceof Evaluator.ContainsOwnText);
 Evaluator lastEvaluator = ((CombiningEvaluator.And) evaluator).get(2);
 assertTrue(lastEvaluator instanceof Evaluator.Class);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_InvalidQuery() {
 query = "div > p:not(#foo) > #bar";
 parser = new QueryParser(query);
 evaluator = parser.parse();
 }

 @Test
 public void testCombinator_NoPreviousSibling() {
 tokenQueue = new TokenQueue("div ~ p");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator previousSiblingEvaluator = ((CombiningEvaluator.And) evaluator).get(1);
 assertNull(previousSiblingEvaluator);
 }

 @Test
 public void testCombinator_PreviousSibling() {
 tokenQueue = new TokenQueue("div + p");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator previousSiblingEvaluator = ((CombiningEvaluator.And) evaluator).get(1);
 assertNotNull(previousSiblingEvaluator);
 assertEquals(Evaluator.Class.class, previousSiblingEvaluator.getClass());
 }

 @Test
 public void testCombinator_ImmediatePreviousSibling() {
 tokenQueue = new TokenQueue("div > p + p");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator previousSiblingEvaluator = ((CombiningEvaluator.And) evaluator).get(1);
 assertNotNull(previousSiblingEvaluator);
 assertEquals(Evaluator.Class.class, previousSiblingEvaluator.getClass());
 assertEquals(((StructuralEvaluator.PreviousSibling) previousSiblingEvaluator).getParent(), parentEvaluator);
 }

 @Test
 public void testCombinator_ImmediateParent() {
 tokenQueue = new TokenQueue("div > p");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 Evaluator childEvaluator = new Evaluator.Tag("p");
 evaluator = parser.parse();
 assertNotNull(evaluator);
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator parentEvaluator = ((CombiningEvaluator.And) evaluator).get(0);
 assertNotNull(parentEvaluator);
 assertEquals(Evaluator.Class.class, parentEvaluator.getClass());
 assertEquals(((StructuralEvaluator.ImmediateParent) parentEvaluator).getChild(), childEvaluator);
 }

 @Test
 public void testConsumeSubQuery() {
 tokenQueue = new TokenQueue("div > p > #foo > #bar");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 String subQuery = parser.consumeSubQuery();
 assertEquals("div > p > #foo", subQuery);
 }

 @Test
 public void testFindElements_ById() {
 tokenQueue = new TokenQueue("#foo");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.Id.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_ByClass() {
 tokenQueue = new TokenQueue(".bar");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.Class.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_ByTag() {
 tokenQueue = new TokenQueue("div");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.Tag.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_ByAttribute() {
 tokenQueue = new TokenQueue("[title=foo]");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.Attribute.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_ByAttributeWithValue() {
 tokenQueue = new TokenQueue("[title='foo']");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.AttributeWithValue.class, parser.evals.get(0).getClass());
 assertEquals("foo", ((Evaluator.AttributeWithValue) parser.evals.get(0)).getValue());
 }

 @Test
 public void testFindElements_ByAttributeWithValueStarting() {
 tokenQueue = new TokenQueue("[title^='foo']");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.AttributeWithValueStarting.class, parser.evals.get(0).getClass());
 assertEquals("foo", ((Evaluator.AttributeWithValueStarting) parser.evals.get(0)).getValue());
 }

 @Test
 public void testFindElements_ByAttributeWithValueEnding() {
 tokenQueue = new TokenQueue("[title$='foo']");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.AttributeWithValueEnding.class, parser.evals.get(0).getClass());
 assertEquals("foo", ((Evaluator.AttributeWithValueEnding) parser.evals.get(0)).getValue());
 }

 @Test
 public void testFindElements_ByAttributeWithValueContaining() {
 tokenQueue = new TokenQueue("[title*='foo']");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.AttributeWithValueContaining.class, parser.evals.get(0).getClass());
 assertEquals("foo", ((Evaluator.AttributeWithValueContaining) parser.evals.get(0)).getValue());
 }

 @Test
 public void testFindElements_ByAttributeWithValueMatching() {
 tokenQueue = new TokenQueue("[title~='foo']");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.AttributeWithValueMatching.class, parser.evals.get(0).getClass());
 assertNotNull(((Evaluator.AttributeWithValueMatching) parser.evals.get(0)).getPattern());
 }

 @Test
 public void testFindElements_AllElements() {
 tokenQueue = new TokenQueue("*");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.AllElements.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_IndexLessThan() {
 tokenQueue = new TokenQueue(":lt(2)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.IndexLessThan.class, parser.evals.get(0).getClass());
 assertEquals(2, ((Evaluator.IndexLessThan) parser.evals.get(0)).getIndex());
 }

 @Test
 public void testFindElements_IndexGreaterThan() {
 tokenQueue = new TokenQueue(":gt(2)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.IndexGreaterThan.class, parser.evals.get(0).getClass());
 assertEquals(2, ((Evaluator.IndexGreaterThan) parser.evals.get(0)).getIndex());
 }

 @Test
 public void testFindElements_IndexEquals() {
 tokenQueue = new TokenQueue(":eq(2)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.IndexEquals.class, parser.evals.get(0).getClass());
 assertEquals(2, ((Evaluator.IndexEquals) parser.evals.get(0)).getIndex());
 }

 @Test
 public void testFindElements_Has() {
 tokenQueue = new TokenQueue(":has(p)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(StructuralEvaluator.Has.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_Contains() {
 tokenQueue = new TokenQueue(":contains(test)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.ContainsText.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_ContainsOwn() {
 tokenQueue = new TokenQueue(":containsOwn(test)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.ContainsOwnText.class, parser.evals.get(0).getClass());
 }

 @Test
 public void testFindElements_Matches() {
 tokenQueue = new TokenQueue(":matches(^foo$)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.Matches.class, parser.evals.get(0).getClass());
 assertEquals(Pattern.compile("^foo$"), ((Evaluator.Matches) parser.evals.get(0)).getPattern());
 }

 @Test
 public void testFindElements_MatchesOwn() {
 tokenQueue = new TokenQueue(":matchesOwn(^foo$)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(Evaluator.MatchesOwn.class, parser.evals.get(0).getClass());
 assertEquals(Pattern.compile("^foo$"), ((Evaluator.MatchesOwn) parser.evals.get(0)).getPattern());
 }

 @Test
 public void testFindElements_Not() {
 tokenQueue = new TokenQueue(":not(p)");
 parser = new QueryParser("");
 parser.query = tokenQueue.toString();
 parser.findElements();
 assertEquals(StructuralEvaluator.Not.class, parser.evals.get(0).getClass());
 }
}