import org.junit.Assert;
import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import static org.jsoup.select.QueryParser.combinators;
import static org.jsoup.select.QueryParser.parse;

public class QueryParserTest {

 private static final String BASIC_QUERY = "div.class1.class2 > p:first-child";

 @Test
 public void testParseBasicQuery() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY);
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseUnknownCombinator() {
 QueryParser queryParser = new QueryParser("div%^%p");
 queryParser.parse(); // should throw Selector.SelectorParseException
 }

 @Test
 public void testParseCombinationOfSelectors() {
 QueryParser queryParser = new QueryParser("div.class1 > p:first-child, ul.list1 li.item1:nth-child(2)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 }

 @Test
 public void testParseIdSelector() {
 QueryParser queryParser = new QueryParser("#elementId");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.Id);
 }

 @Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".class1.class2");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.Class);
 }

 @Test
 public void testParseTagSelector() {
 QueryParser queryParser = new QueryParser("p:first-child");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.Tag);
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser queryParser = new QueryParser("[title='Test']");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.Attribute);
 }

 @Test
 public void testParseAttributeSelectorWithValue() {
 QueryParser queryParser = new QueryParser("[title='Test'] > p:first-child");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 }

 @Test
 public void testParseAttributeSelectorWithOperator() {
 QueryParser queryParser = new QueryParser("[title~='Test'] > p:first-child");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
 }

 @Test
 public void testParseNegatedAttributeSelector() {
 QueryParser queryParser = new QueryParser("[title!='Test'] > p:first-child");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
 }

 @Test
 public void testParsePseudoClassSelector() {
 QueryParser queryParser = new QueryParser(":empty");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.IsEmpty);
 }

 @Test
 public void testParsePseudoClassSelectorWithArgument() {
 QueryParser queryParser = new QueryParser(":nth-child(2) p");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.IsNthChild);
 Assert.assertEquals(2, ((Evaluator.IsNthChild) evaluator).getN());
 }

 @Test
 public void testParsePseudoClassSelectorWithOfType() {
 QueryParser queryParser = new QueryParser("li:nth-of-type(odd) > a");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.IsNthOfType);
 Assert.assertTrue(((Evaluator.IsNthOfType) evaluator).isOfType());
 }

 @Test
 public void testParsePseudoClassSelectorWithBackwards() {
 QueryParser queryParser = new QueryParser(":nth-last-of-type(-n+3) p");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
 }

 @Test
 public void testParsePseudoSelectorHas() {
 QueryParser queryParser = new QueryParser(":has(p:first-child)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testParsePseudoSelectorContains() {
 QueryParser queryParser = new QueryParser(":contains(Test)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.ContainsText);
 }

 @Test
 public void testParsePseudoSelectorContainsOwn() {
 QueryParser queryParser = new QueryParser(":containsOwn(Test)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
 }

 @Test
 public void testParsePseudoSelectorContainsData() {
 QueryParser queryParser = new QueryParser(":containsData(data)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.ContainsData);
 }

 @Test
 public void testParsePseudoSelectorMatches() {
 QueryParser queryParser = new QueryParser(":matches(.foo)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.Matches);
 }

 @Test
 public void testParsePseudoSelectorMatchesOwn() {
 QueryParser queryParser = new QueryParser(":matchesOwn(.foo)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof Evaluator.MatchesOwn);
 }

 @Test
 public void testParsePseudoSelectorNot() {
 QueryParser queryParser = new QueryParser(":not(.class1)");
 Evaluator evaluator = queryParser.parse();

 Assert.assertNotNull(evaluator);
 Assert.assertTrue(evaluator instanceof StructuralEvaluator.Not);
 }
}