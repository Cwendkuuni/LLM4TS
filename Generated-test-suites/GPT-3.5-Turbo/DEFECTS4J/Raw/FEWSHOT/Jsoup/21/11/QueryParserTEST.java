package org.jsoup.select;

import org.junit.Test;
import org.junit.Assert;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        String query = "div#myDiv";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertNotNull(evaluator);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div > span");
        parser.combinator('>');
        Evaluator evaluator = parser.parse();
        Assert.assertNotNull(evaluator);
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div span");
        String subQuery = parser.consumeSubQuery();
        Assert.assertEquals("div span", subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div");
        parser.findElements();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#myId");
        parser.byId();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser(".myClass");
        parser.byClass();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[type=text]");
        parser.byAttribute();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(2)");
        parser.indexLessThan();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(2)");
        parser.indexGreaterThan();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(2)");
        parser.indexEquals();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(div)");
        parser.has();
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        Assert.assertEquals(1, parser.evals.size());
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(div)");
        parser.not();
        Assert.assertEquals(1, parser.evals.size());
    }
}