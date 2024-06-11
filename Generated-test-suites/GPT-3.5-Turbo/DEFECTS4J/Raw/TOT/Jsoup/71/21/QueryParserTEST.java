package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_withValidQuery_shouldReturnEvaluator() {
        String query = "div#id.class[attr=value]:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParse_withInvalidQuery_shouldThrowException() {
        String query = "invalid query";
        QueryParser.parse(query);
    }
    
    @Test
    public void testParse_withCommaCombinator_shouldReturnOrEvaluator() {
        String query = "div, span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.Or);
    }
    
    @Test
    public void testParse_withGreaterThanCombinator_shouldReturnAndEvaluatorWithImmediateParent() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getRightEvaluator() instanceof StructuralEvaluator.ImmediateParent);
    }
    
    @Test
    public void testParse_withPlusCombinator_shouldReturnAndEvaluatorWithImmediatePreviousSibling() {
        String query = "div + span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getRightEvaluator() instanceof StructuralEvaluator.ImmediatePreviousSibling);
    }
    
    @Test
    public void testParse_withTildeCombinator_shouldReturnAndEvaluatorWithPreviousSibling() {
        String query = "div ~ span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getRightEvaluator() instanceof StructuralEvaluator.PreviousSibling);
    }
    
    @Test
    public void testParse_withSpaceCombinator_shouldReturnAndEvaluatorWithParent() {
        String query = "div span";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getRightEvaluator() instanceof StructuralEvaluator.Parent);
    }

    @Test
    public void testCombinator_withUnknownCombinator_shouldThrowException() {
        String query = "div&span";
        try {
            QueryParser.parse(query);
            fail("Expected SelectorParseException to be thrown");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Unknown combinator: &", e.getMessage());
        }
    }
}