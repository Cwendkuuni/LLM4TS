package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser.combinator('>');
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testConsumeSubQuery() {
        String subQuery = queryParser.consumeSubQuery();
        assertNotNull(subQuery);
    }

    @Test
    public void testFindElements() {
        queryParser.findElements();
        assertFalse(queryParser.evals.isEmpty());
    }

    @Test
    public void testById() {
        queryParser.byId();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testByClass() {
        queryParser.byClass();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testByTag() {
        queryParser.byTag();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testByAttribute() {
        queryParser.byAttribute();
        assertFalse(queryParser.evals.isEmpty());
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testIndexLessThan() {
        queryParser.indexLessThan();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.indexGreaterThan();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testIndexEquals() {
        queryParser.indexEquals();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testHas() {
        queryParser.has();
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testContains() {
        queryParser.contains(false);
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testContainsOwn() {
        queryParser.contains(true);
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testMatches() {
        queryParser.matches(false);
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testMatchesOwn() {
        queryParser.matches(true);
        assertNotNull(queryParser.evals.get(0));
    }

    @Test
    public void testNot() {
        queryParser.not();
        assertNotNull(queryParser.evals.get(0));
    }
}