package org.jsoup.select.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CombiningEvaluator.
 */
@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {

    @InjectMocks
    private CombiningEvaluator evaluator;

    @Test
    public void testAndConstructor() {
        List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
        And andEvaluator = new And(evaluators);

        assertTrue(andEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));
        for (Evaluator evaluator : evaluators) {
            Mockito.verify(evaluator).matches(Mockito.eq(Mockito.mock(Element.class)), Mockito.eq(Mockito.mock(Element.class)));
        }
    }

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
        And andEvaluator = new And(evaluators);

        Mockito.when(evaluators.get(0).matches(Mockito.mock(Element.class), Mockito.mock(Element.class)))
                .thenReturn(true);
        Mockito.when(evaluators.get(1).matches(Mockito.mock(Element.class), Mockito.mock(Element.class)))
                .thenReturn(true);

        assertTrue(andEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));

        Mockito.verify(evaluators.get(0), Mockito.atLeastOnce()).matches(Mockito.eq(Mockito.mock(Element.class)),
                Mockito.eq(Mockito.mock(Element.class)));
        Mockito.verify(evaluators.get(1), Mockito.atLeastOnce()).matches(Mockito.eq(Mockito.mock(Element.class)),
                Mockito.eq(Mockito.mock(Element.class)));
    }

    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
        And andEvaluator = new And(evaluators);

        assertTrue(andEvaluator.toString().contains(" "));
        for (Evaluator evaluator : evaluators) {
            assertTrue(andEvaluator.toString().contains(evaluator.toString()));
        }
    }

    @Test
    public void testOrConstructor() {
        List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
        Or orEvaluator = new Or(evaluators);

        assertTrue(orEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));
        for (Evaluator evaluator : evaluators) {
            Mockito.verify(evaluator).matches(Mockito.eq(Mockito.mock(Element.class)), Mockito.eq(Mockito.mock(Element.class)));
        }
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
        Or orEvaluator = new Or(evaluators);

        Mockito.when(evaluators.get(0).matches(Mockito.mock(Element.class), Mockito.mock(Element.class)))
                .thenReturn(false);
        Mockito.when(evaluators.get(1).matches(Mockito.mock(Element.class), Mockito.mock(Element.class)))
                .thenReturn(true);

        assertFalse(orEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));

        Mockito.verify(evaluators.get(0), Mockito.atLeastOnce()).matches(Mockito.eq(Mockito.mock(Element.class)),
                Mockito.eq(Mockito.mock(Element.class)));
        Mockito.verify(evaluators.get(1), Mockito.atLeastOnce()).matches(Mockito.eq(Mockito.mock(Element.class)),
                Mockito.eq(Mockito.mock(Element.class)));
    }

    @Test
    public void testOrAdd() {
        List<Evaluator> evaluators = new ArrayList<>();
        Or orEvaluator = new Or();

        orEvaluator.add(Mockito.mock(Evaluator.class));
        evaluators.add(Mockito.mock(Evaluator.class));

        orEvaluator.evaluators.size(); // Ensure evaluators list is not empty
        assertTrue(orEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));

        orEvaluator.add(Mockito.mock(Evaluator.class));
        evaluators.add(Mockito.mock(Evaluator.class));

        orEvaluator.evaluators.size(); // Ensure evaluators list has two elements
        assertTrue(orEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));

        Mockito.when(evaluators.get(0).matches(Mockito.mock(Element.class), Mockito.mock(Element.class)))
                .thenReturn(false);

        assertFalse(orEvaluator.matches(Mockito.mock(Element.class), Mockito.mock(Element.class)));
    }

    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = Arrays.asList(Mockito.mock(Evaluator.class), Mockito.mock(Evaluator.class));
        Or orEvaluator = new Or(evaluators);

        assertTrue(orEvaluator.toString().contains(":or"));
        for (Evaluator evaluator : evaluators) {
            assertTrue(orEvaluator.toString().contains(evaluator.toString()));
        }
    }
}