package org.jsoup.select;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;

import org.junit.Test;

/**
 * Test class for QueryParser
 */
public class QueryParserTest {

	@Test
	public void testParse() {
		String query = "div.class1 > p#id1 > ul.class2 > li.class3";
		QueryParser parser = new QueryParser(query);
		Evaluator eval = parser.parse();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testParseEmptyQuery() {
		String query = "";
		QueryParser parser = new QueryParser(query);
		Evaluator eval = parser.parse();
		assertEquals(Evaluator.class, eval.getClass());
	}

	@Test(expected = Selector.SelectorParseException.class)
	public void testParseInvalidQuery() {
		String query = "div.class1 > #id1 > ul.class2 > li.class3 >";
		QueryParser parser = new QueryParser(query);
		parser.parse();
	}

	@Test
	public void testCombinator() {
		String query = "div > p";
		QueryParser parser = new QueryParser(query);
		parser.combinator('>');
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testCombinatorParent() {
		String query = "div > p";
		QueryParser parser = new QueryParser(query);
		parser.combinator(' ');
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testCombinatorPreviousSibling() {
		String query = "div > p + ul";
		QueryParser parser = new QueryParser(query);
		parser.combinator('+');
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testCombinatorPreviousSiblingAll() {
		String query = "div > p ~ ul";
		QueryParser parser = new QueryParser(query);
		parser.combinator('~');
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testCombinatorOr() {
		String query = "div > p, div > ul";
		QueryParser parser = new QueryParser(query);
		parser.combinator(',');
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsById() {
		String query = "#id1";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByClass() {
		String query = ".class1";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByTag() {
		String query = "p";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByTagWithNamespace() {
		String query = "html:p";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttribute() {
		String query = "[title]";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttributeValue() {
		String query = "[title=Home]";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttributeValueNot() {
		String query = "[title!='Home']";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttributeValueStarting() {
		String query = "[title^='Home']";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttributeValueEnding() {
		String query = "[title$='Home']";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttributeValueContaining() {
		String query = "[title*='Home']";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByAttributeValueMatching() {
		String query = "[title~='Home']";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindAllElements() {
		String query = "*";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByIndexLessThan() {
		String query = ":lt(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByIndexGreaterThan() {
		String query = ":gt(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByIndexEquals() {
		String query = ":eq(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByHas() {
		String query = ":has(p)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByContains() {
		String query = ":contains(Text)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByContainsOwn() {
		String query = ":containsOwn(Text)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByContainsData() {
		String query = ":containsData(Data)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByMatches() {
		String query = ":matches(RegExp)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByMatchesOwn() {
		String query = ":matchesOwn(RegExp)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByNot() {
		String query = ":not(Selector)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByNthChild() {
		String query = ":nth-child(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByNthLastChild() {
		String query = ":nth-last-child(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByNthOfType() {
		String query = ":nth-of-type(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByNthLastOfType() {
		String query = ":nth-last-of-type(2)";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByFirstChild() {
		String query = ":first-child";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByLastChild() {
		String query = ":last-child";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByFirstOfType() {
		String query = ":first-of-type";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByLastOfType() {
		String query = ":last-of-type";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByEmpty() {
		String query = ":empty";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}

	@Test
	public void testFindElementsByRoot() {
		String query = ":root";
		QueryParser parser = new QueryParser(query);
		parser.findElements();
		// TODO: Add assertions for the evaluator tree
	}
}