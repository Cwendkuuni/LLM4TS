package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test case 1: CSS query has a combinator
        Evaluator eval1 = QueryParser.parse("div > span");
        assertTrue(eval1 instanceof CombiningEvaluator.And);
        CombiningEvaluator.And evalAnd1 = (CombiningEvaluator.And) eval1;
        assertTrue(evalAnd1.getEvaluator(0) instanceof Evaluator.Tag);
        assertTrue(evalAnd1.getEvaluator(1) instanceof StructuralEvaluator.ImmediateParent);
        assertEquals("div", ((Evaluator.Tag) evalAnd1.getEvaluator(0)).tagName());
        assertTrue(evalAnd1.getEvaluator(1) instanceof StructuralEvaluator.ImmediateParent);
        assertEquals("div", ((StructuralEvaluator.ImmediateParent) evalAnd1.getEvaluator(1)).getEvaluator().toString());
        
        // Test case 2: CSS query has multiple combinators
        Evaluator eval2 = QueryParser.parse("p + p ~ span");
        assertTrue(eval2 instanceof CombiningEvaluator.And);
        CombiningEvaluator.And evalAnd2 = (CombiningEvaluator.And) eval2;
        assertEquals(3, evalAnd2.size());
        assertTrue(evalAnd2.getEvaluator(0) instanceof Evaluator.Tag);
        assertTrue(evalAnd2.getEvaluator(1) instanceof StructuralEvaluator.ImmediatePreviousSibling);
        assertTrue(evalAnd2.getEvaluator(2) instanceof StructuralEvaluator.PreviousSibling);
        assertEquals("p", ((Evaluator.Tag) evalAnd2.getEvaluator(0)).tagName());
        assertEquals("p", ((StructuralEvaluator.ImmediatePreviousSibling) evalAnd2.getEvaluator(1)).getEvaluator().toString());
        assertEquals("p", ((StructuralEvaluator.PreviousSibling) evalAnd2.getEvaluator(2)).getEvaluator().toString());
        
        // Test case 3: CSS query has multiple elements
        Evaluator eval3 = QueryParser.parse("div, span, p");
        assertTrue(eval3 instanceof CombiningEvaluator.Or);
        CombiningEvaluator.Or evalOr3 = (CombiningEvaluator.Or) eval3;
        assertEquals(3, evalOr3.size());
        assertTrue(evalOr3.getEvaluator(0) instanceof Evaluator.Tag);
        assertTrue(evalOr3.getEvaluator(1) instanceof Evaluator.Tag);
        assertTrue(evalOr3.getEvaluator(2) instanceof Evaluator.Tag);
        assertEquals("div", ((Evaluator.Tag) evalOr3.getEvaluator(0)).tagName());
        assertEquals("span", ((Evaluator.Tag) evalOr3.getEvaluator(1)).tagName());
        assertEquals("p", ((Evaluator.Tag) evalOr3.getEvaluator(2)).tagName());
        
        // Your test cases...
    }
}