package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {
    private CombiningEvaluator.And evaluatorAnd;
    private CombiningEvaluator.Or evaluatorOr;

    @Before
    public void setUp() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        Evaluator evaluator1 = mock(Evaluator.class);
        when(evaluator1.matches(null, null)).thenReturn(true);
        evaluators.add(evaluator1);
        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator2.matches(null, null)).thenReturn(false);
        evaluators.add(evaluator2);
        evaluatorAnd = new CombiningEvaluator.And(evaluators);

        evaluators = new ArrayList<>();
        Evaluator evaluator3 = mock(Evaluator.class);
        when(evaluator3.matches(null, null)).thenReturn(false);
        evaluators.add(evaluator3);
        Evaluator evaluator4 = mock(Evaluator.class);
        when(evaluator4.matches(null, null)).thenReturn(true);
        evaluators.add(evaluator4);
        evaluatorOr = new CombiningEvaluator.Or(evaluators);
    }

    @Test
    public void testAndMatches() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);
        assertTrue(evaluatorAnd.matches(root, node));
    }

    @Test
    public void testAndMatchesFalse() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);
        when(node.tagName()).thenReturn("div");
        assertFalse(evaluatorAnd.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);
        assertTrue(evaluatorOr.matches(root, node));
    }

    @Test
    public void testOrMatchesFalse() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);
        when(node.tagName()).thenReturn("a");
        assertFalse(evaluatorOr.matches(root, node));
    }

    @Test
    public void testAndToString() {
        String expected = ":and Evaluator1 Evaluator2";
        assertEquals(expected, evaluatorAnd.toString());
    }

    @Test
    public void testOrToString() {
        String expected = ":or [Evaluator3, Evaluator4]";
        assertEquals(expected, evaluatorOr.toString());
    }
}