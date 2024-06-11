package org.apache.commons.cli;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.optimizers.AbstractUnivariateOptimizer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction mockFunction;
    private RandomGenerator mockGenerator;

    @Before
    public void setUp() {
        mockFunction = mock(UnivariateRealFunction.class);
        mockGenerator = mock(RandomGenerator.class);
        optimizer = new MultiStartUnivariateRealOptimizer(new AbstractUnivariateOptimizer() {}, 3, mockGenerator);
    }

    @Test
    public void testOptimizeWithFunction() throws ConvergenceException, FunctionEvaluationException {
        when(mockFunction.value(anyDouble())).thenAnswer(i -> Math.pow(i, 2));

        double[] expectedOptima = {1.0, 1.7320508075688772, 2.0};
        double[] expectedOptimaValues = {1.0, 2.886578639376554, 4.0};

        optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 4.0);

        assertEquals(3, optimizer.getTotalIterations());
        assertEquals(3, optimizer.getTotalEvaluations());
        assertArrayEquals(expectedOptima, optimizer.getOptima());
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues());
    }

    @Test
    public void testOptimizeWithEmptyFunction() throws ConvergenceException, FunctionEvaluationException {
        when(mockFunction.value(anyDouble())).thenReturn(0.0);

        optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 4.0);

        assertEquals(3, optimizer.getTotalIterations());
        assertEquals(3, optimizer.getTotalEvaluations());
        assertEquals(3, optimizer.getOptima().length);
        Arrays.stream(optimizer.getOptima()).allMatch(d -> Double.isNaN(d));
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        when(mockFunction.value(anyDouble())).thenThrow(new ConvergenceException("ConvergenceException"));

        optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 4.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        when(mockFunction.value(anyDouble())).thenThrow(new FunctionEvaluationException("FunctionEvaluationException"));

        optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 4.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        optimizer = new MultiStartUnivariateRealOptimizer(new AbstractUnivariateOptimizer() {}, -1, mockGenerator);

        try {
            optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0.0, 4.0);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected exception", e);
        }
    }
}