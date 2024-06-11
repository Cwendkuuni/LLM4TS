package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.test";
        Evaluator eval = QueryParser.parse(query);
        // Add test assertions here
    }

    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator(',');
        // Add test assertions here
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("");
        String subQuery = parser.consumeSubQuery();
        // Add test assertions here
    }

    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
        // Add test assertions here
    }

    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
        // Add test assertions here
    }

    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
        // Add test assertions here
    }

    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
        // Add test assertions here
    }

    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
        // Add test assertions here
    }

    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
        // Add test assertions here
    }

    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
        // Add test assertions here
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
        // Add test assertions here
    }

    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
        // Add test assertions here
    }

    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
        // Add test assertions here
    }

    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        // Add test assertions here
    }

    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser("");
        parser.contains(true);
        // Add test assertions here
    }

    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        // Add test assertions here
    }

    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser("");
        parser.matches(true);
        // Add test assertions here
    }

    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
        // Add test assertions here
    }
}