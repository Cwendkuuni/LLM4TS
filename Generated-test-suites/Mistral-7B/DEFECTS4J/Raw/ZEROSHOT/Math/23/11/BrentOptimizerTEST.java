package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;

import static org.junit.Assert.*;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.0);
        assertNotNull(optimizer);

        try {
            new BrentOptimizer(-1e-5, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(1e-5, -0.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.0, 1e-5);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(2e-5, 0.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        Function f = new Function() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) * FastMath.exp(-x);
            }
        };

        UnivariateOptimizerResult result = optimizer.optimize(f, 0.5, 1.5, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(result.getPoint().x, 0.749859683461704, 1e-5);
        assertEquals(result.getValue(), -1.123162629626427, 1e-5);

        optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 10;
            }
        });

        result = optimizer.optimize(f, 0.5, 1.5, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(result.getPoint().x, 0.749859683461704, 1e-5);
        assertEquals(result.getValue(), -1.123162629626427, 1e-5);
        assertEquals(optimizer.getIterations(), 11);
    }
}