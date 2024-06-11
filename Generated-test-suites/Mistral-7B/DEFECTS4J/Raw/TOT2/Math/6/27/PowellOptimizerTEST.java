package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.point.PointValue;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimization() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0d, 1d);
        assertEquals(0d, result.getPoint(), 1e-6);
        assertEquals(0d, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a complex function.
     */
    @Test
    public void testOptimizationComplexFunction() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0d, 1d);
        assertEquals(-1.332067111, result.getPoint(), 1e-5);
        assertEquals(-1.831546331, result.getValue(), 1e-5);
    }

    /**
     * Test optimization with invalid input.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizationInvalidInput() {
        new PowellOptimizer(0d, 1e-5);
    }

    /**
     * Test optimization with a function with no solution.
     */
    @Test
    public void testOptimizationNoSolution() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x - 1;
            }
        };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0d, 1d);
        // The optimization should not find a solution and return the initial point.
        assertEquals(0d, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that is not differentiable at the minimum.
     */
    @Test
    public void testOptimizationNotDifferentiable() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1), 0.1);
            }
        };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0d, 1d);
        assertEquals(1d, result.getPoint(), 1e-6);
        assertEquals(0d, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that is not monotonous.
     */
    @Test
    public void testOptimizationNotMonotonous() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0d, 1d);
        // The optimization should not find a minimum and return an error.
        assertThrows(MaxEval.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0d, 1d));
    }
}