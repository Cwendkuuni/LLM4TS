package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.SimpleStartingPointGenerator;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * The PowellOptimizer instance under test.
     */
    private PowellOptimizer optimizer;

    /**
     * Sets up the test.
     * @throws NotStrictlyPositiveException
     */
    @Before
    public void setUp() throws NotStrictlyPositiveException {
        optimizer = new PowellOptimizer(1.0e-4, 1.0e-4);
    }

    /**
     * Test the default constructor with no arguments.
     */
    @Test
    public void testDefaultConstructor() {
        optimizer = new PowellOptimizer();
        assertEquals(1.0e-4, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.0, optimizer.getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @Test
    public void testConstructorWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new MyConvergenceChecker();
        optimizer = new PowellOptimizer(1.0e-4, 1.0e-4, customChecker);
        assertFalse(customChecker.isConverged(0, null, null));
        assertEquals(1.0e-4, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.0, optimizer.getAbsoluteThreshold(), 0.0);
        assertTrue(customChecker.isConverged(1, null, null));
    }

    /**
     * Test the optimize method with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateFunction function = new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, new SimpleStartingPointGenerator(1.0), GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test the optimize method with a non-convex function.
     */
    @Test
    public void testOptimizeNonConvexFunction() {
        UnivariateFunction function = new UnivariateObjectiveFunction() {
            public double value(double x) {
                if (x > 0) {
                    return x * Math.sin(x);
                } else {
                    return -x * Math.sin(-x);
                }
            }
        };
        PointValuePair result = optimizer.optimize(function, new SimpleStartingPointGenerator(1.0), GoalType.MINIMIZE);
        assertEquals(1.27411, result.getPoint(), 1e-4);
        assertEquals(-3.14159, result.getValue(), 1e-4);
    }

    /**
     * Test the optimize method with a multivariate function.
     */
    @Test
    public void testOptimizeMultivariateFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            public double[] gradient(double[] x) {
                double[] gradient = new double[2];
                gradient[0] = 2 * x[0];
                gradient[1] = 2 * x[1];
                return gradient;
            }
        };
        PointValuePair result = optimizer.optimize(function, new AveragedStartingPointGenerator(2), GoalType.MINIMIZE);
        assertEquals(Math.sqrt(2.0) / 2.0, result.getPoint()[0], 1e-4);
        assertEquals(Math.sqrt(2.0) / 2.0, result.getPoint()[1], 1e-4);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test the optimize method with user-defined convergence checker.
     */
    @Test
    public void testOptimizeWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new MyConvergenceChecker();
        optimizer = new PowellOptimizer(1.0e-4, 1.0e-4, customChecker);
        UnivariateFunction function = new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, new SimpleStartingPointGenerator(1.0), GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
        assertTrue(customChecker.isConverged(1, null, result));
    }

    /**
     * Test the optimize method with a non-convex multivariate function.
     */
    @Test
    public void testOptimizeMultivariateNonConvexFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double x1 = x[0];
                double x2 = x[1];
                if (x1 > 0 && x2 > 0) {
                    return x1 * x1 + x2 * x2;
                } else if (x1 > 0 && x2 < 0) {
                    return -(x1 * x1 + x2 * x2);
                } else if (x1 < 0 && x2 > 0) {
                    return -(x1 * x1 + x2 * x2);
                } else { // x1 < 0 && x2 < 0
                    return x1 * x1 + x2 * x2;
                }
            }
        };
        PointValuePair result = optimizer.optimize(function, new AveragedStartingPointGenerator(2), GoalType.MINIMIZE);
        assertEquals(-1.27411, result.getPoint()[0], 1e-4);
        assertEquals(1.27411, result.getPoint()[1], 1e-4);
        assertEquals(-3.14159, result.getValue(), 1e-4);
    }

    /**
     * Custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double difference = Math.abs(previous.getValue() - current.getValue());
            return difference < 1e-6;
        }
    }
}