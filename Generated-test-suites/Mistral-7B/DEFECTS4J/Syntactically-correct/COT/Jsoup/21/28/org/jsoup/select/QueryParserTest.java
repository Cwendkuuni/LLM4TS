import org.junit.Assert;
import org.junit.Test;
import org.jsoup.select.QueryParser;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 private String query;
 private QueryParser parser;

 @Test
 public void testParse_EmptyQuery() {
 query = "";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParse_SingleTag() {
 query = "div";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
 Assert.assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParse_ClassSelector() {
 query = ".test";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
 Assert.assertEquals(".test", evaluator.toString());
 }

 @Test
 public void testParse_IDSelector() {
 query = "#test";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
 Assert.assertEquals("#test", evaluator.toString());
 }

 @Test
 public void testParse_CombinedSelectors() {
 query = "div.test > .another";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
 CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
 Assert.assertEquals(2, and.getEvaluators().size());
 Evaluator first = and.getEvaluators().get(0);
 Assert.assertEquals(Evaluator.Tag.class, first.getClass());
 Assert.assertEquals("div", first.toString());
 Evaluator second = and.getEvaluators().get(1);
 Assert.assertTrue(second instanceof CombiningEvaluator.And);
 CombiningEvaluator.And secondAnd = (CombiningEvaluator.And) second;
 Assert.assertEquals(2, secondAnd.getEvaluators().size());
 Evaluator tag = secondAnd.getEvaluators().get(0);
 Assert.assertEquals(Evaluator.Tag.class, tag.getClass());
 Assert.assertEquals(".another", tag.toString());
 Evaluator classSelector = secondAnd.getEvaluators().get(1);
 Assert.assertEquals(Evaluator.Class.class, classSelector.getClass());
 Assert.assertEquals(".test", classSelector.toString());
 }

 @Test
 public void testParse_AttributeSelector() {
 query = "[href]";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof Evaluator.Attribute);
 Assert.assertEquals("[href]", evaluator.toString());
 }

 @Test
 public void testParse_NegatedAttributeSelector() {
 query = "[?!href]";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
 Assert.assertEquals("[?!href]", evaluator.toString());
 }

 @Test
 public void testParse_AttributeSelectorWithValue() {
 query = "[href='link']";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
 Assert.assertEquals("[href='link']", evaluator.toString());
 }

 @Test
 public void testParse_PseudoClass_Has() {
 query = ":has(p)";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testParse_PseudoClass_Not() {
 query = ":not(p)";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof StructuralEvaluator.Not);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_InvalidQuery() {
 query = "invalidQuery";
 parser = new QueryParser(query);
 parser.parse();
 }

 @Test
 public void testParse_ComplexQuery() {
 query = "div.test > div.another > div.yet.another > p:containsOwn(Java)";
 parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 Assert.assertTrue(evaluator instanceof CombiningEvaluator.And);
 CombiningEvaluator.And and = (CombiningEvaluator.And) evaluator;
 int evaluatorsSize = and.getEvaluators().size();
 Assert.assertEquals(5, evaluatorsSize);
 Evaluator first = and.getEvaluators().get(0);
 Assert.assertEquals(Evaluator.Tag.class, first.getClass());
 Assert.assertEquals("div", first.toString());
 Evaluator second = and.getEvaluators().get(1);
 Assert.assertTrue(second instanceof CombiningEvaluator.And);
 CombiningEvaluator.And secondAnd = (CombiningEvaluator.And) second;
 Assert.assertEquals(3, secondAnd.getEvaluators().size());
 Evaluator tag2 = secondAnd.getEvaluators().get(0);
 Assert.assertEquals(Evaluator.Tag.class, tag2.getClass());
 Assert.assertEquals("div", tag2.toString());
 Evaluator classSelector2 = secondAnd.getEvaluators().get(1);
 Assert.assertEquals(Evaluator.Class.class, classSelector2.getClass());
 Assert.assertEquals(".another", classSelector2.toString());
 Evaluator tag3 = secondAnd.getEvaluators().get(2);
 Assert.assertEquals(Evaluator.Tag.class, tag3.getClass());
 Assert.assertEquals("div", tag3.toString());
 Evaluator third = and.getEvaluators().get(3);
 Assert.assertTrue(third instanceof CombiningEvaluator.And);
 CombiningEvaluator.And thirdAnd = (CombiningEvaluator.And) third;
 Assert.assertEquals(2, thirdAnd.getEvaluators().size());
 Evaluator classSelector3 = thirdAnd.getEvaluators().get(0);
 Assert.assertEquals(Evaluator.Class.class, classSelector3.getClass());
 Assert.assertEquals(".yet.another", classSelector3.toString());
 Evaluator last = and.getEvaluators().get(4);
 Assert.assertTrue(last instanceof Evaluator.MatchesOwn);
 Assert.assertEquals(":containsOwn(Java)", last.toString());
 }

}