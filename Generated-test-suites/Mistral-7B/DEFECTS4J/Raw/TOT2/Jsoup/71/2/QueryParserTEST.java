import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

 private static final String SAMPLE_QUERY = "div.class1 > div.class2:not(.class3)";

 @Test
 public void testParseBasic() {
 QueryParser queryParser = new QueryParser(SAMPLE_QUERY);
 Evaluator evaluator = queryParser.parse();
 // Add assertions here to check the evaluator's structure and behavior
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser queryParser = new QueryParser("invalid query");
 try {
 queryParser.parse();
 fail("Expected Selector.SelectorParseException");
 } catch (Selector.SelectorParseException e) {
 // Add assertions here to check the error message
 }
 }

 @Test
 public void testCombinatorChild() {
 QueryParser queryParser = new QueryParser("div > p");
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 Evaluator childEvaluator = new Evaluator.Tag("p");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(parentEvaluator, new StructuralEvaluator.ImmediateChild(childEvaluator));
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testCombinatorDescendant() {
 QueryParser queryParser = new QueryParser("div p");
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 Evaluator childEvaluator = new Evaluator.Tag("p");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(parentEvaluator, new StructuralEvaluator.Descendant(childEvaluator));
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testCombinatorSibling() {
 QueryParser queryParser = new QueryParser("p ~ div");
 Evaluator previousSiblingEvaluator = new Evaluator.Tag("p");
 Evaluator siblingEvaluator = new Evaluator.Tag("div");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(previousSiblingEvaluator, new StructuralEvaluator.PreviousSibling(siblingEvaluator));
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testCombinatorOr() {
 QueryParser queryParser = new QueryParser("div:first-child, div:last-child");
 Evaluator firstChildEvaluator = new Evaluator.IsFirstChild();
 Evaluator lastChildEvaluator = new Evaluator.IsLastChild();
 Evaluator expectedEvaluator = new CombiningEvaluator.Or(firstChildEvaluator, lastChildEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeSimple() {
 QueryParser queryParser = new QueryParser("p[class='class1']");
 Evaluator attributeEvaluator = new Evaluator.Attribute(new Evaluator.AttributeName("class"), "class1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeComplex() {
 QueryParser queryParser = new QueryParser("p[class='class1'][title='title1']");
 Evaluator attributeClassEvaluator = new Evaluator.Attribute(new Evaluator.AttributeName("class"), "class1");
 Evaluator attributeTitleEvaluator = new Evaluator.Attribute(new Evaluator.AttributeName("title"), "title1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"),
 new CombiningEvaluator.And(attributeClassEvaluator, attributeTitleEvaluator));
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeContains() {
 QueryParser queryParser = new QueryParser("p[class*='class1']");
 Evaluator attributeClassEvaluator = new Evaluator.AttributeWithValueContaining(new Evaluator.AttributeName("class"), "class1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeClassEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeStartsWith() {
 QueryParser queryParser = new QueryParser("p[class^='class1']");
 Evaluator attributeClassEvaluator = new Evaluator.AttributeStarting(new Evaluator.AttributeName("class"), "class1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeClassEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeEndsWith() {
 QueryParser queryParser = new QueryParser("p[class$='class1']");
 Evaluator attributeClassEvaluator = new Evaluator.AttributeWithValueEnding(new Evaluator.AttributeName("class"), "class1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeClassEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueEquals() {
 QueryParser queryParser = new QueryParser("p[class='class1']");
 Evaluator attributeClassEvaluator = new Evaluator.Attribute(new Evaluator.AttributeName("class"), "class1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeClassEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueNotEquals() {
 QueryParser queryParser = new QueryParser("p[class!='class1']");
 Evaluator attributeClassEvaluator = new Evaluator.AttributeWithValueNot(new Evaluator.AttributeName("class"), "class1");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeClassEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueStartsWith() {
 QueryParser queryParser = new QueryParser("p[title^='test']");
 Evaluator attributeTitleEvaluator = new Evaluator.AttributeWithValueStarting(new Evaluator.AttributeName("title"), "test");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeTitleEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueEndsWith() {
 QueryParser queryParser = new QueryParser("p[title$='test']");
 Evaluator attributeTitleEvaluator = new Evaluator.AttributeWithValueEnding(new Evaluator.AttributeName("title"), "test");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeTitleEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueContains() {
 QueryParser queryParser = new QueryParser("p[title*='test']");
 Evaluator attributeTitleEvaluator = new Evaluator.AttributeWithValueContaining(new Evaluator.AttributeName("title"), "test");
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeTitleEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueMatching() {
 QueryParser queryParser = new QueryParser("p[title~='test']");
 Evaluator attributeTitleEvaluator = new Evaluator.AttributeWithValueMatching(new Evaluator.AttributeName("title"), Pattern.compile("test"));
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeTitleEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testAttributeValueMatchingCaseInsensitive() {
 QueryParser queryParser = new QueryParser("p[title~='Test']");
 Evaluator attributeTitleEvaluator = new Evaluator.AttributeWithValueMatching(new Evaluator.AttributeName("title"), Pattern.compile("test", Pattern.CASE_INSENSITIVE));
 Evaluator expectedEvaluator = new CombiningEvaluator.And(new Evaluator.Tag("p"), attributeTitleEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassFirstChild() {
 QueryParser queryParser = new QueryParser("div > p:first-child");
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 Evaluator firstChildEvaluator = new Evaluator.IsFirstChild();
 Evaluator expectedEvaluator = new CombiningEvaluator.And(parentEvaluator, firstChildEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassLastChild() {
 QueryParser queryParser = new QueryParser("div > p:last-child");
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 Evaluator lastChildEvaluator = new Evaluator.IsLastChild();
 Evaluator expectedEvaluator = new CombiningEvaluator.And(parentEvaluator, lastChildEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassOnlyChild() {
 QueryParser queryParser = new QueryParser("div:only-child");
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 Evaluator onlyChildEvaluator = new Evaluator.IsOnlyChild();
 Evaluator expectedEvaluator = new CombiningEvaluator.And(parentEvaluator, onlyChildEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassOnlyOfType() {
 QueryParser queryParser = new QueryParser("div:only-of-type");
 Evaluator parentEvaluator = new Evaluator.Tag("div");
 Evaluator onlyOfTypeEvaluator = new Evaluator.IsOnlyOfType();
 Evaluator expectedEvaluator = new CombiningEvaluator.And(parentEvaluator, onlyOfTypeEvaluator);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassEmpty() {
 QueryParser queryParser = new QueryParser("p:empty");
 Evaluator expectedEvaluator = new Evaluator.IsEmpty();
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassRoot() {
 QueryParser queryParser = new QueryParser(":root");
 Evaluator expectedEvaluator = new Evaluator.IsRoot();
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassNthChildForward() {
 QueryParser queryParser = new QueryParser("div:nth-child(2)");
 Evaluator expectedEvaluator = new Evaluator.IsNthChild(1, 1, true);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassNthChildBackward() {
 QueryParser queryParser = new QueryParser("div:nth-last-child(2)");
 Evaluator expectedEvaluator = new Evaluator.IsNthLastChild(1, 1, true);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassNthOfTypeForward() {
 QueryParser queryParser = new QueryParser("div:nth-of-type(2)");
 Evaluator expectedEvaluator = new Evaluator.IsNthOfType(1, 1, true);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }

 @Test
 public void testPseudoClassNthOfTypeBackward() {
 QueryParser queryParser = new QueryParser("div:nth-last-of-type(2)");
 Evaluator expectedEvaluator = new Evaluator.IsNthLastOfType(1, 1, true);
 Evaluator actualEvaluator = queryParser.parse();
 assertEquals(expectedEvaluator, actualEvaluator);
 }
}