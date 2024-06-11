package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariatePointValuePair solution = optimizer.optimize(x -> Math.pow(x, 2) - 2, -3.0, 3.0);
        assertEquals(-1.0, solution.getPoint(), 1e-6);
        assertEquals(1.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, (iter, previous, current) -> {
            double error = Math.abs(current.getPoint() - previous.getPoint());
            return error < 1e-7;
        });
        UnivariatePointValuePair solution = optimizer.optimize(x -> Math.exp(-x), 0.0, 1.0);
        assertEquals(0.4342945, solution.getPoint(), 1e-6);
        assertEquals(0.5403023, solution.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-5, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        new BrentOptimizer(-1e-5, 1e-5, null);
    }
}