package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        Evaluator evaluator = QueryParser.parse("div h1");
        assertNotNull(evaluator);
    }

    @Test
    public void testById() {
        Evaluator evaluator = QueryParser.parse("#myId");
        assertNotNull(evaluator);
    }

    @Test
    public void testByClass() {
        Evaluator evaluator = QueryParser.parse(".myClass");
        assertNotNull(evaluator);
    }

    @Test
    public void testByAttribute() {
        Evaluator evaluator = QueryParser.parse("[attr=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        Evaluator evaluator = QueryParser.parse(":has(div)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContains() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsData() {
        Evaluator evaluator = QueryParser.parse(":containsData(data)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        Evaluator evaluator = QueryParser.parse(":not(div)");
        assertNotNull(evaluator);
    }
}