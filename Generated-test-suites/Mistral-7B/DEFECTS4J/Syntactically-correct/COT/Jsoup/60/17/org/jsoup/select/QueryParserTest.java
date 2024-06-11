import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 private String query;
 private QueryParser parser;
 private Evaluator expected;

 @Test
 public void testParseEmptyQuery() {
 QueryParser qp = new QueryParser("");
 assertNull(qp.parse());
 }

 @Test
 public void testParseRootElement() {
 query = "*";
 parser = new QueryParser(query);
 expected = new Evaluator.AllElements();
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseTagSelector() {
 query = "div";
 parser = new QueryParser(query);
 expected = new Evaluator.Tag("div");
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseClassSelector() {
 query = ".myClass";
 parser = new QueryParser(query);
 expected = new Evaluator.Class("myClass");
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseIdSelector() {
 query = "#myId";
 parser = new QueryParser(query);
 expected = new Evaluator.Id("myId");
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseCombinedSelectors() {
 query = "div.myClass";
 parser = new QueryParser(query);
 expected = new Evaluator.And(new Evaluator.Tag("div"), new Evaluator.Class("myClass"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseAttributeSelector() {
 query = "input[type='text']";
 parser = new QueryParser(query);
 expected = new Evaluator.Attribute("type", "text");
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseConsecutiveAttributeSelectors() {
 query = "input[type='text'][name='username']";
 parser = new QueryParser(query);
 expected = new Evaluator.And(new Evaluator.Attribute("type", "text"), new Evaluator.Attribute("name", "username"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseNegatedAttributeSelector() {
 query = "input:not([type='text'])";
 parser = new QueryParser(query);
 expected = new StructuralEvaluator.Not(new Evaluator.Attribute("type", "text"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 query = "div*";
 parser = new QueryParser(query);
 parser.parse(); // This should throw an exception
 }

 @Test
 public void testParseSelectorWithNthChild() {
 query = "li:nth-child(3)";
 parser = new QueryParser(query);
 expected = new Evaluator.IsNthChild(2, 1);
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithNthLastChild() {
 query = "li:nth-last-child(3)";
 parser = new QueryParser(query);
 expected = new Evaluator.IsNthLastChild(2, 1);
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithNthOfType() {
 query = "li:nth-of-type(3)";
 parser = new QueryParser(query);
 expected = new Evaluator.IsNthOfType(2, 1);
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithNthLastOfType() {
 query = "li:nth-last-of-type(3)";
 parser = new QueryParser(query);
 expected = new Evaluator.IsNthLastOfType(2, 1);
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithHas() {
 query = "ul > li:has(a)";
 parser = new QueryParser(query);
 expected = new StructuralEvaluator.Has(new Evaluator.Tag("a"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithContains() {
 query = "p:contains(text)";
 parser = new QueryParser(query);
 expected = new Evaluator.ContainsText("text");
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithContainsOwn() {
 query = "p:containsOwn(text)";
 parser = new QueryParser(query);
 expected = new Evaluator.ContainsOwnText("text");
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithMatches() {
 query = "p:matches(.*text)";
 parser = new QueryParser(query);
 expected = new Evaluator.Matches(Pattern.compile(".text"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithMatchesOwn() {
 query = "p:matchesOwn(.*text)";
 parser = new QueryParser(query);
 expected = new Evaluator.MatchesOwn(Pattern.compile(".text"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

 @Test
 public void testParseSelectorWithNot() {
 query = "p:not(#id)";
 parser = new QueryParser(query);
 expected = new StructuralEvaluator.Not(new Evaluator.Id("id"));
 assertNotNull(parser.parse());
 assertEquals(expected, parser.parse());
 }

}