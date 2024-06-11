package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.ALL_ELEMENTS, evaluator);
    }

    @Test
    public void testParseTagQuery() {
        QueryParser parser = new QueryParser("p");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.Tag("p"), evaluator);
    }

    @Test
    public void testParseTagWithNamespaceQuery() {
        QueryParser parser = new QueryParser("html:body");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.TagWithNamespace("html", "body"), evaluator);
    }

    @Test
    public void testParseClassQuery() {
        QueryParser parser = new QueryParser("p.class1");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.Class("class1"), evaluator);
    }

    @Test
    public void testParseIdQuery() {
        QueryParser parser = new QueryParser("#id1");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.Id("id1"), evaluator);
    }

    @Test
    public void testParseAttributeQuery() {
        QueryParser parser = new QueryParser("[data-test=value]");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.AttributeWithValue("data-test", "value"), evaluator);
    }

    @Test
    public void testParseAttributeQueryWithOperator() {
        QueryParser parser = new QueryParser("[data-test!='value']");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.AttributeWithValueNot("data-test", "value"), evaluator);
    }

    @Test
    public void testParseAttributeQueryWithStartsWithOperator() {
        QueryParser parser = new QueryParser("[data-test^='value']");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.AttributeWithValueStarting("data-test", "value"), evaluator);
    }

    @Test
    public void testParseAttributeQueryWithEndsWithOperator() {
        QueryParser parser = new QueryParser("[data-test$='value']");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.AttributeWithValueEnding("data-test", "value"), evaluator);
    }

    @Test
    public void testParseAttributeQueryWithContainsOperator() {
        QueryParser parser = new QueryParser("[data-test*='value']");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.AttributeWithValueContaining("data-test", "value"), evaluator);
    }

    @Test
    public void testParseAttributeQueryWithMatchingOperator() {
        QueryParser parser = new QueryParser("[data-test~='value']");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.AttributeWithValueMatching("data-test", Pattern.compile("value")), evaluator);
    }

    @Test
    public void testParseMultipleCombinedSelectors() {
        QueryParser parser = new QueryParser("p.class1.class2#id1[data-test='value']");
        List<Evaluator> evaluators = parser.parse().asList();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new Evaluator.Class("class1"));
        expected.add(new Evaluator.Class("class2"));
        expected.add(new Evaluator.Id("id1"));
        expected.add(new Evaluator.AttributeWithValue("data-test", "value"));
        assertEquals(expected, evaluators);
    }

    @Test
    public void testParseMultipleCombinedSelectorsWithCombinator() {
        QueryParser parser = new QueryParser("p > ul > li.class1");
        List<Evaluator> evaluators = parser.parse().asList();
        List<Evaluator> expected = new ArrayList<>();
        expected.add(new Evaluator.Tag("p"));
        expected.add(new CombiningEvaluator.Descendant(new Evaluator.Tag("ul"), new Evaluator.Class("class1")));
        assertEquals(expected, evaluators);
    }

    @Test
    public void testParsePseudoClassQuery() {
        QueryParser parser = new QueryParser("p:first-child");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsFirstChild(), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithNthChild() {
        QueryParser parser = new QueryParser("li:nth-child(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsNthChild(1, 1), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithNthLastChild() {
        QueryParser parser = new QueryParser("li:nth-last-child(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsNthLastChild(1, 1), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithNthOfType() {
        QueryParser parser = new QueryParser("li:nth-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsNthOfType(1, 1), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithNthLastOfType() {
        QueryParser parser = new QueryParser("li:nth-last-of-type(2)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsNthLastOfType(1, 1), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithOnlyChild() {
        QueryParser parser = new QueryParser("li:only-child");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsOnlyChild(), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithOnlyOfType() {
        QueryParser parser = new QueryParser("li:only-of-type");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsOnlyOfType(), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithEmpty() {
        QueryParser parser = new QueryParser(":empty");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsEmpty(), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithRoot() {
        QueryParser parser = new QueryParser(":root");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.IsRoot(), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithNot() {
        QueryParser parser = new QueryParser("div:not(p)");
        Evaluator evaluator = parser.parse();
        assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("p")), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithHas() {
        QueryParser parser = new QueryParser("li:has(a)");
        Evaluator evaluator = parser.parse();
        assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("a")), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithContains() {
        QueryParser parser = new QueryParser("li:contains(text)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.ContainsText("text"), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithContainsOwn() {
        QueryParser parser = new QueryParser("li:containsOwn(text)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.ContainsOwnText("text"), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithContainsData() {
        QueryParser parser = new QueryParser("li:containsData(data)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.ContainsData("data"), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithMatches() {
        QueryParser parser = new QueryParser("li:matches(regex)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.Matches(Pattern.compile(parser.query.substring(parser.index))), evaluator);
    }

    @Test
    public void testParsePseudoClassQueryWithMatchesOwn() {
        QueryParser parser = new QueryParser("li:matchesOwn(regex)");
        Evaluator evaluator = parser.parse();
        assertEquals(new Evaluator.MatchesOwn(Pattern.compile(parser.query.substring(parser.index))), evaluator);
    }
}