import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Evaluator;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;
 private List<Evaluator> evaluators;

 @Before
 public void setUp() {
 queryParser = new QueryParser("");
 tokenQueue = new TokenQueue("");
 evaluators = new ArrayList<>();
 }

 @Test
 public void testParseEmptyQuery() {
 queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidQuery() {
 queryParser = new QueryParser("invalidQuery");
 queryParser.parse(); // this should throw an IllegalArgumentException
 }

 @Test
 public void testParseSimpleIdSelector() {
 queryParser = new QueryParser("#id");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.Id.class, evaluators.get(0).getClass());
 assertEquals("id", ((Evaluator.Id) evaluators.get(0)).getId());
 }

 @Test
 public void testParseSimpleClassSelector() {
 queryParser = new QueryParser(".class");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.Class.class, evaluators.get(0).getClass());
 assertEquals("class", ((Evaluator.Class) evaluators.get(0)).getClassname());
 }

 @Test
 public void testParseSimpleTagSelector() {
 queryParser = new QueryParser("tag");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.Tag.class, evaluators.get(0).getClass());
 assertEquals("tag", ((Evaluator.Tag) evaluators.get(0)).getTagName());
 }

 @Test
 public void testParseAttributeSelector() {
 queryParser = new QueryParser("[attr]");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.Attribute.class, evaluators.get(0).getClass());
 assertEquals("attr", ((Evaluator.Attribute) evaluators.get(0)).getAttributeName());
 }

 @Test
 public void testParseAttributeWithValueSelector() {
 queryParser = new QueryParser("[attr='value']");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.AttributeWithValue.class, evaluators.get(0).getClass());
 assertEquals("attr", ((Evaluator.AttributeWithValue) evaluators.get(0)).getAttributeName());
 assertEquals("value", ((Evaluator.AttributeWithValue) evaluators.get(0)).getValue());
 }

 @Test
 public void testParseCombinedSelectors() {
 queryParser = new QueryParser("#id > .class");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(2, evaluators.size());
 Evaluator idEvaluator = evaluators.get(0);
 assertEquals(Evaluator.Id.class, idEvaluator.getClass());
 assertEquals("id", ((Evaluator.Id) idEvaluator).getId());
 Evaluator classEvaluator = evaluators.get(1);
 assertEquals(Evaluator.Class.class, classEvaluator.getClass());
 assertEquals("class", ((Evaluator.Class) classEvaluator).getClassname());
 }

 @Test
 public void testParseComplexSelector() {
 queryParser = new QueryParser("div#id > .class1 > .class2:has(p) + input[type='text']");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(6, evaluators.size());
 assertEquals(Evaluator.Tag.class, evaluators.get(0).getClass());
 assertEquals("div", ((Evaluator.Tag) evaluators.get(0)).getTagName());
 Evaluator idEvaluator = evaluators.get(1);
 assertEquals(Evaluator.Id.class, idEvaluator.getClass());
 assertEquals("id", ((Evaluator.Id) idEvaluator).getId());
 Evaluator class1Evaluator = evaluators.get(2);
 assertEquals(Evaluator.Class.class, class1Evaluator.getClass());
 assertEquals("class1", ((Evaluator.Class) class1Evaluator).getClassname());
 Evaluator hasEvaluator = evaluators.get(3);
 assertEquals(StructuralEvaluator.Has.class, hasEvaluator.getClass());
 Evaluator pEvaluator = ((StructuralEvaluator.Has) hasEvaluator).getChildSelectors().get(0);
 assertEquals(Evaluator.Tag.class, pEvaluator.getClass());
 assertEquals("p", ((Evaluator.Tag) pEvaluator).getTagName());
 Evaluator inputEvaluator = evaluators.get(5);
 assertEquals(Evaluator.Attribute.class, inputEvaluator.getClass());
 assertEquals("type", ((Evaluator.Attribute) inputEvaluator).getAttributeName());
 assertEquals("text", ((Evaluator.Attribute) inputEvaluator).getValue());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidCombiner() {
 queryParser = new QueryParser("div#id > .class ~ invalid-combiner");
 queryParser.parse(tokenQueue); // this should throw a Selector.SelectorParseException
 }

 @Test
 public void testParsePseudoSelectors() {
 queryParser = new QueryParser(":last-child");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.IsLastChild.class, evaluators.get(0).getClass());
 queryParser = new QueryParser(":nth-child(2) of type p");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.IsNthOfType.class, evaluators.get(0).getClass());
 assertEquals(2, ((Evaluator.IsNthOfType) evaluators.get(0)).getNthIndex());
 assertEquals("p", ((Evaluator.IsNthOfType) evaluators.get(0)).getElementType());
 queryParser = new QueryParser(":contains(text)");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.ContainsText.class, evaluators.get(0).getClass());
 assertEquals("text", ((Evaluator.ContainsText) evaluators.get(0)).getText());
 queryParser = new QueryParser(":containsOwn(text)");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.ContainsOwnText.class, evaluators.get(0).getClass());
 assertEquals("text", ((Evaluator.ContainsOwnText) evaluators.get(0)).getText());
 queryParser = new QueryParser(":matches(regex)");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.Matches.class, evaluators.get(0).getClass());
 Pattern regexPattern = Pattern.compile((String) ((Evaluator.Matches) evaluators.get(0)).getRegex());
 assertNotNull(regexPattern.matcher("test").find());
 queryParser = new QueryParser(":matchesOwn(regex)");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(Evaluator.MatchesOwn.class, evaluators.get(0).getClass());
 Pattern regexPattern2 = Pattern.compile((String) ((Evaluator.MatchesOwn) evaluators.get(0)).getRegex());
 assertNotNull(regexPattern2.matcher("test").find());
 queryParser = new QueryParser(":not(selector)");
 evaluators.clear();
 queryParser.parse(tokenQueue);
 assertEquals(1, evaluators.size());
 assertEquals(StructuralEvaluator.Not.class, evaluators.get(0).getClass());
 }
}