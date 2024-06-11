import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private String query;
 private QueryParser parser;

 @Before
 public void setup() {
 query = "div.class1 > div.class2 > input[type='text']:first-child";
 parser = new QueryParser(query);
 }

 @Test
 public void testParse() {
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testCombinator() {
 char combinator = '>';
 String subQuery = "div.class2";
 Evaluator rootEval = new StructuralEvaluator.ImmediateParent(new Evaluator.Class("class1"));
 Evaluator currentEval = rootEval;
 Evaluator newEval = new Evaluator.Class("class2");

 parser.combinator(combinator, subQuery);

 Evaluator result = parser.evals.get(parser.evals.size() - 1);
 assertNotNull(result);
 assertEquals(new CombiningEvaluator.And(newEval, currentEval), result);
 }

 @Test
 public void testConsumeSubQuery() {
 StringBuilder expected = new StringBuilder();
 expected.append("div.class1 > div.class3 > input[type='text']:first-child");
 expected.append(" div.class1 > div.class4 > input[type='text']:first-child");
 String result = parser.consumeSubQuery();
 assertEquals(expected.toString(), result);
 }

 @Test
 public void testFindElements() {
 String tagName = "div";
 String className = "class1";
 String id = "id1";
 String attributeName = "data-test";
 String attributeValue = "value1";
 int index = 1;

 parser.findElements();

 Evaluator result;
 if (tagName.equals(parser.tq.remainder())) {
 result = parser.evals.get(parser.evals.size() - 1);
 assertEquals(new Evaluator.Tag(tagName), result);
 } else if (className.equals(parser.tq.remainder())) {
 result = parser.evals.get(parser.evals.size() - 1);
 assertEquals(new Evaluator.Class(className), result);
 } else if (id.equals(parser.tq.remainder())) {
 result = parser.evals.get(parser.evals.size() - 1);
 assertEquals(new Evaluator.Id(id), result);
 } else if (attributeName.equals(parser.tq.remainder())) {
 parser.findElements();
 result = parser.evals.get(parser.evals.size() - 1);
 assertEquals(new Evaluator.Attribute(attributeName), result);

 parser.tq.consume(' '); // consume space after attributeName
 parser.tq.consume('='); // consume '='
 parser.tq.consumeWhitespace(); // consume space before attributeValue
 String attributeValueExpected = "value1";
 assertEquals(attributeValueExpected, parser.tq.remainder());
 parser.consumeSubQuery(); // consume attributeValue
 } else if (index >= 0) {
 result = parser.evals.get(parser.evals.size() - 1);
 assertEquals(new Evaluator.Index(index, false), result);
 }
 }
}