package org.jsoup.select;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    Evaluator evaluationMock;
    Evaluator evaluationMock2;
    Evaluator andEvaluator;
    Evaluator orEvaluator;

    @Before
    public void setup() {
        evaluationMock = mock(Evaluator.class);
        evaluationMock2 = mock(Evaluator.class);
        andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluationMock, evaluationMock2));
        orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluationMock, evaluationMock2));
    }

    @Test
    public void testAndMethod() {
        when(evaluationMock.matches(mock(Element.class), mock(Element.class))).thenReturn(true);
        when(evaluationMock2.matches(mock(Element.class), mock(Element.class))).thenReturn(true);

        assertTrue(andEvaluator.matches(mock(Element.class), mock(Element.class)));
    }

    @Test
    public void testAndMethod_whenOneFalse_returnsFalse() {
        when(evaluationMock.matches(mock(Element.class), mock(Element.class))).thenReturn(true);
        when(evaluationMock2.matches(mock(Element.class), mock(Element.class))).thenReturn(false);

        assertFalse(andEvaluator.matches(mock(Element.class), mock(Element.class)));
    }

    @Test
    public void testOrMethod() {
        when(evaluationMock.matches(mock(Element.class), mock(Element.class))).thenReturn(true);
        when(evaluationMock2.matches(mock(Element.class), mock(Element.class))).thenReturn(false);

        orEvaluator.add(evaluationMock);
        orEvaluator.add(evaluationMock2);

        assertTrue(orEvaluator.matches(mock(Element.class), mock(Element.class)));
    }

    @Test
    public void testOrMethod_whenBothFalse_returnsFalse() {
        when(evaluationMock.matches(mock(Element.class), mock(Element.class))).thenReturn(false);
        when(evaluationMock2.matches(mock(Element.class), mock(Element.class))).thenReturn(false);

        orEvaluator.add(evaluationMock);
        orEvaluator.add(evaluationMock2);

        assertFalse(orEvaluator.matches(mock(Element.class), mock(Element.class)));
    }
}