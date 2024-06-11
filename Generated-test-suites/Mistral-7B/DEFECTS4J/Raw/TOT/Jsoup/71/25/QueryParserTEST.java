import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.Validate;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Assert.assertNull(parser.parse());
 }

 @Test
 public void testParseSimpleQuerySelector() {
 QueryParser parser = new QueryParser("#myId");
 Assert.assertEquals(new Evaluator.Id("myId"), parser.parse());
 }

 @Test
 public void testParseSimpleTagSelector() {
 QueryParser parser = new QueryParser("div");
 Assert.assertEquals(new Evaluator.Tag("div"), parser.parse());
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser parser = new QueryParser("[class='red']");
 Assert.assertEquals(new Evaluator.Class("red"), parser.parse());
 }

 @Test
 public void testParseClassAndIDSelector() {
 QueryParser parser = new QueryParser(".red #myId");
 Assert.assertEquals(new CombiningEvaluator.And(
 new Evaluator.Class("red"),
 new Evaluator.Id("myId")),
 parser.parse());
 }

 @Test
 public void testParseDescendantSelector() {
 QueryParser parser = new QueryParser("div ul li");
 Assert.assertEquals(new StructuralEvaluator.Descendant(
 new Evaluator.Tag("li"),
 new Evaluator.Tag("ul"),
 new Evaluator.Tag("div")),
 parser.parse());
 }

 @Test
 public void testParseChildSelector() {
 QueryParser parser = new QueryParser("div > ul > li");
 Assert.assertEquals(new StructuralEvaluator.Child(
 new Evaluator.Tag("li"),
 new Evaluator.Tag("ul"),
 new Evaluator.Tag("div")),
 parser.parse());
 }

 @Test
 public void testParseSiblingSelector() {
 QueryParser parser = new QueryParser("li + li");
 Assert.assertEquals(new StructuralEvaluator.Adjacent(
 new Evaluator.Tag("li"),
 new Evaluator.Tag("li")),
 parser.parse());
 }

 @Test
 public void testParseCombinatorError1() {
 QueryParser parser = new QueryParser("#myId > div + ul");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseCombinatorError2() {
 QueryParser parser = new QueryParser("div ~ ul > li");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParsePseudoClass() {
 QueryParser parser = new QueryParser("li:first-child");
 Assert.assertEquals(new Evaluator.IsFirstChild(), parser.parse());
 }

 @Test
 public void testParsePseudoClassError() {
 QueryParser parser = new QueryParser("li:1st-child");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParsePseudoElement() {
 QueryParser parser = new QueryParser("ul > li:first-child > ul > li:first-child:before");
 Assert.assertEquals(new Evaluator.PseudoElement("before"), parser.parse());
 }

 @Test
 public void testParseNthChildSelector() {
 QueryParser parser = new QueryParser("li:nth-child(3)");
 Assert.assertEquals(new Evaluator.IsNthChild(3, 0), parser.parse());
 }

 @Test
 public void testParseNthChildSelectorWithOnly() {
 QueryParser parser = new QueryParser("li:nth-child(3) of type p");
 Assert.assertEquals(new Evaluator.IsNthOfType(3, 0), parser.parse());
 }

 @Test
 public void testParseNthLastChildSelector() {
 QueryParser parser = new QueryParser("li:nth-last-child(2)");
 Assert.assertEquals(new Evaluator.IsNthLastChild(2, 0), parser.parse());
 }

 @Test
 public void testParseNthLastChildSelectorWithOnly() {
 QueryParser parser = new QueryParser("li:nth-last-of-type(2)");
 Assert.assertEquals(new Evaluator.IsNthLastOfType(2, 0), parser.parse());
 }

 @Test
 public void testParseAttributeValueSelector() {
 QueryParser parser = new QueryParser("[href='/index.html']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("href", "/index.html"), parser.parse());
 }

 @Test
 public void testParseAttributeValueWithDashSelector() {
 QueryParser parser = new QueryParser("[data-foo='-bar']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("data-foo", "-bar"), parser.parse());
 }

 @Test
 public void testParseAttributeValueStartingSelector() {
 QueryParser parser = new QueryParser("[data-foo^='foo']");
 Assert.assertEquals(new Evaluator.AttributeWithValueStarting("data-foo", "foo"), parser.parse());
 }

 @Test
 public void testParseAttributeValueEndingSelector() {
 QueryParser parser = new QueryParser("[data-foo$='bar']");
 Assert.assertEquals(new Evaluator.AttributeWithValueEnding("data-foo", "bar"), parser.parse());
 }

 @Test
 public void testParseAttributeValueContainingSelector() {
 QueryParser parser = new QueryParser("[data-foo*='foo']");
 Assert.assertEquals(new Evaluator.AttributeWithValueContaining("data-foo", "foo"), parser.parse());
 }

 @Test
 public void testParseAttributeValueMatchingSelector() {
 QueryParser parser = new QueryParser("[data-foo~='bar']");
 Pattern pattern = Pattern.compile("(bar|baz)");
 Assert.assertEquals(new Evaluator.AttributeWithValueMatching("data-foo", pattern), parser.parse());
 }

 @Test
 public void testParseAttributeValueMatchingSelectorWithMultipleWords() {
 QueryParser parser = new QueryParser("[data-foo~='bar baz']");
 Pattern pattern = Pattern.compile("(bar|baz)");
 Assert.assertEquals(new Evaluator.AttributeWithValueMatching("data-foo", pattern), parser.parse());
 }

 @Test
 public void testParseAttributeValueMatchingSelectorWithMultiplePatterns() {
 QueryParser parser = new QueryParser("[data-foo~='bar'|'baz']");
 Pattern pattern = Pattern.compile("(bar|baz)");
 Assert.assertEquals(new Evaluator.AttributeWithValueMatching("data-foo", pattern), parser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithDashAndEqualSign() {
 QueryParser parser = new QueryParser("[data-foo='-class']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("data-foo", "-class"), parser.parse());
 }

 @Test
 public void testParseAttributeValueSelectorWithDashAndEqualSignAndQuotes() {
 QueryParser parser = new QueryParser("[data-foo='-class'\"']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("data-foo", "-class"), parser.parse());
 }

 @Test
 public void testParseAttributeSelectorWithNameContainingDash() {
 QueryParser parser = new QueryParser("[data-foo-bar]");
 Assert.assertEquals(new Evaluator.Attribute("data-foo-bar"), parser.parse());
 }

 @Test
 public void testParseAttributeSelectorWithNameContainingQuotes() {
 QueryParser parser = new QueryParser("[data-foo\"bar\"]");
 Assert.assertEquals(new Evaluator.Attribute("data-foo\"bar\""), parser.parse());
 }

 @Test
 public void testParseAttributeSelectorWithValueContainingQuotes() {
 QueryParser parser = new QueryParser("[data-foo='data-foo\"bar']");
 Assert.assertEquals(new Evaluator.AttributeWithValue("data-foo", "data-foo\"bar\""), parser.parse());
 }

 @Test
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("div:foo");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery2() {
 QueryParser parser = new QueryParser("div:foo bar");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery3() {
 QueryParser parser = new QueryParser("div:foo(bar)");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery4() {
 QueryParser parser = new QueryParser("div:foo[bar]");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery5() {
 QueryParser parser = new QueryParser("div:foo[data-foo[data-bar]]");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery6() {
 QueryParser parser = new QueryParser("div:foo[data-foo[data-bar]:foo]");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery7() {
 QueryParser parser = new QueryParser("div:foo[data-foo]:foo[data-bar]");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery8() {
 QueryParser parser = new QueryParser("div:foo[data-foo]:foo[data-bar]:foo");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery9() {
 QueryParser parser = new QueryParser("div:foo[data-foo]:foo[data-bar]:foo[data-foo]:foo");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }

 @Test
 public void testParseInvalidQuery10() {
 QueryParser parser = new QueryParser("div:foo[data-foo]:foo[data-bar]:foo[data-foo]:foo[data-bar]:foo[data-foo]:foo");
 Assert.assertThrows(Selector.SelectorParseException.class, () -> parser.parse());
 }
}