package org.jsoup.select;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("");
        queryParser.combinator('>');
        queryParser.combinator(' ');
        queryParser.combinator('+');
        queryParser.combinator('~');
    }

    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("");
        String subQuery = queryParser.consumeSubQuery();
        assertEquals("", subQuery);
    }

    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.findElements();
    }

    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byId();
    }

    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byClass();
    }

    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byTag();
    }

    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("");
        queryParser.byAttribute();
    }

    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("");
        queryParser.allElements();
    }

    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexLessThan();
    }

    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexGreaterThan();
    }

    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("");
        queryParser.indexEquals();
    }

    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("");
        queryParser.has();
    }

    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("");
        queryParser.contains(false);
    }

    @Test
    public void testContainsOwn() {
        QueryParser queryParser = new QueryParser("");
        queryParser.contains(true);
    }

    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("");
        queryParser.matches(false);
    }

    @Test
    public void testMatchesOwn() {
        QueryParser queryParser = new QueryParser("");
        queryParser.matches(true);
    }

    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("");
        queryParser.not();
    }
}