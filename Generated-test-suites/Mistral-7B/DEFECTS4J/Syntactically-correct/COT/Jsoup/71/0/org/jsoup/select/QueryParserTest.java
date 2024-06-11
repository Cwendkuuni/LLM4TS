import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import java.util.List;
import static org.hamcrest.CoreMatchers.instanceOf;

public class QueryParserTest {

 private String query;
 private QueryParser parser;
 private Evaluator result;

 @Test
 public void testParseEmptyQuery() {
 query = "";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertNull(result);
 }

 @Test
 public void testParseSingleElement() {
 query = "div";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.Tag.class));
 }

 @Test
 public void testParseIdSelector() {
 query = "#myId";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.Id.class));
 }

 @Test
 public void testParseClassSelector() {
 query = ".myClass";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.Class.class));
 }

 @Test
 public void testParseTagSelector() {
 query = "tagName";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.Tag.class));
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidTagSelector() {
 query = "tagName#myId";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test
 public void testParseAttributeSelector() {
 query = "[title]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.Attribute.class));
 }

 @Test
 public void testParseAttributeSelectorWithValue() {
 query = "[title=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.AttributeWithValue.class));
 }

 @Test
 public void testParseAttributeSelectorWithPrefixValue() {
 query = "[title^=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.AttributeWithValueStarting.class));
 }

 @Test
 public void testParseAttributeSelectorWithSuffixValue() {
 query = "[title$=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.AttributeWithValueEnding.class));
 }

 @Test
 public void testParseAttributeSelectorWithSubstringValue() {
 query = "[title*=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.AttributeWithValueContaining.class));
 }

 @Test
 public void testParseAttributeSelectorWithRegexValue() {
 query = "[title~=value]";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.AttributeWithValueMatching.class));
 }

 @Test
 public void testParseMultipleSelectorsWithCombinatorSpace() {
 query = "div.myClass";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(CombiningEvaluator.And.class));
 }

 @Test
 public void testParseMultipleSelectorsWithCombinatorGreaterThan() {
 query = "div > p";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(CombiningEvaluator.And.class));
 }

 @Test
 public void testParseMultipleSelectorsWithCombinatorPlus() {
 query = "div + p";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(CombiningEvaluator.And.class));
 }

 @Test
 public void testParseMultipleSelectorsWithCombinatorTilde() {
 query = "div ~ p";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(CombiningEvaluator.And.class));
 }

 @Test
 public void testParseMultipleSelectorsWithCombinatorComma() {
 query = "div, p";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(CombiningEvaluator.Or.class));
 }

 @Test
 public void testParseDescendantSelector() {
 query = "div p";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(StructuralEvaluator.Descendant.class));
 }

 @Test
 public void testParseChildSelector() {
 query = "div > p";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(StructuralEvaluator.ImmediateChild.class));
 }

 @Test
 public void testParsePreviousSiblingSelector() {
 query = "p ~ div";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(StructuralEvaluator.PreviousSibling.class));
 }

 @Test
 public void testParsePreviousSiblingSelectorImmediate() {
 query = "p > ~ div";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(StructuralEvaluator.ImmediatePreviousSibling.class));
 }

 @Test
 public void testParsePseudoElementFirstChild() {
 query = "div > :first-child";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsFirstChild.class));
 }

 @Test
 public void testParsePseudoElementLastChild() {
 query = "div > :last-child";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsLastChild.class));
 }

 @Test
 public void testParsePseudoElementFirstOfType() {
 query = "div > :first-of-type";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsFirstOfType.class));
 }

 @Test
 public void testParsePseudoElementLastOfType() {
 query = "div > :last-of-type";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsLastOfType.class));
 }

 @Test
 public void testParsePseudoElementOnlyChild() {
 query = "div:only-child";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsOnlyChild.class));
 }

 @Test
 public void testParsePseudoElementOnlyOfType() {
 query = "div:only-of-type";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsOnlyOfType.class));
 }

 @Test
 public void testParsePseudoElementEmpty() {
 query = "div:empty";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsEmpty.class));
 }

 @Test
 public void testParsePseudoElementRoot() {
 query = ":root";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsRoot.class));
 }

 @Test
 public void testParsePseudoElementNthChild() {
 query = "div:nth-child(2)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsNthChild.class));
 }

 @Test
 public void testParsePseudoElementNthLastChild() {
 query = "div:nth-last-child(2)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsNthLastChild.class));
 }

 @Test
 public void testParsePseudoElementNthOfType() {
 query = "div:nth-of-type(2)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsNthOfType.class));
 }

 @Test
 public void testParsePseudoElementNthLastOfType() {
 query = "div:nth-last-of-type(2)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.IsNthLastOfType.class));
 }

 @Test
 public void testParsePseudoElementHas() {
 query = "div:has(p)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(StructuralEvaluator.Has.class));
 }

 @Test
 public void testParsePseudoElementContains() {
 query = "div:contains(text)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.ContainsText.class));
 }

 @Test
 public void testParsePseudoElementContainsOwn() {
 query = "div:containsOwn(text)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.ContainsOwnText.class));
 }

 @Test
 public void testParsePseudoElementContainsData() {
 query = "div:containsData(data)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.ContainsData.class));
 }

 @Test
 public void testParsePseudoElementMatches() {
 query = "div:matches(regex)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.Matches.class));
 }

 @Test
 public void testParsePseudoElementMatchesOwn() {
 query = "div:matchesOwn(regex)";
 parser = new QueryParser(query);
 result = parser.parse();
 Assert.assertThat(result, instanceOf(Evaluator.MatchesOwn.class));
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 query = "div:lt(2) > p";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithCombinatorGreaterThan() {
 query = "div > p > :first-child";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithCombinatorPlus() {
 query = "div + p + :first-child";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithCombinatorTilde() {
 query = "div ~ p ~ :first-child";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithCombinatorComma() {
 query = "div, p > :first-child";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test (expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithCombinatorSpace() {
 query = "div > p div";
 parser = new QueryParser(query);
 parser.parse();
 }
}