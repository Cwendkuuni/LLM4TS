import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

 private static final String QUERY_PARSER_TEST_QUERY = "div.class1 > div.class2 > ul#id1 > li:first-child:containsOwn(Test)";

 @Test
 public void testParseQuery() {
 QueryParser queryParser = new QueryParser(QUERY_PARSER_TEST_QUERY);
 Selector expectedSelector = Selector.parse("div.class1 > div.class2 > ul#id1 > li:first-child:containsOwn(Test)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 try {
 QueryParser queryParser = new QueryParser("invalid query");
 } catch (Selector.SelectorParseException e) {
 // Expected exception
 assertEquals("Invalid query: invalid query", e.getMessage());
 }
 }

 @Test
 public void testParseQueryWithCombinators() {
 QueryParser queryParser = new QueryParser("div.class1 > div.class2");
 Selector expectedSelector = Selector.parse("div > div.class2");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithId() {
 QueryParser queryParser = new QueryParser("#id1");
 Selector expectedSelector = Selector.parse("#id1");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithClass() {
 QueryParser queryParser = new QueryParser(".class1");
 Selector expectedSelector = Selector.parse("class:class1");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithTag() {
 QueryParser queryParser = new QueryParser("ul");
 Selector expectedSelector = Selector.parse("tag:ul");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithAttribute() {
 QueryParser queryParser = new QueryParser("[title]");
 Selector expectedSelector = Selector.parse("[attr=title]");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithAttributeValue() {
 QueryParser queryParser = new QueryParser("[title='Test']");
 Selector expectedSelector = Selector.parse("[attr='Test']");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithPseudoClass() {
 QueryParser queryParser = new QueryParser("li:first-child");
 Selector expectedSelector = Selector.parse("li:first-child");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithPseudoElement() {
 QueryParser queryParser = new QueryParser("ul > li:first-child > a:first-child::before");
 Selector expectedSelector = Selector.parse("ul > li > a:first-child + ::before");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithNthChild() {
 QueryParser queryParser = new QueryParser("div.class1 > ul > li:nth-child(2)");
 Selector expectedSelector = Selector.parse("div > ul > li:nth-child(2)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithNthLastChild() {
 QueryParser queryParser = new QueryParser("div.class1 > ul > li:nth-last-child(2)");
 Selector expectedSelector = Selector.parse("div > ul > li:nth-last-child(2)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithNthOfType() {
 QueryParser queryParser = new QueryParser("li:nth-of-type(2)");
 Selector expectedSelector = Selector.parse("li:nth-of-type(2)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithNthLastOfType() {
 QueryParser queryParser = new QueryParser("li:nth-last-of-type(2)");
 Selector expectedSelector = Selector.parse("li:nth-last-of-type(2)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithHas() {
 QueryParser queryParser = new QueryParser("ul:has(li.class1)");
 Selector expectedSelector = Selector.parse("ul > li.class1");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithContains() {
 QueryParser queryParser = new QueryParser("li:contains(Test)");
 Selector expectedSelector = Selector.parse("li:contains(Test)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithContainsOwn() {
 QueryParser queryParser = new QueryParser("li:containsOwn(Test)");
 Selector expectedSelector = Selector.parse("li:containsOwn(Test)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithMatches() {
 QueryParser queryParser = new QueryParser("li:matches(.*Test.*)");
 Selector expectedSelector = Selector.parse("li:matches(.*Test.*)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithMatchesOwn() {
 QueryParser queryParser = new QueryParser("li:matchesOwn(.*Test.*)");
 Selector expectedSelector = Selector.parse("li:matchesOwn(.*Test.*)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }

 @Test
 public void testParseQueryWithNot() {
 QueryParser queryParser = new QueryParser("li:not(.class1)");
 Selector expectedSelector = Selector.parse("li:not(.class1)");
 Selector actualSelector = queryParser.parse().getSelector();
 assertEquals(expectedSelector.toString(), actualSelector.toString());
 }
}