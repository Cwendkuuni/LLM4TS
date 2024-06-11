package org.apache.commons.cli.optimization; // change package name if necessary

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MaxEval;
import org.apache.commons.math.optimization.PointValuePair;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.optimization.optimizers.BrentOptimizer;
import org.apache.commons.math.optimization.optimizers.PowellOptimizer;
import org.junit.Test;

/**
 * Test cases for MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test multi-start optimizer with Brent optimizer as underlying optimizer.
     */
    @Test
    public void testMultiStartBrentOptimizer() {
        UnivariateRealOptimizer baseOptimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 5, mock(RandomGenerator.class));

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        double[] expectedOptima = new double[]{
                -1.0, 0.0, 1.0, 1.5, 1.5, 2.0};

        double[] expectedOptimaValues = new double[]{
                0.25, 0.0, 0.25, 0.0, 0.0, 0.25};

        try {
            PointValuePair result = optimizer.optimize(function, goalType, min, max);
            double[] optimizerOptima = optimizer.getOptima();
            double[] optimizerOptimaValues = optimizer.getOptimaValues();

            assertEquals(result.getPoint(), expectedOptima[0], 0.001);
            assertEquals(result.getValue(), expectedOptimaValues[0], 0.001);
            assertArrayEquals(optimizerOptima, expectedOptima);
            assertArrayEquals(optimizerOptimaValues, expectedOptimaValues);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // Should not reach here
        }
    }

    /**
     * Test multi-start optimizer with Powell optimizer as underlying optimizer.
     */
    @Test
    public void testMultiStartPowellOptimizer() {
        UnivariateRealOptimizer baseOptimizer = new PowellOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 5, mock(RandomGenerator.class));

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        double[] expectedOptima = new double[]{
                -2.0, -1.2, 0.0, 0.5, 1.2, 2.0};

        double[] expectedOptimaValues = new double[]{
                -12.0, -2.41, 0.0, 0.25, 1.44, 4.0};

        try {
            PointValuePair result = optimizer.optimize(function, goalType, min, max);
            double[] optimizerOptima = optimizer.getOptima();
            double[] optimizerOptimaValues = optimizer.getOptimaValues();

            assertEquals(result.getPoint(), expectedOptima[0], 0.001);
            assertEquals(result.getValue(), expectedOptimaValues[0], 0.001);
            assertArrayEquals(optimizerOptima, expectedOptima);
            assertArrayEquals(optimizerOptimaValues, expectedOptimaValues);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // Should not reach here
        }
    }

    /**
     * Test multi-start optimizer without finding any optima.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoOptimaFound() {
        UnivariateRealOptimizer baseOptimizer = mock(UnivariateRealOptimizer.class);
        when(baseOptimizer.optimize(FunctionUtils.IDENTITY_FUNCTION, GoalType.MINIMIZE, 1.0, 3.0))
            .thenThrow(new ConvergenceException("Test exception"));

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 5, mock(RandomGenerator.class));

        UnivariateRealFunction function = FunctionUtils.IDENTITY_FUNCTION;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 3.0;

        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test multi-start optimizer with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        UnivariateRealOptimizer baseOptimizer = mock(UnivariateRealOptimizer.class);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 5, null);
    }
}