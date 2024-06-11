package org.jsoup.select.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.jsoup.parser.TokenQueue;

import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

	@org.junit.Test
	public void testParseSimpleSelector() {
		String query = "div.class1";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("div"), new Evaluator.Class("class1"));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithCombinators() {
		String query = "div > ul > li:first-child";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("div"), new CombiningEvaluator.And(new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("ul")), new Evaluator.IsFirstChild()));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithGrouping() {
		String query = "div.class1, ul li:first-child";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = new ArrayList<Evaluator>();
		expectedEvaluators.add(new Evaluator.Tag("div"));
		expectedEvaluators.add(new Evaluator.Class("class1"));
		expectedEvaluators.add(new CombiningEvaluator.Or(new Evaluator.Tag("ul"), new StructuralEvaluator.Has(new Evaluator.IsFirstChild())));
		expectedEvaluators.add(new Evaluator.Tag("li"));
		expectedEvaluators.add(new Evaluator.IsFirstChild());
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithAttributes() {
		String query = "input[type='text']";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("input"), new Evaluator.Attribute(new String[]{"type"}, new String[]{"text"}));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithNotOperator() {
		String query = "div:not(.class1)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = new ArrayList<Evaluator>();
		expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("class1")));
		expectedEvaluators.add(new Evaluator.Tag("div"));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithNthChild() {
		String query = "li:nth-child(3)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("li"), new Evaluator.IsNthChild(0, 3));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithNthOfType() {
		String query = "li:nth-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("li"), new Evaluator.IsNthOfType(0, 2));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithNthLastChild() {
		String query = "li:nth-last-child(3)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("li"), new Evaluator.IsNthLastChild(0, 3));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithNthLastOfType() {
		String query = "li:nth-last-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("li"), new Evaluator.IsNthLastOfType(0, 2));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithContainingText() {
		String query = "span:contains(test)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("span"), new Evaluator.ContainsText("test"));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithContainingData() {
		String query = "input:containsData(data)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("input"), new Evaluator.ContainsData("data"));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithMatches() {
		String query = "li:matches(^foo$)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("li"), new Evaluator.Matches(Pattern.compile("^foo$")));
		assertEquals(expectedEvaluators, evaluator.toList());
	}

	@org.junit.Test
	public void testParseComplexSelectorWithMatchesOwn() {
		String query = "li:matchesOwn(^foo$)";
		Evaluator evaluator = QueryParser.parse(query);
		assertNotNull(evaluator);
		List<Evaluator> expectedEvaluators = Arrays.asList(new Evaluator.Tag("li"), new Evaluator.MatchesOwn(Pattern.compile("^foo$")));
		assertEquals(expectedEvaluators, evaluator.toList());
	}
}