package org.jsoup.select;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    private Element root;
    private Element node;

    @Before
    public void setUp() {
        root = new Element();
        node = new Element();
    }

    @Test
    public void testAndMatches() {
        Evaluator e1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator e2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(e1, e2);
        assertFalse(and.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Evaluator e1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        Evaluator e2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(e1, e2);
        assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrWithNullEvaluators() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(null);
        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrAddMethod() {
        Evaluator e1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator e2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(e1);
        or.add(e2);
        assertFalse(or.matches(root, node));
    }

    // Additional test cases for edge cases and exceptions can be added here

}