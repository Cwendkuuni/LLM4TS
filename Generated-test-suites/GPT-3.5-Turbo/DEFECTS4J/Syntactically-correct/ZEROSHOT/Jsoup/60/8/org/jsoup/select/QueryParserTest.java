package org.jsoup.select;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("test query");
        // assert statements
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator(',');
        // assert statements
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("");
        String subQuery = parser.consumeSubQuery();
        // assert statements
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
        // assert statements
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
        // assert statements
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
        // assert statements
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
        // assert statements
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
        // assert statements
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
        // assert statements
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
        // assert statements
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
        // assert statements
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
        // assert statements
    }

    @Test
    public void testCssNthChild() {
        QueryParser parser = new QueryParser("");
        parser.cssNthChild(false, false);
        // assert statements
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
        // assert statements
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        // assert statements
    }

    @Test
    public void testContainsData() {
        QueryParser parser = new QueryParser("");
        parser.containsData();
        // assert statements
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        // assert statements
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
        // assert statements
    }
}