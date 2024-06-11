package org.jsoup.select;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

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
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(eval1, eval2);

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndMatchesOneFalse() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(eval1, eval2);

        assertFalse(and.matches(root, node));
    }

    @Test
    public void testAndToString() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.And and = new CombiningEvaluator.And(eval1, eval2);

        assertEquals("true true", and.toString());
    }

    @Test
    public void testOrMatches() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(eval1, eval2);

        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrMatchesOneTrue() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(eval1, eval2);

        assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrToString() {
        Evaluator eval1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        Evaluator eval2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(eval1, eval2);

        assertEquals(":or[false, false]", or.toString());
    }
}