package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.Evaluator evaluator1;
    private CombiningEvaluator.Evaluator evaluator2;
    private CombiningEvaluator.Evaluator evaluator3;
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setup() {
        evaluator1 = mock(CombiningEvaluator.Evaluator.class);
        evaluator2 = mock(CombiningEvaluator.Evaluator.class);
        evaluator3 = mock(CombiningEvaluator.Evaluator.class);
        andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2, evaluator3));
        orEvaluator = new CombiningEvaluator.Or(Collections.singletonList(andEvaluator));
    }

    @Test
    public void matches_shouldReturnTrue_whenAllEvaluatorsReturnTrue() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);

        when(evaluator1.matches(root, node)).thenReturn(true);
        when(evaluator2.matches(root, node)).thenReturn(true);
        when(evaluator3.matches(root, node)).thenReturn(true);

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void matches_shouldReturnFalse_whenAnyEvaluatorReturnsFalse() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);

        when(evaluator1.matches(root, node)).thenReturn(true);
        when(evaluator2.matches(root, node)).thenReturn(false);
        when(evaluator3.matches(root, node)).thenReturn(true);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void toString_shouldReturnCorrectStringRepresentation() {
        String expected = evaluator1.toString() + " " + evaluator2.toString() + " " + evaluator3.toString();
        assertEquals(expected, andEvaluator.toString());
    }

    @Test
    public void matches_shouldReturnTrue_whenAnyEvaluatorReturnsTrue() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        when(evaluator1.matches(root, node)).thenReturn(false);
        when(evaluator2.matches(root, node)).thenReturn(true);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void matches_shouldReturnFalse_whenAllEvaluatorsReturnFalse() {
        Element root = mock(Element.class);
        Element node = mock(Element.class);
        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        when(evaluator1.matches(root, node)).thenReturn(false);
        when(evaluator2.matches(root, node)).thenReturn(false);

        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void add_shouldAddEvaluatorToList() {
        CombiningEvaluator.Evaluator evaluator4 = mock(CombiningEvaluator.Evaluator.class);
        orEvaluator.add(evaluator4);
        assertEquals(1, orEvaluator.evaluators.size());
        assertEquals(evaluator4, orEvaluator.evaluators.get(0));
    }
}