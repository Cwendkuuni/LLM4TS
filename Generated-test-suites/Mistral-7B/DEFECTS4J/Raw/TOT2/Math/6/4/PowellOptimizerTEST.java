package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private double[] startPoint;
    private MultivariateFunction testFunction;

    /**
     * Sets up the test environment.
     * Initializes the optimizer and the test function.
     *
     * @throws NotStrictlyPositiveException if the threshold values are not positive.
     * @throws NumberIsTooSmallException if the relative threshold is less than the machine epsilon.
     */
    @Before
    public void setUp() throws NotStrictlyPositiveException, NumberIsTooSmallException {
        optimizer = new PowellOptimizer(1e-4, 1e-4);
        startPoint = new double[]{0.0, 0.0};
        testFunction = f -> Math.pow(x[0] * x[0] + x[1] * x[1], 2.0);
    }

    /**
     * Tests the basic functionality of the {@link PowellOptimizer} class.
     */
    @Test
    public void testBasicFunctionality() {
        PointValuePair result = optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE, new MaxEval(1000), null);
        double[] expected = new double[]{0.0, 0.0};
        Assert.assertArrayEquals(expected, result.getPoint(), 0.01);
        Assert.assertEquals(0.0, result.getValue(), 0.01);
    }

    /**
     * Tests the behavior of the {@link PowellOptimizer} class when the function is not defined in a quadratic region around the starting point.
     */
    @Test
    public void testOutOfQuadraticRegion() {
        double[] startPoint = new double[]{-1.0, 1.0};
        MultivariateFunction testFunction = f -> Math.abs(x[0] * Math.exp(x[1]));
        PointValuePair result = optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE, new MaxEval(1000), null);
        Assert.assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    /**
     * Tests the behavior of the {@link PowellOptimizer} class when the function is not continuous.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testDiscontinuousFunction() {
        double[] startPoint = new double[]{0.0, 0.0};
        MultivariateFunction testFunction = f -> {
            if (x[0] > 0.5) {
                throw new NumberIsTooSmallException(-1.0);
            }
            return Math.pow(x[0] * x[0] + x[1] * x[1], 2.0);
        };
        optimizer.optimize(testFunction, startPoint, GoalType.MINIMIZE, new MaxEval(1000), null);
    }
}