package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "";
        Evaluator result = QueryParser.parse(query);
        assertNotNull(result);
    }
    
    @Test
    public void testParser() {
        QueryParser parser = new QueryParser("");
        Evaluator result = parser.parse();
        assertNotNull(result);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("");
        parser.combinator(',');
        parser.combinator('>');
        parser.combinator('+');
        parser.combinator('~');
        try {
            parser.combinator('!');
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: !", e.getMessage());
        }
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("");
        String result = parser.consumeSubQuery();
        assertEquals("", result);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("");
        parser.findElements();
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("");
        parser.byId();
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser("");
        parser.byClass();
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("");
        parser.byTag();
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("");
        parser.byAttribute();
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("");
        parser.allElements();
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser("");
        parser.indexLessThan();
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser("");
        parser.indexGreaterThan();
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser("");
        parser.indexEquals();
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser("");
        parser.has();
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser("");
        parser.contains(false);
        parser.contains(true);
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser("");
        parser.matches(false);
        parser.matches(true);
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser("");
        parser.not();
    }
}