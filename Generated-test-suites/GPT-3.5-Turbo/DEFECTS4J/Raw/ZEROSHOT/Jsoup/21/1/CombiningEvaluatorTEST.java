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

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;
    private Element root;
    private Element node;

    @Before
    public void setUp() {
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

        andEvaluator = new CombiningEvaluator.And(evaluators);

        orEvaluator = new CombiningEvaluator.Or(evaluators);

        root = new Element("root");
        node = new Element("node");
    }

    @Test
    public void testAndMatches() {
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        assertFalse(andEvaluator.matches(root, root));
    }

    @Test
    public void testOrMatches() {
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        assertFalse(orEvaluator.matches(root, root));
    }

    @Test
    public void testOrAdd() {
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        orEvaluator.add(evaluator);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testToStringAnd() {
        String expected = ":and Evaluator1 Evaluator2";
        String actual = andEvaluator.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringOr() {
        String expected = ":or[Evaluator1, Evaluator2]";
        String actual = orEvaluator.toString();

        assertEquals(expected, actual);
    }
}