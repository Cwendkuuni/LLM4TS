package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optim.goals.GoalType;
import org.apache.commons.math.optim.initialGuess.InitialGuess;
import org.apache.commons.math.optim.initialGuess.RandomInitialGuess;
import org.apache.commons.math.optim.maximization.PointValuePair;
import org.apache.commons.math.optim.nonlinear.scalar.GoalTypeSensitiveOptimizer;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private GoalTypeSensitiveOptimizer innerOptimizer;

    @Before
    public void setUp() {
        innerOptimizer = mock(GoalTypeSensitiveOptimizer.class);
        function = mock(UnivariateRealFunction.class);
        when(function.value(0.0)).thenReturn(Double.NaN);
        when(innerOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0)).thenReturn(
                new PointValuePair(1.0, 5.0));
        optimizer = new MultiStartUnivariateRealOptimizer(innerOptimizer, 3, new RandomInitialGuess()) {
            @Override
            public UnivariateRealFunction getObjectiveFunction() {
                return function;
            }
        };
    }

    @Test
    public void testGetFunctionValue() {
        assertEquals(innerOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0).getValue(),
                optimizer.getFunctionValue(), 0.001);
    }

    @Test
    public void testGetResult() {
        assertEquals(1.0, optimizer.getResult(), 0.001);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetIterationCount() {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);

        assertEquals(3, optimizer.getIterationCount());
    }

    @Test
    public void testGetMaxIterations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);

        assertEquals(3, optimizer.getEvaluations());
    }

    @Test
    public void testGetRelativeAccuracy() {
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        optimizer.resetAbsoluteAccuracy();
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testResetMaxIterations() {
        optimizer.resetMaxIterations();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxIterations());
    }

    @Test
    public void testResetRelativeAccuracy() {
        optimizer.resetRelativeAccuracy();
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        optimizer.setAbsoluteAccuracy(0.1);
        assertEquals(0.1, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(0.1);
        assertEquals(0.1, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testGetOptima() throws IllegalStateException {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);

        double[] expectedOptima = {1.0, 1.0, 1.0};
        double[] actualOptima = optimizer.getOptima();
        assertArrayEquals(expectedOptima, actualOptima, 0.001);
    }

    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);

        double[] expectedOptimaValues = {5.0, 5.0, 5.0};
        double[] actualOptimaValues = optimizer.getOptimaValues();
        assertArrayEquals(expectedOptimaValues, actualOptimaValues, 0.001);
    }

    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 10.0);

        double[] expectedOptima = {1.0, 1.0, 1.0};
        double[] actualOptima = optimizer.getOptima();
        assertArrayEquals(expectedOptima, actualOptima, 0.001);

        double[] expectedOptimaValues = {5.0, 5.0, 5.0};
        double[] actualOptimaValues = optimizer.getOptimaValues();
        assertArrayEquals(expectedOptimaValues, actualOptimaValues, 0.001);

        assertEquals(1.0, optimizer.getResult(), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaBeforeOptimize() throws IllegalStateException {
        optimizer.getOptima();
    }

}