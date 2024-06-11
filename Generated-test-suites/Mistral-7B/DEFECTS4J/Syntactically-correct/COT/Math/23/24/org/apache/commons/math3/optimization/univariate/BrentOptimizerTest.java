package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.convergence.IterationNumberConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // Test valid constructor
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorNegativeAbs() {
        // Test constructor with negative absolute threshold
        new BrentOptimizer(1.0, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorSmallRelativeThreshold() {
        // Test constructor with small relative threshold
        new BrentOptimizer(0.001, 1.0);
    }

    @Test
    public void testBrentOptimizerOptimizeBasic() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, Math.PI / 2));
        assertEquals(Math.PI / 2, result.getPoint(), 0.0001);
        assertEquals(-Math.sin(Math.PI / 2), result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerOptimizeWithConvergenceChecker() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3, new IterationNumberConvergenceChecker(1000));
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(-Math.PI, Math.PI), MaxEval.MAX_EVAL);
        assertTrue(result != null);
        assertEquals(Math.PI / 2, result.getPoint(), 0.0001);
        assertEquals(-Math.pow(Math.sin(Math.PI / 2), 2), result.getValue(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerOptimizeInvalidBounds() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.optimize(f, new SimpleBounds(Math.PI, 0));
    }

    @Test
    public void testBrentOptimizerOptimizeEmptyFunction() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 1));
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithInfMinimum() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(1, 10));
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerOptimizeFunctionWithInfMaximum() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.optimize(f, new SimpleBounds(-1, 1));
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithInfMaximumAndGoalTypeMinimize() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(-1, 1));
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithNegativeValue() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return -BesselFunctions.besselJ(0, x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 1));
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(-BesselFunctions.besselJ(0, 0.0), result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithDifferentGoalType() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, Math.PI / 2));
        assertEquals(Math.PI / 2, result.getPoint(), 0.0001);
        assertEquals(-Math.sin(Math.PI / 2), result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithInitialGuess() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(-3, 3), InitialGuess.MENTERIS_BRENT);
        assertTrue(result != null);
        assertEquals(-1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithInitialGuessOutOfBounds() {
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(-3, 3), InitialGuess.MENTERIS_BRENT, 10);
        assertTrue(result != null);
        assertEquals(-1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);

        optimizer = new BrentOptimizer(1.0, 1.0);
        PointValuePair resultOutOfBounds = optimizer.optimize(f, new SimpleBounds(-10, 3), InitialGuess.MENTERIS_BRENT, 10);
        assertFalse(resultOutOfBounds == null);
        assertNull(resultOutOfBounds.getPoint());
    }
}