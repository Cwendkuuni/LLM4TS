package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithDefaults() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2) - 4 * x + 4, -2.0, 2.0);
        assertEquals(-1.5, result.getPoint(), 0.001);
        assertEquals(3.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerCustomConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 0.001;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0, customChecker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x), 0.0, Math.PI);
        assertEquals(Math.PI / 2, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerOutOfRangeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
        try {
            optimizer.optimize(x -> Math.pow(x, 2) - 4 * x + 4, -5.0, 2.0);
        } catch (AssertionError e) {
            Assert.assertEquals("Expected interval to be non-empty: (2.0, 2.0)", e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-0.01, 0.01);
    }
}