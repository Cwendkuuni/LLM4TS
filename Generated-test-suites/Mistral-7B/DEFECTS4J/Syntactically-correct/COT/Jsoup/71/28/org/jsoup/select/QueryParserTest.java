package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

import org.junit.Test;

public class QueryParserTest {

	@Test
	public void testParseEmptyQuery() {
		String query = "";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.class, evaluator.getClass());
	}

	@Test
	public void testParseSimpleTagSelector() {
		String query = "div";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.Tag.class, evaluator.getClass());
		assertEquals("div", evaluator.toString());
	}

	@Test
	public void testParseClassSelector() {
		String query = ".myClass";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.Class.class, evaluator.getClass());
		assertEquals(".myClass", evaluator.toString());
	}

	@Test
	public void testParseIdSelector() {
		String query = "#myId";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.Id.class, evaluator.getClass());
		assertEquals("#myId", evaluator.toString());
	}

	@Test
	public void testParseCombinedSelectors() {
		String query = "div.myClass";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.And.class, evaluator.getClass());
		assertEquals("div.myClass", evaluator.toString());

		List<Evaluator> subEvaluators = ((Evaluator.And) evaluator).getSubEvaluators();
		assertEquals(2, subEvaluators.size());
		Evaluator tagEvaluator = subEvaluators.get(0);
		assertEquals(Evaluator.Tag.class, tagEvaluator.getClass());
		assertEquals("div", tagEvaluator.toString());

		Evaluator classEvaluator = subEvaluators.get(1);
		assertEquals(Evaluator.Class.class, classEvaluator.getClass());
		assertEquals(".myClass", classEvaluator.toString());
	}

	@Test
	public void testParseAttributeSelector() {
		String query = "[class='myClass']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.Attribute.class, evaluator.getClass());
		assertEquals("[class='myClass']", evaluator.toString());
	}

	@Test
	public void testParseAttributeSelectorWithValue() {
		String query = "[id='myId']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
		assertEquals("[id='myId']", evaluator.toString());
	}

	@Test
	public void testParseAttributeSelectorWithOperator() {
		String query = "[class~='myClass']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
		assertEquals("[class~='myClass']", evaluator.toString());
	}

	@Test
	public void testParseComplexSelector() {
		String query = "div.myClass#myId > ul > li:nth-child(2)";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.Combinator.class, evaluator.getClass());
		assertEquals("div.myClass#myId > ul > li:nth-child(2)", evaluator.toString());

		List<Evaluator> subEvaluators = ((Evaluator.Combinator) evaluator).getSubEvaluators();
		assertEquals(4, subEvaluators.size());
		Evaluator tag1Evaluator = subEvaluators.get(0);
		assertEquals(Evaluator.Tag.class, tag1Evaluator.getClass());
		assertEquals("div", tag1Evaluator.toString());

		Evaluator class1Evaluator = subEvaluators.get(1);
		assertEquals(Evaluator.Class.class, class1Evaluator.getClass());
		assertEquals(".myClass", class1Evaluator.toString());

		Evaluator id1Evaluator = subEvaluators.get(2);
		assertEquals(Evaluator.Id.class, id1Evaluator.getClass());
		assertEquals("#myId", id1Evaluator.toString());

		Evaluator combinatorEvaluator = subEvaluators.get(3);
		assertEquals(Evaluator.Child.class, combinatorEvaluator.getClass());
		assertEquals(">", combinatorEvaluator.toString());

		subEvaluators = ((Evaluator.Child) combinatorEvaluator).getSubEvaluators();
		assertEquals(2, subEvaluators.size());
		Evaluator tag2Evaluator = subEvaluators.get(0);
		assertEquals(Evaluator.Tag.class, tag2Evaluator.getClass());
		assertEquals("ul", tag2Evaluator.toString());

		Evaluator nthChildEvaluator = subEvaluators.get(1);
		assertEquals(Evaluator.IsNthChild.class, nthChildEvaluator.getClass());
		assertEquals("li:nth-child(2)", nthChildEvaluator.toString());
	}

	@Test
	public void testParseNthChildSelectorWithNegativeIndex() {
		String query = "li:nth-last-child(-2)";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
		assertEquals("li:nth-last-child(-2)", evaluator.toString());
	}

	@Test
	public void testParseNthChildSelectorWithOfType() {
		String query = "li:nth-of-type(2)";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
		assertEquals("li:nth-of-type(2)", evaluator.toString());
	}

	@Test
	public void testParseNotSelector() {
		String query = ":not(p)";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertNotNull(evaluator);
		assertEquals(Evaluator.Not.class, evaluator.getClass());
		assertEquals(":not(p)", evaluator.toString());

		List<Evaluator> subEvaluators = ((Evaluator.Not) evaluator).getSubEvaluators();
		assertEquals(1, subEvaluators.size());
		Evaluator tagEvaluator = subEvaluators.get(0);
		assertEquals(Evaluator.Tag.class, tagEvaluator.getClass());
		assertEquals("p", tagEvaluator.toString());
	}

	@Test(expected = Selector.SelectorParseException.class)
	public void testParseInvalidQuery() {
		String query = "div.myClass#myId > ul > li:nth-child(-3)";
		QueryParser parser = new QueryParser(query);
		parser.parse();
	}
}