package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setUp() {
        andEvaluator = new CombiningEvaluator.And();
        orEvaluator = new CombiningEvaluator.Or();
    }

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
                return false;
            }
        };

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);

        assertTrue(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndDoesNotMatch() {
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

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);

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

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);

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

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);

        assertFalse(orEvaluator.matches(null, null));
    }
}