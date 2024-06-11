import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

 private String query;
 private QueryParser parser;

 @Before
 public void setUp() {
 query = "div#myId > p.myClass:containsOwn(test)";
 parser = new QueryParser(query);
 }

 @Test
 public void testParse() {
 Evaluator evaluator = parser.parse();
 // Add more assertions based on the expected structure of the Evaluator tree
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQuery() {
 String invalidQuery = "div#myId > p.myClass:contains(test)"; // missing 'Own' keyword
 parser = new QueryParser(invalidQuery);
 parser.parse(); // This should throw an exception
 }

 @Test
 public void testCombinatorGreaterThan() {
 Evaluator parent = new Evaluator.Tag("div");
 Evaluator child = new Evaluator.Tag("p");
 Evaluator combinator = new CombiningEvaluator.And(parent, new StructuralEvaluator.ImmediateParent(child));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(combinator);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testCombinatorSpace() {
 Evaluator parent = new Evaluator.Tag("div");
 Evaluator child = new Evaluator.Tag("p");
 Evaluator combinator = new CombiningEvaluator.And(parent, new StructuralEvaluator.Parent(child));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(combinator);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testCombinatorPlus() {
 Evaluator previousSibling = new Evaluator.Tag("p");
 Evaluator currentElement = new Evaluator.Tag("p");
 Evaluator combinator = new CombiningEvaluator.And(currentElement, new StructuralEvaluator.ImmediatePreviousSibling(previousSibling));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(combinator);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testCombinatorTilde() {
 Evaluator previousElement = new Evaluator.Tag("p");
 Evaluator currentElement = new Evaluator.Tag("p");
 Evaluator combinator = new CombiningEvaluator.And(currentElement, new StructuralEvaluator.PreviousSibling(previousElement));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(combinator);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testIdSelector() {
 String id = "myId";
 Evaluator idSelector = new Evaluator.Id(id);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(idSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testClassSelector() {
 String className = "myClass";
 Evaluator classSelector = new Evaluator.Class(className.trim().toLowerCase());
 List<Evaluator> expected = new ArrayList<>();
 expected.add(classSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testTagSelector() {
 String tagName = "div";
 Evaluator tagSelector = new Evaluator.Tag(tagName.trim().toLowerCase());
 List<Evaluator> expected = new ArrayList<>();
 expected.add(tagSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testAttributeSelector() {
 String attributeName = "class";
 String attributeValue = "myClass";
 Evaluator attributeSelector = new Evaluator.AttributeWithValue(attributeName, attributeValue);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(attributeSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testAttributeWithValueStartingSelector() {
 String attributeName = "class";
 String attributeValue = "start";
 Evaluator attributeSelector = new Evaluator.AttributeStarting(attributeName, attributeValue);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(attributeSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testContainsTextSelector() {
 String searchText = "test";
 Evaluator containsTextSelector = new Evaluator.ContainsText(searchText);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(containsTextSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testContainsOwnTextSelector() {
 String searchText = "test";
 Evaluator containsOwnTextSelector = new Evaluator.ContainsOwnText(searchText);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(containsOwnTextSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testMatchesTextSelector() {
 String regex = "test";
 Evaluator matchesTextSelector = new Evaluator.Matches(Pattern.compile(regex));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(matchesTextSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testMatchesOwnTextSelector() {
 String regex = "test";
 Evaluator matchesOwnTextSelector = new Evaluator.MatchesOwn(Pattern.compile(regex));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(matchesOwnTextSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testNotSelector() {
 String subQuery = "p.myClass";
 StructuralEvaluator notSelector = new StructuralEvaluator.Not(parser.parse(subQuery));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(notSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testAllElementsSelector() {
 Evaluator allElementsSelector = new Evaluator.AllElements();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(allElementsSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testIndexLessThanSelector() {
 int index = 0;
 Evaluator indexLessThanSelector = new Evaluator.IndexLessThan(index);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(indexLessThanSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testIndexGreaterThanSelector() {
 int index = 0;
 Evaluator indexGreaterThanSelector = new Evaluator.IndexGreaterThan(index);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(indexGreaterThanSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testIndexEqualsSelector() {
 int index = 0;
 Evaluator indexEqualsSelector = new Evaluator.IndexEquals(index);
 List<Evaluator> expected = new ArrayList<>();
 expected.add(indexEqualsSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }

 @Test
 public void testHasSelector() {
 String subQuery = "p.myClass";
 StructuralEvaluator hasSelector = new StructuralEvaluator.Has(parser.parse(subQuery));
 List<Evaluator> expected = new ArrayList<>();
 expected.add(hasSelector);

 Evaluator parsed = parser.parse();
 assertEquals(expected.size(), parsed.size());
 for (Evaluator e : expected) {
 assertTrue(parsed.contains(e));
 }
 }
}