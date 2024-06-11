package org.jsoup.select;
import org.jsoup.select.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        // Test for typical use case
        String query = "div.container > .item";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for empty query
        query = "";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for query with combinators
        query = "div.container + .item";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_Combinator() {
        // Test for space combinator
        String query = "div.container .item";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for comma combinator
        query = "div.container, .item";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for > combinator
        query = "div.container > .item";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for + combinator
        query = "div.container + .item";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for ~ combinator
        query = "div.container ~ .item";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_Hierarchy() {
        // Test for element with class and ID
        String query = "div#container";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for element with attribute
        query = "input[type=text]";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        
        // Test for element with pseudo class
        query = "a:contains('example')";
        evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }
    
    @Test
    public void testParse_Error() {
        // Test for invalid query with unknown combinator
        String query = "div.container & .item";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: &", e.getMessage());
        }
        
        // Test for attribute query with unexpected token
        query = "input[type=]";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse attribute query 'input[type=]': unexpected token at ''", e.getMessage());
        }
        
        // Test for empty :has subselect
        query = "a:has()";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":has(el) subselect must not be empty", e.getMessage());
        }
        
        // Test for empty :contains(text) query
        query = "a:contains()";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":contains(text) query must not be empty", e.getMessage());
        }
        
        // Test for empty :not selector subselect
        query = ":not()";
        try {
            Evaluator evaluator = QueryParser.parse(query);
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals(":not(selector) subselect must not be empty", e.getMessage());
        }
    }
}