package org.jsoup.select;
import org.jsoup.select.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.class");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_invalidQuery() {
        try {
            Evaluator evaluator = QueryParser.parse("*[.class");
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_combinator() {
        Evaluator evaluator = QueryParser.parse("div > p");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_combinator_unknown() {
        try {
            Evaluator evaluator = QueryParser.parse("div ?? p");
            fail("Expected Selector.SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // expected
        }
    }

    @Test
    public void testParse_indexLessThan() {
        Evaluator evaluator = QueryParser.parse("div:lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexGreaterThan() {
        Evaluator evaluator = QueryParser.parse("div:gt(1)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_indexEquals() {
        Evaluator evaluator = QueryParser.parse("div:eq(5)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_cssNthChild() {
        Evaluator evaluator = QueryParser.parse("div:nth-child(2n+3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_has() {
        Evaluator evaluator = QueryParser.parse("div:has(a)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_contains() {
        Evaluator evaluator = QueryParser.parse("div:contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_containsOwn() {
        Evaluator evaluator = QueryParser.parse("div:containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_containsData() {
        Evaluator evaluator = QueryParser.parse("div:containsData(data)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_matches() {
        Evaluator evaluator = QueryParser.parse("div:matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_matchesOwn() {
        Evaluator evaluator = QueryParser.parse("div:matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_not() {
        Evaluator evaluator = QueryParser.parse("div:not(p)");
        assertNotNull(evaluator);
    }

}