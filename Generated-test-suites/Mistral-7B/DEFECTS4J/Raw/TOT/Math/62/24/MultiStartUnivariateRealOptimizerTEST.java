package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.WeightedSumFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** A simple test function. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        public double value(double x) {
            return Math.sin(x) * Math.exp(-x);
        }
    };

    /** A test function with multiple local minima. */
    private static final UnivariateRealFunction MULTI_LOCAL_MINIMA = new UnivariateRealFunction() {
        public double value(double x) {
            return Math.pow(Math.abs(x - 2.0), 2.0) + Math.pow(Math.abs(x + 2.0), 2.0);
        }
    };

    /** A test function with no root. */
    private static final UnivariateRealFunction NO_ROOT = new UnivariateRealFunction() {
        public double value(double x) {
            return Math.sin(Math.PI) * Math.cos(Math.PI * x);
        }
    };

    /** A test function with an infinite root. */
    private static final UnivariateRealFunction INFINITE_ROOT = new UnivariateRealFunction() {
        public double value(double x) {
            return 1.0 / x;
        }
    };

    /** A test function with a complex root. */
    private static final UnivariateRealFunction COMPLEX_ROOT = new UnivariateRealFunction() {
        public double value(double x) {
            return Math.pow(x - 2.0, 2.0) + Math.pow(Math.sqrt(2.0) * Math.I * x, 2.0);
        }
    };

    /**
     * Test optimization with a single start point.
     */
    @Test
    public void testSingleStart() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 1, new RandomGenerator() {
                    public double nextDouble() {
                        throw new UnsupportedOperationException();
                    }
                });

        PointValuePair result = null;
        try {
            result = multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint(), 1e-8);
        Assert.assertEquals(-1.24200457e-01, result.getValue(), 1e-8);
    }

    /**
     * Test optimization with multiple start points.
     */
    @Test
    public void testMultipleStarts() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new java.util.Random());

        PointValuePair[] results = null;
        try {
            results = multiStartOptimizer.optimize(MULTI_LOCAL_MINIMA, GoalType.MINIMIZE, -5.0, 5.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(results);
        Assert.assertEquals(5, results.length);
        for (PointValuePair p : results) {
            Assert.assertNotNull(p);
            Assert.assertNotNull(p.getPoint());
            Assert.assertFalse(Double.isNaN(p.getPoint()));
            Assert.assertFalse(Double.isInfinite(p.getPoint()));
            Assert.assertEquals(0.0, p.getValue(), 1e-8);
            // The points should be different because of the different
            // starting points, but the values should be close to the
            // minimum.
            for (int i = 0; i < results.length; ++i) {
                Assert.assertNotSame(results[i], results[0]);
                Assert.assertNotEquals(results[0].getPoint(), results[i].getPoint());
                Assert.assertEquals(results[0].getValue(), results[i].getValue(), 1e-6);
            }
        }
    }

    /**
     * Test optimization with a function having no root.
     */
    @Test(expected = NoRootFoundException.class)
    public void testNoRoot() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new java.util.Random());

        multiStartOptimizer.optimize(NO_ROOT, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with a function having an infinite root.
     */
    @Test(expected = ConvergenceException.class)
    public void testInfiniteRoot() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new java.util.Random());

        multiStartOptimizer.optimize(INFINITE_ROOT, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with a function having a complex root.
     */
    @Test
    public void testComplexRoot() {
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new java.util.Random());

        PointValuePair result = null;
        try {
            result = multiStartOptimizer.optimize(COMPLEX_ROOT, GoalType.MINIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertTrue(Double.isNaN(result.getPoint()));
        // The value is NaN because the function has a complex root.
        Assert.assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    /**
     * Test optimization with a weighted sum objective function.
     */
    @Test
    public void testWeightedSum() {
        UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new java.util.Random());

        WeightedSumFunction wsf = new WeightedSumFunction(new double[] { 2.0, 1.0 },
                                                           new UnivariateRealFunction[] {
                                                               new UnivariateRealFunction() {
                                                                   public double value(double x) {
                                                                       return x * x;
                                                                   }
                                                               },
                                                               new UnivariateRealFunction() {
                                                                   public double value(double x) {
                                                                       return Math.exp(-x);
                                                                   }
                                                               }
                                                           });

        PointValuePair result = null;
        try {
            result = multiStartOptimizer.optimize(wsf, GoalTypeWeightedSum.MINIMIZE, new SimpleBounds(0.0, 10.0));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertFalse(Double.isNaN(result.getPoint()));
        Assert.assertFalse(Double.isInfinite(result.getPoint()));
        Assert.assertEquals(1.64843654e-01, result.getValue(), 1e-8);
    }
}