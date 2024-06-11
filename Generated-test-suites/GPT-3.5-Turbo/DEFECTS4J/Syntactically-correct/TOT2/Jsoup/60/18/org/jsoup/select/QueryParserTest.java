package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Typical use case
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);

        // Edge cases
        evaluator = QueryParser.parse("");
        assertNull(evaluator);
        evaluator = QueryParser.parse(" ");
        assertNull(evaluator);
    }

    @Test
    public void testParseSubQuery() {
        QueryParser queryParser = new QueryParser("div > span");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("> span", subQuery);
    }

    @Test
    public void testFindByTag() {
        QueryParser queryParser = new QueryParser("div");
        queryParser.findElements();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testFindById() {
        QueryParser queryParser = new QueryParser("#id");
        queryParser.byId();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testFindByClass() {
        QueryParser queryParser = new QueryParser(".class");
        queryParser.byClass();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testFindByAttribute() {
        QueryParser queryParser = new QueryParser("[name=value]");
        queryParser.byAttribute();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testFindAllElements() {
        QueryParser queryParser = new QueryParser("*");
        queryParser.allElements();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser(":lt(3)");
        queryParser.indexLessThan();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser(":gt(3)");
        queryParser.indexGreaterThan();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser(":eq(3)");
        queryParser.indexEquals();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testMatchesOwn() {
        QueryParser queryParser = new QueryParser(":matchesOwn(\\d+)");
        queryParser.matches(true);
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser(":not(p)");
        queryParser.not();
        assertEquals(1, queryParser.evals.size());
    }

    // Add more test cases for other methods

}