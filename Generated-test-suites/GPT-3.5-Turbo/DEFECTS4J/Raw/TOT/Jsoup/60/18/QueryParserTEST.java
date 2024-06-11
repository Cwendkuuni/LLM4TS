package org.jsoup.select;

import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_BasicQuery_ReturnsEvaluator() {
        String query = "div#id.class[attr=value]:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("Evaluator", evaluator.getClass().toString());
    }

    @Test
    public void testParse_QueryWithMultipleCombinators_ReturnsEvaluator() {
        String query = "div > p + span ~ a";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("Evaluator", evaluator.getClass().toString());
    }

    @Test
    public void testParse_QueryWithMultipleSelectors_ReturnsAndEvaluator() {
        String query = "div, p, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertEquals("AndEvaluator", evaluator.getClass().toString());
    }

    @Test
    public void testParse_EmptyQuery_ThrowsParseException() {
        String query = "";
        try {
            QueryParser.parse(query);
            fail("ParseException not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_QueryWithInvalidCombinator_ThrowsParseException() {
        String query = "div p @ span";
        try {
            QueryParser.parse(query);
            fail("ParseException not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: @", e.getMessage());
        }
    }

    @Test
    public void testParse_QueryWithInvalidAttributeQuery_ThrowsParseException() {
        String query = "div[attr=123";
        try {
            QueryParser.parse(query);
            fail("ParseException not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query 'attr=123': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_QueryWithInvalidNthIndex_ThrowsParseException() {
        String query = "div:nth-child(abc)";
        try {
            QueryParser.parse(query);
            fail("ParseException not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse nth-index 'abc': unexpected format", e.getMessage());
        }
    }

    @Test
    public void testParse_QueryWithInvalidPseudoSelector_ThrowsParseException() {
        String query = "div:not(attr)";
        try {
            QueryParser.parse(query);
            fail("ParseException not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div:not(attr)': unexpected token at '':not(attr)'", e.getMessage());
        }
    }

}