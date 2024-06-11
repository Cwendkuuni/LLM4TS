import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private QueryParser parser;
 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 query = "div.class1 > p:first-child #id1";
 parser = new QueryParser(query);
 tokenQueue = new TokenQueue(query);
 }

 @Test
 public void testParse() {
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 String invalidQuery = "div.class1 > p:first-child #id1, p:first-child #id1";
 QueryParser parser = new QueryParser(invalidQuery);
 parser.parse(); // this should throw an exception
 }

 @Test
 public void testCombinator() {
 parser.consumeWhitespace();
 parser.combinator('>');
 Evaluator parentEval = new StructuralEvaluator.ImmediateParent();
 Evaluator childEval = parser.parse();
 Evaluator resultEval = parser.combineEvaluators(parentEval, childEval);
 assertEquals(parentEval, resultEval);
 }

 @Test
 public void testCombinatorSpace() {
 parser.consumeWhitespace();
 parser.combinator(' ');
 Evaluator parentEval = new StructuralEvaluator.Parent();
 Evaluator childEval = parser.parse();
 Evaluator resultEval = parser.combineEvaluators(parentEval, childEval);
 assertEquals(parentEval, resultEval);
 }

 @Test
 public void testCombinatorPlus() {
 parser.consumeWhitespace();
 parser.combinator('+');
 Evaluator previousSiblingEval = new StructuralEvaluator.ImmediatePreviousSibling();
 Evaluator currentSiblingEval = parser.parse();
 Evaluator resultEval = parser.combineEvaluators(previousSiblingEval, currentSiblingEval);
 assertEquals(previousSiblingEval, resultEval);
 }

 @Test
 public void testCombinatorTilde() {
 parser.consumeWhitespace();
 parser.combinator('~');
 Evaluator previousSiblingEval = new StructuralEvaluator.PreviousSibling();
 Evaluator currentSiblingEval = parser.parse();
 Evaluator resultEval = parser.combineEvaluators(previousSiblingEval, currentSiblingEval);
 assertEquals(previousSiblingEval, resultEval);
 }

 @Test
 public void testCombinatorComma() {
 parser.consumeWhitespace();
 parser.combinator(',');
 Evaluator leftOREval = new CombiningEvaluator.Or();
 Evaluator rightOREval = new CombiningEvaluator.Or();
 Evaluator resultOREval = parser.combineEvaluators(leftOREval, rightOREval);
 assertEquals(leftOREval, resultOREval);
 }

 @Test
 public void testFindElementById() {
 parser.findElements();
 parser.byId();
 Evaluator idEval = new Evaluator.Id("id1");
 Evaluator resultEval = parser.getCurrentEvaluator();
 assertEquals(idEval, resultEval);
 }

 @Test
 public void testFindElementByClass() {
 parser.findElements();
 parser.byClass();
 Evaluator classEval = new Evaluator.Class("class1");
 Evaluator resultEval = parser.getCurrentEvaluator();
 assertEquals(classEval, resultEval);
 }

 @Test
 public void testFindElementByTag() {
 parser.findElements();
 parser.byTag();
 Evaluator tagEval = new Evaluator.Tag("div");
 Evaluator resultEval = parser.getCurrentEvaluator();
 assertEquals(tagEval, resultEval);
 }

 @Test
 public void testFindElementByAttribute() {
 parser.consumeWhitespace();
 parser.consume('[');
 parser.consume("id");
 parser.consume(']');
 parser.consume('=');
 parser.consume("id1");
 Evaluator attributeEval = new Evaluator.AttributeWithValue("id", "id1");
 Evaluator resultEval = parser.parse();
 assertEquals(attributeEval, resultEval);
 }

 @Test
 public void testFindAllElements() {
 parser.findElements();
 parser.allElements();
 Evaluator allElementsEval = new Evaluator.AllElements();
 Evaluator resultEval = parser.getCurrentEvaluator();
 assertEquals(allElementsEval, resultEval);
 }

 @Test
 public void testIndexLessThan() {
 parser.consumeWhitespace();
 parser.consume(":lt");
 int index = parser.consumeIndex();
 Evaluator indexLessThanEval = new Evaluator.IndexLessThan(index);
 Evaluator resultEval = parser.parse();
 assertEquals(indexLessThanEval, resultEval);
 }

 @Test
 public void testIndexGreaterThan() {
 parser.consumeWhitespace();
 parser.consume(":gt");
 int index = parser.consumeIndex();
 Evaluator indexGreaterThanEval = new Evaluator.IndexGreaterThan(index);
 Evaluator resultEval = parser.parse();
 assertEquals(indexGreaterThanEval, resultEval);
 }

 @Test
 public void testIndexEquals() {
 parser.consumeWhitespace();
 parser.consume(":eq");
 int index = parser.consumeIndex();
 Evaluator indexEqualsEval = new Evaluator.IndexEquals(index);
 Evaluator resultEval = parser.parse();
 assertEquals(indexEqualsEval, resultEval);
 }

 @Test
 public void testHas() {
 parser.consumeWhitespace();
 parser.consume(":has");
 String subQuery = "p:first-child";
 QueryParser subQueryParser = new QueryParser(subQuery);
 Evaluator subQueryEval = subQueryParser.parse();
 Evaluator hasEval = new StructuralEvaluator.Has(subQueryEval);
 Evaluator resultEval = parser.parse();
 assertEquals(hasEval, resultEval);
 }

 @Test
 public void testContains() {
 parser.consumeWhitespace();
 parser.consume(":contains");
 String text = "text";
 Evaluator containsEval = new Evaluator.ContainsText(text);
 Evaluator resultEval = parser.parse();
 assertEquals(containsEval, resultEval);
 }

 @Test
 public void testContainsOwn() {
 parser.consumeWhitespace();
 parser.consume(":containsOwn");
 String text = "text";
 Evaluator containsOwnEval = new Evaluator.ContainsOwnText(text);
 Evaluator resultEval = parser.parse();
 assertEquals(containsOwnEval, resultEval);
 }

 @Test
 public void testContainsData() {
 parser.consumeWhitespace();
 parser.consume(":containsData");
 String data = "data";
 Evaluator containsDataEval = new Evaluator.ContainsData(data);
 Evaluator resultEval = parser.parse();
 assertEquals(containsDataEval, resultEval);
 }

 @Test
 public void testMatches() {
 parser.consumeWhitespace();
 parser.consume(":matches");
 String regex = "regex";
 Evaluator matchesEval = new Evaluator.Matches(Pattern.compile(regex));
 Evaluator resultEval = parser.parse();
 assertEquals(matchesEval, resultEval);
 }

 @Test
 public void testMatchesOwn() {
 parser.consumeWhitespace();
 parser.consume(":matchesOwn");
 String regex = "regex";
 Evaluator matchesOwnEval = new Evaluator.MatchesOwn(Pattern.compile(regex));
 Evaluator resultEval = parser.parse();
 assertEquals(matchesOwnEval, resultEval);
 }

 @Test
 public void testNot() {
 parser.consumeWhitespace();
 parser.consume(":not");
 String subQuery = "p:first-child";
 QueryParser subQueryParser = new QueryParser(subQuery);
 Evaluator subQueryEval = subQueryParser.parse();
 Evaluator notEval = new StructuralEvaluator.Not(subQueryEval);
 Evaluator resultEval = parser.parse();
 assertEquals(notEval, resultEval);
 }

 @Test
 public void testNthChild() {
 parser.consumeWhitespace();
 parser.consume(":nth-child");
 boolean backwards = false;
 boolean ofType = false;
 int index = parser.consumeIndex();
 Evaluator nthChildEval = new Evaluator.IsNthChild(index, backwards, ofType);
 Evaluator resultEval = parser.parse();
 assertEquals(nthChildEval, resultEval);
 }

 @Test
 public void testNthLastChild() {
 parser.consumeWhitespace();
 parser.consume(":nth-last-child");
 boolean backwards = true;
 boolean ofType = false;
 int index = parser.consumeIndex();
 Evaluator nthLastChildEval = new Evaluator.IsNthLastChild(index, backwards, ofType);
 Evaluator resultEval = parser.parse();
 assertEquals(nthLastChildEval, resultEval);
 }

 @Test
 public void testNthOfType() {
 parser.consumeWhitespace();
 parser.consume(":nth-of-type");
 boolean backwards = false;
 boolean ofType = true;
 int index = parser.consumeIndex();
 Evaluator nthOfTypeEval = new Evaluator.IsNthOfType(index, backwards, ofType);
 Evaluator resultEval = parser.parse();
 assertEquals(nthOfTypeEval, resultEval);
 }

 @Test
 public void testNthLastOfType() {
 parser.consumeWhitespace();
 parser.consume(":nth-last-of-type");
 boolean backwards = true;
 boolean ofType = true;
 int index = parser.consumeIndex();
 Evaluator nthLastOfTypeEval = new Evaluator.IsNthLastOfType(index, backwards, ofType);
 Evaluator resultEval = parser.parse();
 assertEquals(nthLastOfTypeEval, resultEval);
 }

 @Test
 public void testIsFirstChild() {
 parser.findElements();
 parser.findElements();
 parser.consumeWhitespace();
 parser.combinator('>');
 Evaluator isFirstChildEval = new Evaluator.IsFirstChild();
 Evaluator resultEval = parser.parse();
 assertEquals(isFirstChildEval, resultEval);
 }

 @Test
 public void testIsLastChild() {
 parser.findElements();
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isLastChildEval = new Evaluator.IsLastChild();
 Evaluator resultEval = parser.parse();
 assertEquals(isLastChildEval, resultEval);
 }

 @Test
 public void testIsFirstOfType() {
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isFirstOfTypeEval = new Evaluator.IsFirstOfType();
 Evaluator resultEval = parser.parse();
 assertEquals(isFirstOfTypeEval, resultEval);
 }

 @Test
 public void testIsLastOfType() {
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isLastOfTypeEval = new Evaluator.IsLastOfType();
 Evaluator resultEval = parser.parse();
 assertEquals(isLastOfTypeEval, resultEval);
 }

 @Test
 public void testIsOnlyChild() {
 parser.findElements();
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isOnlyChildEval = new Evaluator.IsOnlyChild();
 Evaluator resultEval = parser.parse();
 assertEquals(isOnlyChildEval, resultEval);
 }

 @Test
 public void testIsOnlyOfType() {
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isOnlyOfTypeEval = new Evaluator.IsOnlyOfType();
 Evaluator resultEval = parser.parse();
 assertEquals(isOnlyOfTypeEval, resultEval);
 }

 @Test
 public void testIsEmpty() {
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isEmptyEval = new Evaluator.IsEmpty();
 Evaluator resultEval = parser.parse();
 assertEquals(isEmptyEval, resultEval);
 }

 @Test
 public void testIsRoot() {
 parser.findElements();
 parser.consumeWhitespace();
 Evaluator isRootEval = new Evaluator.IsRoot();
 Evaluator resultEval = parser.parse();
 assertEquals(isRootEval, resultEval);
 }
}