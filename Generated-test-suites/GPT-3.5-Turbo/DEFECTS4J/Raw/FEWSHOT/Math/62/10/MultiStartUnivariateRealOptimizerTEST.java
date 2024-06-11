package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;
import org.mockito.Mockito;

public class MultiStartUnivariateRealOptimizerTest {
    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return false;
            }
        };
        optimizer.setConvergenceChecker(checker);
        Mockito.verify(mockedOptimizer).setConvergenceChecker(checker);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return false;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        Mockito.when(mockedOptimizer.getConvergenceChecker()).thenReturn(checker);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        Mockito.when(mockedOptimizer.getMaxEvaluations()).thenReturn(100);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        optimizer.setMaxEvaluations(50);
        Mockito.verify(mockedOptimizer).setMaxEvaluations(50);
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        Mockito.when(mockedOptimizer.getEvaluations()).thenReturn(50);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testOptimize() throws Exception {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(3.4, 1.2);
        Mockito.when(mockedOptimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(expected);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        UnivariateRealFunction function = Mockito.mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        UnivariateRealPointValuePair result = optimizer.optimize(function, goal, 0.0, 10.0, 5.0);
        assertEquals(expected, result);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeThrowsConvergenceException() throws Exception {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        Mockito.when(mockedOptimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble())).thenThrow(new ConvergenceException());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        UnivariateRealFunction function = Mockito.mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MINIMIZE;
        optimizer.optimize(function, goal, 0.0, 10.0, 5.0);
    }

    @Test
    public void testGetOptima() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        UnivariateRealPointValuePair[] expected = new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(3.4, 1.2), null, new UnivariateRealPointValuePair(5.6, 2.3)};
        Mockito.when(mockedOptimizer.getOptima()).thenReturn(expected);
        UnivariateRealPointValuePair[] result = optimizer.getOptima();
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaThrowsIllegalStateException() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockedOptimizer = Mockito.mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(mockedOptimizer, 3, Mockito.mock(RandomGenerator.class));
        optimizer.getOptima();
    }

    @Test
    public void testSortPairsMinimize() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 0, null);
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[]{
                new UnivariateRealPointValuePair(3.0, 2.2),
                new UnivariateRealPointValuePair(2.5, 1.8),
                new UnivariateRealPointValuePair(4.0, 1.2),
                null,
                new UnivariateRealPointValuePair(2.0, 5.0)
        };
        optimizer.sortPairs(GoalType.MINIMIZE);
        assertEquals(new UnivariateRealPointValuePair(2.0, 5.0), pairs[0]);
        assertEquals(new UnivariateRealPointValuePair(2.5, 1.8), pairs[1]);
        assertEquals(new UnivariateRealPointValuePair(3.0, 2.2), pairs[2]);
        assertEquals(new UnivariateRealPointValuePair(4.0, 1.2), pairs[3]);
        assertNull(pairs[4]);
    }

    @Test
    public void testSortPairsMaximize() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 0, null);
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[]{
                new UnivariateRealPointValuePair(3.0, 2.2),
                new UnivariateRealPointValuePair(2.5, 1.8),
                new UnivariateRealPointValuePair(4.0, 1.2),
                null,
                new UnivariateRealPointValuePair(2.0, 5.0)
        };
        optimizer.sortPairs(GoalType.MAXIMIZE);
        assertEquals(new UnivariateRealPointValuePair(2.0, 5.0), pairs[0]);
        assertEquals(new UnivariateRealPointValuePair(4.0, 1.2), pairs[1]);
        assertEquals(new UnivariateRealPointValuePair(3.0, 2.2), pairs[2]);
        assertEquals(new UnivariateRealPointValuePair(2.5, 1.8), pairs[3]);
        assertNull(pairs[4]);
    }
}