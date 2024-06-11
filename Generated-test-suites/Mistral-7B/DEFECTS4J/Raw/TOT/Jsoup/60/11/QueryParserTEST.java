package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

	@Test
	public void testParseEmptyQuery() {
		String query = "";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.AllElements);
	}

	@Test
	public void testParseTagQuery() {
		String query = "div";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.Tag);
		assertEquals("div", evaluator.toString());
	}

	@Test
	public void testParseClassQuery() {
		String query = ".myClass";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.Class);
		assertEquals(".myClass", evaluator.toString());
	}

	@Test
	public void testParseIDQuery() {
		String query = "#myID";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.Id);
		assertEquals("#myID", evaluator.toString());
	}

	@Test
	public void testParseAttributeQuery() {
		String query = "[title='Hello']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
		assertEquals("[title='Hello']", evaluator.toString());
	}

	@Test
	public void testParseAttributeQueryWithOperator() {
		String query = "[class!='myClass']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
		assertEquals("[class!='myClass']", evaluator.toString());
	}

	@Test
	public void testParseAttributeQueryWithPrefix() {
		String query = "[data-prefix='prefix']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueStarting);
		assertEquals("[data-prefix='prefix']", evaluator.toString());
	}

	@Test
	public void testParseAttributeQueryWithSuffix() {
		String query = "[data-suffix='suffix']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
		assertEquals("[data-suffix='suffix']", evaluator.toString());
	}

	@Test
	public void testParseAttributeQueryWithSubstring() {
		String query = "[data*='prefix']";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
		assertEquals("[data*='prefix']", evaluator.toString());
	}

	@Test
	public void testParseCombinedQuery() {
		String query = "div.myClass#myID:contains(Hello)";
		Evaluator evaluator = QueryParser.parse(query);
		assertEquals(evaluator.getClass(), CombiningEvaluator.class);
		assertEquals(evaluator.toString(), "div.myClass#myID:contains(Hello)");
	}

	@Test(expected = Selector.SelectorParseException.class)
	public void testParseInvalidQuery() {
		String query = "div@myClass";
		QueryParser.parse(query);
	}

	@Test(expected = Selector.SelectorParseException.class)
	public void testParseInvalidQuery2() {
		String query = "div[class=myClass#myID]";
		QueryParser.parse(query);
	}

	@Test
	public void testParseNthChildQuery() {
		String query = "li:nth-child(2)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.IsNthChild);
		assertEquals("li:nth-child(2)", evaluator.toString());
	}

	@Test
	public void testParseNthLastChildQuery() {
		String query = "li:nth-last-child(2)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
		assertEquals("li:nth-last-child(2)", evaluator.toString());
	}

	@Test
	public void testParseNthOfTypeQuery() {
		String query = "li:nth-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.IsNthOfType);
		assertEquals("li:nth-of-type(2)", evaluator.toString());
	}

	@Test
	public void testParseNthLastOfTypeQuery() {
		String query = "li:nth-last-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.IsNthLastOfType);
		assertEquals("li:nth-last-of-type(2)", evaluator.toString());
	}

	@Test
	public void testParseHasQuery() {
		String query = "li:has(.myClass)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof StructuralEvaluator.Has);
		assertEquals("li:has(.myClass)", evaluator.toString());
	}

	@Test
	public void testParseContainsTextQuery() {
		String query = "li:contains(Hello)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.ContainsText);
		assertEquals("li:contains(Hello)", evaluator.toString());
	}

	@Test
	public void testParseContainsOwnTextQuery() {
		String query = "li:containsOwn(Hello)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
		assertEquals("li:containsOwn(Hello)", evaluator.toString());
	}

	@Test
	public void testParseContainsDataQuery() {
		String query = "li:containsData(data:value)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.ContainsData);
		assertEquals("li:containsData(data:value)", evaluator.toString());
	}

	@Test
	public void testParseMatchesQuery() {			String query = "li:matches(.myClass)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.Matches);
		assertEquals("li:matches(.myClass)", evaluator.toString());
	}

	@Test
	public void testParseMatchesOwnQuery() {
		String query = "li:matchesOwn(.myClass)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof Evaluator.MatchesOwn);
		assertEquals("li:matchesOwn(.myClass)", evaluator.toString());
	}

	@Test
	public void testParseNotQuery() {
		String query = "li:not(.myClass)";
		Evaluator evaluator = QueryParser.parse(query);
		assertTrue(evaluator instanceof StructuralEvaluator.Not);
		assertEquals("li:not(.myClass)", evaluator.toString());
	}

	@Test
	public void testParseMultipleQueries() {
		String query = "div.myClass#myID:nth-child(2):contains(Hello):not(.otherClass)";
		Evaluator evaluator = QueryParser.parse(query);
		assertEquals(evaluator.getClass(), CombiningEvaluator.class);
		assertEquals(evaluator.toString(), "div.myClass#myID:nth-child(2):contains(Hello):not(.otherClass)");
	}
}