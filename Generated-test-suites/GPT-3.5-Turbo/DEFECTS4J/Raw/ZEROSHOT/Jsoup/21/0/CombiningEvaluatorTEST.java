package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    private Element root;
    private Element element;

    @Before
    public void setUp() {
        root = new Element("root");
        element = new Element("element");
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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        assertTrue(and.matches(root, element));
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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        assertFalse(and.matches(root, element));
    }

    @Test
    public void testAndToString() {
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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        assertEquals(":and[evaluator1 evaluator2]", and.toString());
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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        assertTrue(or.matches(root, element));
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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        assertFalse(or.matches(root, element));
    }

    @Test
    public void testOrToString() {
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

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        assertEquals(":or[evaluator1 evaluator2]", or.toString());
    }
}