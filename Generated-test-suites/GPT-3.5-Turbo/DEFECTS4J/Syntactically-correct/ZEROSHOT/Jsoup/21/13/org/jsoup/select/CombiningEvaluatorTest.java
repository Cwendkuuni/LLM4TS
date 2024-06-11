package org.jsoup.select;

import org.junit.Test;

import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();

        assertTrue("And evaluator should match with no evaluators", andEvaluator.matches(null, null));

        Element root = new Element("div");
        Element node = new Element("span");

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        andEvaluator = new CombiningEvaluator.And(evaluators);

        assertTrue("And evaluator should match when all inner evaluators match", andEvaluator.matches(root, node));

        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse("And evaluator should not match when at least one inner evaluator doesn't match", andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            @Override
            public String toString() {
                return "Evaluator1";
            }
        });

        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            @Override
            public String toString() {
                return "Evaluator2";
            }
        });

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals("And evaluator toString should return combined inner evaluators", "Evaluator1 Evaluator2", andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();

        assertFalse("Or evaluator should not match with no evaluators", orEvaluator.matches(null, null));

        Element root = new Element("div");
        Element node = new Element("span");

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertFalse("Or evaluator should not match when all inner evaluators don't match", orEvaluator.matches(root, node));

        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertTrue("Or evaluator should match when at least one inner evaluator matches", orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();

        Element root = new Element("div");
        Element node = new Element("span");

        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        assertFalse("Or evaluator should not match when no inner evaluator matches", orEvaluator.matches(root, node));

        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue("Or evaluator should match when at least one inner evaluator matches", orEvaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            @Override
            public String toString() {
                return "Evaluator1";
            }
        });

        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
            @Override
            public String toString() {
                return "Evaluator2";
            }
        });

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertEquals("Or evaluator toString should return combined inner evaluators", ":or[Evaluator1, Evaluator2]", orEvaluator.toString());
    }

}