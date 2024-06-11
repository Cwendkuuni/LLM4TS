package org.jsoup.select;
import org.jsoup.select.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_withCombinators_returnsRootEvaluator() {
        String query = "div > p";
        Evaluator expected = new StructuralEvaluator.Root();
        
        Evaluator actual = QueryParser.parse(query);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_startsWithWhitespace_returnsRootEvaluator() {
        String query = " div p";
        Evaluator expected = new StructuralEvaluator.Root();
        
        Evaluator actual = QueryParser.parse(query);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_withAttributeQuery_returnsAttributeEvaluator() {
        String query = "a[href^=https]";
        Evaluator expected = new Evaluator.AttributeStarting("https");
        
        Evaluator actual = QueryParser.parse(query);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_withMultipleAttributes_returnsAndEvaluator() {
        String query = "a[href^=https][rel=nofollow]";
        Evaluator expected = new CombiningEvaluator.And(
            new Evaluator.AttributeStarting("https"),
            new Evaluator.Attribute("rel")
        );
        
        Evaluator actual = QueryParser.parse(query);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_withUnknownCombinator_throwsParseException() {
        String query = "a >+ p";
        
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: +", e.getMessage());
        }
    }
    
    @Test
    public void testParse_withInvalidAttributeQuery_throwsParseException() {
        String query = "a[href!";
        
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query 'a[href!': unexpected token at '!'", e.getMessage());
        }
    }
    
    @Test
    public void testParse_withEmptyQuery_throwsParseException() {
        String query = "";
        
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }


}