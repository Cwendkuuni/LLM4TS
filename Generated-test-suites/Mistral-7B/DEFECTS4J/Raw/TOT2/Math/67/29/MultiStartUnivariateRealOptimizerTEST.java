package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FallingStepsGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethod;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction testFunction;
    private MultiStartUnivariateRealOptimizer optimizer;
    private Random rnd;

    @Before
    public void setUp() {
        testFunction = new FallingStepsGradient();
        optimizer = new MultiStartUnivariateRealOptimizer(new SimpleBoundingMethod(), 3, new MersenneTwister());
        rnd = new Random();
    }

    /**
     * Test that {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * returns the expected minimum value for the given test function.
     * <p>
     * In this test case, we simply check that the optimizer returns a value
     * close enough to the expected minimum value for the given test function.
     * </p>
     */
    @Test
    public void testOptimizeBasic() {
        final double min = 0.5;
        final double max = 2.0;
        final double expectedMin = 1.0;
        final double tolerance = 0.001;

        final double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);

        Assert.assertNotEquals(Double.NaN, result, 0.0);
        Assert.assertFalse(Double.isInfinite(result));
        Assert.assertEquals(expectedMin, result, tolerance);
    }

    /**
     * Test that {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * can find multiple local minima.
     * <p>
     * In this test case, we check that the optimizer can find multiple local
     * minima for the given test function.
     * </p>
     */
    @Test
    public void testOptimizeMultipleMinima() {
        final double min = -3.0;
        final double max = 3.0;
        final int starts = 5;

        final double[] expectedOptima = new double[]{
                -1.2, -1.0, -0.8, -0.5, 0.5, 1.2
        };

        final double[] actualOptima = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, starts);

        Assert.assertArrayEquals(expectedOptima, actualOptima, 0.001);
    }

    /**
     * Test that {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * throws {@link ConvergenceException} when the optimizer does not converge to a solution.
     * <p>
     * In this test case, we test that the optimizer throws a {@link ConvergenceException}
     * when the optimizer does not converge to a solution for the given test function.
     * </p>
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() {
        final double min = 0.0;
        final double max = 1.0;
        final int starts = 5;

        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * Math.exp(-Math.pow(x, 2.0));
            }
        }, GoalType.MINIMIZE, min, max, starts);
    }

    /**
     * Test that {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * throws {@link FunctionEvaluationException} when the optimizer fails to evaluate the function.
     * <p>
     * In this test case, we test that the optimizer throws a {@link FunctionEvaluationException}
     * when the optimizer fails to evaluate the function for the given test function.
     * </p>
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() {
        final OptimizationException exception = Assert.thrown(OptimizationException.class, () -> {
            optimizer.optimize(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new FunctionEvaluationException("Test exception");
                }
            }, GoalType.MINIMIZE, 0.0, 1.0, 1);
        });
        Assert.assertEquals("Test exception", exception.getMessage());
    }

    /**
     * Test that {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * throws {@link MathRuntimeException} when an unexpected exception occurs.
     * <p>
     * In this test case, we test that the optimizer throws a {@link MathRuntimeException}
     * when an unexpected exception occurs during optimization.
     * </p>
     */
    @Test(expected = MathRuntimeException.class)
    public void testOptimizeUnexpectedException() {
        final OptimizationException exception = Assert.thrown(OptimizationException.class, () -> {
            optimizer.optimize(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new RuntimeException("Test exception");
                }
            }, GoalType.MINIMIZE, 0.0, 1.0, 1);
        });
        Assert.assertEquals("Test exception", exception.getMessage());
    }
}