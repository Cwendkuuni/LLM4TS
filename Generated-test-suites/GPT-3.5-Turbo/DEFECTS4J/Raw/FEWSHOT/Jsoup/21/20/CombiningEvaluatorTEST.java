
package org.jsoup.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    private Element rootElement;

    @Before
    public void setUp() {
        rootElement = mock(Element.class);
    }

    @Test
    public void testAndMatches() {
        // Arrange
        Evaluator evaluator1 = mock(Evaluator.class);
        when(evaluator1.matches(eq(rootElement), any(Element.class))).thenReturn(true);

        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator2.matches(eq(rootElement), any(Element.class))).thenReturn(true);

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = andEvaluator.matches(rootElement, mock(Element.class));

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAndDoesNotMatch() {
        // Arrange
        Evaluator evaluator1 = mock(Evaluator.class);
        when(evaluator1.matches(eq(rootElement), any(Element.class))).thenReturn(true);

        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator2.matches(eq(rootElement), any(Element.class))).thenReturn(false);

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = andEvaluator.matches(rootElement, mock(Element.class));

        // Assert
        assertFalse(result);
    }

    @Test
    public void testOrMatches() {
        // Arrange
        Evaluator evaluator1 = mock(Evaluator.class);
        when(evaluator1.matches(eq(rootElement), any(Element.class))).thenReturn(false);

        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator2.matches(eq(rootElement), any(Element.class))).thenReturn(true);

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = orEvaluator.matches(rootElement, mock(Element.class));

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrDoesNotMatch() {
        // Arrange
        Evaluator evaluator1 = mock(Evaluator.class);
        when(evaluator1.matches(eq(rootElement), any(Element.class))).thenReturn(false);

        Evaluator evaluator2 = mock(Evaluator.class);
        when(evaluator2.matches(eq(rootElement), any(Element.class))).thenReturn(false);

        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = orEvaluator.matches(rootElement, mock(Element.class));

        // Assert
        assertFalse(result);
    }
}
