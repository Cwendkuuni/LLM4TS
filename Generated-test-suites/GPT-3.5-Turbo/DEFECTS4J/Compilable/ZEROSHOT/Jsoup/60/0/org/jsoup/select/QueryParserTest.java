package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#test";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div.test :contains('example')";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = ".example";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_emptyQuery() {
        String query = "";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_combinator() {
        String query = "div > p";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div + p";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div ~ p";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div , p";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_attribute() {
        String query = "div[attr='example']";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div[attr!='example']";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div[attr^='example']";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div[attr$='example']";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div[attr*='example']";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div[attr~='example']";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_allElements() {
        String query = "*";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_index() {
        String query = "div:lt(5)";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div:gt(5)";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div:eq(5)";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_has() {
        String query = "div:has(p)";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_contains() {
        String query = "div:contains('example')";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div:containsOwn('example')";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_containsData() {
        String query = "div:containsData('example')";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_matches() {
        String query = "div:matches('example')";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
        
        query = "div:matchesOwn('example')";
        eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
    
    @Test
    public void testParse_not() {
        String query = "div:not(p)";
        Evaluator eval = QueryParser.parse(query);
        assertNotNull(eval);
    }
}