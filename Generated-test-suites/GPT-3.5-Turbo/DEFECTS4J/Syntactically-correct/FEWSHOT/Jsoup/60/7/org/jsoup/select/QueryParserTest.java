package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div[class='test']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_Empty() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNull(evaluator);
    }

    @Test
    public void testParse_WithCombinator() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_WithMultipleSelectors() {
        String query = "div, p";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_WithAttribute() {
        String query = "div[class^='test']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test
    public void testParse_WithPseudoSelector() {
        String query = "div:first-of-type";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
}