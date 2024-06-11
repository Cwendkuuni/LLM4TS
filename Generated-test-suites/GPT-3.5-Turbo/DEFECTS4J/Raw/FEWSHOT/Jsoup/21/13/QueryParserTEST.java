package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;

public class QueryParserTest {
    
    @Test
    public void testParse() {
        try {
            QueryParser.parse("");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_withQuery() {
        QueryParser parser = new QueryParser("div[name='myDiv']");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
    }
    
    @Test
    public void testParse_withCombinators() {
        QueryParser parser = new QueryParser("div.class > h1 + p");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }
    
    @Test
    public void testCombinator() {
        QueryParser parser = new QueryParser("div.class");
        parser.combinator('>');
        assertTrue(parser.evals.get(1) instanceof StructuralEvaluator.ImmediateParent);
    }
    
    @Test
    public void testConsumeSubQuery() {
        QueryParser parser = new QueryParser("div.class[attr=value]");
        String subQuery = parser.consumeSubQuery();
        assertEquals("div.class", subQuery);
    }
    
    @Test
    public void testFindElements() {
        QueryParser parser = new QueryParser("div.class");
        parser.findElements();
        assertTrue(parser.evals.get(0) instanceof Evaluator.Class);
    }
    
    @Test
    public void testById() {
        QueryParser parser = new QueryParser("#myDiv");
        parser.byId();
        assertTrue(parser.evals.get(0) instanceof Evaluator.Id);
    }
    
    @Test
    public void testByClass() {
        QueryParser parser = new QueryParser(".myClass");
        parser.byClass();
        assertTrue(parser.evals.get(0) instanceof Evaluator.Class);
    }
    
    @Test
    public void testByTag() {
        QueryParser parser = new QueryParser("div");
        parser.byTag();
        assertTrue(parser.evals.get(0) instanceof Evaluator.Tag);
    }
    
    @Test
    public void testByAttribute() {
        QueryParser parser = new QueryParser("[name='myName']");
        parser.byAttribute();
        assertTrue(parser.evals.get(0) instanceof Evaluator.AttributeWithValue);
    }
    
    @Test
    public void testAllElements() {
        QueryParser parser = new QueryParser("*");
        parser.allElements();
        assertTrue(parser.evals.get(0) instanceof Evaluator.AllElements);
    }
    
    @Test
    public void testIndexLessThan() {
        QueryParser parser = new QueryParser(":lt(3)");
        parser.indexLessThan();
        assertTrue(parser.evals.get(0) instanceof Evaluator.IndexLessThan);
    }
    
    @Test
    public void testIndexGreaterThan() {
        QueryParser parser = new QueryParser(":gt(3)");
        parser.indexGreaterThan();
        assertTrue(parser.evals.get(0) instanceof Evaluator.IndexGreaterThan);
    }
    
    @Test
    public void testIndexEquals() {
        QueryParser parser = new QueryParser(":eq(3)");
        parser.indexEquals();
        assertTrue(parser.evals.get(0) instanceof Evaluator.IndexEquals);
    }
    
    @Test
    public void testHas() {
        QueryParser parser = new QueryParser(":has(.myClass)");
        parser.has();
        assertTrue(parser.evals.get(0) instanceof StructuralEvaluator.Has);
    }
    
    @Test
    public void testContains() {
        QueryParser parser = new QueryParser(":contains(text)");
        parser.contains(false);
        assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsText);
    }
    
    @Test
    public void testContainsOwn() {
        QueryParser parser = new QueryParser(":containsOwn(text)");
        parser.contains(true);
        assertTrue(parser.evals.get(0) instanceof Evaluator.ContainsOwnText);
    }
    
    @Test
    public void testMatches() {
        QueryParser parser = new QueryParser(":matches(regex)");
        parser.matches(false);
        assertTrue(parser.evals.get(0) instanceof Evaluator.Matches);
    }
    
    @Test
    public void testMatchesOwn() {
        QueryParser parser = new QueryParser(":matchesOwn(regex)");
        parser.matches(true);
        assertTrue(parser.evals.get(0) instanceof Evaluator.MatchesOwn);
    }
    
    @Test
    public void testNot() {
        QueryParser parser = new QueryParser(":not(.myClass)");
        parser.not();
        assertTrue(parser.evals.get(0) instanceof StructuralEvaluator.Not);
    }
}