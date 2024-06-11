package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluatorMatches() {
        And andEvaluator = new And();
        assertFalse(andEvaluator.matches(null, null));

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

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);

        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndEvaluatorToString() {
        And andEvaluator = new And();
        assertEquals("", andEvaluator.toString());

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

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);

        assertEquals(" ", andEvaluator.toString());
    }

    @Test
    public void testOrEvaluatorMatches() {
        Or orEvaluator = new Or();
        assertFalse(orEvaluator.matches(null, null));

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

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);

        assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrEvaluatorToString() {
        Or orEvaluator = new Or();
        assertEquals(":or[]", orEvaluator.toString());

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

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);

        assertEquals(":or[[ false ,  true ]]", orEvaluator.toString());
    }
}