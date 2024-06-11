import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private static final String TEST_QUERY = "body > div.testClass:not(.excluded):has(:contains(Hello)):nth-child(2)";

 @Test
 public void testParse() {
 QueryParser queryParser = new QueryParser(TEST_QUERY);
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidQuery() {
 QueryParser queryParser = new QueryParser("invalidQuery");
 }

 @Test
 public void testCombinator() {
 QueryParser queryParser = new QueryParser("div > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator rootEval = test.getChildren().get(0);
 Evaluator childEval = test.getChildren().get(1);
 assertEquals(rootEval.getClass(), StructuralEvaluator.Root.class);
 assertEquals(childEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithId() {
 QueryParser queryParser = new QueryParser("#testId > div > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator idEval = test.getChildren().get(0);
 assertEquals(idEval.getClass(), Evaluator.Id.class);
 Evaluator divEval = test.getChildren().get(1);
 Evaluator pEval = test.getChildren().get(2);
 assertEquals(divEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithClass() {
 QueryParser queryParser = new QueryParser("div.testClass > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), StructuralEvaluator.HasClass.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithAttribute() {
 QueryParser queryParser = new QueryParser("div[data-test=\"test\"] > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.Attribute.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithContainers() {
 QueryParser queryParser = new QueryParser("div > div > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator div1Eval = test.getChildren().get(0);
 Evaluator div2Eval = test.getChildren().get(1);
 Evaluator pEval = test.getChildren().get(2);
 assertEquals(div1Eval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(div2Eval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithMultipleAttributes() {
 QueryParser queryParser = new QueryParser("div[data-test=\"test1\"][data-test2=\"test2\"] > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.And.class);
 assertEquals(divEval.getChildren().get(0).getClass(), Evaluator.Attribute.class);
 assertEquals(divEval.getChildren().get(1).getClass(), Evaluator.Attribute.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithMultipleSelectors() {
 QueryParser queryParser = new QueryParser("div > p, div > span");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 List<Evaluator> children = test.getChildren();
 assertEquals(children.size(), 2);
 Evaluator pEval = children.get(0);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 Evaluator spanEval = children.get(1);
 assertEquals(spanEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithMultipleCombinators() {
 QueryParser queryParser = new QueryParser("div > div > p > span");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator div1Eval = test.getChildren().get(0);
 Evaluator div2Eval = test.getChildren().get(1);
 Evaluator pEval = test.getChildren().get(2);
 Evaluator spanEval = test.getChildren().get(3);
 assertEquals(div1Eval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(div2Eval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(spanEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithIndex() {
 QueryParser queryParser = new QueryParser("div:nth-child(2) > p:nth-child(3)");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.IsNthChild.class);
 assertEquals(pEval.getClass(), Evaluator.IsNthChild.class);
 }

 @Test
 public void testCombinatorWithPseudoClass() {
 QueryParser queryParser = new QueryParser("div:last-child > p:first-child");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.IsLastOfType.class);
 assertEquals(pEval.getClass(), Evaluator.IsFirstChild.class);
 }

 @Test
 public void testCombinatorWithPseudoClassAndAttribute() {
 QueryParser queryParser = new QueryParser("div[data-test=\"test\"]:hover > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.And.class);
 assertEquals(divEval.getChildren().get(0).getClass(), Evaluator.Attribute.class);
 assertEquals(divEval.getChildren().get(1).getClass(), Evaluator.PseudoClass.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithPseudoClassAndElement() {
 QueryParser queryParser = new QueryParser("div:hover > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.PseudoClass.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithPseudoClassAndElementAndAttribute() {
 QueryParser queryParser = new QueryParser("div[data-test=\"test\"]:hover > p[data-test2=\"test2\"]");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.And.class);
 assertEquals(divEval.getChildren().get(0).getClass(), Evaluator.PseudoClass.class);
 assertEquals(divEval.getChildren().get(1).getClass(), Evaluator.Attribute.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 assertEquals(pEval.getChildren().get(0).getClass(), Evaluator.Attribute.class);
 }

 @Test
 public void testCombinatorWithPseudoClassAndMultipleElements() {
 QueryParser queryParser = new QueryParser("div:hover > p, div:hover > span");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 List<Evaluator> children = test.getChildren();
 assertEquals(children.size(), 2);
 Evaluator pEval = children.get(0);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 Evaluator spanEval = children.get(1);
 assertEquals(spanEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

 @Test
 public void testCombinatorWithPseudoClassAndMultiplePseudoClasses() {
 QueryParser queryParser = new QueryParser("div:hover:focus > p");
 evaluator test = queryParser.parse();
 assertNotNull(test);
 Evaluator divEval = test.getChildren().get(0);
 Evaluator pEval = test.getChildren().get(1);
 assertEquals(divEval.getClass(), Evaluator.And.class);
 assertEquals(divEval.getChildren().get(0).getClass(), Evaluator.PseudoClass.class);
 assertEquals(divEval.getChildren().get(1).getClass(), Evaluator.PseudoClass.class);
 assertEquals(pEval.getClass(), StructuralEvaluator.ImmediateChild.class);
 }

}