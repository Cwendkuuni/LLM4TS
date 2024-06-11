package org.jsoup.select;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#content > p.class";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withComma() {
        String query = "div#content, p.class";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withMultipleCombinators() {
        String query = "div#content > p.class + span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_withInvalidCombinator() {
        String query = "div#content & p.class";
        QueryParser.parse(query);
    }
    
    @Test
    public void testParse_withId() {
        String query = "#content";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withClass() {
        String query = ".class";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withTag() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttribute() {
        String query = "div[attribute]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttributeValue() {
        String query = "div[attribute=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttributeNotValue() {
        String query = "div[attribute!=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttributeStarting() {
        String query = "div[attribute^=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttributeEnding() {
        String query = "div[attribute$=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttributeContaining() {
        String query = "div[attribute*=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAttributeMatching() {
        String query = "div[attribute~=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withAllElements() {
        String query = "*";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withIndexLessThan() {
        String query = ":lt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withIndexGreaterThan() {
        String query = ":gt(0)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withIndexEquals() {
        String query = ":eq(2)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withHas() {
        String query = ":has(span)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withContains() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withContainsOwn() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withMatches() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withMatchesOwn() {
        String query = ":matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withNot() {
        String query = ":not(span)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_withNestedQueries() {
        String query = "div#content > p.class, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
}