package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
    private RandomGenerator generatorMock = mock(RandomGenerator.class);
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @org.junit.Before
    public void setup() {
        when(optimizerMock.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(new UnivariateRealPointValuePair(1.0, 1.0));
    }

    @org.junit.Test
    public void testGetOptima_ZeroStarts() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 0, generatorMock);
        assertNull(optimizer.getOptima());
    }

    @org.junit.Test
    public void testGetOptima_OneStart() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 1, generatorMock);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1.0, 1.0)}, optima);
    }

    @org.junit.Test
    public void testGetOptima_MultipleStarts() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, generatorMock);

        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1.0, 1.0),
                                                             new UnivariateRealPointValuePair(1.0, 1.0),
                                                             new UnivariateRealPointValuePair(1.0, 1.0)}, optima);
    }

    @org.junit.Test
    public void testGetOptima_NoConvergence() {
        when(optimizerMock.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("No convergence"));

        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, generatorMock);

        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[]{null, null, null}, optima);
    }

    @org.junit.Test
    public void testGetOptima_DifferentStartPoints() {
        when(optimizerMock.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(new UnivariateRealPointValuePair(1.0, 1.0));
        when(generatorMock.nextDouble()).thenReturn(0.5);

        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, generatorMock);

        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.optimize(mock(UnivariateRealFunction.class), GoalType.MINIMIZE, 0.0, 1.0);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertTrue(Arrays.asList(optima).contains(new UnivariateRealPointValuePair(1.0, 1.0)));
        assertTrue(Arrays.asList(optima).contains(new UnivariateRealPointValuePair(0.5, Double.NaN)));
        assertTrue(Arrays.asList(optima).contains(new UnivariateRealPointValuePair(0.5, Double.NaN)));
    }
}