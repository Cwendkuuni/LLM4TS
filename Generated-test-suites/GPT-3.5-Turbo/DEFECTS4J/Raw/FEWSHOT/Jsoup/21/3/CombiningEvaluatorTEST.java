package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    private Element root;
    private Element node;

    @Before
    public void setUp() {
        root = new Element("");
        node = new Element("");
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
                return true;
            }
        };
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertTrue(andEvaluator.matches(root, node));
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
                return false;
            }
        };
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse(andEvaluator.matches(root, node));
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
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertTrue(orEvaluator.matches(root, node));
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
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertFalse(orEvaluator.matches(root, node));
    }
}