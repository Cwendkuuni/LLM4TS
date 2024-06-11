package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser testParser;

    @Before
    public void setUp() {
        testParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        String query = "div.container > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        testParser.combinator(',');
        testParser.combinator('>');
        testParser.combinator('+');
        testParser.combinator('~');
        testParser.combinator(' ');
        testParser.combinator('#');
        testParser.combinator('.');
        testParser.combinator(':');
    }

    @Test
    public void testConsumeSubQuery() {
        testParser.consumeSubQuery();
    }

    @Test
    public void testFindElements() {
        testParser.findElements();
    }

    @Test
    public void testById() {
        testParser.byId();
    }

    @Test
    public void testByClass() {
        testParser.byClass();
    }

    @Test
    public void testByTag() {
        testParser.byTag();
    }

    @Test
    public void testByAttribute() {
        testParser.byAttribute();
    }

    @Test
    public void testAllElements() {
        testParser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        testParser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        testParser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        testParser.indexEquals();
    }

    @Test
    public void testCssNthChild() {
        testParser.cssNthChild(false, false);
        testParser.cssNthChild(true, false);
        testParser.cssNthChild(false, true);
        testParser.cssNthChild(true, true);
    }

    @Test
    public void testHas() {
        testParser.has();
    }

    @Test
    public void testContains() {
        testParser.contains(false);
        testParser.contains(true);
    }

    @Test
    public void testContainsData() {
        testParser.containsData();
    }

    @Test
    public void testMatches() {
        testParser.matches(false);
        testParser.matches(true);
    }

    @Test
    public void testNot() {
        testParser.not();
    }

}