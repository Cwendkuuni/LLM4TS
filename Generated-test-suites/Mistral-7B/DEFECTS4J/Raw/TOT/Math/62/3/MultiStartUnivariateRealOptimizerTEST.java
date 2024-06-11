package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) * Math.exp(-x * x / 2);
        }

        @Override
        public double derivative(double x) {
            return (Math.cos(x) * Math.exp(-x * x / 2) - x * Math.sin(x) * Math.exp(-x * x / 2)) / 2;
        }
    }

    /**
     * Test the optimization with a simple function.
     */
    @Test
    public void testOptimize() {
        // Initialize test function.
        final UnivariateRealFunction function = new TestFunction();

        // Initialize optimizer.
        final UnivariateRealOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomDataGenerator());

        // Set convergence criteria.
        final double tolerance = 1e-7;
        final double maxIterations = 1000;
        multiOptimizer.setMaxIterations(maxIterations);
        multiOptimizer.setTolerance(tolerance);

        // Optimize function.
        PointValuePair optimum = null;
        try {
            optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }

        // Check optimization result.
        Assert.assertNotNull(optimum);
        Assert.assertEquals(Precision.isClose(function.value(optimum.getPoint()), 0.0, tolerance), true);
    }

    /**
     * Test the optimization with a function having multiple local minima.
     */
    @Test
    public void testOptimizeMultipleLocalMinima() {
        // Initialize test function.
        final UnivariateRealFunction function = new TestFunction();

        // Initialize optimizer.
        final UnivariateRealOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomDataGenerator());

        // Set convergence criteria.
        final double tolerance = 1e-7;
        final double maxIterations = 1000;
        multiOptimizer.setMaxIterations(maxIterations);
        multiOptimizer.setTolerance(tolerance);

        // Optimize function.
        PointValuePair[] optima = null;
        try {
            optima = multiOptimizer.optimizeMultipleStarts(function, GoalType.MINIMIZE, -5, 5);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }

        // Check optimization result.
        Assert.assertNotNull(optima);
        Assert.assertEquals(optima.length, 3);
        for (PointValuePair pvp : optima) {
            Assert.assertNotNull(pvp);
            Assert.assertEquals(Precision.isClose(function.value(pvp.getPoint()), 0.0, tolerance), true);
        }
    }

    /**
     * Test the optimization with a function having no solution.
     */
    @Test
    public void testOptimizeNoSolution() {
        // Initialize test function.
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        // Initialize optimizer.
        final UnivariateRealOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomDataGenerator());

        // Set convergence criteria.
        final double tolerance = 1e-7;
        final double maxIterations = 1000;
        multiOptimizer.setMaxIterations(maxIterations);
        multiOptimizer.setTolerance(tolerance);

        // Optimize function.
        PointValuePair optimum = null;
        try {
            optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        } catch (Exception e) {
            Assert.assertEquals("Failed to find a solution. Returned: " + e.getMessage(), e.getMessage(), "Failed to find a solution.");
        }

        // Check optimization result.
        Assert.assertNull(optimum);
    }

    /**
     * Test the optimization with a function having no solution and different bounds.
     */
    @Test
    public void testOptimizeNoSolutionDifferentBounds() {
        // Initialize test function.
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        // Initialize optimizer.
        final UnivariateRealOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomDataGenerator());

        // Set convergence criteria.
        final double tolerance = 1e-7;
        final double maxIterations = 1000;
        multiOptimizer.setMaxIterations(maxIterations);
        multiOptimizer.setTolerance(tolerance);

        // Optimize function.
        PointValuePair optimum = null;
        try {
            optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, 1, 10);
        } catch (Exception e) {
            Assert.assertEquals("Failed to find a solution. Returned: " + e.getMessage(), e.getMessage(), "Failed to find a solution.");
        }

        // Check optimization result.
        Assert.assertNull(optimum);
    }
}