import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueNot;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.StructuralEvaluator.Has;
import org.jsoup.select.Evaluator.Evaluator.Matches;
import org.jsoup.select.Evaluator.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Evaluator.Not;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.junit.Test;

public class QueryParserTest {

 private static final String VALID_QUERY = "p > #header > ul > li:nth-child(2) > a[href]";
 private static final String INVALID_QUERY = "p > #header > ul > li:nth-child(-1) > a[href]"; // negative index

 @Test
 public void testParseValidQuery() {
 QueryParser parser = new QueryParser(VALID_QUERY);
 Evaluator evaluator = parser.parse();
 // Perform assertions on the evaluator tree
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser(INVALID_QUERY);
 parser.parse(); // This should throw an exception
 }

 @Test
 public void testById() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Id);
 }

 @Test
 public void testByClass() {
 QueryParser parser = new QueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Class);
 }

 @Test
 public void testByTag() {
 QueryParser parser = new QueryParser("tag");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Tag);
 }

 @Test
 public void testBasicAttributeSelection() {
 QueryParser parser = new QueryParser("[attr='value']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Attribute);
 assertEquals("value", evaluator.toString());
 }

 @Test
 public void testComplexAttributeSelection() {
 QueryParser parser = new QueryParser("[attr^='prefix']"); // starts with prefix
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueStarting);
 assertEquals("prefix", evaluator.toString());

 QueryParser parser2 = new QueryParser("[attr$='suffix']"); // ends with suffix
 Evaluator evaluator2 = parser2.parse();
 assertTrue(evaluator2 instanceof AttributeWithValueEnding);
 assertEquals("suffix", evaluator2.toString());
 }

 @Test
 public void testAttributeWithValueContaining() {
 QueryParser parser = new QueryParser("[attr*='value']"); // contains value
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueContaining);
 assertEquals("value", evaluator.toString());
 }

 @Test
 public void testAttributeMatchingRegularExpression() {
 QueryParser parser = new QueryParser("[attr~='regex']"); // matches regex
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueMatching);
 }

 @Test
 public void testAttributeValueEquals() {
 QueryParser parser = new QueryParser("[attr=value]");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValue);
 assertEquals("value", evaluator.toString());
 }

 @Test
 public void testAttributeValueNotEquals() {
 QueryParser parser = new QueryParser("[attr!='value']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueNot);
 assertEquals("value", evaluator.toString());
 }

 @Test
 public void testFindAllElements() {
 QueryParser parser = new QueryParser("*");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AllElements);
 }

 @Test
 public void testFindByIndexLessThan() {
 QueryParser parser = new QueryParser(":lt(index)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IndexLessThan);
 assertEquals(Integer.toString(index), evaluator.toString());
 }

 @Test
 public void testFindByIndexGreaterThan() {
 QueryParser parser = new QueryParser(":gt(index)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IndexGreaterThan);
 assertEquals(Integer.toString(index), evaluator.toString());
 }

 @Test
 public void testFindByIndexEquals() {
 QueryParser parser = new QueryParser(":eq(index)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IndexEquals);
 assertEquals(Integer.toString(index), evaluator.toString());
 }

 @Test
 public void testHasChildSelector() {
 QueryParser parser = new QueryParser(":has(child)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Has);
 }

 @Test
 public void testNotSelector() {
 QueryParser parser = new QueryParser(":not(selector)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Not);
 }

 @Test
 public void testCombinatorSelectors() {
 // Test descendant combinator ">"
 QueryParser parser1 = new QueryParser("div > p");
 Evaluator evaluator1 = parser1.parse();
 assertTrue(evaluator1 instanceof StructuralEvaluator.ImmediateParent);

 // Test ancestor combinator " "
 QueryParser parser2 = new QueryParser("div p");
 Evaluator evaluator2 = parser2.parse();
 assertTrue(evaluator2 instanceof StructuralEvaluator.Parent);

 // Test adjacent sibling combinator "+"
 QueryParser parser3 = new QueryParser("div + p");
 Evaluator evaluator3 = parser3.parse();
 assertTrue(evaluator3 instanceof StructuralEvaluator.ImmediatePreviousSibling);

 // Test general sibling combinator "~"
 QueryParser parser4 = new QueryParser("div ~ p");
 Evaluator evaluator4 = parser4.parse();
 assertTrue(evaluator4 instanceof StructuralEvaluator.PreviousSibling);
 }
}