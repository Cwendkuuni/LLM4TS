package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CombiningEvaluatorTest {
  
    @Test
    public void testAndMatches() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();
        assertTrue(evaluator.matches(null, null)); // Test with empty evaluators list
        
        // Test with evaluators that always return true
        Evaluator trueEvaluator1 = (root, node) -> true;
        Evaluator trueEvaluator2 = (root, node) -> true;
        evaluator.add(trueEvaluator1);
        evaluator.add(trueEvaluator2);
        assertTrue(evaluator.matches(null, null));
        
        // Test with evaluators that return true and false
        Evaluator trueEvaluator3 = (root, node) -> true;
        Evaluator falseEvaluator1 = (root, node) -> false;
        Evaluator trueEvaluator4 = (root, node) -> true;
        evaluator.add(trueEvaluator3);
        evaluator.add(falseEvaluator1);
        evaluator.add(trueEvaluator4);
        assertFalse(evaluator.matches(null, null));
    }
  
    @Test
    public void testAndToString() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();
        assertTrue(evaluator.toString().isEmpty()); // Test with empty evaluators list
        
        // Test with evaluators
        Evaluator evaluator1 = (root, node) -> true;
        Evaluator evaluator2 = (root, node) -> true;
        Evaluator evaluator3 = (root, node) -> false;
        evaluator.add(evaluator1);
        evaluator.add(evaluator2);
        evaluator.add(evaluator3);
        assertEquals(evaluator.toString(), "true true false");
    }
  
    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();
        assertFalse(evaluator.matches(null, null)); // Test with empty evaluators list
        
        // Test with evaluators that always return false
        Evaluator falseEvaluator1 = (root, node) -> false;
        Evaluator falseEvaluator2 = (root, node) -> false;
        evaluator.add(falseEvaluator1);
        evaluator.add(falseEvaluator2);
        assertFalse(evaluator.matches(null, null));
        
        // Test with evaluators that return true and false
        Evaluator trueEvaluator1 = (root, node) -> true;
        Evaluator falseEvaluator3 = (root, node) -> false;
        Evaluator trueEvaluator2 = (root, node) -> true;
        evaluator.add(trueEvaluator1);
        evaluator.add(falseEvaluator3);
        evaluator.add(trueEvaluator2);
        assertTrue(evaluator.matches(null, null));
    }
  
    @Test
    public void testOrToString() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();
        assertTrue(evaluator.toString().isEmpty()); // Test with empty evaluators list
        
        // Test with evaluators
        Evaluator evaluator1 = (root, node) -> true;
        Evaluator evaluator2 = (root, node) -> true;
        Evaluator evaluator3 = (root, node) -> false;
        evaluator.add(evaluator1);
        evaluator.add(evaluator2);
        evaluator.add(evaluator3);
        assertEquals(evaluator.toString(), ":or[true, true, false]");
    }
}