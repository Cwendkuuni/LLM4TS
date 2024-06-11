import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

 private QueryParser queryParser;

 @Before
 public void setup() {
 queryParser = new QueryParser("");
 }

 @Test
 public void testParseEmptyQuery() {
 Assert.assertNull(queryParser.parse());
 }

 @Test
 public void testParseRootSelector() {
 QueryParser parser = new QueryParser("*");
 Assert.assertEquals(new CombiningEvaluator.And(new Evaluator.AllElements()), parser.parse());
 }

 @Test
 public void testParseElementSelector() {
 QueryParser parser = new QueryParser("div");
 Assert.assertEquals(new Evaluator.Tag("div"), parser.parse().evals.get(0));
 }

 @Test
 public void testParseClassSelector() {
 QueryParser parser = new QueryParser(".class");
 Assert.assertEquals(new Evaluator.Class("class"), parser.parse().evals.get(0));
 }

 @Test
 public void testParseIdSelector() {
 QueryParser parser = new QueryParser("#id");
 Assert.assertEquals(new Evaluator.Id("id"), parser.parse().evals.get(0));
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser parser = new QueryParser("[title]");
 Assert.assertEquals(new Evaluator.Attribute("title"), parser.parse().evals.get(0));
 }

 @Test
 public void testParseAttributeWithValueSelector() {
 QueryParser parser = new QueryParser("[title='abc']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("title", "abc"), parser.parse().evals.get(0));
 }

 @Test
 public void testParseCombinedSelectors() {
 QueryParser parser = new QueryParser("div#id > .class");
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Tag("div"));
 expected.add(new Evaluator.Id("id"));
 expected.add(new CombiningEvaluator.And(new Evaluator.Tag(".class"), new StructuralEvaluator.ImmediateChild()));
 Assert.assertEquals(new CombiningEvaluator.And(expected.toArray(new Evaluator[0])), parser.parse());
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser("div#id > .class1:not(.class2):containsOwn(text):has(p)");
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Tag("div"));
 expected.add(new Evaluator.Id("id"));
 expected.add(new CombiningEvaluator.And(
 new Evaluator.Tag(".class1"),
 new StructuralEvaluator.ImmediateChild(),
 new StructuralEvaluator.Not(new Evaluator.Class("class2")),
 new StructuralEvaluator.Has(new Evaluator.Tag("p"))));
 Assert.assertEquals(new CombiningEvaluator.And(expected.toArray(new Evaluator[0])), parser.parse());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQuery() {
 new QueryParser("*[ab:c]"); // Invalid attribute name
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQuery2() {
 new QueryParser("div#id > .class[ab:c]"); // Invalid attribute name
 }
}