import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
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
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 private static final String[] combinators = {",", ">", "+", "~", " "};

 @Test
 public void testParseMethod_NormalCase() {
 QueryParser parser = new QueryParser("p.class1.class2");
 Evaluator evaluator = parser.parse();
 // Add your assertions here
 }

 @Test
 public void testParseMethod_CombinatorWithRootCase() {
 QueryParser parser = new QueryParser("div > p.class1");
 Evaluator evaluator = parser.parse();
 // Add your assertions here
 }

 @Test
 public void testParseMethod_MultipleSelectorsWithCombinatorCase() {
 QueryParser parser = new QueryParser("div > p.class1 + p.class2");
 Evaluator evaluator = parser.parse();
 // Add your assertions here
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseMethod_UnknownCombinatorCase() {
 QueryParser parser = new QueryParser("div ~% p.class1");
 parser.parse();
 fail("Should throw an exception");
 }

 @Test
 public void testCombinatorMethod_ImmediateParentCase() {
 QueryParser parser = new QueryParser("p > div");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), StructuralEvaluator.And.class);
 assertEquals(evaluator.size(), 2);
 assertEquals(evaluator.get(0).getClass(), StructuralEvaluator.ImmediateParent.class);
 assertEquals(evaluator.get(1).getClass(), Evaluator.Tag.class);
 assertEquals(((StructuralEvaluator.ImmediateParent) evaluator.get(0)).getParentElementTagName(), "p");
 }

 @Test
 public void testCombinatorMethod_ParentCase() {
 QueryParser parser = new QueryParser("p ~ div");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), StructuralEvaluator.And.class);
 assertEquals(evaluator.size(), 2);
 assertEquals(evaluator.get(0).getClass(), StructuralEvaluator.Parent.class);
 assertEquals(((StructuralEvaluator.Parent) evaluator.get(0)).getParentElementTagName(), "p");
 }

 @Test
 public void testCombinatorMethod_ImmediatePreviousSiblingCase() {
 QueryParser parser = new QueryParser("p + ~ div");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), StructuralEvaluator.And.class);
 assertEquals(evaluator.size(), 2);
 assertEquals(evaluator.get(0).getClass(), StructuralEvaluator.ImmediatePreviousSibling.class);
 assertEquals(((StructuralEvaluator.ImmediatePreviousSibling) evaluator.get(0)).getPreviousSiblingElementTagName(), "p");
 }

 @Test
 public void testCombinatorMethod_PreviousSiblingCase() {
 QueryParser parser = new QueryParser("p ~ div");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), StructuralEvaluator.And.class);
 assertEquals(evaluator.size(), 2);
 assertEquals(evaluator.get(0).getClass(), StructuralEvaluator.PreviousSibling.class);
 assertEquals(((StructuralEvaluator.PreviousSibling) evaluator.get(0)).getPreviousSiblingElementTagName(), "p");
 }

 @Test
 public void testFindElementsMethod_ByIdCase() {
 QueryParser parser = new QueryParser("#id");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.Id.class);
 assertEquals(((Evaluator.Id) evaluator).getId(), "id");
 }

 @Test
 public void testFindElementsMethod_ByClassCase() {
 QueryParser parser = new QueryParser(".class1");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.Class.class);
 assertEquals(((Evaluator.Class) evaluator).getClassName(), "class1");
 }

 @Test
 public void testFindElementsMethod_ByTagCase() {
 QueryParser parser = new QueryParser("p");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.Tag.class);
 assertEquals(((Evaluator.Tag) evaluator).getTagName(), "p");
 }

 @Test
 public void testFindElementsMethod_ByAttributeStartingCase() {
 QueryParser parser = new QueryParser("[starting]");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.AttributeStarting.class);
 assertEquals(((Evaluator.AttributeStarting) evaluator).getAttributeName(), "starting");
 }

 @Test
 public void testFindElementsMethod_ByAttributeCase() {
 QueryParser parser = new QueryParser("[attr=value]");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.Attribute.class);
 assertEquals(((Evaluator.Attribute) evaluator).getAttributeName(), "attr");
 assertEquals(((Evaluator.Attribute) evaluator).getValue(), "value");
 }

 @Test
 public void testFindElementsMethod_ByAttributeWithValueContainingCase() {
 QueryParser parser = new QueryParser("[attr*=value]");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.AttributeWithValueContaining.class);
 assertEquals(((Evaluator.AttributeWithValueContaining) evaluator).getAttributeName(), "attr");
 assertEquals(((Evaluator.AttributeWithValueContaining) evaluator).getValue(), "value");
 }

 @Test
 public void testFindElementsMethod_ByAttributeWithValueEndingCase() {
 QueryParser parser = new QueryParser("[attr$=value]");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.AttributeWithValueEnding.class);
 assertEquals(((Evaluator.AttributeWithValueEnding) evaluator).getAttributeName(), "attr");
 assertEquals(((Evaluator.AttributeWithValueEnding) evaluator).getValue(), "value");
 }

 @Test
 public void testFindElementsMethod_ByAttributeWithValueMatchingCase() {
 QueryParser parser = new QueryParser("[attr~=value]");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.AttributeWithValueMatching.class);
 assertEquals(((Evaluator.AttributeWithValueMatching) evaluator).getAttributeName(), "attr");
 assertEquals(((Evaluator.AttributeWithValueMatching) evaluator).getRegex(), Pattern.compile(value));
 }

 @Test
 public void testFindElementsMethod_ByAttributeWithValueNotCase() {
 QueryParser parser = new QueryParser("[attr!=[value]]");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.AttributeWithValueNot.class);
 assertEquals(((Evaluator.AttributeWithValueNot) evaluator).getAttributeName(), "attr");
 assertEquals(((Evaluator.AttributeWithValueNot) evaluator).getValue(), "value");
 }

 @Test
 public void testFindElementsMethod_AllElementsCase() {
 QueryParser parser = new QueryParser("*");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.AllElements.class);
 }

 @Test
 public void testFindElementsMethod_IndexLessThanCase() {
 QueryParser parser = new QueryParser(":lt(2)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.IndexLessThan.class);
 assertEquals(((Evaluator.IndexLessThan) evaluator).getIndex(), 2);
 }

 @Test
 public void testFindElementsMethod_IndexGreaterThanCase() {
 QueryParser parser = new QueryParser(":gt(2)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.IndexGreaterThan.class);
 assertEquals(((Evaluator.IndexGreaterThan) evaluator).getIndex(), 2);
 }

 @Test
 public void testFindElementsMethod_IndexEqualsCase() {
 QueryParser parser = new QueryParser(":eq(2)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.IndexEquals.class);
 assertEquals(((Evaluator.IndexEquals) evaluator).getIndex(), 2);
 }

 @Test
 public void testHasMethod_Case() {
 QueryParser parser = new QueryParser(":has(p)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), StructuralEvaluator.Has.class);
 assertEquals(((StructuralEvaluator.Has) evaluator).getChildEvaluator(), parse("p").parse());
 }

 @Test
 public void testContainsMethod_TextCase() {
 QueryParser parser = new QueryParser(":contains(text)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.ContainsText.class);
 assertEquals(((Evaluator.ContainsText) evaluator).getText(), "text");
 }

 @Test
 public void testContainsMethod_TextOwnCase() {
 QueryParser parser = new QueryParser(":containsOwn(text)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.ContainsOwnText.class);
 assertEquals(((Evaluator.ContainsOwnText) evaluator).getText(), "text");
 }

 @Test
 public void testMatchesMethod_RegexCase() {
 QueryParser parser = new QueryParser(":matches(regex)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.Matches.class);
 assertEquals(((Evaluator.Matches) evaluator).getRegex(), Pattern.compile(regex));
 }

 @Test
 public void testMatchesMethod_RegexOwnCase() {
 QueryParser parser = new QueryParser(":matchesOwn(regex)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), Evaluator.MatchesOwn.class);
 assertEquals(((Evaluator.MatchesOwn) evaluator).getRegex(), Pattern.compile(regex));
 }

 @Test
 public void testNotMethod_SelectorCase() {
 QueryParser parser = new QueryParser(":not(selector)");
 evaluator = parser.parse();
 assertEquals(evaluator.getClass(), StructuralEvaluator.Not.class);
 assertEquals(((StructuralEvaluator.Not) evaluator).getChildEvaluator(), parse("selector").parse());
 }
}