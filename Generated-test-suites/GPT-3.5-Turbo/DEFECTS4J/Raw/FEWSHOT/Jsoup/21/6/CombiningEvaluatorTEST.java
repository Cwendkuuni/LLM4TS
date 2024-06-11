package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("div");
        Element node = new Element("p");

        // Create evaluators
        Evaluator evaluator1 = new FakeEvaluator(true);
        Evaluator evaluator2 = new FakeEvaluator(true);
        Evaluator evaluator3 = new FakeEvaluator(false);

        // Create And evaluator
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3);

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("p");

        // Create evaluators
        Evaluator evaluator1 = new FakeEvaluator(true);
        Evaluator evaluator2 = new FakeEvaluator(true);
        Evaluator evaluator3 = new FakeEvaluator(true);

        // Create And evaluator
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("div");
        Element node = new Element("p");

        // Create evaluators
        Evaluator evaluator1 = new FakeEvaluator(false);
        Evaluator evaluator2 = new FakeEvaluator(false);
        Evaluator evaluator3 = new FakeEvaluator(true);

        // Create Or evaluator
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("p");

        // Create evaluators
        Evaluator evaluator1 = new FakeEvaluator(false);
        Evaluator evaluator2 = new FakeEvaluator(false);
        Evaluator evaluator3 = new FakeEvaluator(false);

        // Create Or evaluator
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);

        assertFalse(orEvaluator.matches(root, node));
    }

    // FakeEvaluator class for testing purposes
    private static class FakeEvaluator implements Evaluator {
        private boolean matches;

        public FakeEvaluator(boolean matches) {
            this.matches = matches;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return matches;
        }

        @Override
        public String toString() {
            return StringUtil.join(new ArrayList<Evaluator>(), " ");
        }
    }
}