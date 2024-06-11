package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        try {
            new BrentOptimizer(-0.1, 0.0);
            assert false;
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            new BrentOptimizer(0.0, -0.1);
            assert false;
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        new BrentOptimizer(1.0, 0.0);
        new BrentOptimizer(0.01, 0.0);
    }

    @Test
    public void testDoOptimize_minimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.0, result.getX(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_minimize_convergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new MyConvergenceChecker());

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.718281828459045, result.getX(), 1e-6);
        assertEquals(0.718281828459045, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_minimize_largeStep() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-2, 1.0e-6);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertTrue(result.getX() > 0.6);
        assertTrue(result.getX() < 0.8);
        assertEquals(Math.log(Math.E), result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_maximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, GoalType.MAXIMIZE);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.0, result.getX(), 1e-6);
        assertEquals(-Math.log(Math.E), result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_maximize_convergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, GoalType.MAXIMIZE, new MyConvergenceChecker());

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(Math.E, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_maximize_largeStep() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-2, 1.0e-6, GoalType.MAXIMIZE);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertTrue(result.getX() > 1.6);
        assertTrue(result.getX() < 1.8);
        assertEquals(Math.pow(Math.E, 1.6), result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_largeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);

        assertEquals(3.141592653589793, result.getX(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_largeInterval_convergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new MyConvergenceChecker());

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);

        assertEquals(3.141592653589793, result.getX(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_largeInterval_largeStep() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-2, 1.0e-6);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);

        assertTrue(Math.abs(result.getX() - 3.141592653589793) < 1.570796326794897);
        assertTrue(Math.abs(result.getValue()) < 1e-6);
    }

    @Test
    public void testDoOptimize_largeInterval_largeStep_convergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-2, 1.0e-6, new MyConvergenceChecker());

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);

        assertTrue(Math.abs(result.getX() - 3.141592653589793) < 1.570796326794897);
        assertTrue(Math.abs(result.getValue()) < 1e-6);
    }

    @Test
    public void testDoOptimize_largeInterval_largeStep_largeFunctionValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-2, 1.0e-6);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Double.MAX_VALUE;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);

        assertTrue(Math.abs(result.getX()) > 1e10);
        assertTrue(Math.abs(result.getValue()) > 1e10);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testDoOptimize_emptyInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        optimizer.optimize(function, 0.0, 0.0);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return previous != null && Math.abs(previous.getValue() - current.getValue()) < 1e-12;
        }
    }

    private static interface UnivariateFunction {
        double value(double x);
    }
}