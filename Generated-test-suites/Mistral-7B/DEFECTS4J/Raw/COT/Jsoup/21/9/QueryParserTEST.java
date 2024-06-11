import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

public class QueryParserTest {

 private static Evaluator parseQuery(String query) {
 QueryParser parser = new QueryParser(query);
 return parser.parse();
 }

 @Test
 public void testParse_emptyQuery() {
 Evaluator evaluator = parseQuery("");
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @Test
 public void testParse_simpleTagSelect() {
 Evaluator evaluator = parseQuery("tag");
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("tag", evaluator.toString());
 }

 @Test
 public void testParse_classSelector() {
 Evaluator evaluator = parseQuery(".class");
 assertTrue(evaluator instanceof Evaluator.Class);
 assertEquals("class", evaluator.toString());
 }

 @Test
 public void testParse_idSelector() {
 Evaluator evaluator = parseQuery("#id");
 assertTrue(evaluator instanceof Evaluator.Id);
 assertEquals("id", evaluator.toString());
 }

 @Test
 public void testParse_multipleSelectors() {
 Evaluator evaluator = parseQuery("tag.class#id");
 List<Evaluator> evaluators = (List<Evaluator>) evaluator;
 assertEquals(3, evaluators.size());
 assertTrue(evaluators.get(0) instanceof Evaluator.Tag);
 assertTrue(evaluators.get(1) instanceof Evaluator.Class);
 assertTrue(evaluators.get(2) instanceof Evaluator.Id);
 }

 @Test
 public void testParse_childSelector() {
 Evaluator evaluator = parseQuery("tag > tag2");
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator tag1 = (Evaluator) ((CombiningEvaluator.And) evaluator).getEvaluator(0);
 assertTrue(tag1 instanceof Evaluator.Tag);
 Evaluator tag2 = (Evaluator) ((CombiningEvaluator.And) evaluator).getEvaluator(1);
 assertTrue(tag2 instanceof StructuralEvaluator.ImmediateChild);
 }

 @Test
 public void testParse_descendantSelector() {
 Evaluator evaluator = parseQuery("tag > tag2 > tag3");
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 Evaluator tag1 = (Evaluator) ((CombiningEvaluator.And) evaluator).getEvaluator(0);
 assertTrue(tag1 instanceof Evaluator.Tag);
 Evaluator tag2 = (Evaluator) ((CombiningEvaluator.And) evaluator).getEvaluator(1);
 assertTrue(tag2 instanceof CombiningEvaluator.And);
 Evaluator tag3 = (Evaluator) ((CombiningEvaluator.And) tag2).getEvaluator(1);
 assertTrue(tag3 instanceof Evaluator.Tag);
 }

 @Test
 public void testParse_attributeSelector() {
 Evaluator evaluator = parseQuery("tag[attr='value']");
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("tag[attr='value']", evaluator.toString());
 }

 @Test
 public void testParse_attributeSelectorWithOperator() {
 Evaluator evaluator = parseQuery("tag[attr='value']:not([attr='otherValue'])");
 assertTrue(evaluator instanceof StructuralEvaluator.Not);
 Evaluator attributeEvaluator = (Evaluator) ((StructuralEvaluator.Not) evaluator).getEvaluator();
 assertTrue(attributeEvaluator instanceof CombiningEvaluator.And);
 Evaluator attributeTrue = (Evaluator) ((CombiningEvaluator.And) attributeEvaluator).getEvaluator(0);
 assertTrue(attributeTrue instanceof Evaluator.Attribute);
 Evaluator attributeFalse = (Evaluator) ((CombiningEvaluator.And) attributeEvaluator).getEvaluator(1);
 assertTrue(attributeFalse instanceof Evaluator.AttributeWithValueNot);
 }

 @Test
 public void testParse_pseudoClassSelector_has() {
 Evaluator evaluator = parseQuery("tag:has(tag2)");
 assertTrue(evaluator instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testParse_pseudoClassSelector_contains() {
 Evaluator evaluator = parseQuery("tag:contains(text)");
 assertTrue(evaluator instanceof Evaluator.ContainsText);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_invalidQuery() {
 parseQuery("invalidQuery");
 }

}