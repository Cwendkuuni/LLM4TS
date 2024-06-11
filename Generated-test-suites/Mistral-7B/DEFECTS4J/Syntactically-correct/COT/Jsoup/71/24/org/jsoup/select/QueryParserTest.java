import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;

 @Before
 public void setup() {
 queryParser = new QueryParser("");
 tokenQueue = new TokenQueue("");
 }

 /**
 * Test constructor
 */
 @Test
 public void testQueryParserConstructor() {
 String query = "div p";
 queryParser = new QueryParser(query);
 assertEquals("div p", queryParser.query);
 }

 /**
 * Test parse method with empty query
 */
 @Test
 public void testParseEmptyQuery() {
 TokenQueue expectedTokenQueue = new TokenQueue("");
 assertEquals(expectedTokenQueue, queryParser.parse());
 }

 /**
 * Test parse method with simple query
 */
 @Test
 public void testParseSimpleQuery() {
 String query = "div p";
 queryParser = new QueryParser(query);
 TokenQueue expectedTokenQueue = new TokenQueue("div", "p");
 assertEquals(expectedTokenQueue, queryParser.parse());
 }

 /**
 * Test parse method with complex query
 */
 @Test
 public void testParseComplexQuery() {
 String query = "div > p:first-child:has(a)";
 queryParser = new QueryParser(query);
 CombiningEvaluator and = new CombiningEvaluator.And(
 new StructuralEvaluator.ChildOf(new Evaluator.Tag("div"), new Evaluator.IsFirstChild()),
 new StructuralEvaluator.Has(new Evaluator.Tag("a"))
 );
 assertEquals(and, queryParser.parse());
 }

 /**
 * Test combinator method with invalid combinator
 */
 @Test(expected = Selector.SelectorParseException.class)
 public void testCombinatorInvalidCombinator() {
 char combinator = 'Z';
 queryParser.combinator(combinator);
 }

 /**
 * Test combinator method with valid combinators
 */
 @Test
 public void testCombinatorValidCombinators() {
 char combinator = '>';
 queryParser.query = "div > p";
 queryParser.tq = new TokenQueue("div", ">", "p");
 StructuralEvaluator.ImmediateParent immediateParent = new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p"));
 assertEquals(immediateParent, queryParser.combinator(combinator));
 }

 /**
 * Test consumeSubQuery method with empty query
 */
 @Test
 public void testConsumeSubQueryEmptyQuery() {
 StringBuilder expectedStringBuilder = new StringBuilder("");
 assertEquals(expectedStringBuilder.toString(), queryParser.consumeSubQuery());
 }

 /**
 * Test consumeSubQuery method with simple query
 */
 @Test
 public void testConsumeSubQuerySimpleQuery() {
 StringBuilder expectedStringBuilder = new StringBuilder("div p");
 assertEquals(expectedStringBuilder.toString(), queryParser.consumeSubQuery());
 }

 /**
 * Test consumeSubQuery method with complex query
 */
 @Test
 public void testConsumeSubQueryComplexQuery() {
 StringBuilder expectedStringBuilder = new StringBuilder("div > p:first-child:has(a)");
 assertEquals(expectedStringBuilder.toString(), queryParser.consumeSubQuery());
 }

 /**
 * Test findElements method with empty query
 */
 @Test
 public void testFindElementsEmptyQuery() {
 }

 /**
 * Test findElements method with id selector
 */
 @Test
 public void testFindElementsByIdSelector() {
 queryParser.query = "#myId";
 queryParser.findElements();
 assertEquals(new Evaluator.Id("myId"), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with class selector
 */
 @Test
 public void testFindElementsByClassSelector() {
 queryParser.query = ".myClass";
 queryParser.findElements();
 assertEquals(new Evaluator.Class("myClass"), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with tag selector
 */
 @Test
 public void testFindElementsByTagSelector() {
 queryParser.query = "div";
 queryParser.findElements();
 assertEquals(new Evaluator.Tag("div"), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with attribute selector
 */
 @Test
 public void testFindElementsByAttributeSelector() {
 queryParser.query = "[title]";
 queryParser.findElements();
 assertEquals(new Evaluator.Attribute("title"), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with index selector
 */
 @Test
 public void testFindElementsByIndexSelector() {
 queryParser.query = ":nth-child(2)";
 queryParser.findElements();
 assertEquals(new Evaluator.IsNthChild(1, 2), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with pseudo selector :contains
 */
 @Test
 public void testFindElementsByContainsSelector() {
 queryParser.query = ":contains(text)";
 queryParser.findElements();
 assertEquals(new Evaluator.ContainsText("text"), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with pseudo selector :matches
 */
 @Test
 public void testFindElementsByMatchesSelector() {
 queryParser.query = ":matches(regex)";
 queryParser.findElements();
 assertEquals(new Evaluator.Matches(Pattern.compile("regex")), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with pseudo selector :has
 */
 @Test
 public void testFindElementsByHasSelector() {
 queryParser.query = ":has(el)";
 queryParser.findElements();
 assertEquals(new StructuralEvaluator.Has(parse("el")), queryParser.evals.get(0));
 }

 /**
 * Test findElements method with pseudo selector :not
 */
 @Test
 public void testFindElementsByNotSelector() {
 queryParser.query = ":not(selector)";
 queryParser.findElements();
 assertEquals(new StructuralEvaluator.Not(parse("selector")), queryParser.evals.get(0));
 }
}