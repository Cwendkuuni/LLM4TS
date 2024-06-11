
import org.jsoup.select.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div.test";
        Evaluator evaluator = QueryParser.parse(query);
        
        assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div.test");
        parser.combinator('>');
        
        // add assertion here
    }
    
    @Test
    public void testParseSubQuery() {
        QueryParser parser = new QueryParser("div.test");
        String subQuery = parser.consumeSubQuery();
        
        assertEquals("div", subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div.test");
        parser.findElements();
        
        // add assertion here
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#test");
        parser.byId();
        
        // add assertion here
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser(".test");
        parser.byClass();
        
        // add assertion here
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        
        // add assertion here
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[attr=value]");
        parser.byAttribute();
        
        // add assertion here
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        
        // add assertion here
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(3)");
        parser.indexLessThan();
        
        // add assertion here
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(3)");
        parser.indexGreaterThan();
        
        // add assertion here
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(3)");
        parser.indexEquals();
        
        // add assertion here
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div.test)");
        parser.has();
        
        // add assertion here
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        
        // add assertion here
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        
        // add assertion here
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        
        // add assertion here
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        
        // add assertion here
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div.test)");
        parser.not();
        
        // add assertion here
    }
}

