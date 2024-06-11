package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;

public class QueryParserTest {

	@Test
	public void testParseSimpleSelector() {
		String query = "div";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.Tag("div");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseClassSelector() {
		String query = ".class1";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.Class("class1");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseIDSelector() {
		String query = "#id1";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.Id("id1");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseTagWithNamespaceSelector() {
		String query = "html:body";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new CombiningEvaluator.Or(new Evaluator.Tag("html"), new Evaluator.TagEndsWith("body"));
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeSelector() {
		String query = "[title]";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.Attribute("title");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeWithValueSelector() {
		String query = "[title='Hello World']";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.AttributeWithValue("title", "Hello World");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeStartingWithValueSelector() {
		String query = "[title^='Hello']";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.AttributeWithValueStarting("title", "Hello");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeEndsWithValueSelector() {
		String query = "[title$='World']";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.AttributeWithValueEnding("title", "World");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeContainsValueSelector() {
		String query = "[title*='World']";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.AttributeWithValueContaining("title", "World");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeMatchesValueSelector() {
		String query = "[title~='World']";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.AttributeWithValueMatching("title", Pattern.compile("World"));
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseAttributeNotSelector() {
		String query = "[title!='Hello World']";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.AttributeWithValueNot("title", "Hello World");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseContainingTextSelector() {
		String query = ":contains(Text)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.ContainsText("Text");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseContainingOwnTextSelector() {
		String query = ":containsOwn(Text)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.ContainsOwnText("Text");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseContainingDataSelector() {
		String query = ":containsData(Data)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.ContainsData("Data");
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseMatchesSelector() {
		String query = ":matches(regex)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.Matches(Pattern.compile(query.substring(query.indexOf(":matches(") + 9, query.length() - 1).replaceAll("\\\\.", ".")));
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseMatchesOwnSelector() {
		String query = ":matchesOwn(regex)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.MatchesOwn(Pattern.compile(query.substring(query.indexOf(":matchesOwn(") + 10, query.length() - 1).replaceAll("\\\\.", ".")));
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseNotSelector() {
		String query = ":not(selector)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new StructuralEvaluator.Not(QueryParser.parse(query.substring(query.indexOf(":not(") + 5, query.length() - 1)));
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinedSelectors() {
		String query = "div:not(.error) > input";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new CombiningEvaluator.And(
				new StructuralEvaluator.Not(new Evaluator.Class("error")),
				new Evaluator.Tag("div"),
				new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("input"))
		);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinators() {
		String query = "div > p ~ ul";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new CombiningEvaluator.And(
				new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("div"), new Evaluator.Tag("p")),
				new StructuralEvaluator.PreviousSibling(new Evaluator.Tag("ul"))
		);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithMultipleSelectors() {
		String query = "div > p, div > ul > li";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new CombiningEvaluator.Or(
				new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("p"))),
				new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateChildOf(new Evaluator.Tag("ul"), new Evaluator.Tag("li")))
		);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithNthChild() {
		String query = "ul > li:nth-child(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsNthChild(1, 2);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithNthLastChild() {
		String query = "ul > li:nth-last-child(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsNthLastChild(1, 2);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithNthOfType() {
		String query = "ul > li:nth-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsNthOfType(1, 2);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithNthLastOfType() {
		String query = "ul > li:nth-last-of-type(2)";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsNthLastOfType(1, 2);
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithFirstChild() {
		String query = "ul > li:first-child";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsFirstChild();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithLastChild() {
		String query = "ul > li:last-child";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsLastChild();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithFirstOfType() {
		String query = "ul > li:first-of-type";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsFirstOfType();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithLastOfType() {
		String query = "ul > li:last-of-type";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsLastOfType();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithOnlyChild() {
		String query = "ul > li:only-child";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsOnlyChild();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithOnlyOfType() {
		String query = "ul > li:only-of-type";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsOnlyOfType();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithEmpty() {
		String query = "ul > li:empty";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsEmpty();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithRoot() {
		String query = ":root";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new Evaluator.IsRoot();
		assertEquals(expectedEvaluator, evaluator);
	}

	@Test
	public void testParseCombinatorWithHasSelector() {
		String query = ":has(p:contains(Text)) > li";
		Evaluator evaluator = QueryParser.parse(query);
		Evaluator expectedEvaluator = new StructuralEvaluator.Has(
				new Evaluator.Tag("p"),
				new Evaluator.ContainsText("Text")
		).and(new Evaluator.Tag("li"));
		assertEquals(expectedEvaluator, evaluator);
	}
}