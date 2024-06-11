package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.RandomStartingPointGenerator;
import org.apache.commons.math3.optim.point.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization of a simple function.
     *
     * @throws NotStrictlyPositiveException if initial threshold values are not positive.
     */
    @Test
    public void testSimpleFunctionOptimization() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 0.5, 2) + Math.sin(x);
            }
        };
        PointValuePair solution = optimizer.optimize(f, null);
        double expectedSolution = 0.43389336429125;
        assertEquals("Expected solution is not correct.", expectedSolution, solution.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with a large search space.
     *
     * @throws NotStrictlyPositiveException if initial threshold values are not positive.
     */
    @Test
    public void testLargeSearchSpaceOptimization() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-(x + 1));
            }
        };
        PointValuePair solution = optimizer.optimize(f, null);
        double expectedSolution = 0.5;
        assertEquals("Expected solution is not correct.", expectedSolution, solution.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with multiple local optima.
     *
     * @throws NotStrictlyPositiveException if initial threshold values are not positive.
     */
    @Test
    public void testFunctionWithMultipleLocalOptima() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * Math.exp(-x) + Math.sin(x);
            }
        };
        PointValuePair solution = optimizer.optimize(f, null);
        double expectedSolution = 0.74165857966834;
        assertEquals("Expected solution is not correct.", expectedSolution, solution.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with a non-convex search space.
     *
     * @throws NotStrictlyPositiveException if initial threshold values are not positive.
     */
    @Test
    public void testFunctionWithNonConvexSearchSpace() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 1) {
                    return Math.pow(x - 1, 2);
                } else {
                    return -Math.pow(x, 2);
                }
            }
        };
        PointValuePair solution = optimizer.optimize(f, null);
        double expectedSolution = 1;
        assertEquals("Expected solution is not correct.", expectedSolution, solution.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with invalid input (null function).
     *
     * @throws NotStrictlyPositiveException if initial threshold values are not positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunctionOptimization() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        optimizer.optimize(null, null);
    }

    /**
     * Test optimization with random initial points.
     *
     * @throws NotStrictlyPositiveException if initial threshold values are not positive.
     */
    @Test
    public void testRandomInitialPoints() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 0.5, 2) + Math.sin(x);
            }
        };
        PointValuePair solution = optimizer.optimize(f, new RandomStartingPointGenerator(1));
        double expectedSolution = 0.43389336429125;
        assertEquals("Expected solution is not correct.", expectedSolution, solution.getPoint(), 1e-5);

        PointValuePair solution2 = optimizer.optimize(f, new AveragedStartingPointGenerator(10));
        double expectedSolution2 = 0.43389336429125;
        assertEquals("Expected solution is not correct.", expectedSolution2, solution2.getPoint(), 1e-5);
    }
}