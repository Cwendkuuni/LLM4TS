package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse_emptyQuery_shouldThrowException() {
        try {
            QueryParser.parse("");
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            assertEquals("Could not parse query '': unexpected token at ''", e.getMessage());
        }
    }

    @Test
    public void testParse_combinatorComma_shouldParseSuccessfully() {
        Evaluator result = QueryParser.parse("div, span");
        assertTrue(result instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or or = (CombiningEvaluator.Or) result;
        assertEquals(2, or.size());
        assertTrue(or.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) or.get(0)).tagName);
        assertTrue(or.get(1) instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) or.get(1)).tagName);
    }

    @Test
    public void testParse_combinatorChild_shouldParseSuccessfully() {
        Evaluator result = QueryParser.parse("div > span");
        assertTrue(result instanceof CombiningEvaluator.And);
        CombiningEvaluator.And and = (CombiningEvaluator.And) result;
        assertEquals(2, and.size());
        assertTrue(and.get(0) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) and.get(0)).tagName);
        assertTrue(and.get(1) instanceof StructuralEvaluator.ImmediateParent);
        assertTrue(((StructuralEvaluator.ImmediateParent) and.get(1)).childEvaluator instanceof Evaluator.Tag);
        assertEquals("span", ((Evaluator.Tag) ((StructuralEvaluator.ImmediateParent) and.get(1)).childEvaluator).tagName);
    }

    // ... Continue with more test cases

}