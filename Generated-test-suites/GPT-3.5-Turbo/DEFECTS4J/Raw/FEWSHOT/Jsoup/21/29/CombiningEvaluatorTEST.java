package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        assertTrue(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);

        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testOrMatches() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);

        assertFalse(orEvaluator.matches(null, null));
    }
}