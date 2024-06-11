package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for the PowellOptimizer.
 */
public class PowellOptimizerTest extends AbstractMathTestCase {

    /**
     * Test the Powell optimizer with a univariate function.
     */
    @Test
    public void testUnivariateFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0e-5, 1.0e-5);
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        PointValuePair solution = powellOptimizer.optimize(function, new SimpleBounds(0.0, 5.0));
        assertEquals(2.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
        assertNull(powellOptimizer.getLastIterate());
    }

    /**
     * Test the Powell optimizer with a multivariate function.
     */
    @Test
    public void testMultivariateFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0e-5, 1.0e-5);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double value = 0.0;
                for (int i = 0; i < x.length; i++) {
                    value += Math.pow(x[i] - 2.0, 2.0);
                }
                return value;
            }
        };
        PointValuePairCollection solutions = powellOptimizer.optimize(function,
                new AroundMinimumInitializer(new double[] {2.0, 2.0, 2.0}),
                100);
        assertTrue(solutions.size() > 0);
        PointValuePair solution = solutions.getPointValues().get(0);
        assertArrayEquals(new double[] {2.0, 2.0, 2.0}, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
        assertArrayEquals(new double[] {2.0, 2.0, 2.0}, powellOptimizer.getLastIterate());
    }

    /**
     * Test the convergence checker.
     */
    @Test
    public void testConvergenceChecker()
            throws NumberIsTooSmallException {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0e-5, 1.0e-5,
                new SimpleUnivariateValueChecker(1.0e-6, 1.0e-7));
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        powellOptimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iter, PointValuePair previous,
                                     PointValuePair current) {
                return Precision.abs(previous.getValue() - current.getValue()) < 1.0e-7;
            }
        });
        PointValuePair solution = powellOptimizer.optimize(function, new SimpleBounds(0.0, 5.0));
        assertEquals(2.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
        assertNull(powellOptimizer.getLastIterate());
    }

    /**
     * Test the Powell optimizer with a multivariate function and convergence
     * checker.
     */
    @Test
    public void testMultivariateFunctionConvergenceChecker()
            throws NumberIsTooSmallException {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0e-5, 1.0e-5,
                new SimpleUnivariateValueChecker(1.0e-6, 1.0e-7),
                1.0e-6, 1.0e-7);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double value = 0.0;
                for (int i = 0; i < x.length; i++) {
                    value += Math.pow(x[i] - 2.0, 2.0);
                }
                return value;
            }
        };
        powellOptimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iter, PointValuePair previous,
                                     PointValuePair current) {
                double deltaF = previous.getValue() - current.getValue();
                for (int i = 0; i < x.length; i++) {
                    double deltaX = previous.getPoint()[i] - current.getPoint()[i];
                    double delta = Math.abs(deltaF) + Math.abs(deltaX) + 1.0e-8;
                    if (Math.abs(deltaF) < 1.0e-6 || Math.abs(deltaX) < powellOptimizer.getRelativeThreshold() || delta < powellOptimizer.getAbsoluteThreshold()) {
                        return true;
                    }
                }
                return false;
            }
        });
        PointValuePairCollection solutions = powellOptimizer.optimize(function,
                new AroundMinimumInitializer(new double[] {2.0, 2.0, 2.0}),
                100);
        assertTrue(solutions.size() > 0);
        PointValuePair solution = solutions.getPointValues().get(0);
        assertArrayEquals(new double[] {2.0, 2.0, 2.0}, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
        assertArrayEquals(new double[] {2.0, 2.0, 2.0}, powellOptimizer.getLastIterate());
    }

    /**
     * Test that the Powell optimizer throws an exception when the absolute
     * threshold is not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(1.0e-5, -1.0e-5);
    }

    /**
     * Test that the Powell optimizer throws an exception when the relative
     * threshold is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new PowellOptimizer(1.0e-9, 1.0e-5);
    }
}