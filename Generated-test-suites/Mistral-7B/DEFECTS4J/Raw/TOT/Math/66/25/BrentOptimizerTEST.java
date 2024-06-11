package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.Log;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test for minimization of a simple function.
     */
    @Test
    public void testMinimizeSimpleFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - MathConstants.E, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Optimization failed: " + e.getMessage(), e);
        }
        assertEquals(Math.E - 1.0, result, 1e-6);
    }

    /**
     * Test for minimization of a function with a unique minimum.
     */
    @Test
    public void testMinimizeFunctionWithUniqueMinimum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Optimization failed: " + e.getMessage(), e);
        }
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test for minimization of a function with multiple local minima.
     */
    @Test
    public void testMinimizeFunctionWithMultipleLocalMinima() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                double xAbs = Math.abs(x);
                return (xAbs > 1.0) ? (xAbs * Math.exp(-Math.log(xAbs + 1.0) / 2.0)) : xAbs * xAbs;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 3.0, 1.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Optimization failed: " + e.getMessage(), e);
        }
        assertEquals(1.0, result, 1e-6);
    }

    /**
     * Test for minimization of a function with no real roots.
     */
    @Test
    public void testMinimizeFunctionWithNoRealRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) * Math.sin(Math.PI / x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(FunctionEvaluationException.class, () -> {
            optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        });
    }

    /**
     * Test for minimization with invalid arguments.
     */
    @Test
    public void testMinimizeWithInvalidArguments() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NullArgumentException.class, () -> {
            optimizer.optimize(null, GoalType.MINIMIZE, -1.0, 1.0, 0.5);
        });
        assertThrows(NotStrictlyPositiveException.class, () -> {
            optimizer.optimize(f, GoalType.MINIMIZE, -1.0, -1.0, 0.5);
        });
        assertThrows(NumberIsTooSmallException.class, () -> {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, -0.5);
        });
        assertThrows(MaxIterationsExceededException.class, () -> {
            optimizer.setMaxEvaluations(1);
            optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5);
        });
    }

    /**
     * Test for maximization of a simple function.
     */
    @Test
    public void testMaximizeSimpleFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - MathConstants.E, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result;
        try {
            result = optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0, 0.5);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Optimization failed: " + e.getMessage(), e);
        }
        assertEquals(Math.E, result, 1e-6);
    }

    /**
     * Test for maximization of a function with a unique maximum.
     */
    @Test
    public void testMaximizeFunctionWithUniqueMaximum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Log.log(2.0, Math.exp(Math.PI * x));
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result;
        try {
            result = optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0, 0.5);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Optimization failed: " + e.getMessage(), e);
        }
        assertEquals(0.5 * Math.PI, result, 1e-6);
    }

    /**
     * Test for maximization of a function with multiple local maxima.
     */
    @Test
    public void testMaximizeFunctionWithMultipleLocalMaxima() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                double xAbs = Math.abs(x);
                return (xAbs > 1.0) ? (xAbs * Math.exp(-Math.log(xAbs + 1.0) / 2.0)) : -xAbs * xAbs;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        double result;
        try {
            result = optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 3.0, 1.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Optimization failed: " + e.getMessage(), e);
        }
        assertEquals(-1.0, result, 1e-6);
    }

    /**
     * Test for maximization of a function with no real roots.
     */
    @Test
    public void testMaximizeFunctionWithNoRealRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) * Math.cos(Math.PI / x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(FunctionEvaluationException.class, () -> {
            optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        });
    }

    /**
     * Test for maximization with invalid arguments.
     */
    @Test
    public void testMaximizeWithInvalidArguments() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NullArgumentException.class, () -> {
            optimizer.optimize(null, GoalType.MAXIMIZE, -1.0, 1.0, 0.5);
        });
        assertThrows(NotStrictlyPositiveException.class, () -> {
            optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, -1.0, 0.5);
        });
        assertThrows(NumberIsTooSmallException.class, () -> {
            optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0, -0.5);
        });
        assertThrows(MaxIterationsExceededException.class, () -> {
            optimizer.setMaxEvaluations(1);
            optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0, 0.5);
        });
    }
}