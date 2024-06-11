package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#id.class[attr='value']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withNullQuery() {
        String query = null;
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParse_withEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParse_withWhitespaceQuery() {
        String query = "         ";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParse_withTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withIdSelector() {
        String query = "#id";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withClassSelector() {
        String query = ".class";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withAttributeSelector() {
        String query = "[attr='value']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withCombinationSelector() {
        String query = "div#id.class[attr='value']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withCommaSelector() {
        String query = "div, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withChildCombinator() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withAdjacentSiblingCombinator() {
        String query = "div + span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withGeneralSiblingCombinator() {
        String query = "div ~ span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMultipleCombinators() {
        String query = "div > span + a ~ h1";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withPseudoSelector() {
        String query = "div:has(span)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withContainsSelector() {
        String query = "div:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withMatchesSelector() {
        String query = "div:matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_withNotSelector() {
        String query = "div:not(.class)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

}