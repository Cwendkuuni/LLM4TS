package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        // Initialize test objects
        Element root = new Element("div");
        Element node = new Element("span");

        // Create evaluators
        Evaluator evaluator1 = createMockEvaluator(true);
        Evaluator evaluator2 = createMockEvaluator(true);

        // Create AndEvaluator
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Test AndEvaluator matches
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        // Initialize test objects
        Element root = new Element("div");
        Element node = new Element("span");

        // Create evaluators
        Evaluator evaluator1 = createMockEvaluator(true);
        Evaluator evaluator2 = createMockEvaluator(false);

        // Create AndEvaluator
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        // Test AndEvaluator does not match
        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        // Initialize test objects
        Element root = new Element("div");
        Element node = new Element("span");

        // Create evaluators
        Evaluator evaluator1 = createMockEvaluator(false);
        Evaluator evaluator2 = createMockEvaluator(true);

        // Create OrEvaluator
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        // Test OrEvaluator matches
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        // Initialize test objects
        Element root = new Element("div");
        Element node = new Element("span");

        // Create evaluators
        Evaluator evaluator1 = createMockEvaluator(false);
        Evaluator evaluator2 = createMockEvaluator(false);

        // Create OrEvaluator
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        // Test OrEvaluator does not match
        assertFalse(orEvaluator.matches(root, node));
    }

    private Evaluator createMockEvaluator(boolean matches) {
        return new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return matches;
            }
        };
    }
}