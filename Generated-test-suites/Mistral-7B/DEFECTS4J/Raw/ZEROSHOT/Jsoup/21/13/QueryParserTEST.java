import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import static org.jsoup.helper.StringUtil.isQuotedString;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseQueryWithId() {
 QueryParser parser = new QueryParser("#myId");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#myId", evaluator.toString());
 }

 @Test
 public void testParseQueryWithClass() {
 QueryParser parser = new QueryParser(".myClass");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".myClass", evaluator.toString());
 }

 @Test
 public void testParseQueryWithTagAndClass() {
 QueryParser parser = new QueryParser("div.myClass");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
 assertEquals("div", ((Evaluator.Tag) evals.get(0)).toString());
 assertEquals(Evaluator.Class.class, evals.get(1).getClass());
 assertEquals(".myClass", ((Evaluator.Class) evals.get(1)).toString());
 }

 @Test
 public void testParseQueryWithCombinator() {
 QueryParser parser = new QueryParser("div > p");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
 assertEquals("div", ((Evaluator.Tag) evals.get(0)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(1).getClass());
 }

 @Test
 public void testParseQueryWithAttribute() {
 QueryParser parser = new QueryParser("input[type=text]");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
 assertEquals("input", ((Evaluator.Tag) evals.get(0)).toString());
 assertEquals(Evaluator.Attribute.class, evals.get(1).getClass());
 assertEquals("type", ((Evaluator.Attribute) evals.get(1)).getKey());
 assertEquals("text", ((Evaluator.Attribute) evals.get(1)).getValue());
 }

 @Test
 public void testParseQueryWithMultipleCombinators() {
 QueryParser parser = new QueryParser("div > p > ul > li");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
 assertEquals("div", ((Evaluator.Tag) evals.get(0)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(1).getClass());
 assertEquals(Evaluator.Tag.class, evals.get(1).getClass());
 assertEquals("p", ((Evaluator.Tag) evals.get(1)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(2).getClass());
 assertEquals(Evaluator.Tag.class, evals.get(2).getClass());
 assertEquals("ul", ((Evaluator.Tag) evals.get(2)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(3).getClass());
 assertEquals(Evaluator.Tag.class, evals.get(3).getClass());
 assertEquals("li", ((Evaluator.Tag) evals.get(3)).toString());
 }

 @Test
 public void testParseQueryWithIdAndClass() {
 QueryParser parser = new QueryParser("#myId.myClass");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Id.class, evals.get(0).getClass());
 assertEquals("#myId", ((Evaluator.Id) evals.get(0)).toString());
 assertEquals(Evaluator.Class.class, evals.get(1).getClass());
 assertEquals(".myClass", ((Evaluator.Class) evals.get(1)).toString());
 }

 @Test
 public void testParseQueryWithAttributeAndValue() {
 QueryParser parser = new QueryParser("input[type=text][value=hello]");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
 assertEquals("input", ((Evaluator.Tag) evals.get(0)).toString());
 assertEquals(Evaluator.Attribute.class, evals.get(1).getClass());
 assertEquals("type", ((Evaluator.Attribute) evals.get(1)).getKey());
 assertEquals("text", ((Evaluator.Attribute) evals.get(1)).getValue());
 assertEquals(Evaluator.Attribute.class, evals.get(2).getClass());
 assertEquals("value", ((Evaluator.Attribute) evals.get(2)).getKey());
 assertEquals("hello", ((Evaluator.Attribute) evals.get(2)).getValue());
 }

 @Test
 public void testParseQueryWithAttributeValueContaining() {
 QueryParser parser = new QueryParser("input[name*=search]");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
 assertEquals("search", ((Evaluator.ContainsText) evaluator).getValue());
 }

 @Test
 public void testParseQueryWithAttributeValueMatching() {
 QueryParser parser = new QueryParser("input[name~=search]");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Matches.class, evaluator.getClass());
 String regex = ((Evaluator.Matches) evaluator).getPattern().pattern();
 assertTrue(Pattern.matches("^[sS]earch.*", regex));
 }

 @Test
 public void testParseQueryWithAttributeValueNot() {
 QueryParser parser = new QueryParser("input:not([type=checkbox])");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.Not) evaluator).getChild().getClass());
 assertEquals("input", ((Evaluator.Tag) ((StructuralEvaluator.Not) evaluator).getChild()).toString());
 assertEquals(Evaluator.Attribute.class, ((StructuralEvaluator.Not) evaluator).getSelector().get(0).getClass());
 assertEquals("type", ((Evaluator.Attribute) ((StructuralEvaluator.Not) evaluator).getSelector().get(0)).getKey());
 assertEquals("checkbox", ((Evaluator.Attribute) ((StructuralEvaluator.Not) evaluator).getSelector().get(0)).getValue());
 }

 @Test
 public void testParseQueryWithPseudoSelectorHas() {
 QueryParser parser = new QueryParser("a:has(p:containsOwn(text))");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
 }

 @Test
 public void testParseQueryWithPseudoSelectorContains() {
 QueryParser parser = new QueryParser("div:containsOwn(text)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
 assertEquals("text", ((Evaluator.ContainsOwnText) evaluator).getValue());
 }

 @Test
 public void testParseQueryWithPseudoSelectorMatches() {
 QueryParser parser = new QueryParser("a:matchesOwn(.*text.*)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
 String regex = ((Evaluator.MatchesOwn) evaluator).getPattern().pattern();
 assertTrue(Pattern.matches("^.*text.*$", regex));
 }

 @Test
 public void testParseQueryWithPseudoSelectorNot() {
 QueryParser parser = new QueryParser("div:not(.myClass)");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 assertEquals(Evaluator.Class.class, ((StructuralEvaluator.Not) evaluator).getChild().getClass());
 assertEquals(".myClass", ((Evaluator.Class) ((StructuralEvaluator.Not) evaluator).getChild()).toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseQueryWithInvalidToken() {
 QueryParser parser = new QueryParser("div:not(.myClass):foo");
 }

 @Test
 public void testParseQueryWithComplexSelectors() {
 QueryParser parser = new QueryParser("div > ul > li > div > input:not([type=hidden])[type=password]:not([value=password])[value*=pass]");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evals = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(Evaluator.Tag.class, evals.get(0).getClass());
 assertEquals("div", ((Evaluator.Tag) evals.get(0)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(1).getClass());
 assertEquals(Evaluator.Tag.class, evals.get(1).getClass());
 assertEquals("ul", ((Evaluator.Tag) evals.get(1)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(2).getClass());
 assertEquals(Evaluator.Tag.class, evals.get(2).getClass());
 assertEquals("li", ((Evaluator.Tag) evals.get(2)).toString());
 assertEquals(StructuralEvaluator.ImmediateParent.class, evals.get(3).getClass());
 assertEquals(Evaluator.Tag.class, evals.get(3).getClass());
 assertEquals("div", ((Evaluator.Tag) evals.get(3)).toString());
 assertEquals(Evaluator.Tag.class, evals.get(4).getClass());
 assertEquals("input", ((Evaluator.Tag) evals.get(4)).toString());
 assertEquals(StructuralEvaluator.Not.class, evals.get(5).getClass());
 assertEquals(Evaluator.Tag.class, ((StructuralEvaluator.Not) evals.get(5)).getChild().getClass());
 assertEquals("input", ((Evaluator.Tag) ((StructuralEvaluator.Not) evals.get(5)).getChild()).toString());
 assertEquals(Evaluator.Attribute.class, ((StructuralEvaluator.Not) evals.get(5)).getSelector().get(0).getClass());
 assertEquals("type", ((Evaluator.Attribute) ((StructuralEvaluator.Not) evals.get(5)).getSelector().get(0)).getKey());
 assertEquals("hidden", ((Evaluator.Attribute) ((StructuralEvaluator.Not) evals.get(5)).getSelector().get(0)).getValue());
 assertEquals(Evaluator.Attribute.class, evals.get(6).getClass());
 assertEquals("type", ((Evaluator.Attribute) evals.get(6)).getKey());
 assertEquals("password", ((Evaluator.Attribute) evals.get(6)).getValue());
 assertEquals(Evaluator.Attribute.class, evals.get(7).getClass());
 assertEquals("value", ((Evaluator.Attribute) evals.get(7)).getKey());
 assertEquals("pass", ((Evaluator.Attribute) evals.get(7)).getValue());
 }
}