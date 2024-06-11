package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        assertNotNull(QueryParser.parse("div"));
    }

    @Test
    public void testCombinator() {
        queryParser.combinator('>');
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testConsumeSubQuery() {
        queryParser.tq.append(":has(p)");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals(":has(p)", subQuery);
    }

    @Test
    public void testById() {
        queryParser.tq.append("#testId");
        queryParser.byId();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testByClass() {
        queryParser.tq.append(".testClass");
        queryParser.byClass();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testByTag() {
        queryParser.tq.append("body");
        queryParser.byTag();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testByAttribute() {
        queryParser.tq.append("[name=testName]");
        queryParser.byAttribute();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexLessThan() {
        queryParser.tq.append(":lt(3)");
        queryParser.indexLessThan();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.tq.append(":gt(4)");
        queryParser.indexGreaterThan();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testIndexEquals() {
        queryParser.tq.append(":eq(2)");
        queryParser.indexEquals();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testCssNthChild() {
        queryParser.tq.append(":nth-child(2n+1)");
        queryParser.cssNthChild(false, false);
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testHas() {
        queryParser.tq.append(":has(p)");
        queryParser.has();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testContains() {
        queryParser.tq.append(":contains(text)");
        queryParser.contains(false);
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testContainsOwn() {
        queryParser.tq.append(":containsOwn(text)");
        queryParser.contains(true);
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testContainsData() {
        queryParser.tq.append(":containsData(data)");
        queryParser.containsData();
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testMatches() {
        queryParser.tq.append(":matches(regex)");
        queryParser.matches(false);
        assertEquals(1, queryParser.evals.size());
    }

    @Test
    public void testNot() {
        queryParser.tq.append(":not(div)");
        queryParser.not();
        assertEquals(1, queryParser.evals.size());
    }

}