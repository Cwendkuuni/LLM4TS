package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#myId[class='myClass']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_EmptyQuery() {
        String query = "";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Exception not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }
    
    @Test
    public void testParse_InvalidQuery() {
        String query = "div#myId[";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Exception not thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query 'div#myId[': unexpected token at '['", e.getMessage());
        }
    }
    
    @Test
    public void testParse_ValidTagQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_ValidTagNameQuery() {
        String query = "div.myClass";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_ValidAttributeValueQuery() {
        String query = "[data-name='John']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_ValidAttributeQuery() {
        String query = "[data-name]";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_ValidCombinationQuery() {
        String query = "div.myClass[data-name='John']";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_ValidNestedQuery() {
        String query = "div.myClass :has(p)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

}