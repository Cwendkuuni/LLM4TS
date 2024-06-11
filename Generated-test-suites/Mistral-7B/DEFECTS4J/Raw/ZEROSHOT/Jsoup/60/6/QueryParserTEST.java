import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.helper.StringUtil;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 Assert.assertNull(evaluator);
 }

 @Test
 public void testParseSimpleTagSelector() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.Tag.class));
 Assert.assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".class");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.Class.class));
 Assert.assertEquals(".class", evaluator.toString());
 }

 @Test
 public void testParseIDSelector() {
 QueryParser queryParser = new QueryParser("#id");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.Id.class));
 Assert.assertEquals("#id", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser queryParser = new QueryParser("[title]");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.Attribute.class));
 Assert.assertEquals("[title]", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueSelector() {
 QueryParser queryParser = new QueryParser("[title='test']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.AttributeWithValue.class));
 Assert.assertEquals("[title='test']", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueStartingSelector() {
 QueryParser queryParser = new QueryParser("[title^='test']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.AttributeWithValueStarting.class));
 Assert.assertEquals("[title^='test']", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueEndingSelector() {
 QueryParser queryParser = new QueryParser("[title$='test']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.AttributeWithValueEnding.class));
 Assert.assertEquals("[title$='test']", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueContainingSelector() {
 QueryParser queryParser = new QueryParser("[title*='test']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.AttributeWithValueContaining.class));
 Assert.assertEquals("[title*='test']", evaluator.toString());
 }

 @Test
 public void testParseAttributeWithValueMatchingSelector() {
 QueryParser queryParser = new QueryParser("[title~='test']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(org.jsoup.select.Evaluator.AttributeWithValueMatching.class));
 Assert.assertEquals("[title~='test']", evaluator.toString());
 }

 @Test
 public void testParseCombinedSelectors() {
 QueryParser queryParser = new QueryParser("div.class #id > .subclass:first-child");
 Evaluator evaluator = queryParser.parse();
 // The order of the evaluators in the Evaluator tree is important, so we can't just compare the toString()
 // values. Instead, we'll check for the correct types of Evaluators in the Evaluator list.
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new Evaluator.Tag("div"));
 expectedEvaluators.add(new Evaluator.Class("class"));
 expectedEvaluators.add(new Evaluator.Id("id"));
 expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
 expectedEvaluators.add(new Evaluator.Tag(".subclass"));
 expectedEvaluators.add(new Evaluator.IsFirstChild());
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).getClass(), evaluator.get(i).getClass());
 }
 }

 @Test
 public void testParseTagAndClassSelector() {
 QueryParser queryParser = new QueryParser("div.class");
 Evaluator evaluator = queryParser.parse();
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new Evaluator.Tag("div"));
 expectedEvaluators.add(new Evaluator.Class("class"));
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).toString(), evaluator.get(i).toString());
 }
 }

 @Test
 public void testParseComplexSelector() {
 QueryParser queryParser = new QueryParser("a.btn:not(.disabled):first-child > button#submit:contains(Submit):first-child");
 Evaluator evaluator = queryParser.parse();
 // The order of the evaluators in the Evaluator tree is important, so we can't just compare the toString()
 // values. Instead, we'll check for the correct types of Evaluators in the Evaluator list.
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new Evaluator.Tag("a"));
 expectedEvaluators.add(new Evaluator.Class("btn"));
 expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("disabled")));
 expectedEvaluators.add(new Evaluator.Tag("button"));
 expectedEvaluators.add(new Evaluator.ContainsText("Submit"));
 expectedEvaluators.add(new Evaluator.IsFirstChild());
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).toString(), evaluator.get(i).toString());
 }
 }

 @Test
 public void testParsePseudoClassSelectors() {
 QueryParser queryParser = new QueryParser(":first-child, :last-child, :only-child");
 Evaluator evaluator = queryParser.parse();
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new Evaluator.IsFirstChild());
 expectedEvaluators.add(new Evaluator.IsLastChild());
 expectedEvaluators.add(new Evaluator.IsOnlyChild());
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).toString(), evaluator.get(i).toString());
 }
 }

 @Test
 public void testParsePseudoElementSelectors() {
 QueryParser queryParser = new QueryParser(":before, :after");
 Evaluator evaluator = queryParser.parse();
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new StructuralEvaluator.PseudoElementBefore());
 expectedEvaluators.add(new StructuralEvaluator.PseudoElementAfter());
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).toString(), evaluator.get(i).toString());
 }
 }

 @Test
 public void testParseCombinedPseudoClassAndTagSelectors() {
 QueryParser queryParser = new QueryParser("ul.menu > li:first-child > a:link");
 Evaluator evaluator = queryParser.parse();
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new Evaluator.Tag("ul"));
 expectedEvaluators.add(new Evaluator.Class("menu"));
 expectedEvaluators.add(new StructuralEvaluator.ImmediateChild());
 expectedEvaluators.add(new Evaluator.Tag("li"));
 expectedEvaluators.add(new Evaluator.IsFirstChild());
 expectedEvaluators.add(new Evaluator.Tag("a"));
 expectedEvaluators.add(new Evaluator.PseudoClass("link"));
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).toString(), evaluator.get(i).toString());
 }
 }

 @Test
 public void testParseCombinedPseudoClassAndAttributeSelectors() {
 QueryParser queryParser = new QueryParser("a[class]:not(.active):hover");
 Evaluator evaluator = queryParser.parse();
 List<Evaluator> expectedEvaluators = new ArrayList<>();
 expectedEvaluators.add(new Evaluator.Tag("a"));
 expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("active")));
 expectedEvaluators.add(new Evaluator.PseudoClass("hover"));
 Assert.assertEquals(expectedEvaluators.size(), evaluator.depth());
 for (int i = 0; i < expectedEvaluators.size(); i++) {
 Assert.assertEquals(expectedEvaluators.get(i).toString(), evaluator.get(i).toString());
 }
 }

 @Test
 public void testParseAttributeWithRegularExpressionSelector() {
 QueryParser queryParser = new QueryParser("[title~='test']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(Evaluator.Matches.class));
 }

 @Test
 public void testParseComplexAttributeWithRegularExpressionSelector() {
 QueryParser queryParser = new QueryParser("[title~='(test|Test)']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(Evaluator.MatchesOwn.class));
 }

 @Test
 public void testParseComplexAttributeWithMultipleRegularExpressionSelectors() {
 QueryParser queryParser = new QueryParser("[title~='(test|Test)|(foo|Foo)']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(CombiningEvaluator.Or.class));
 List<Evaluator> orEvaluators = (List<Evaluator>) evaluator;
 Assert.assertEquals(2, orEvaluators.size());
 Assert.assertThat(orEvaluators.get(0), instanceOf(Evaluator.MatchesOwn.class));
 Assert.assertThat(orEvaluators.get(1), instanceOf(Evaluator.MatchesOwn.class));
 }

 @Test
 public void testParseComplexAttributeWithMultipleRegularExpressionSelectorsWithDifferentTypes() {
 QueryParser queryParser = new QueryParser("[title~='(test|Test)'][href$='.pdf']");
 Evaluator evaluator = queryParser.parse();
 Assert.assertThat(evaluator, instanceOf(CombiningEvaluator.And.class));
 List<Evaluator> andEvaluators = (List<Evaluator>) evaluator;
 Assert.assertEquals(2, andEvaluators.size());
 Assert.assertThat(andEvaluators.get(0), instanceOf(Evaluator.MatchesOwn.class));
 Assert.assertThat(andEvaluators.get(1), instanceOf(Evaluator.AttributeWithValueEnding.class));
 }
}