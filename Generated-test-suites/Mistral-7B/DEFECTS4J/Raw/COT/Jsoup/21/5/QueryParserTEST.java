import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Has;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.Not;
import org.jsoup.select.StructuralEvaluator.Or;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.Tag;
import org.jsoup.select.TestElement;
import org.junit.Test;

public class QueryParserTest {

 private void assertEvaluatorMatches(Evaluator evaluator, TestElement element) {
 assertEvaluatorMatches(evaluator, element, null);
 }

 private void assertEvaluatorMatches(Evaluator evaluator, TestElement element, TestElement expected) {
 TestElement actual = evaluator.matches(element);
 if (expected != null) {
 assertEquals(expected.tagName(), actual.tagName());
 assertEquals(expected.attributes(), actual.attributes());
 } else {
 assertTrue(actual != null);
 }
 }

 @Test
 public void testParse_EmptyQuery() {
 String query = "";
 Evaluator parser = QueryParser.parse(query);
 assertTrue(parser instanceof Root);
 }

 @Test
 public void testParse_SingleElement() {
 String query = "div";
 Evaluator parser = QueryParser.parse(query);
 assertEvaluatorMatches(parser, new TestElement("div"));
 }

 @Test
 public void testParse_ClassSelector() {
 String query = ".button";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.Class("button");
 assertEvaluatorMatches(parser, new TestElement("button"));
 assertEvaluatorMatches(parser, new TestElement("input", "class", "button"));
 assertEvaluatorMatches(parser, new TestElement("button", "data-role", "button"));
 }

 @Test
 public void testParse_IDSelector() {
 String query = "#main";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.Id("main");
 assertEvaluatorMatches(parser, new TestElement("main"));
 }

 @Test
 public void testParse_TagNameSelector() {
 String query = "ul";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.Tag("ul");
 assertEvaluatorMatches(parser, new TestElement("ul"));
 }

 @Test
 public void testParse_Combinator_Child() {
 String query = "div > ul";
 Evaluator parser = QueryParser.parse(query);
 And and = (And) parser;
 assertEquals(1, and.size());
 Evaluator child = and.get(0);
 assertEquals(StructuralEvaluator.Type.IMMEDIATE_PARENT, child.getCombinerType());
 assertEquals(EvaluatorType.ELEMENT, child.getType());
 Evaluator expectedChild = new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("div"));
 assertEvaluatorMatches(expectedChild, new TestElement("ul", "class", "child-of-div"));
 }

 @Test
 public void testParse_Combinator_Descendant() {
 String query = "div ul";
 Evaluator parser = QueryParser.parse(query);
 And and = (And) parser;
 assertEquals(1, and.size());
 Evaluator child = and.get(0);
 assertEquals(StructuralEvaluator.Type.PARENT, child.getCombinerType());
 assertEquals(EvaluatorType.ELEMENT, child.getType());
 Evaluator expectedChild = new StructuralEvaluator.Parent(new Evaluator.Tag("div"));
 assertEvaluatorMatches(expectedChild, new TestElement("ul", "class", "descendant-of-div"));
 }

 @Test
 public void testParse_AttributeSelector_Equal() {
 String query = "[href='https://example.com']";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValue("href", "https://example.com");
 assertEvaluatorMatches(parser, new TestElement("a", "href", "https://example.com"));
 assertEvaluatorMatches(parser, new TestElement("img", "src", "https://example.com"));
 }

 @Test
 public void testParse_AttributeSelector_NotEqual() {
 String query = "[href!='https://example.com']";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValueNot("href", "https://example.com");
 assertEvaluatorMatches(parser, new TestElement("a", "href", "http://example.com"));
 assertEvaluatorMatches(parser, new TestElement("img", "src", "https://another-example.com"));
 assertEvaluatorMatches(parser, new TestElement("div"));
 }

 @Test
 public void testParse_AttributeSelector_StartsWith() {
 String query = "[src^='https://']";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValueStarting("src", "https://");
 assertEvaluatorMatches(parser, new TestElement("img", "src", "https://example.com"));
 assertEvaluatorMatches(parser, new TestElement("script", "src", "https://code.jquery.com/jquery-3.3.1.min.js"));
 }

 @Test
 public void testParse_AttributeSelector_EndsWith() {
 String query = "[href$='.pdf']";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValueEnding("href", ".pdf");
 assertEvaluatorMatches(parser, new TestElement("a", "href", "https://example.com/document.pdf"));
 assertEvaluatorMatches(parser, new TestElement("a", "href", "https://example.com/document.pdf?version=1"));
 }

 @Test
 public void testParse_AttributeSelector_Contains() {
 String query = "[title*='inner']";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValueContaining("title", "inner");
 assertEvaluatorMatches(parser, new TestElement("h1", "title", "Inner Page"));
 assertEvaluatorMatches(parser, new TestElement("h1", "title", "Welcome to Inner Page"));
 }

 @Test
 public void testParse_AttributeSelector_Matches() {
 String query = "[title~='home']";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValueMatching("title", Pattern.compile("home"));
 assertEvaluatorMatches(parser, new TestElement("a", "title", "Home"));
 assertEvaluatorMatches(parser, new TestElement("li", "title", "Go to Home"));
 }

 @Test
 public void testParse_AttributeSelector_MatchesOwn() {
 String query = "[title~=own]";
 Evaluator parser = QueryParser.parse(query);
 Evaluator expected = new Evaluator.AttributeWithValueMatching(null, Pattern.compile("own"));
 assertEvaluatorMatches(parser, new TestElement("h1", "title", "Own Page"));
 assertEvaluatorMatches(parser, new TestElement("h1", "data-own", "Own Page"));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_Unhandled_PseudoClass() {
 String query = ":foo";
 QueryParser.parse(query);
 fail("QueryParser should not parse unhandled pseudo-classes");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_Unhandled_Attribute() {
 String query = "[bar]";
 QueryParser.parse(query);
 fail("QueryParser should not parse unhandled attributes");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_Unhandled_Combinator() {
 String query = "div ^ ul";
 QueryParser.parse(query);
 fail("QueryParser should not parse unhandled combinators");
 }
}