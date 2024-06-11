package org.jsoup.select;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div.container > p.text";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParse_InvalidQuery() {
        String query = "div.container > p.text @";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div.container > p.text @': unexpected token at '@'", e.getMessage());
        }
    }

    @Test
    public void testParse_IdSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_ClassSelector() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_TagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_AttributeSelector() {
        String query = "[name=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_AllElementsSelector() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_IndexLessThanSelector() {
        String query = ":lt(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_IndexGreaterThanSelector() {
        String query = ":gt(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_IndexEqualsSelector() {
        String query = ":eq(3)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_HasSelector() {
        String query = ":has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_ContainsSelector() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_ContainsOwnSelector() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_ContainsDataSelector() {
        String query = ":containsData(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_MatchesSelector() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_MatchesOwnSelector() {
        String query = ":matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_NotSelector() {
        String query = ":not(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
}