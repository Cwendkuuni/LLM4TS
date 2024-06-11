import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.AllElements);
 }

 @Test
 public void testParseSimpleTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.Tag);
 assertEquals("div", eval.toString());
 }

 @Test
 public void testParseTagWithIdQuery() {
 QueryParser parser = new QueryParser("div#id");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.Id);
 assertEquals("id", eval.toString());
 }

 @Test
 public void testParseTagWithClassQuery() {
 QueryParser parser = new QueryParser("div.class");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.Class);
 assertEquals("class", eval.toString());
 }

 @Test
 public void testParseTagWithIdAndClassQuery() {
 QueryParser parser = new QueryParser("div#id.class");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof CombiningEvaluator.And);
 Evaluator idEval = ((CombiningEvaluator.And) eval).getChild(0);
 assertTrue(idEval instanceof Evaluator.Id);
 assertEquals("id", idEval.toString());
 Evaluator classEval = ((CombiningEvaluator.And) eval).getChild(1);
 assertTrue(classEval instanceof Evaluator.Class);
 assertEquals("class", classEval.toString());
 }

 @Test
 public void testParseCombinedQuery() {
 QueryParser parser = new QueryParser("div > p > ul > li:eq(0)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof CombiningEvaluator.And);
 Evaluator parent1 = ((CombiningEvaluator.And) eval).getChild(0);
 assertTrue(parent1 instanceof StructuralEvaluator.ImmediateParent);
 Evaluator parent2 = ((CombiningEvaluator.And) eval).getChild(1);
 assertTrue(parent2 instanceof StructuralEvaluator.ImmediateParent);
 Evaluator tagEval = ((CombiningEvaluator.And) eval).getChild(2);
 assertTrue(tagEval instanceof Evaluator.Tag);
 assertEquals("ul", tagEval.toString());
 Evaluator indexEval = ((CombiningEvaluator.And) eval).getChild(3);
 assertTrue(indexEval instanceof Evaluator.IndexEquals);
 assertEquals(0, ((Evaluator.IndexEquals) indexEval).getIndex());
 }

 @Test
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("div div"); // no combinator
 Evaluator eval = parser.parse();
 fail("Expected Selector.SelectorParseException");
 }

 @Test
 public void testParseInvalidQueryWithCombinator() {
 QueryParser parser = new QueryParser("div div >"); // no tag after combinator
 Evaluator eval = parser.parse();
 fail("Expected Selector.SelectorParseException");
 }

 @Test
 public void testParseInvalidQueryWithInvalidCombinator() {
 QueryParser parser = new QueryParser("div > ~ p"); // invalid combinator
 Evaluator eval = parser.parse();
 fail("Expected Selector.SelectorParseException");
 }

 @Test
 public void testParseQueryWithAttribute() {
 QueryParser parser = new QueryParser("div[class='red']");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.Attribute);
 assertEquals("class='red'", eval.toString());
 }

 @Test
 public void testParseQueryWithAttributeValue() {
 QueryParser parser = new QueryParser("div[href='http://example.com']");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.AttributeWithValue);
 assertEquals("http://example.com", eval.toString());
 }

 @Test
 public void testParseQueryWithAttributeValueStarting() {
 QueryParser parser = new QueryParser("div[title^='foo']");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.AttributeWithValueStarting);
 assertEquals("title^='foo'", eval.toString());
 }

 @Test
 public void testParseQueryWithAttributeValueEnding() {
 QueryParser parser = new QueryParser("div[title$='foo']");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.AttributeWithValueEnding);
 assertEquals("title$='foo'", eval.toString());
 }

 @Test
 public void testParseQueryWithAttributeValueContaining() {
 QueryParser parser = new QueryParser("div[title*='foo']");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.AttributeWithValueContaining);
 assertEquals("title*='foo'", eval.toString());
 }

 @Test
 public void testParseQueryWithAttributeValueMatching() {
 QueryParser parser = new QueryParser("div[title~='foo']");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.AttributeWithValueMatching);
 assertEquals("title~='foo'", eval.toString());
 }

 @Test
 public void testParseQueryWithPseudoClassLessThan() {
 QueryParser parser = new QueryParser("div:lt(2)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.IndexLessThan);
 assertEquals(2, ((Evaluator.IndexLessThan) eval).getIndex());
 }

 @Test
 public void testParseQueryWithPseudoClassGreaterThan() {
 QueryParser parser = new QueryParser("div:gt(2)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.IndexGreaterThan);
 assertEquals(3, ((Evaluator.IndexGreaterThan) eval).getIndex());
 }

 @Test
 public void testParseQueryWithPseudoClassEquals() {
 QueryParser parser = new QueryParser("div:eq(2)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.IndexEquals);
 assertEquals(2, ((Evaluator.IndexEquals) eval).getIndex());
 }

 @Test
 public void testParseQueryWithPseudoClassHas() {
 QueryParser parser = new QueryParser("div:has(p)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testParseQueryWithPseudoClassContains() {
 QueryParser parser = new QueryParser("div:contains(text)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.ContainsText);
 assertEquals("text", eval.toString());
 }

 @Test
 public void testParseQueryWithPseudoClassContainsOwn() {
 QueryParser parser = new QueryParser("div:containsOwn(text)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.ContainsOwnText);
 assertEquals("text", eval.toString());
 }

 @Test
 public void testParseQueryWithPseudoClassMatches() {
 QueryParser parser = new QueryParser("div:matches(.foo)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.Matches);
 assertEquals(".foo", eval.toString());
 }

 @Test
 public void testParseQueryWithPseudoClassMatchesOwn() {
 QueryParser parser = new QueryParser("div:matchesOwn(.foo)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof Evaluator.MatchesOwn);
 assertEquals(".foo", eval.toString());
 }

 @Test
 public void testParseQueryWithPseudoClassNot() {
 QueryParser parser = new QueryParser("div:not(p)");
 Evaluator eval = parser.parse();
 assertTrue(eval instanceof StructuralEvaluator.Not);
 }
}