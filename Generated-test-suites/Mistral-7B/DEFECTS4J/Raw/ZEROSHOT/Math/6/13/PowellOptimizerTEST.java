package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 * Test class for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    @org.junit.Test
    public void testPowellOptimizer() {
        MultivariateFunction func = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        // Test with default settings.
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(func);
        double[] expected = new double[] {-1.0, -1.0};
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(expected, result.getPoint(), 1e-6);
        assertEquals(-2.0, result.getValue(), 1e-6);

        // Test with custom convergence checker.
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                double[] p1 = previous.getPoint();
                double[] p2 = current.getPoint();
                double[] d = new double[] {p2[0] - p1[0], p2[1] - p1[1]};
                double dist = Math.sqrt(Arrays.stream(d).map(Math::pow).sum());
                return dist < 1e-7;
            }
        };
        PowellOptimizer optimizer2 = new PowellOptimizer(1e-6, 1e-6, customChecker);
        result = optimizer2.optimize(func);
        assertEquals(GoalType.MINIMIZE, optimizer2.getGoalType());
        assertArrayEquals(expected, result.getPoint(), 1e-6);
        assertEquals(-2.0, result.getValue(), 1e-6);

        // Test with invalid input.
        try {
            new PowellOptimizer(-1.0, 1e-6);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new PowellOptimizer(1e-6, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new PowellOptimizer(1e-6, 1e-6, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new PowellOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
                @Override
                public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                    return false;
                }
            });
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test with convergence checker that never converges.
        ConvergenceChecker<PointValuePair> neverConverter = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };
        PowellOptimizer optimizer3 = new PowellOptimizer(1e-6, 1e-6, neverConverter);
        try {
            optimizer3.optimize(func);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}