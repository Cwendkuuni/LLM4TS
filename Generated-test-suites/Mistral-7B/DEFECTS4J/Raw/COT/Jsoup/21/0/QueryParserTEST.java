import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 private QueryParser parser;
 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 query = "div#id > p.class1 + p.class2 ~ p.class3";
 parser = new QueryParser(query);
 tokenQueue = parser.tq;
 }

 @Test
 public void testParse() {
 Evaluator evaluator = parser.parse();
 // Add more assertions based on the expected structure of the Evaluator tree.
 }

 @Test
 public void testParseCombinatorGreaterThan() {
 parser = new QueryParser("div > p");
 Evaluator evaluator = parser.parse();
 Evaluator parentEvaluator = (Evaluator.Tag) evaluator.get(0);
 Evaluator childEvaluator = (Evaluator.Tag) evaluator.get(1);
 assertTrue(parentEvaluator.matches(tokenQueue.peek().getElementName()) &&
 childEvaluator.matches(tokenQueue.peek().getElementName()) &&
 parentEvaluator.getNextSibling().equals(childEvaluator));
 }

 @Test
 public void testParseCombinatorSpace() {
 parser = new QueryParser("div p");
 Evaluator evaluator = parser.parse();
 Evaluator parentEvaluator = (Evaluator.Tag) evaluator.get(0);
 Evaluator childEvaluator = (Evaluator.Class) evaluator.get(1);
 assertTrue(parentEvaluator.matches(tokenQueue.peek().getElementName()) &&
 childEvaluator.matches("p"));
 }

 @Test
 public void testParseCombinatorPlus() {
 parser = new QueryParser("div p + p");
 Evaluator evaluator = parser.parse();
 Evaluator parentEvaluator = (Evaluator.Tag) evaluator.get(0);
 Evaluator siblingEvaluator = (Evaluator.Tag) evaluator.get(1);
 Evaluator nextSiblingEvaluator = (Evaluator.Tag) evaluator.get(2);
 assertTrue(parentEvaluator.matches(tokenQueue.peek().getElementName()) &&
 siblingEvaluator.matches(tokenQueue.peek().getElementName()) &&
 nextSiblingEvaluator.matches(tokenQueue.peek().getElementName()) &&
 siblingEvaluator.getPreviousSibling().equals(parentEvaluator));
 }

 @Test
 public void testParseCombinatorTilde() {
 parser = new QueryParser("div p ~ p");
 Evaluator evaluator = parser.parse();
 Evaluator parentEvaluator = (Evaluator.Tag) evaluator.get(0);
 Evaluator previousSiblingEvaluator = (Evaluator.Tag) evaluator.get(1);
 Evaluator nextSiblingEvaluator = (Evaluator.Tag) evaluator.get(2);
 assertTrue(parentEvaluator.matches(tokenQueue.peek().getElementName()) &&
 previousSiblingEvaluator.matches(tokenQueue.peek().getElementName()) &&
 nextSiblingEvaluator.matches(tokenQueue.peek().getElementName()) &&
 previousSiblingEvaluator.getNextSibling().equals(nextSiblingEvaluator));
 }

 @Test
 public void testParseIdSelector() {
 parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 Evaluator idEvaluator = (Evaluator.Id) evaluator.get(0);
 assertEquals("id", idEvaluator.getValue());
 }

 @Test
 public void testParseClassSelector() {
 parser = new QueryParser(".class1");
 Evaluator evaluator = parser.parse();
 Evaluator classEvaluator = (Evaluator.Class) evaluator.get(0);
 assertEquals("class1", classEvaluator.getValue());
 }

 @Test
 public void testParseTagSelector() {
 parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 Evaluator tagEvaluator = (Evaluator.Tag) evaluator.get(0);
 assertEquals("div", tagEvaluator.getValue());
 }

 @Test
 public void testParseAttributeSelector() {
 parser = new QueryParser("[href]");
 Evaluator evaluator = parser.parse();
 Evaluator attributeEvaluator = (Evaluator.Attribute) evaluator.get(0);
 assertEquals("href", attributeEvaluator.getKey());
 }

 @Test
 public void testParseAttributeSelectorWithValue() {
 parser = new QueryParser("[href='home.html']");
 Evaluator evaluator = parser.parse();
 Evaluator attributeEvaluator = (Evaluator.AttributeWithValue) evaluator.get(0);
 assertEquals("href", attributeEvaluator.getKey());
 assertEquals("home.html", attributeEvaluator.getValue());
 }

 @Test
 public void testParseAttributeSelectorWithValueStarting() {
 parser = new QueryParser("[href^='home']");
 Evaluator evaluator = parser.parse();
 Evaluator attributeEvaluator = (Evaluator.AttributeWithValueStarting) evaluator.get(0);
 assertEquals("href", attributeEvaluator.getKey());
 assertEquals("home", attributeEvaluator.getValue().substring(0, 4));
 }

 @Test
 public void testParseAttributeSelectorWithValueEnding() {
 parser = new QueryParser("[href$='.html']");
 Evaluator evaluator = parser.parse();
 Evaluator attributeEvaluator = (Evaluator.AttributeWithValueEnding) evaluator.get(0);
 assertEquals("href", attributeEvaluator.getKey());
 assertEquals(".html", attributeEvaluator.getValue().substring(attributeEvaluator.getValue().length() - 5));
 }

 @Test
 public void testParseAttributeSelectorWithValueContaining() {
 parser = new QueryParser("[href*='home']");
 Evaluator evaluator = parser.parse();
 Evaluator attributeEvaluator = (Evaluator.AttributeWithValueContaining) evaluator.get(0);
 assertEquals("href", attributeEvaluator.getKey());
 assertTrue(attributeEvaluator.getValue().contains("home"));
 }

 @Test
 public void testParseAttributeSelectorWithValueMatching() {
 parser = new QueryParser("[href~='home']");
 Evaluator evaluator = parser.parse();
 Evaluator attributeEvaluator = (Evaluator.AttributeWithValueMatching) evaluator.get(0);
 assertEquals("href", attributeEvaluator.getKey());
 assertTrue(Pattern.compile(attributeEvaluator.getValue()).matcher("home.html").matches());
 }

 @Test
 public void testParseAllElementsSelector() {
 parser = new QueryParser("*");
 Evaluator evaluator = parser.parse();
 Evaluator allElementsEvaluator = (Evaluator.AllElements) evaluator.get(0);
 }

 @Test
 public void testParseIndexLessThan() {
 parser = new QueryParser("div:lt(2)");
 Evaluator evaluator = parser.parse();
 Evaluator indexLessThanEvaluator = (Evaluator.IndexLessThan) evaluator.get(0);
 assertEquals(2, indexLessThanEvaluator.getValue());
 }

 @Test
 public void testParseIndexGreaterThan() {
 parser = new QueryParser("div:gt(2)");
 Evaluator evaluator = parser.parse();
 Evaluator indexGreaterThanEvaluator = (Evaluator.IndexGreaterThan) evaluator.get(0);
 assertEquals(3, indexGreaterThanEvaluator.getValue());
 }

 @Test
 public void testParseIndexEquals() {
 parser = new QueryParser("div:eq(2)");
 Evaluator evaluator = parser.parse();
 Evaluator indexEqualsEvaluator = (Evaluator.IndexEquals) evaluator.get(0);
 assertEquals(2, indexEqualsEvaluator.getValue());
 }

 @Test
 public void testParseHasSelector() {
 parser = new QueryParser("div:has(p)");
 Evaluator evaluator = parser.parse();
 Evaluator hasEvaluator = (Evaluator.Has) evaluator.get(0);
 Evaluator childEvaluator = (Evaluator.Tag) hasEvaluator.getChild();
 assertEquals("p", childEvaluator.getValue());
 }

 @Test
 public void testParseContainsSelector() {
 parser = new QueryParser("div:contains(text)");
 Evaluator evaluator = parser.parse();
 Evaluator containsEvaluator = (Evaluator.ContainsText) evaluator.get(0);
 assertEquals("text", containsEvaluator.getValue());
 }

 @Test
 public void testParseContainsOwnSelector() {
 parser = new QueryParser("div:containsOwn(text)");
 Evaluator evaluator = parser.parse();
 Evaluator containsOwnEvaluator = (Evaluator.ContainsOwnText) evaluator.get(0);
 assertEquals("text", containsOwnEvaluator.getValue());
 }

 @Test
 public void testParseMatchesSelector() {
 parser = new QueryParser("div:matches(regex)");
 Evaluator evaluator = parser.parse();
 Evaluator matchesEvaluator = (Evaluator.Matches) evaluator.get(0);
 Pattern regex = matchesEvaluator.getPattern();
 assertTrue(regex.matcher("test").matches());
 }

 @Test
 public void testParseMatchesOwnSelector() {
 parser = new QueryParser("div:matchesOwn(regex)");
 Evaluator evaluator = parser.parse();
 Evaluator matchesOwnEvaluator = (Evaluator.MatchesOwn) evaluator.get(0);
 Pattern regex = matchesOwnEvaluator.getPattern();
 assertTrue(regex.matcher("test").matches());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidSelector() {
 parser = new QueryParser("div:not( selector )");
 parser.parse();
 fail("Expected Selector.SelectorParseException but none was thrown.");
 }

 @After
 public void tearDown() {
 parser = null;
 tokenQueue = null;
 }

}