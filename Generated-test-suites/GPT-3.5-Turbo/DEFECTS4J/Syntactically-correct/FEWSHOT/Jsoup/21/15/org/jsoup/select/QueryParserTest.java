package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_ComplexQuery() {
        String query = "div > p, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_InvalidQuery() {
        String query = "div >";
        try {
            QueryParser.parse(query);
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_QueryWithElementId() {
        String query = "#elementId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithClassName() {
        String query = ".className";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithTagAndClassName() {
        String query = "div.className";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithTagAndMultipleClasses() {
        String query = "div.className1.className2";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithTagAndAttribute() {
        String query = "div[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithTagAndMultipleAttributes() {
        String query = "div[attr1=value1][attr2=value2]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithChildCombinator() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithDescendantCombinator() {
        String query = "div p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithAdjacentSiblingCombinator() {
        String query = "div + p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithGeneralSiblingCombinator() {
        String query = "div ~ p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithMultipleCombinators() {
        String query = "div > p + span ~ a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithGrouping() {
        String query = "div, p, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_QueryWithPseudoSelectors() {
        String query = "div:has(p), span:contains(text), a:not(.className)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
}