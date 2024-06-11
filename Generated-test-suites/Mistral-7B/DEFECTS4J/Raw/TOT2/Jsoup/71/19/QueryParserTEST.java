import org.jsoup.parser.QueryParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private static final String BASIC_QUERY = "div.class1";
 private static final String BASIC_QUERY_WITH_ID = "div#id1.class1";
 private static final String BASIC_QUERY_WITH_ATTRIBUTE = "div[data-attr='value']";
 private static final String BASIC_QUERY_WITH_TAG = "ul > li:nth-child(2)";
 private static final String BASIC_QUERY_WITH_COMbinator = "ul > li > a";
 private static final String BASIC_QUERY_WITH_PSEUDO_CLASS = "li:last-child";

 @Test
 public void testBasicQueryParse() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY);
 Evaluator queryResult = queryParser.parse();
 assertNotNull(queryResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyQueryParse() {
 QueryParser queryParser = new QueryParser("");
 }

 @Test
 public void testBasicQueryEvaluate() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY);
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testBasicQueryWithIdEvaluate() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY_WITH_ID);
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testBasicQueryWithAttributeEvaluate() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY_WITH_ATTRIBUTE);
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testBasicQueryWithTagEvaluate() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY_WITH_TAG);
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testBasicQueryWithCombinatorEvaluate() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY_WITH_COMbinator);
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testBasicQueryWithPseudoClassEvaluate() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY_WITH_PSEUDO_CLASS);
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParse() {
 QueryParser queryParser = new QueryParser("div[ data-attr=value ]");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParseWithUnexpectedToken() {
 QueryParser queryParser = new QueryParser("div:lt(>10");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParseWithUnknownCombinator() {
 QueryParser queryParser = new QueryParser("div~>li");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParseWithUnknownPseudoClass() {
 QueryParser queryParser = new QueryParser("div:invalidPseudoClass");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParseWithMissingBrackets() {
 QueryParser queryParser = new QueryParser("div[data-attr=value class1");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParseWithMissingQuotes() {
 QueryParser queryParser = new QueryParser("div[data-attrvalue]");
 }

 @Test
 public void testCombineElements() {
 QueryParser queryParser = new QueryParser("ul > li > a > input[type='text']");
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testCombineElementsWithOr() {
 QueryParser queryParser = new QueryParser("div.class1, div.class2");
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches any of the elements
 List<Evaluator> evaluators = ((CombiningEvaluator.Or) queryResult).getEvaluators();
 for (Evaluator evaluator : evaluators) {
 boolean result = evaluator.evaluate();
 assertEquals(true, result);
 }
 }

 @Test
 public void testCombineElementsWithAnd() {
 QueryParser queryParser = new QueryParser("div > ul > li > a:containsOwn(text)");
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query matches
 boolean result = queryResult.evaluate();
 assertEquals(true, result);
 }

 @Test
 public void testCombineElementsWithNegation() {
 QueryParser queryParser = new QueryParser("div:not(.class1)");
 Evaluator queryResult = queryParser.parse();
 // Assuming we have a way to evaluate the queryResult against some DOM
 // and this method returns true if the query does not match
 List<Evaluator> evaluators = ((StructuralEvaluator.Not) queryResult).getElements();
 Evaluator evaluator = evaluators.get(0);
 boolean result = evaluator.evaluate();
 assertEquals(false, result);
 }
}