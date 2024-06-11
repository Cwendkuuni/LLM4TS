package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > p[class=\"test\"]";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testCombinator() {
        String query = "div + p";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testFindElements() {
        String query = "div";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testById() {
        String query = "#id";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testByClass() {
        String query = ".class";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testByTag() {
        String query = "div";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testByAttribute() {
        String query = "[name=value]";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testAllElements() {
        String query = "*";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testIndexLessThan() {
        String query = ":lt(5)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testIndexGreaterThan() {
        String query = ":gt(5)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testIndexEquals() {
        String query = ":eq(5)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testNthChild() {
        String query = ":nth-child(2n+1)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testHas() {
        String query = ":has(span)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testContains() {
        String query = ":contains(text)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testContainsData() {
        String query = ":containsData(text)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testMatches() {
        String query = ":matches(regex)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testMatchesOwn() {
        String query = ":matchesOwn(regex)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }

    @Test
    public void testNot() {
        String query = ":not(span)";
        Evaluator e = QueryParser.parse(query);
        assertNotNull(e);
    }
}