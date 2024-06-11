import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.helper.TokenQueue;
import java.util.List;
import java.util.ArrayList;

public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;

 @Test
 public void testParse_EmptyQuery() {
 queryParser = new QueryParser("");
 Assert.assertNull(queryParser.parse());
 }

 @Test
 public void testParse_SingleTag() {
 queryParser = new QueryParser("div");
 Assert.assertEquals(new Evaluator.Tag("div"), queryParser.parse());
 }

 @Test
 public void testParse_MultipleTags() {
 queryParser = new QueryParser("div, p, span");
 Evaluator andEvaluator = new CombiningEvaluator.And(
 new Evaluator.Tag("div"),
 new Evaluator.Tag("p"),
 new Evaluator.Tag("span")
 );
 Assert.assertEquals(andEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_TagWithClass() {
 queryParser = new QueryParser("div.class");
 List<Evaluator> evals = new ArrayList<>();
 evals.add(new Evaluator.Tag("div"));
 evals.add(new Evaluator.Class("class"));
 Assert.assertEquals(new CombiningEvaluator.And(evals), queryParser.parse());
 }

 @Test
 public void testParse_TagWithId() {
 queryParser = new QueryParser("div#id");
 List<Evaluator> evals = new ArrayList<>();
 evals.add(new Evaluator.Tag("div"));
 evals.add(new Evaluator.Id("id"));
 Assert.assertEquals(new CombiningEvaluator.And(evals), queryParser.parse());
 }

 @Test
 public void testParse_TagWithAttribute() {
 queryParser = new QueryParser("div[title='test']");
 List<Evaluator> evals = new ArrayList<>();
 evals.add(new Evaluator.Tag("div"));
 evals.add(new Evaluator.AttributeWithValue("title", "'test'"));
 Assert.assertEquals(new CombiningEvaluator.And(evals), queryParser.parse());
 }

 @Test
 public void testParse_TagWithMultipleAttributes() {
 queryParser = new QueryParser("div[title='test'][class='class']");
 List<Evaluator> evals = new ArrayList<>();
 evals.add(new Evaluator.Tag("div"));
 evals.add(new Evaluator.AttributeWithValue("title", "'test'"));
 evals.add(new Evaluator.Class("class"));
 Assert.assertEquals(new CombiningEvaluator.And(evals), queryParser.parse());
 }

 @Test
 public void testParse_DescendantSelector() {
 queryParser = new QueryParser("div p");
 StructuralEvaluator.Descendant descendantEvaluator = new StructuralEvaluator.Descendant("p", "div");
 Assert.assertEquals(descendantEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ChildSelector() {
 queryParser = new QueryParser("div > p");
 StructuralEvaluator.Child childEvaluator = new StructuralEvaluator.Child("p", "div");
 Assert.assertEquals(childEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_AdjacentSiblingSelector() {
 queryParser = new QueryParser("div ~ p");
 StructuralEvaluator.AdjacentSibling adjacentSiblingEvaluator = new StructuralEvaluator.AdjacentSibling("p", "div");
 Assert.assertEquals(adjacentSiblingEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_GeneralSiblingSelector() {
 queryParser = new QueryParser("div > p ~ .class");
 CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
 new StructuralEvaluator.Child("p", "div"),
 new StructuralEvaluator.AdjacentSibling(new Evaluator.Class("class"), "p")
 );
 Assert.assertEquals(andEvaluator, queryParser.parse());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_UnknownCombinator() {
 queryParser = new QueryParser("div ~% p");
 }

 @Test
 public void testParse_AttributeSelectorWithOperator() {
 queryParser = new QueryParser("div[title~='test']");
 Evaluator attributeEvaluator = new Evaluator.AttributeWithValueMatching("title", Pattern.compile("test"));
 Assert.assertEquals(attributeEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithNthChild() {
 queryParser = new QueryParser("div:nth-child(1)");
 Evaluator isNthChildEvaluator = new Evaluator.IsNthChild(1, 0);
 Assert.assertEquals(isNthChildEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithNthLastChild() {
 queryParser = new QueryParser("div:nth-last-child(1)");
 Evaluator isNthLastChildEvaluator = new Evaluator.IsNthLastChild(1, 0);
 Assert.assertEquals(isNthLastChildEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithNthOfType() {
 queryParser = new QueryParser("div:nth-of-type(2)");
 Evaluator isNthOfTypeEvaluator = new Evaluator.IsNthOfType(2, 0);
 Assert.assertEquals(isNthOfTypeEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithNthLastOfType() {
 queryParser = new QueryParser("div:nth-last-of-type(2)");
 Evaluator isNthLastOfTypeEvaluator = new Evaluator.IsNthLastOfType(2, 0);
 Assert.assertEquals(isNthLastOfTypeEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithFirstChild() {
 queryParser = new QueryParser("div:first-child");
 Evaluator isFirstChildEvaluator = new Evaluator.IsFirstChild();
 Assert.assertEquals(isFirstChildEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithLastChild() {
 queryParser = new QueryParser("div:last-child");
 Evaluator isLastChildEvaluator = new Evaluator.IsLastChild();
 Assert.assertEquals(isLastChildEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithFirstOfType() {
 queryParser = new QueryParser("div:first-of-type");
 Evaluator isFirstOfTypeEvaluator = new Evaluator.IsFirstOfType();
 Assert.assertEquals(isFirstOfTypeEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithLastOfType() {
 queryParser = new QueryParser("div:last-of-type");
 Evaluator isLastOfTypeEvaluator = new Evaluator.IsLastOfType();
 Assert.assertEquals(isLastOfTypeEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithOnlyChild() {
 queryParser = new QueryParser("div:only-child");
 Evaluator isOnlyChildEvaluator = new Evaluator.IsOnlyChild();
 Assert.assertEquals(isOnlyChildEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithOnlyOfType() {
 queryParser = new QueryParser("div:only-of-type");
 Evaluator isOnlyOfTypeEvaluator = new Evaluator.IsOnlyOfType();
 Assert.assertEquals(isOnlyOfTypeEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithEmpty() {
 queryParser = new QueryParser("div:empty");
 Evaluator isEmptyEvaluator = new Evaluator.IsEmpty();
 Assert.assertEquals(isEmptyEvaluator, queryParser.parse());
 }

 @Test
 public void testParse_ElementWithRoot() {
 queryParser = new QueryParser(":root");
 Evaluator isRootEvaluator = new Evaluator.IsRoot();
 Assert.assertEquals(isRootEvaluator, queryParser.parse());
 }
}