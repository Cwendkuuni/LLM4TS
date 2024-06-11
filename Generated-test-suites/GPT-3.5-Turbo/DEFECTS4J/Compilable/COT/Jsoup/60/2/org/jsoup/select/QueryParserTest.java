package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        String query = "div + p";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testAttribute() {
        String query = "[name=value]";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        String query = ":lt(3)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        String query = ":gt(1)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        String query = ":eq(2)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        String query = ":has(div)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testContains() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testContainsData() {
        String query = ":containsData(data)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testMatchesOwn() {
        String query = ":matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        String query = ":not(div)";
        Evaluator evaluator = QueryParser.parse(query);

        assertNotNull(evaluator);
    }
}

