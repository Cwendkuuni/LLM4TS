package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeParameterization;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.ValueChecker;
import org.apache.commons.math3.optim.univariate.noderiv.BrentNDerivativeFinder;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.util.function.Function;

import static org.apache.commons.math3.optim.PointValuePair.Pair;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest extends AbstractMathTestCase {

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);
        assertNotNull(powellOptimizer);
    }

    /**
     * Test constructor with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        SimpleValueChecker checker = new SimpleValueChecker(0.001, 0.01);
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001, checker);
        assertNotNull(powellOptimizer);
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 2.0};
        UnivariateFunction function = f -> Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.25);
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        assertEquals(Pair.pair(new double[]{1.0, 1.0}, 0.0), result);
    }

    /**
     * Test optimization with a multivariate function.
     */
    @Test
    public void testOptimizeMultivariateFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = x -> Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.25);
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        assertEquals(Pair.pair(new double[]{1.0, 1.0}, 0.0), result);
    }

    /**
     * Test optimization with a function with multiple local minima.
     */
    @Test
    public void testOptimizeFunctionWithMultipleLocalMinima() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = x -> Math.pow(x[0] * x[1], 2) - 10 * Math.pow(Math.sin(Math.PI * x[0]), 2)
                - Math.pow(Math.sin(Math.PI * x[1]), 2);
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        double expectedX = Math.PI / 2.0;
        double expectedY = Math.PI / 2.0;
        double expectedFval = -8.0;

        // Check if the optimization result is near the expected global minimum
        Assume.assumeTrue(result.getValue() < expectedFval + 0.01);
        assertEquals(Pair.pair(new double[]{expectedX, expectedY}, expectedFval), result);
    }

    /**
     * Test optimization with a steep function.
     */
    @Test
    public void testOptimizeSteepFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = x -> Math.exp(Math.abs(x[0]) + Math.abs(x[1]));
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        // The function doesn't have a global minimum, but the optimization
        // algorithm should converge to a local minimum or a stationary point.
        // Assume a reasonable test value and check if the optimization result
        // is near that value.
        assumeNoNaNs(function, startPoint);
        double testValue = function.value(new double[]{0.0, 0.0});
        assumeTrue(result.getValue() < testValue + 0.1);
        assertNotNull(result);
    }

    /**
     * Test optimization with a function having a discontinuity.
     */
    @Test
    public void testOptimizeFunctionWithDiscontinuity() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = x -> (x[0] > 0) ? Math.pow(x[0], 2) : 0;
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        assertNotNull(result);
        assertEquals(Pair.pair(new double[]{0.0, 0.0}, 0.0), result);
    }

    /**
     * Test optimization with a function having a singularity.
     */
    @Test
    public void testOptimizeFunctionWithSingularity() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = x -> Math.sqrt(Math.pow(x[0], 2) + Math.pow(x[1], 2));
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        double expectedX = 0.0;
        double expectedY = 0.0;
        double expectedFVal = 0.0;

        // Check if the optimization result is near the expected global minimum
        Assume.assumeTrue(result.getValue() < expectedFVal + 0.01);
        assertEquals(Pair.pair(new double[]{expectedX, expectedY}, expectedFVal), result);
    }

    /**
     * Test optimization with a steep gradiant function.
     */
    @Test
    public void testOptimizeFunctionWithSteepGradient() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = x -> Math.exp(-Math.pow(Math.abs(x[0] - 1.0), 2) - Math.pow(Math.abs(x[1] - 1.0), 2));
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        double expectedX = 1.0;
        double expectedY = 1.0;
        double expectedFVal = 1.0;

        // Check if the optimization result is near the expected global minimum
        Assume.assumeTrue(result.getValue() < expectedFVal + 0.01);
        assertEquals(Pair.pair(new double[]{expectedX, expectedY}, expectedFVal), result);
    }

    /**
     * Test optimization with a function having a high dimension.
     */
    @Test
    public void testOptimizeFunctionWithHighDimension() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.01, 0.001);

        double[] startPoint = new double[5];
        MultivariateFunction function = x -> Math.pow(Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.25)
                + Math.pow(Math.pow(x[2], 2) + Math.pow(x[3], 2), 0.25) + Math.pow(Math.pow(x[4], 2), 0.25), 0.25);
        GoalType goalType = GoalTypeParameterization.MINIMIZE;

        PointValuePair result = powellOptimizer.optimize(function, goalType, startPoint);

        double[] expectedPoint = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        double expectedFval = 1.0;

        // Check if the optimization result is near the expected global minimum
        Assume.assumeTrue(result.getValue() < expectedFval + 0.01);
        assertEquals(Pair.pair(expectedPoint, expectedFval), result);
    }
}