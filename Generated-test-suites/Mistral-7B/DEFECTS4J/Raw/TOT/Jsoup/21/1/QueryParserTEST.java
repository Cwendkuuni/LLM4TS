import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 Assert.assertNotNull(evaluator);
 }

 @Test
 public void testParseSingleElementSelector() {
 QueryParser parser = new QueryParser(".class-name");
 Evaluator evaluator = parser.parse();
 Evaluator expected = new Evaluator.Class("class-name");
 Assert.assertEquals(expected, evaluator);
 }

 @Test
 public void testParseSingleIdSelector() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 Evaluator expected = new Evaluator.Id("id");
 Assert.assertEquals(expected, evaluator);
 }

 @Test
 public void testParseSimpleCombination() {
 QueryParser parser = new QueryParser(".class1 > .class2");
 Evaluator evaluator = parser.parse();
 Evaluator expected = new CombiningEvaluator.And(
 new StructuralEvaluator.ImmediateParent(new Evaluator.Class("class1")),
 new Evaluator.Class("class2")
 );
 Assert.assertEquals(expected, evaluator);
 }

 @Test
 public void testParseMultipleCombinations() {
 QueryParser parser = new QueryParser(".class1 > .class2 > .class3");
 Evaluator evaluator = parser.parse();
 Evaluator expected = new CombiningEvaluator.And(
 new StructuralEvaluator.ImmediateParent(new Evaluator.Class("class1")),
 new Evaluator.Class("class2"),
 new Evaluator.Class("class3")
 );
 Assert.assertEquals(expected, evaluator);
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser parser = new QueryParser("[href='mailto:test@example.com']");
 Evaluator evaluator = parser.parse();
 Evaluator expected = new Evaluator.AttributeWithValue(
 "href", "mailto:test@example.com"
 );
 Assert.assertEquals(expected, evaluator);
 }

 @Test
 public void testParseClassListSelector() {
 QueryParser parser = new QueryParser(".class1, .class2, .class3");
 Evaluator evaluator = parser.parse();
 Evaluator expected = new CombiningEvaluator.Or(
 new CombiningEvaluator.And(new Evaluator.Class("class1"), new Evaluator.Class("class2")),
 new Evaluator.Class("class3")
 );
 Assert.assertEquals(expected, evaluator);
 }

 @Test
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser(":not(#id:foo)"); // no combinator after :not
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser("div.class1 > ul > li:first-child:has(a:contains('Link Text'))");
 Evaluator evaluator = parser.parse();
 // complex query structure:
 //   div.class1
 //   └── ul
 //       └── li:first-child
 //           └── a:contains('Link Text')
 Evaluator expected = new CombiningEvaluator.And(
 new Evaluator.Tag("div"),
 new Evaluator.Class("class1"),
 new CombiningEvaluator.And(
 new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("ul")),
 new StructuralEvaluator.FirstChild(),
 new Evaluator.Class("li"),
 new StructuralEvaluator.Has(
 new Evaluator.ContainsText("Link Text")
 )
 )
 );
 Assert.assertEquals(expected, evaluator);
 }
}