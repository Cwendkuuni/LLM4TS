package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(eval1, eval2);

        assertTrue(and.matches(null, null));
    }

    @Test
    public void testAndDoesNotMatch() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(eval1, eval2);

        assertFalse(and.matches(null, null));
    }

    @Test
    public void testOrMatches() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(eval1, eval2);

        assertTrue(or.matches(null, null));
    }

    @Test
    public void testOrDoesNotMatch() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(eval1, eval2);

        assertFalse(or.matches(null, null));
    }
}