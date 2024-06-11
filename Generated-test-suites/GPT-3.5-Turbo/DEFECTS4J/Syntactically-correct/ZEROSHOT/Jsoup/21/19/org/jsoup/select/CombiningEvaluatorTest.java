package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "evaluator1";
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "evaluator2";
            }
        };

        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals("evaluator1 evaluator2", andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

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

        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }

            @Override
            public String toString() {
                return "evaluator1";
            }
        };

        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }

            @Override
            public String toString() {
                return "evaluator2";
            }
        };

        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertEquals(":or[evaluator1, evaluator2]", orEvaluator.toString());
    }

    @Test
    public void testOrAddEvaluator() {
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

        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        orEvaluator.add(evaluator3);

        assertEquals(3, orEvaluator.evaluators.size());
    }
}