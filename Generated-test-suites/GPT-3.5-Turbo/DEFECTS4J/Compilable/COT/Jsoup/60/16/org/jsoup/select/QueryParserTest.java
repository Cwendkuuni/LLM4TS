package org.jsoup.select;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testCombinator() {
        Evaluator evaluator = QueryParser.parse("div > span");
        assertNotNull(evaluator);
    }

    @Test
    public void testId() {
        Evaluator evaluator = QueryParser.parse("#id");
        assertNotNull(evaluator);
    }

    @Test
    public void testClass() {
        Evaluator evaluator = QueryParser.parse(".class");
        assertNotNull(evaluator);
    }

    @Test
    public void testTag() {
        Evaluator evaluator = QueryParser.parse("tag");
        assertNotNull(evaluator);
    }

    @Test
    public void testAttribute() {
        Evaluator evaluator = QueryParser.parse("[attribute=value]");
        assertNotNull(evaluator);
    }

    @Test
    public void testAllElements() {
        Evaluator evaluator = QueryParser.parse("*");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexLessThan() {
        Evaluator evaluator = QueryParser.parse(":lt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexGreaterThan() {
        Evaluator evaluator = QueryParser.parse(":gt(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testIndexEquals() {
        Evaluator evaluator = QueryParser.parse(":eq(3)");
        assertNotNull(evaluator);
    }

    @Test
    public void testHas() {
        Evaluator evaluator = QueryParser.parse(":has(span)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContains() {
        Evaluator evaluator = QueryParser.parse(":contains(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsOwn() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(text)");
        assertNotNull(evaluator);
    }

    @Test
    public void testContainsData() {
        Evaluator evaluator = QueryParser.parse(":containsData(data)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatches() {
        Evaluator evaluator = QueryParser.parse(":matches(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testMatchesOwn() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(regex)");
        assertNotNull(evaluator);
    }

    @Test
    public void testNot() {
        Evaluator evaluator = QueryParser.parse(":not(selector)");
        assertNotNull(evaluator);
    }

    @Test
    public void testInvalidQuery() {
        try {
            QueryParser.parse("~invalid query~");
            fail("Should throw SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '~invalid query~': unexpected token at '~invalid query~'", e.getMessage());
        }
    }
    
    @Test
    public void testCombiningEvaluators() {
        Evaluator evaluator = QueryParser.parse("div, span");
        assertNotNull(evaluator);
    }    

    @Test
    public void testEvaluate() {
        Evaluator evaluator = QueryParser.parse("div");
        assertNotNull(evaluator);
        
        // Test the evaluate method here
    }

    
}