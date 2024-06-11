package org.jsoup.select;

import org.junit.Test;
import org.junit.Assert;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Assert.assertTrue(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndNotMatches() {
        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Assert.assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndToString() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator 1";
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator 2";
            }
        });

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        Assert.assertEquals("Evaluator 1 Evaluator 2", andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        Assert.assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrNotMatches() {
        Collection<Evaluator> evaluators = new ArrayList<>();
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

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        Assert.assertFalse(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrToString() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator 1";
            }
        });
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "Evaluator 2";
            }
        });

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        Assert.assertEquals(":or[Evaluator 1, Evaluator 2]", orEvaluator.toString());
    }

}