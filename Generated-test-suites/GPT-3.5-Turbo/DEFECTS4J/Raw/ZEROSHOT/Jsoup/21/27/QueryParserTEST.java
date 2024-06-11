package org.jsoup.select;

import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {

    private QueryParser queryParser;

    @Before
    public void setUp() {
        queryParser = new QueryParser("");
    }

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull("Evaluator object should not be null", evaluator);
    }

    @Test
    public void testCombinator() {
        queryParser.combinator('>');
        assertFalse("Expected evals to be empty after combinator method", queryParser.evals.isEmpty());
    }

    @Test
    public void testParseSubQuery() {
        String subQuery = queryParser.consumeSubQuery();
        assertNotNull("SubQuery should not be null", subQuery);
    }

    @Test
    public void testFindElements() {
        queryParser.findElements();
        assertEquals("Expected evals size to be 0", 0, queryParser.evals.size());
    }

    @Test
    public void testById() {
        queryParser.byId();
        assertFalse("Expected evals to be empty after byId method", queryParser.evals.isEmpty());
    }

    @Test
    public void testByClass() {
        queryParser.byClass();
        assertFalse("Expected evals to be empty after byClass method", queryParser.evals.isEmpty());
    }

    @Test
    public void testByTag() {
        queryParser.byTag();
        assertFalse("Expected evals to be empty after byTag method", queryParser.evals.isEmpty());
    }

    @Test
    public void testByAttribute() {
        queryParser.byAttribute();
        assertFalse("Expected evals to be empty after byAttribute method", queryParser.evals.isEmpty());
    }

    @Test
    public void testAllElements() {
        queryParser.allElements();
        assertFalse("Expected evals to be empty after allElements method", queryParser.evals.isEmpty());
    }

    @Test
    public void testIndexLessThan() {
        queryParser.indexLessThan();
        assertFalse("Expected evals to be empty after indexLessThan method", queryParser.evals.isEmpty());
    }

    @Test
    public void testIndexGreaterThan() {
        queryParser.indexGreaterThan();
        assertFalse("Expected evals to be empty after indexGreaterThan method", queryParser.evals.isEmpty());
    }

    @Test
    public void testIndexEquals() {
        queryParser.indexEquals();
        assertFalse("Expected evals to be empty after indexEquals method", queryParser.evals.isEmpty());
    }

    @Test
    public void testHas() {
        queryParser.has();
        assertFalse("Expected evals to be empty after has method", queryParser.evals.isEmpty());
    }

    @Test
    public void testContains() {
        queryParser.contains(false);
        assertFalse("Expected evals to be empty after contains method", queryParser.evals.isEmpty());
    }

    @Test
    public void testContainsOwn() {
        queryParser.contains(true);
        assertFalse("Expected evals to be empty after containsOwn method", queryParser.evals.isEmpty());
    }

    @Test
    public void testMatches() {
        queryParser.matches(false);
        assertFalse("Expected evals to be empty after matches method", queryParser.evals.isEmpty());
    }

    @Test
    public void testMatchesOwn() {
        queryParser.matches(true);
        assertFalse("Expected evals to be empty after matchesOwn method", queryParser.evals.isEmpty());
    }

    @Test
    public void testNot() {
        queryParser.not();
        assertFalse("Expected evals to be empty after not method", queryParser.evals.isEmpty());
    }
}