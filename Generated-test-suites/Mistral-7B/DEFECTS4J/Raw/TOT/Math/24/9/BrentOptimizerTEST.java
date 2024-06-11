package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.NonUnaryOptimizer;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function f(x) = x^3 - 2x^2 + x - 1.
     */
    private static UnivariateFunction testFunction = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x * x * x - 2 * x * x + x - 1;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    };

    /**
     * Test the optimization of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateOptimizer result = optimizer.optimize(testFunction, new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with a local minimum.
     */
    @Test
    public void testFunctionWithLocalMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-(x - 2) * (x - 2)) - Math.cos(x);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        UnivariateOptimizer result = optimizer.optimize(function, new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        assertEquals(-3.1265, result.getValue(), 1e-4);
        assertEquals(2.1125, result.getPoint(), 1e-4);
    }

    /**
     * Test the optimization of a function with a singularity.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFunctionWithSingularity() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-20, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.optimize(function, new SimpleBounds(1e-10, 1), GoalType.MINIMIZE);
    }

    /**
     * Test the optimization of a function with infinite minimum.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testFunctionWithInfiniteMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, 100);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.optimize(function, new SimpleBounds(1e-5, 1), GoalType.MINIMIZE);
    }

    /**
     * Test the optimization of a function with a limit.
     */
    @Test
    public void testFunctionWithLimit() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 1) {
                    return x * Math.log(x);
                } else {
                    return -x * Math.log(x);
                }
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        UnivariateOptimizer result = optimizer.optimize(function, new SimpleBounds(0, 2), GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-4);
        assertEquals(1.0, result.getPoint(), 1e-4);
    }

    /**
     * Test the optimization of a function with a discontinuity.
     */
    @Test
    public void testFunctionWithDiscontinuity() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * x;
                } else {
                    throw new UnsupportedOperationException("Not defined for negative x.");
                }
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        try {
            optimizer.optimize(function, new SimpleBounds(-1, 1), GoalType.MINIMIZE);
            fail("Expected an UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // Expected.
        }
    }

    /**
     * Test the optimization of a function with a plateau.
     */
    @Test
    public void testFunctionWithPlateau() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x > 0) ? x * x : 0;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        UnivariateOptimizer result = optimizer.optimize(function, new SimpleBounds(-1, 1), GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue(), 1e-4);
        assertEquals(0.0, result.getPoint(), 1e-4);
    }

    /**
     * Test the optimization of a function with an initial guess outside the interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithInitialGuessOutsideInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.sin(x);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.optimize(function, new SimpleBounds(-1, 1), GoalType.MINIMIZE, new InitialGuess(-5));
    }

    /**
     * Test the optimization of a function with an initial guess inside the interval.
     */
    @Test
    public void testFunctionWithInitialGuessInsideInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.sin(x);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        UnivariateOptimizer result = optimizer.optimize(function, new SimpleBounds(-1, 1), GoalType.MINIMIZE, new InitialGuess(0));
        assertEquals(-1.0726, result.getValue(), 1e-4);
        assertEquals(-0.5366, result.getPoint(), 1e-4);
    }
}