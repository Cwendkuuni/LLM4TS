package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        try {
            new BrentOptimizer(-0.1, 0.1, null);
            assert false; // exception expected
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        new BrentOptimizer(0.01, 0.1, null);
        new BrentOptimizer(0.1, 0.0, null);
        new BrentOptimizer(0.1, 0.1, null);
        new BrentOptimizer(2.0, 0.1, null);

        new BrentOptimizer(0.1, 0.1, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });

        new BrentOptimizer(0.1, 0.1, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous == null || current == null || previous.getValue() == current.getValue();
            }
        });
    }

    @Test
    public void testBrentOptimizerMinimizeSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10, 10);
        assertNull(result); // The optimization is not performed, the function is not bounded

        optimizer = new BrentOptimizer(0.01, 0.1, null);
        result = optimizer.optimize(function, -10, 10);
        assertEquals(0.0, result.getX(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithInfMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10, 10);
        assertNull(result); // The optimization is not performed, the function is not bounded

        optimizer = new BrentOptimizer(0.01, 0.1, null);
        result = optimizer.optimize(function, -10, 10);
        assertEquals(-1.0 / 0.1, result.getX(), 1e-7); // Approximation of -1.0
        assertEquals(-1.0 / result.getX(), result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithInfMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -100, 100);
        assertNull(result); // The optimization is not performed, the function is not bounded

        optimizer = new BrentOptimizer(0.01, 0.1, null);
        result = optimizer.optimize(function, -100, 100);
        assertEquals(0.0, result.getX(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithInfMinAndInfMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -100, 100);
        assertNull(result); // The optimization is not performed, the function is not bounded

        optimizer = new BrentOptimizer(0.01, 0.1, null);
        result = optimizer.optimize(function, -100, 100);
        assertEquals(Double.NaN, result.getX(), 0.0);
        assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithLocalMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.1 * Math.pow(x - 2.5, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -10, 10);
        assertEquals(2.5, result.getX(), 1e-7);
        assertEquals(0.0244948974278317, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithMultipleLocalMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.1 * Math.pow(x - 2.5, 2);
            }
        };

        UnivariatePointValuePair result1 = optimizer.optimize(function, -20, 0);
        assertEquals(0.0, result1.getX(), 1e-7);
        assertEquals(0.0, result1.getValue(), 1e-7);

        UnivariatePointValuePair result2 = optimizer.optimize(function, 0, 10);
        assertEquals(2.5, result2.getX(), 1e-7);
        assertEquals(0.0244948974278317, result2.getValue(), 1e-7);

        optimizer = new BrentOptimizer(1e-8, 1e-8, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 100;
            }
        });

        result1 = optimizer.optimize(function, -20, 0);
        assertEquals(0.0, result1.getX(), 1e-7);
        assertEquals(0.0, result1.getValue(), 1e-7);

        result2 = optimizer.optimize(function, 0, 10);
        assertEquals(2.5, result2.getX(), 1e-7);
        assertEquals(0.0244948974278317, result2.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithMultipleLocalMinimaAndInfMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.1 * Math.pow(x - 2.5, 2);
            }
        };

        UnivariatePointValuePair result1 = optimizer.optimize(function, -20, 10);
        assertEquals(0.0, result1.getX(), 1e-7);
        assertEquals(0.0, result1.getValue(), 1e-7);

        UnivariatePointValuePair result2 = optimizer.optimize(function, 0, 10);
        assertEquals(2.5, result2.getX(), 1e-7);
        assertEquals(0.0244948974278317, result2.getValue(), 1e-7);

        optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariatePointValuePair result3 = optimizer.optimize(function, -20, 50);
        assertEquals(-3.88238878153641, result3.getX(), 1e-7);
        assertEquals(14.3035644938328, result3.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithMultipleLocalMinimaAndInfMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.1 * Math.pow(x - 2.5, 2);
            }
        };

        UnivariatePointValuePair result1 = optimizer.optimize(function, -50, 0);
        assertEquals(-3.88238878153641, result1.getX(), 1e-7);
        assertEquals(14.3035644938328, result1.getValue(), 1e-7);

        UnivariatePointValuePair result2 = optimizer.optimize(function, 0, 10);
        assertEquals(2.5, result2.getX(), 1e-7);
        assertEquals(0.0244948974278317, result2.getValue(), 1e-7);

        optimizer = new BrentOptimizer(1e-8, 1e-8, null);
        UnivariatePointValuePair result3 = optimizer.optimize(function, -20, 50);
        assertEquals(0.0, result3.getX(), 1e-7);
        assertEquals(0.0, result3.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithMultipleLocalMinimaAndUserDefinedConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 10 || (previous != null && current != null && Math.abs(previous.getValue() - current.getValue()) < 0.001);
            }
        });

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.1 * Math.pow(x - 2.5, 2);
            }
        };

        UnivariatePointValuePair result1 = optimizer.optimize(function, -20, 10);
        assertEquals(0.0, result1.getX(), 1e-7);
        assertEquals(0.0, result1.getValue(), 1e-7);

        UnivariatePointValuePair result2 = optimizer.optimize(function, 0, 10);
        assertEquals(2.5, result2.getX(), 1e-7);
        assertEquals(0.0244948974278317, result2.getValue(), 1e-7);

        UnivariatePointValuePair result3 = optimizer.optimize(function, -20, 50);
        assertEquals(-3.88238878153641, result3.getX(), 1e-7);
        assertEquals(14.3035644938328, result3.getValue(), 1e-7);
    }
}