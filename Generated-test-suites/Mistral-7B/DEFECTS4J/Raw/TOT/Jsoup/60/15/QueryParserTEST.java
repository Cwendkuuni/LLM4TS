import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.QueryParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNull(evaluator);
 }

 @Test
 public void testParseSimpleQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParseQueryWithId() {
 QueryParser parser = new QueryParser("#myId");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 }

 @Test
 public void testParseQueryWithClass() {
 QueryParser parser = new QueryParser(".myClass");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test
 public void testParseQueryWithTag() {
 QueryParser parser = new QueryParser("p");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParseQueryWithCombination() {
 QueryParser parser = new QueryParser("div > p");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 Evaluator target = ((CombiningEvaluator.And) evaluator).getLeft();
 assertEquals(Evaluator.Tag.class, target.getClass());
 assertEquals("div", target.toString());
 target = ((CombiningEvaluator.And) evaluator).getRight();
 assertEquals(Evaluator.Tag.class, target.getClass());
 assertEquals("p", target.toString());
 }

 @Test
 public void testParseQueryWithAttribute() {
 QueryParser parser = new QueryParser("input[type=text]");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 Evaluator target = ((CombiningEvaluator.And) evaluator).getLeft();
 assertEquals(Evaluator.Attribute.class, target.getClass());
 assertEquals("input", target.toString());
 target = ((CombiningEvaluator.And) evaluator).getRight();
 assertEquals(Evaluator.AttributeWithValue.class, target.getClass());
 assertEquals("type", target.getAttrName());
 assertEquals("text", target.getValue());
 }

 @Test
 public void testParseQueryWithPseudoClass() {
 QueryParser parser = new QueryParser("a:hover");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(PseudoClassEvaluator.class, evaluator.getClass());
 assertEquals("a", evaluator.toString());
 assertEquals("hover", evaluator.getPseudoClass());
 }

 @Test
 public void testParseQueryWithNegation() {
 QueryParser parser = new QueryParser("div:not(.myClass)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 Evaluator target = ((StructuralEvaluator.Not) evaluator).getInnerEvaluator();
 assertEquals(CombiningEvaluator.And.class, target.getClass());
 Evaluator innerTarget = ((CombiningEvaluator.And) target).getRight();
 assertEquals(Evaluator.Class.class, innerTarget.getClass());
 assertEquals(".myClass", innerTarget.toString());
 }

 @Test
 public void testParseQueryWithMultipleSelectors() {
 QueryParser parser = new QueryParser("h1, p, input[type=text]:first-child");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
 List<Evaluator> targets = ((CombiningEvaluator.Or) evaluator).getEvaluators();
 assertEquals(3, targets.size());
 Evaluator h1Target = targets.get(0);
 assertEquals(Evaluator.Tag.class, h1Target.getClass());
 assertEquals("h1", h1Target.toString());
 Evaluator pTarget = targets.get(1);
 assertEquals(Evaluator.Tag.class, pTarget.getClass());
 assertEquals("p", pTarget.toString());
 Evaluator inputTarget = targets.get(2);
 assertEquals(CombiningEvaluator.And.class, inputTarget.getClass());
 Evaluator innerInputTarget = ((CombiningEvaluator.And) inputTarget).getLeft();
 assertEquals(Evaluator.Tag.class, innerInputTarget.getClass());
 assertEquals("input", innerInputTarget.toString());
 Evaluator inputAttributeTarget = ((CombiningEvaluator.And) inputTarget).getRight();
 assertEquals(Evaluator.AttributeWithValue.class, inputAttributeTarget.getClass());
 assertEquals("type", inputAttributeTarget.getAttrName());
 assertEquals("text", inputAttributeTarget.getValue());
 Evaluator isFirstChildTarget = ((CombiningEvaluator.And) inputAttributeTarget).getRight();
 assertEquals(Evaluator.IsFirstChild.class, isFirstChildTarget.getClass());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("div > <p");
 QueryParser.parse(parser.query());
 }
}