import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import java.util.List;

public class QueryParserTest {

 private static final String BASIC_QUERY = "div.class1.class2";
 private static final String ID_QUERY = "#id";
 private static final String TAG_QUERY = "tag";
 private static final String ATTRIBUTE_QUERY = "[attribute='value']";
 private static final String ATTRIBUTE_QUERY_WITH_OPERATOR = "[attribute!=value]";
 private static final String ATTRIBUTE_QUERY_WITH_START_CONTAINS = "[attribute^='value']";
 private static final String ATTRIBUTE_QUERY_WITH_END_CONTAINS = "[attribute$='value']";
 private static final String ATTRIBUTE_QUERY_WITH_CONTAINS = "[attribute*='value']";
 private static final String ATTRIBUTE_QUERY_WITH_REGEX = "[attribute~='value']";
 private static final String INDEX_QUERY = ":nth-child(1)";
 private static final String INDEX_QUERY_WITH_PLUS = ":nth-child(+1)";
 private static final String INDEX_QUERY_WITH_MINUS = ":nth-child(-1)";
 private static final String INDEX_QUERY_WITH_OF_TYPE = ":nth-of-type(1)";
 private static final String INDEX_QUERY_WITH_OF_TYPE_PLUS = ":nth-of-type(+1)";
 private static final String INDEX_QUERY_WITH_OF_TYPE_MINUS = ":nth-of-type(-1)";
 private static final String PSEUDO_QUERY_HAS = ":has(.child)";
 private static final String PSEUDO_QUERY_CONTAINS = ":contains(text)";
 private static final String PSEUDO_QUERY_CONTAINS_OWN = ":containsOwn(text)";
 private static final String PSEUDO_QUERY_CONTAINS_DATA = ":containsData(data)";
 private static final String PSEUDO_QUERY_MATCHES = ":matches(regex)";
 private static final String PSEUDO_QUERY_MATCHES_OWN = ":matchesOwn(regex)";
 private static final String PSEUDO_QUERY_NOT = ":not(selector)";

 @Test
 public void testParseBasicQuery() {
 QueryParser parser = new QueryParser(BASIC_QUERY);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Assert.assertEquals(1, evaluators.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("invalidQuery");
 }

 @Test
 public void testParseIdQuery() {
 QueryParser parser = new QueryParser(ID_QUERY);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator idEvaluator = evaluators.get(0);
 Assert.assertTrue(idEvaluator instanceof Evaluator.Id);
 }

 @Test
 public void testParseTagQuery() {
 QueryParser parser = new QueryParser(TAG_QUERY);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator tagEvaluator = evaluators.get(0);
 Assert.assertTrue(tagEvaluator instanceof Evaluator.Tag);
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator attributeEvaluator = evaluators.get(0);
 Assert.assertTrue(attributeEvaluator instanceof Evaluator.Attribute);
 }

 @Test
 public void testParseAttributeQueryWithOperator() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY_WITH_OPERATOR);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator attributeEvaluator = evaluators.get(0);
 Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValueNot);
 }

 @Test
 public void testParseAttributeQueryWithStartContains() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY_WITH_START_CONTAINS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator attributeEvaluator = evaluators.get(0);
 Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValueStarting);
 }

 @Test
 public void testParseAttributeQueryWithEndContains() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY_WITH_END_CONTAINS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator attributeEvaluator = evaluators.get(0);
 Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValueEnding);
 }

 @Test
 public void testParseAttributeQueryWithContains() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY_WITH_CONTAINS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator attributeEvaluator = evaluators.get(0);
 Assert.assertTrue(attributeEvaluator instanceof Evaluator.AttributeWithValueContaining);
 }

 @Test
 public void testParseAttributeQueryWithRegex() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY_WITH_REGEX);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator attributeEvaluator = evaluators.get(0);
 Assert.assertTrue(attributeEvaluator instanceof Evaluator.Matches);
 }

 @Test
 public void testParseIndexQuery() {
 QueryParser parser = new QueryParser(INDEX_QUERY);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator indexEvaluator = evaluators.get(0);
 Assert.assertTrue(indexEvaluator instanceof Evaluator.IsNthChild);
 }

 @Test
 public void testParseIndexQueryWithPlus() {
 QueryParser parser = new QueryParser(INDEX_QUERY_WITH_PLUS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator indexEvaluator = evaluators.get(0);
 Assert.assertTrue(indexEvaluator instanceof Evaluator.IsNthChild);
 }

 @Test
 public void testParseIndexQueryWithMinus() {
 QueryParser parser = new QueryParser(INDEX_QUERY_WITH_MINUS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator indexEvaluator = evaluators.get(0);
 Assert.assertTrue(indexEvaluator instanceof Evaluator.IsNthChild);
 }

 @Test
 public void testParseIndexQueryWithOfType() {
 QueryParser parser = new QueryParser(INDEX_QUERY_WITH_OF_TYPE);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator indexEvaluator = evaluators.get(0);
 Assert.assertTrue(indexEvaluator instanceof Evaluator.IsNthOfType);
 }

 @Test
 public void testParseIndexQueryWithOfTypePlus() {
 QueryParser parser = new QueryParser(INDEX_QUERY_WITH_OF_TYPE_PLUS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator indexEvaluator = evaluators.get(0);
 Assert.assertTrue(indexEvaluator instanceof Evaluator.IsNthOfType);
 }

 @Test
 public void testParseIndexQueryWithOfTypeMinus() {
 QueryParser parser = new QueryParser(INDEX_QUERY_WITH_OF_TYPE_MINUS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator indexEvaluator = evaluators.get(0);
 Assert.assertTrue(indexEvaluator instanceof Evaluator.IsNthOfType);
 }

 @Test
 public void testParsePseudoQueryHas() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_HAS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator hasEvaluator = evaluators.get(0);
 Assert.assertTrue(hasEvaluator instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testParsePseudoQueryContains() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_CONTAINS);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator containsEvaluator = evaluators.get(0);
 Assert.assertTrue(containsEvaluator instanceof Evaluator.ContainsText);
 }

 @Test
 public void testParsePseudoQueryContainsOwn() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_CONTAINS_OWN);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator containsOwnEvaluator = evaluators.get(0);
 Assert.assertTrue(containsOwnEvaluator instanceof Evaluator.ContainsOwnText);
 }

 @Test
 public void testParsePseudoQueryContainsData() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_CONTAINS_DATA);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator containsDataEvaluator = evaluators.get(0);
 Assert.assertTrue(containsDataEvaluator instanceof Evaluator.ContainsData);
 }

 @Test
 public void testParsePseudoQueryMatches() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_MATCHES);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator matchesEvaluator = evaluators.get(0);
 Assert.assertTrue(matchesEvaluator instanceof Evaluator.Matches);
 }

 @Test
 public void testParsePseudoQueryMatchesOwn() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_MATCHES_OWN);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator matchesOwnEvaluator = evaluators.get(0);
 Assert.assertTrue(matchesOwnEvaluator instanceof Evaluator.MatchesOwn);
 }

 @Test
 public void testParsePseudoQueryNot() {
 QueryParser parser = new QueryParser(PSEUDO_QUERY_NOT);
 Evaluator evaluator = parser.parse();
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(evaluator);
 Evaluator notEvaluator = evaluators.get(0);
 Assert.assertTrue(notEvaluator instanceof StructuralEvaluator.Not);
 }
}