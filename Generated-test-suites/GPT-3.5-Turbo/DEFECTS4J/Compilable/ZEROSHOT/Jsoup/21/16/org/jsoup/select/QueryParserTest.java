package org.jsoup.select;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withQuery() {
        String query = "div.test";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withCombinator() {
        String query = "div.test > span";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withMultipleCombinators() {
        String query = "div.test > span + p";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withMultipleSelectors() {
        String query = "div.test, span.class, p#id";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withAttributeSelector() {
        String query = "div[test=value]";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withAttributeSelectorWithValue() {
        String query = "div[test=value]";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withIndexLessThan() {
        String query = "div:lt(3)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withIndexGreaterThan() {
        String query = "div:gt(3)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withIndexEquals() {
        String query = "div:eq(3)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withHasPseudoSelector() {
        String query = "div:has(p)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withContainsPseudoSelector() {
        String query = "div:contains(text)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withContainsOwnPseudoSelector() {
        String query = "div:containsOwn(text)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withMatchesPseudoSelector() {
        String query = "div:matches(regex)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withMatchesOwnPseudoSelector() {
        String query = "div:matchesOwn(regex)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
    
    @Test
    public void testParse_withNotPseudoSelector() {
        String query = "div:not(p)";
        Evaluator result = QueryParser.parse(query);
        
        assertNotNull(result);
        assertTrue(result instanceof Evaluator);
    }
}