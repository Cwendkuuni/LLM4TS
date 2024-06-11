import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.jsoup.parser.QueryParser.parse;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Assert.assertNull(queryParser.parse());
 }

 @Test
 public void testParseSimpleSelector() {
 QueryParser queryParser = new QueryParser("#myId");
 Assert.assertEquals(new Evaluator.Id("myId"), queryParser.parse());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".myClass");
 Assert.assertEquals(new Evaluator.Class("myClass"), queryParser.parse());
 }

 @Test
 public void testParseTagNameSelector() {
 QueryParser queryParser = new QueryParser("div");
 Assert.assertEquals(new Evaluator.Tag("div"), queryParser.parse());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser queryParser = new QueryParser("[href]");
 Assert.assertEquals(new Evaluator.Attribute("href"), queryParser.parse());
 }

 @Test
 public void testParseAttributeValueSelector() {
 QueryParser queryParser = new QueryParser("[href='example.html']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("href", "example.html"), queryParser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithOperator() {
 QueryParser queryParser = new QueryParser("[href!='example.html']");
 Assert.assertEquals(new Evaluator.AttributeWithValueNot("href", "example.html"), queryParser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithStartOperator() {
 QueryParser queryParser = new QueryParser("[href^='example']");
 Assert.assertEquals(new Evaluator.AttributeWithValueStarting("href", "example"), queryParser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithEndOperator() {
 QueryParser queryParser = new QueryParser("[href$='html']");
 Assert.assertEquals(new Evaluator.AttributeWithValueEnding("href", "html"), queryParser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithContainsOperator() {
 QueryParser queryParser = new QueryParser("[href*='example']");
 Assert.assertEquals(new Evaluator.AttributeWithValueContaining("href", "example"), queryParser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithMatchOperator() {
 QueryParser queryParser = new QueryParser("[href~='example']");
 String regex = "/.*example.*/";
 Assert.assertEquals(new Evaluator.AttributeWithValueMatching("href", Pattern.compile(regex)), queryParser.parse());
 }

 @Test
 public void testParseDescendantSelector() {
 QueryParser queryParser = new QueryParser("div > p");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new StructuralEvaluator.Tag("p");
 StructuralEvaluator and = new StructuralEvaluator.And(div, p);
 CombiningEvaluator andParent = new CombiningEvaluator.And(root, and);
 Assert.assertEquals(andParent, queryParser.parse());
 }

 @Test
 public void testParseAdjacentSiblingSelector() {
 QueryParser queryParser = new QueryParser("div > p + p");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p1 = new StructuralEvaluator.Tag("p");
 StructuralEvaluator p2 = new StructuralEvaluator.Tag("p");
 StructuralEvaluator and = new StructuralEvaluator.And(div, p1);
 StructuralEvaluator andPreviousSibling = new StructuralEvaluator.PreviousSibling(p1);
 CombiningEvaluator or = new CombiningEvaluator.Or(and, andPreviousSibling);
 CombiningEvaluator and = new CombiningEvaluator.And(root, or);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseGeneralSiblingSelector() {
 QueryParser queryParser = new QueryParser("div > p ~ p");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p1 = new StructuralEvaluator.Tag("p");
 StructuralEvaluator p2 = new StructuralEvaluator.Tag("p");
 StructuralEvaluator and = new StructuralEvaluator.And(div, p1);
 StructuralEvaluator previousSibling = new StructuralEvaluator.PreviousSibling(p1);
 CombiningEvaluator or = new CombiningEvaluator.Or(and, previousSibling);
 CombiningEvaluator and = new CombiningEvaluator.And(root, or);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseChildSelectorWithIndex() {
 QueryParser queryParser = new QueryParser("div > p:first-child");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new Evaluator.IsFirstChild();
 CombiningEvaluator and = new CombiningEvaluator.And(div, p);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseLastChildSelectorWithIndex() {
 QueryParser queryParser = new QueryParser("div > p:last-child");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new Evaluator.IsLastChild();
 CombiningEvaluator and = new CombiningEvaluator.And(div, p);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseNthChildSelector() {
 QueryParser queryParser = new QueryParser("div > p:nth-child(2)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new Evaluator.IsNthChild(2, 0);
 CombiningEvaluator and = new CombiningEvaluator.And(div, p);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseNthLastChildSelector() {
 QueryParser queryParser = new QueryParser("div > p:nth-last-child(2)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new Evaluator.IsNthLastChild(2, 0);
 CombiningEvaluator and = new CombiningEvaluator.And(div, p);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseNthOfTypeSelector() {
 QueryParser queryParser = new QueryParser("div p:nth-of-type(2)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new Evaluator.IsNthOfType(2, true);
 CombiningEvaluator and = new CombiningEvaluator.And(div, p);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseNthLastOfTypeSelector() {
 QueryParser queryParser = new QueryParser("div p:nth-last-of-type(2)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator p = new Evaluator.IsNthLastOfType(2, true);
 CombiningEvaluator and = new CombiningEvaluator.And(div, p);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseHasSelector() {
 QueryParser queryParser = new QueryParser("div:has(p)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator has = new StructuralEvaluator.Has(parse("p"));
 CombiningEvaluator and = new CombiningEvaluator.And(div, has);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseContainsSelector() {
 QueryParser queryParser = new QueryParser("div:contains(text)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 Evaluator containsText = new Evaluator.ContainsText("text");
 CombiningEvaluator and = new CombiningEvaluator.And(div, containsText);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseContainsDataSelector() {
 QueryParser queryParser = new QueryParser("div:containsData(data)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 Evaluator containsData = new Evaluator.ContainsData("data");
 CombiningEvaluator and = new CombiningEvaluator.And(div, containsData);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseMatchesSelector() {
 QueryParser queryParser = new QueryParser("div:matches(regex)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 Evaluator matches = new Evaluator.Matches(Pattern.compile("/.*regex.*/"));
 CombiningEvaluator and = new CombiningEvaluator.And(div, matches);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseMatchesOwnSelector() {
 QueryParser queryParser = new QueryParser("div:matchesOwn(regex)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 Evaluator matchesOwn = new Evaluator.MatchesOwn(Pattern.compile("/.*regex.*/"));
 CombiningEvaluator and = new CombiningEvaluator.And(div, matchesOwn);
 Assert.assertEquals(and, queryParser.parse());
 }

 @Test
 public void testParseNotSelector() {
 QueryParser queryParser = new QueryParser("div:not(p)");
 StructuralEvaluator root = new StructuralEvaluator.Root();
 StructuralEvaluator div = new StructuralEvaluator.Tag("div");
 StructuralEvaluator not = new StructuralEvaluator.Not(parse("p"));
 Assert.assertEquals(not, queryParser.parse());
 }
}