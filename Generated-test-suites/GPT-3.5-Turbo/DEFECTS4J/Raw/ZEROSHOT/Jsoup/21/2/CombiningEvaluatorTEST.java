package org.jsoup.select;

import org.jsoup.nodes.Element;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("div");
        Element node = new Element("span");
        List<Evaluator> evaluators = new ArrayList<>();

        Evaluator evaluator1 = new TestEvaluator(true);
        evaluators.add(evaluator1);
        Evaluator evaluator2 = new TestEvaluator(true);
        evaluators.add(evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        boolean result = andEvaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");
        List<Evaluator> evaluators = new ArrayList<>();

        Evaluator evaluator1 = new TestEvaluator(true);
        evaluators.add(evaluator1);
        Evaluator evaluator2 = new TestEvaluator(false);
        evaluators.add(evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        boolean result = andEvaluator.matches(root, node);

        assertFalse(result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("div");
        Element node = new Element("span");
        List<Evaluator> evaluators = new ArrayList<>();

        Evaluator evaluator1 = new TestEvaluator(false);
        evaluators.add(evaluator1);
        Evaluator evaluator2 = new TestEvaluator(true);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        boolean result = orEvaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");
        List<Evaluator> evaluators = new ArrayList<>();

        Evaluator evaluator1 = new TestEvaluator(false);
        evaluators.add(evaluator1);
        Evaluator evaluator2 = new TestEvaluator(false);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        boolean result = orEvaluator.matches(root, node);

        assertFalse(result);
    }

    private class TestEvaluator extends Evaluator {
        private boolean matches;

        public TestEvaluator(boolean matches) {
            this.matches = matches;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return matches;
        }
    }
}