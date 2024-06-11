package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BroydenFletcherGoldfarbShanno;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BroydenFletcherGoldfarbShanno.Options;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test for {@link GaussianFitter#fit(double[], double[])}.
     */
    @Test
    public void testFit() {
        // Test with initial guesses
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
            new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
            new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
            new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
            new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
            new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
            new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
            new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
            new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
            new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
            new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
            new WeightedObservedPoint(4.08366408, 620014.0, 1.0)
        };
        double[] initialGuess = new double[] { 1.0, 4.0, 1.0 };
        double[] result = new GaussianFitter(new BroydenFletcherGoldfarbShanno())
                .fit(initialGuess);
        Assert.assertArrayEquals(new double[] { 2.302585092994, 4.0453339136224, 0.628142096739 }, result, 0.0001);

        // Test with automatic initial guesses
        result = new GaussianFitter(new BroydenFletcherGoldfarbShanno())
                .fit();
        Assert.assertArrayEquals(new double[] { 2.302585092994, 4.0453339136224, 0.628142096739 }, result, 0.0001);

        // Test with invalid initial guesses
        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno()).fit(null);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno())
                    .fit(new double[] { Double.NaN, 4.0, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno())
                    .fit(new double[] { 1.0, Double.NaN, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno())
                    .fit(new double[] { 1.0, 4.0, Double.NaN });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with wrong number of initial guesses
        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno())
                    .fit(new double[] { 1.0, 4.0, 1.0, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with optimizer not implementing the required interface
        try {
            new GaussianFitter(new Object())
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with optimizer not supporting constraints
        UnivariateOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        try {
            new GaussianFitter(optimizer)
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        Options options = new Options();
        options.setMaxIterations(1000);
        optimizer = new BroydenFletcherGoldfarbShanno(options);

        // Test with optimizer not converging
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            public double gradient(double x) {
                return -2 * Math.sin(x) * Math.cos(x);
            }
        };
        try {
            new GaussianFitter(optimizer)
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        // Test with optimizer throwing an exception during optimization
        try {
            new GaussianFitter(new ObjectiveFunction() {
                public double value(double x) {
                    throw new RuntimeException("Test exception");
                }

                public double gradient(double x) {
                    throw new RuntimeException("Test exception");
                }
            })
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }

        // Test with optimizer not supporting bounds
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MAXIMIZE, bounds))
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with optimizer with bounds
        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(0.0, 10.0)))
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with optimizer with incorrect bounds
        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(10.0, 0.0)))
                    .fit(new double[] { 1.0, 4.0, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with optimizer with bounds and initial guesses outside bounds
        try {
            new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(0.0, 5.0)))
                    .fit(new double[] { 10.0, 4.0, 1.0 });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with optimizer with bounds and initial guesses at the bounds
        PointValuePair pvp = new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(0.0, 5.0)))
                .optimize(new InitialGuess(2.5), f, null);
        Assert.assertEquals(2.5, pvp.getX(), 0.0001);
        Assert.assertEquals(0.0, pvp.getFunValue(), 0.0001);

        // Test with optimizer with bounds and initial guesses inside bounds
        pvp = new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(0.0, 5.0)))
                .optimize(new InitialGuess(3.0), f, null);
        Assert.assertEquals(3.0, pvp.getX(), 0.0001);
        Assert.assertEquals(0.1436875552297, pvp.getFunValue(), 0.0001);

        // Test with optimizer with bounds and initial guesses outside bounds but within penalty radius
        pvp = new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(0.0, 5.0), 1.0))
                .optimize(new InitialGuess(10.0), f, null);
        Assert.assertEquals(5.0, pvp.getX(), 0.0001);
        Assert.assertEquals(14.7619024481793, pvp.getFunValue(), 0.0001);

        // Test with optimizer with bounds and initial guesses outside bounds but beyond penalty radius
        pvp = new GaussianFitter(new BroydenFletcherGoldfarbShanno(GoalType.MINIMIZE, new SimpleBounds(0.0, 5.0), 5.0))
                .optimize(new InitialGuess(100.0), f, null);
        Assert.assertEquals(100.0, pvp.getX(), 0.0001);
        Assert.assertEquals(Infinite.POSITIVE_INFINITY, pvp.getFunValue(), 0.0001);
    }
}