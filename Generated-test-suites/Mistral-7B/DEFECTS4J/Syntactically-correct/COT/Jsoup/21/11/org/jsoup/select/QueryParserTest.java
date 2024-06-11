import static org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AttributeWithValueNot;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Or;
import org.jsoup.select.StructuralEvaluator.Not;

public class QueryParserTest {

 private static final String TEST_QUERY = "div > p:first-child + p.red:containsOwn(test)";

 @Test
 public void testParse() {
 QueryParser parser = new QueryParser(TEST_QUERY);
 Evaluator evaluator = parser.parse();
 // Add more assertions based on the structure of the expected Evaluator tree
 }

 @Test
 public void testParseWithEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 // No evaluators should be added to the list
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertTrue(evaluator.getClass().getSimpleName().equals("And"));
 assertEquals(0, ((And) evaluator).getChildren().size());
 }

 @Test
 public void testParseWithId() {
 QueryParser parser = new QueryParser("#testId");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Id"));
 assertEquals("testId", ((Id) evaluator).getElementName());
 }

 @Test
 public void testParseWithClass() {
 QueryParser parser = new QueryParser(".testClass");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Class"));
 assertEquals("testClass", ((Class) evaluator).getElementName());
 }

 @Test
 public void testParseWithTag() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Tag"));
 assertEquals("div", ((Tag) evaluator).getElementName());
 }

 @Test
 public void testParseWithAttribute() {
 QueryParser parser = new QueryParser("[title]");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Attribute"));
 assertEquals("title", ((Attribute) evaluator).getAttributeName());
 }

 @Test
 public void testParseWithAttributeValue() {
 QueryParser parser = new QueryParser("[title='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("AttributeWithValue"));
 assertEquals("title", ((AttributeWithValue) evaluator).getAttributeName());
 assertEquals("Test", ((AttributeWithValue) evaluator).getValue());
 }

 @Test
 public void testParseWithAttributeValueContaining() {
 QueryParser parser = new QueryParser("[title*='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("AttributeWithValueContaining"));
 assertEquals("title", ((AttributeWithValueContaining) evaluator).getAttributeName());
 }

 @Test
 public void testParseWithAttributeValueEnding() {
 QueryParser parser = new QueryParser("[title$='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("AttributeWithValueEnding"));
 assertEquals("title", ((AttributeWithValueEnding) evaluator).getAttributeName());
 }

 @Test
 public void testParseWithAttributeValueMatching() {
 QueryParser parser = new QueryParser("[title~='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("AttributeWithValueMatching"));
 assertEquals("title", ((AttributeWithValueMatching) evaluator).getAttributeName());
 }

 @Test
 public void testParseWithAttributeValueNot() {
 QueryParser parser = new QueryParser("[title!='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("AttributeWithValueNot"));
 assertEquals("title", ((AttributeWithValueNot) evaluator).getAttributeName());
 assertEquals("Test", ((AttributeWithValueNot) evaluator).getValue());
 }

 @Test
 public void testParseWithAllElements() {
 QueryParser parser = new QueryParser("*");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("AllElements"));
 }

 @Test
 public void testParseWithIndexLessThan() {
 QueryParser parser = new QueryParser(":lt(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("IndexLessThan"));
 assertEquals(2, ((IndexLessThan) evaluator).getIndex());
 }

 @Test
 public void testParseWithIndexGreaterThan() {
 QueryParser parser = new QueryParser(":gt(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("IndexGreaterThan"));
 assertEquals(2, ((IndexGreaterThan) evaluator).getIndex());
 }

 @Test
 public void testParseWithIndexEquals() {
 QueryParser parser = new QueryParser(":eq(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("IndexEquals"));
 assertEquals(2, ((IndexEquals) evaluator).getIndex());
 }

 @Test
 public void testParseWithHas() {
 QueryParser parser = new QueryParser(":has(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Has"));
 }

 @Test
 public void testParseWithContains() {
 QueryParser parser = new QueryParser(":contains(Test)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Evaluator.ContainsText"));
 }

 @Test
 public void testParseWithContainsOwn() {
 QueryParser parser = new QueryParser(":containsOwn(Test)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Evaluator.ContainsOwnText"));
 }

 @Test
 public void testParseWithMatches() {
 QueryParser parser = new QueryParser(":matches(.test)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Evaluator.Matches"));
 }

 @Test
 public void testParseWithMatchesOwn() {
 QueryParser parser = new QueryParser(":matchesOwn(.test)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Evaluator.MatchesOwn"));
 }

 @Test
 public void testParseWithNot() {
 QueryParser parser = new QueryParser(":not(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Not"));
 }

 @Test
 public void testParseWithCombiningAnd() {
 QueryParser parser = new QueryParser("p.red:contains(Test)");
 Evaluator evaluator = parser.parse();
 // Assert that the evaluator is an "And" with the correct children
 assertEquals(2, evaluator.getClass().getSimpleName().equals("And"));
 Evaluator firstChild = evaluator.getChildren().get(0);
 assertEquals(1, firstChild.getClass().getSimpleName().equals("Class"));
 assertEquals("red", ((Class) firstChild).getElementName());
 Evaluator secondChild = evaluator.getChildren().get(1);
 assertEquals(1, secondChild.getClass().getSimpleName().equals("Evaluator.ContainsText"));
 assertEquals("Test", ((Evaluator.ContainsText) secondChild).getText());
 }

 @Test
 public void testParseWithCombiningOr() {
 QueryParser parser = new QueryParser("p.red:contains(Test), p.blue:contains(Test)");
 Evaluator evaluator = parser.parse();
 // Assert that the evaluator is an "Or" with the correct children
 assertEquals(1, evaluator.getClass().getSimpleName().equals("Or"));
 List<Evaluator> children = evaluator.getChildren();
 Evaluator firstChild = children.get(0);
 assertEquals(2, firstChild.getClass().getSimpleName().equals("Class"));
 List<String> firstClassNames = new ArrayList<String>(Arrays.asList("red", "contains", "Test"));
 assertEquals(firstClassNames, getClassNames(firstChild));
 Evaluator secondChild = children.get(1);
 assertEquals(2, secondChild.getClass().getSimpleName().equals("Class"));
 List<String> secondClassNames = new ArrayList<String>(Arrays.asList("blue", "contains", "Test"));
 assertEquals(secondClassNames, getClassNames(secondChild));
 }

 private List<String> getClassNames(Evaluator evaluator) {
 List<String> classNames = new ArrayList<String>();
 if (evaluator instanceof Class) {
 classNames.add(((Class) evaluator).getElementName());
 } else if (evaluator instanceof And) {
 for (Evaluator child : evaluator.getChildren()) {
 classNames.addAll(getClassNames(child));
 }
 } else if (evaluator instanceof Or) {
 for (Evaluator child : evaluator.getChildren()) {
 classNames.addAll(getClassNames(child));
 }
 }
 return classNames;
 }
}