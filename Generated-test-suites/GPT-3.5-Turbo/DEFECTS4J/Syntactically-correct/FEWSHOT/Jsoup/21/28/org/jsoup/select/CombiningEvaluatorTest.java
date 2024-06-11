package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CombiningEvaluatorTest {
    
    private Element root;
    private Element node;

    @Before
    public void setup() {
        root = new Element("root");
        node = new Element("node");
    }

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(evaluators);
        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testAndNotMatches() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(evaluators);
        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(evaluators);
        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrNotMatches() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(evaluators);
        assertFalse(evaluator.matches(root, node));
    }
}