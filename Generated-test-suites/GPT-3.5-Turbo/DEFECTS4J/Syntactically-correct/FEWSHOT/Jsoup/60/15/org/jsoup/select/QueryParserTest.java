package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParse() {
        // Test case 1
        Evaluator eval1 = QueryParser.parse("div");
        assertEquals(new Evaluator.Tag("div"), eval1);
        
        // Test case 2
        Evaluator eval2 = QueryParser.parse(".class");
        assertEquals(new Evaluator.Class("class"), eval2);
        
        // Test case 3
        Evaluator eval3 = QueryParser.parse("#id");
        assertEquals(new Evaluator.Id("id"), eval3);
        
        // Test case 4
        Evaluator eval4 = QueryParser.parse("div > p");
        Evaluator expectedEval4 = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateParent(new Evaluator.Tag("p")));
        assertEquals(expectedEval4, eval4);
        
        // Test case 5
        Evaluator eval5 = QueryParser.parse("div + p");
        Evaluator expectedEval5 = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.ImmediateNextSibling(new Evaluator.Tag("p")));
        assertEquals(expectedEval5, eval5);
        
        // Test case 6
        Evaluator eval6 = QueryParser.parse("div ~ p");
        Evaluator expectedEval6 = new CombiningEvaluator.And(new Evaluator.Tag("div"), new StructuralEvaluator.Follower(new Evaluator.Tag("p")));
        assertEquals(expectedEval6, eval6);
    }
}