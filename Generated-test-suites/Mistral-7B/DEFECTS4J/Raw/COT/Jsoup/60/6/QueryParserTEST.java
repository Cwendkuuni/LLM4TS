import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;
 private Evaluator expectedResult;

 @Before
 public void setup() {
 queryParser = new QueryParser("");
 tokenQueue = new TokenQueue("");
 }

 @Test
 public void testParseEmptyQuery() {
 Evaluator result = queryParser.parse();
 assertEquals(Evaluator.class, result.getClass());
 assertNull(result);
 }

 @Test
 public void testParseSimpleTagSelector() {
 tokenQueue.putAll("div");
 queryParser.parse(tokenQueue);
 expectedResult = new Evaluator.Tag("div");
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseClassSelector() {
 tokenQueue.putAll("div.className");
 queryParser.parse(tokenQueue);
 expectedResult = new Evaluator.Class("className");
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseIdSelector() {
 tokenQueue.putAll("div#id");
 queryParser.parse(tokenQueue);
 expectedResult = new Evaluator.Id("id");
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseTagAndClassSelector() {
 tokenQueue.putAll("div.className");
 queryParser.parse(tokenQueue);
 Evaluator tagEvaluator = new Evaluator.Tag("div");
 Evaluator classEvaluator = new Evaluator.Class("className");
 Evaluator andEvaluator = new CombiningEvaluator.And(tagEvaluator, classEvaluator);
 expectedResult = andEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseTagAndIdSelector() {
 tokenQueue.putAll("div#id");
 queryParser.parse(tokenQueue);
 Evaluator tagEvaluator = new Evaluator.Tag("div");
 Evaluator idEvaluator = new Evaluator.Id("id");
 expectedResult = new CombiningEvaluator.And(tagEvaluator, idEvaluator);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseNthChildSelector() {
 tokenQueue.putAll("div:nth-child(2)");
 queryParser.parse(tokenQueue);
 int index = 2;
 expectedResult = new Evaluator.IsNthChild(0, index);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseNthOfTypeSelector() {
 tokenQueue.putAll("div:nth-of-type(2)");
 queryParser.parse(tokenQueue);
 int index = 2;
 expectedResult = new Evaluator.IsNthOfType(0, index);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseNthLastChildSelector() {
 tokenQueue.putAll("div:nth-last-child(2)");
 queryParser.parse(tokenQueue);
 int index = 2;
 expectedResult = new Evaluator.IsNthLastChild(0, index);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseNthLastOfTypeSelector() {
 tokenQueue.putAll("div:nth-last-of-type(2)");
 queryParser.parse(tokenQueue);
 int index = 2;
 expectedResult = new Evaluator.IsNthLastOfType(0, index);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidSelector() {
 tokenQueue.putAll("div:nothing");
 queryParser.parse(tokenQueue);
 fail("Expected SelectorParseException");
 }

 @Test
 public void testParseCombinedSelectors() {
 tokenQueue.putAll("div.className:nth-child(2)");
 queryParser.parse(tokenQueue);
 Evaluator tagEvaluator = new Evaluator.Tag("div");
 Evaluator classEvaluator = new Evaluator.Class("className");
 Evaluator nthChildEvaluator = new Evaluator.IsNthChild(0, 2);
 Evaluator andThenNthChildEvaluator = new CombiningEvaluator.And(classEvaluator, nthChildEvaluator);
 expectedResult = andThenNthChildEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseAttributeSelectors() {
 tokenQueue.putAll("div[class^='prefix']");
 queryParser.parse(tokenQueue);
 String prefix = "prefix";
 expectedResult = new Evaluator.AttributeWithValueStarting("class", prefix);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseAttributeSelectorWithValue() {
 tokenQueue.putAll("div[class='className']");
 queryParser.parse(tokenQueue);
 String attributeName = "class";
 String attributeValue = "className";
 expectedResult = new Evaluator.AttributeWithValue(attributeName, attributeValue);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseAttributeSelectorWithValueStarting() {
 tokenQueue.putAll("div[class^='prefix']");
 queryParser.parse(tokenQueue);
 String prefix = "prefix";
 expectedResult = new Evaluator.AttributeWithValueStarting("class", prefix);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseAttributeSelectorWithValueEnding() {
 tokenQueue.putAll("div[class$='suffix']");
 queryParser.parse(tokenQueue);
 String suffix = "suffix";
 expectedResult = new Evaluator.AttributeWithValueEnding("class", suffix);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseAttributeSelectorWithValueContaining() {
 tokenQueue.putAll("div[class*='keyword']");
 queryParser.parse(tokenQueue);
 String keyword = "keyword";
 expectedResult = new Evaluator.AttributeWithValueContaining("class", keyword);
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseAttributeSelectorWithValueMatching() {
 tokenQueue.putAll("div[class~='keyword']");
 queryParser.parse(tokenQueue);
 String keyword = "keyword";
 expectedResult = new Evaluator.AttributeWithValueMatching("class", Pattern.compile(keyword));
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseNegatedAttributeSelectors() {
 tokenQueue.putAll("div:not([class])");
 queryParser.parse(tokenQueue);
 expectedResult = new StructuralEvaluator.Not(new Evaluator.Attribute(new String[]{"class"}));
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseCombinedAttributeSelectors() {
 tokenQueue.putAll("div[class='className'][title^='prefix']");
 queryParser.parse(tokenQueue);
 String className = "className";
 String prefix = "prefix";
 Evaluator classEvaluator = new Evaluator.Attribute(new String[]{"class"}, className);
 Evaluator titleStartsWithEvaluator = new Evaluator.AttributeWithValueStarting("title", prefix);
 Evaluator andEvaluator = new CombiningEvaluator.And(classEvaluator, titleStartsWithEvaluator);
 expectedResult = andEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParsePseudoClassSelectors() {
 tokenQueue.putAll("div:empty");
 queryParser.parse(tokenQueue);
 expectedResult = new Evaluator.IsEmpty();
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParsePseudoElementSelectors() {
 tokenQueue.putAll("div::before");
 queryParser.parse(tokenQueue);
 expectedResult = new StructuralEvaluator.PseudoElementBefore();
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseMultipleSelectors() {
 tokenQueue.putAll("div.className.anotherClass:nth-child(2)");
 queryParser.parse(tokenQueue);
 String className = "className";
 String anotherClass = "anotherClass";
 int index = 2;
 Evaluator classEvaluator1 = new Evaluator.Class(className);
 Evaluator classEvaluator2 = new Evaluator.Class(anotherClass);
 Evaluator nthChildEvaluator = new Evaluator.IsNthChild(0, index);
 Evaluator andThenNthChildEvaluator = new CombiningEvaluator.And(classEvaluator1, classEvaluator2, nthChildEvaluator);
 expectedResult = andThenNthChildEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseMultipleSelectorsWithCombiningDescendants() {
 tokenQueue.putAll("div > p.className:nth-child(2)");
 queryParser.parse(tokenQueue);
 String className = "className";
 int index = 2;
 Evaluator tagEvaluator1 = new Evaluator.Tag("div");
 Evaluator tagEvaluator2 = new Evaluator.Tag("p");
 Evaluator classEvaluator = new Evaluator.Class(className);
 Evaluator nthChildEvaluator = new Evaluator.IsNthChild(tagEvaluator1, index);
 Evaluator descendantEvaluator = new CombiningEvaluator.Descendant(tagEvaluator2, classEvaluator, nthChildEvaluator);
 expectedResult = descendantEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseMultipleSelectorsWithCombiningDescendantsAnd() {
 tokenQueue.putAll("div > p, div > ul > li.className");
 queryParser.parse(tokenQueue);
 Evaluator tagEvaluator1 = new Evaluator.Tag("div");
 Evaluator tagEvaluator2 = new Evaluator.Tag("p");
 Evaluator tagEvaluator3 = new Evaluator.Tag("ul");
 Evaluator tagEvaluator4 = new Evaluator.Tag("li");
 Evaluator classEvaluator = new Evaluator.Class("className");
 Evaluator descendantEvaluator1 = new CombiningEvaluator.Descendant(tagEvaluator2, new Evaluator.Tag("div"));
 Evaluator descendantEvaluator2 = new CombiningEvaluator.Descendant(tagEvaluator4, tagEvaluator3, classEvaluator);
 Evaluator orEvaluator = new CombiningEvaluator.Or(descendantEvaluator1, descendantEvaluator2);
 expectedResult = orEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }

 @Test
 public void testParseMultipleSelectorsWithCombiningDescendantsAndAnd() {
 tokenQueue.putAll("div > p > span.className, div > ul > li > span.className");
 queryParser.parse(tokenQueue);
 Evaluator tagEvaluator1 = new Evaluator.Tag("div");
 Evaluator tagEvaluator2 = new Evaluator.Tag("p");
 Evaluator tagEvaluator3 = new Evaluator.Tag("span");
 Evaluator tagEvaluator4 = new Evaluator.Tag("ul");
 Evaluator tagEvaluator5 = new Evaluator.Tag("li");
 Evaluator classEvaluator = new Evaluator.Class("className");
 Evaluator descendantEvaluator1 = new CombiningEvaluator.Descendant(tagEvaluator3, tagEvaluator2);
 Evaluator descendantEvaluator2 = new CombiningEvaluator.Descendant(tagEvaluator3, tagEvaluator4, tagEvaluator5, classEvaluator);
 Evaluator andEvaluator1 = new CombiningEvaluator.And(tagEvaluator2, descendantEvaluator1);
 Evaluator andEvaluator2 = new CombiningEvaluator.And(tagEvaluator4, tagEvaluator5, classEvaluator);
 Evaluator orEvaluator = new CombiningEvaluator.Or(andEvaluator1, andEvaluator2);
 expectedResult = orEvaluator;
 assertEquals(expectedResult, queryParser.evals.get(0));
 }
}