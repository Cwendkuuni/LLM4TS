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

/**
 * Unit tests for CombiningEvaluator class.
 */
public class CombiningEvaluatorTest {
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setUp() {
        andEvaluator = new CombiningEvaluator.And();
        orEvaluator = new CombiningEvaluator.Or();
    }

    @Test
    public void testAndEvaluatorMatches() {
        Element root = new Element("div");
        Element node = new Element("p");

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluatorDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("p");

        andEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluatorMatches() {
        Element root = new Element("div");
        Element node = new Element("p");

        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluatorDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("p");

        assertFalse(orEvaluator.matches(root, node));
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

        andEvaluator.add(evaluator1);
        andEvaluator.add(evaluator2);

        assertEquals("evaluator1 evaluator2", andEvaluator.toString());
    }

    @Test
    public void testOrToString() {
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

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);

        assertEquals(":or[evaluator1, evaluator2]", orEvaluator.toString());
    }
}