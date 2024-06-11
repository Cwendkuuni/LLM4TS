package org.jsoup.select;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div > a";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
    }

    @Test
    public void testParse_withComma() {
        String query = "div, p";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
    }

    @Test
    public void testParse_withIdSelector() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Id);
    }

    @Test
    public void testParse_withClassSelector() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Class);
    }

    @Test
    public void testParse_withTagSelector() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Tag);
    }

    @Test
    public void testParse_withAttributeSelector() {
        String query = "[name=value]";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Attribute);
    }

    @Test
    public void testParse_withAttributeWithValueSelector() {
        String query = "[name=value]";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
    }

    @Test
    public void testParse_withIndexLessThanSelector() {
        String query = ":lt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexLessThan);
    }

    @Test
    public void testParse_withIndexGreaterThanSelector() {
        String query = ":gt(5)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);
    }

    @Test
    public void testParse_withIndexEqualsSelector() {
        String query = ":eq(5)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.IndexEquals);
    }

    @Test
    public void testParse_withHasSelector() {
        String query = ":has(div)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Has);
    }

    @Test
    public void testParse_withContainsSelector() {
        String query = ":contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsText);
    }

    @Test
    public void testParse_withContainsOwnSelector() {
        String query = ":containsOwn(text)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
    }

    @Test
    public void testParse_withMatchesSelector() {
        String query = ":matches(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.Matches);
    }

    @Test
    public void testParse_withMatchesOwnSelector() {
        String query = ":matchesOwn(regex)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof Evaluator.MatchesOwn);
    }

    @Test
    public void testParse_withNotSelector() {
        String query = ":not(div)";
        Evaluator evaluator = QueryParser.parse(query);
        
        // Test assertions
        assertNotNull(evaluator);
        assertTrue(evaluator instanceof StructuralEvaluator.Not);
    }
}