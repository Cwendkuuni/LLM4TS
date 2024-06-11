package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructionWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 0.0001;
            }
        });
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerConstructionWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructionWithNegativeRelativeThreshold() {
        new BrentOptimizer(-0.01, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructionWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructionWithRelativeThresholdSmallerThanMachineEpsilon() {
        new BrentOptimizer(1.0e-15, 0.001);
    }

    @Test
    public void testBrentOptimizerMinimizationExample() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2, 2) + 5;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 4);
        assertEquals(2, result.getPoint(), 0.001);
        assertEquals(-9.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMaximizationExample() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 3, 2) + 2;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 5);
        assertEquals(3, result.getPoint(), 0.001);
        assertEquals(11.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5) {
            @Override
            protected ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker() {
                return new ConvergenceChecker<UnivariatePointValuePair>() {
                    @Override
                    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                        return current.getValue() < 0.001;
                    }
                };
            }
        };
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2) + 0.01;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, 0, 4);
        assertEquals(2, result.getPoint(), 0.001);
        assertEquals(4.0001, result.getValue(), 0.01);
    }
}