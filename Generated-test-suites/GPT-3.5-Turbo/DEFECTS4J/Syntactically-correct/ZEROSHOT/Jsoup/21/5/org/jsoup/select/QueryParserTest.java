package org.jsoup.select;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#id.class[attr^=value]";
        Evaluator evaluator = QueryParser.parse(query);
        
        assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.combinator(',');
        queryParser.combinator('>');
        queryParser.combinator('+');
        queryParser.combinator('~');
        
        // Assert here
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser queryParser = new QueryParser("");
        
        String subQuery = queryParser.consumeSubQuery();
        
        // Assert here
    }
    
    @Test
    public void testFindElements() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.findElements();
        
        // Assert here
    }
    
    @Test
    public void testById() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.byId();
        
        // Assert here
    }
    
    @Test
    public void testByClass() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.byClass();
        
        // Assert here
    }
    
    @Test
    public void testByTag() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.byTag();
        
        // Assert here
    }
    
    @Test
    public void testByAttribute() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.byAttribute();
        
        // Assert here
    }
    
    @Test
    public void testAllElements() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.allElements();
        
        // Assert here
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.indexLessThan();
        
        // Assert here
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.indexGreaterThan();
        
        // Assert here
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.indexEquals();
        
        // Assert here
    }
    
    @Test
    public void testHas() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.has();
        
        // Assert here
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.contains(true);
        
        // Assert here
    }
    
    @Test
    public void testContains() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.contains(false);
        
        // Assert here
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.matches(true);
        
        // Assert here
    }
    
    @Test
    public void testMatches() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.matches(false);
        
        // Assert here
    }
    
    @Test
    public void testNot() {
        QueryParser queryParser = new QueryParser("");
        
        queryParser.not();
        
        // Assert here
    }
}