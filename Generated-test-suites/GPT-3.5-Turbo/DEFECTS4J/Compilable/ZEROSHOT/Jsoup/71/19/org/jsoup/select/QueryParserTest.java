package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#id.class[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testFindElements() {
        String query = "div, p";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testById() {
        String query = "#id";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testByClass() {
        String query = ".class";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testByTag() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testByAttribute() {
        String query = "[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        String query = ":lt(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        String query = ":gt(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        String query = ":eq(3)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        String query = ":has(div)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testContains() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testContainsData() {
        String query = ":containsData(data)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testMatchesOwn() {
        String query = ":matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        String query = ":not(div)";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }
}