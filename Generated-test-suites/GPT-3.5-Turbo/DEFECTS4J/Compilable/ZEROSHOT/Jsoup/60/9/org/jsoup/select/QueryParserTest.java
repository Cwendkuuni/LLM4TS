package org.jsoup.select;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#id.class[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div > span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div + span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div ~ span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div, span";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div[attr^=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div[attr$=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div[attr*=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div[attr~=value]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:lt(3)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:gt(3)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:eq(3)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:has(span)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:contains(text)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:containsOwn(text)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:containsData(text)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:matches(regex)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:matchesOwn(regex)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        query = "div:not(span)";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test(expected = org.jsoup.select.Selector.SelectorParseException.class)
    public void testParseWithInvalidQuery() {
        String query = "invalid query";
        Evaluator evaluator = QueryParser.parse(query);
    }
    
    @Test
    public void testParseWithEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithWhitespaceQuery() {
        String query = " ";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithOnlyWhitespaceQuery() {
        String query = "       ";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParseWithCombinedSelectors() {
        String query = "div > span + p, a";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
}