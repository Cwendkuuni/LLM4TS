package org.jsoup.select;

import static org.junit.Assert.*;
import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        Evaluator evaluator = QueryParser.parse("div.test");
        assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div > span");
        parser.combinator('>');
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div > span");
        String subQuery = parser.consumeSubQuery();
        assertNotNull(subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div");
        parser.findElements();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#id");
        parser.byId();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser(".class");
        parser.byClass();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[name=value]");
        parser.byAttribute();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(5)");
        parser.indexLessThan();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(5)");
        parser.indexGreaterThan();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(5)");
        parser.indexEquals();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testCssNthChild() {
        QueryParser parser = new QueryParser(":nth-child(2n+1)");
        parser.cssNthChild(false, false);
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div)");
        parser.has();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testContainsData() {
        QueryParser parser = new QueryParser(":containsData(text)");
        parser.containsData();
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        assertNotNull(parser.evals.get(0));
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div)");
        parser.not();
        assertNotNull(parser.evals.get(0));
    }
}