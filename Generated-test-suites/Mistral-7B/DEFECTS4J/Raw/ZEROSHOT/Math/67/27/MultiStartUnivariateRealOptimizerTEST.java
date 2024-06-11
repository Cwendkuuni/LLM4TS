package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        Assert.assertEquals(optimizer.getClass(), optimizerMultiStart.getOptimizer().getClass());
        Assert.assertEquals(5, optimizerMultiStart.getStarts());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getFunctionValue()} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerGetFunctionValue() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        double result = 0.0;

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            result = optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.assertNotEquals("Start " + i + " should not have the same result as previous starts", result,
                                  optimizerMultiStart.getResult(), 1e-6);
        }
        Assert.assertEquals("Last result should be the same as the one returned by the optimizer",
                            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0), optimizerMultiStart.getResult(), 1e-6);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerGetIterationCount() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        int totalIterations = 0;
        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            totalIterations += optimizerMultiStart.getIterationCount();
        }
        Assert.assertEquals("Total iterations should be greater or equal to the product of the iterations of each start",
                            optimizer.getMaximalIterationCount() * optimizerMultiStart.getStarts(),
                            totalIterations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerGetEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        int totalEvaluations = 0;
        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            totalEvaluations += optimizerMultiStart.getEvaluations();
        }
        Assert.assertEquals("Total evaluations should be greater or equal to the product of the evaluations of each start",
                            optimizer.getMaxEvaluations() * optimizerMultiStart.getStarts(),
                            totalEvaluations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerGetOptima() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        double[] optima = new double[optimizerMultiStart.getStarts()];
        double[] optimaValues = new double[optimizerMultiStart.getStarts()];

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, new SimpleBounds(0.0, 1.0));
            optima[i] = optimizerMultiStart.getOptima()[i];
            optimaValues[i] = optimizerMultiStart.getOptimaValues()[i];
        }

        double[] expectedOptima = new double[optimizerMultiStart.getStarts()];
        double[] expectedOptimaValues = new double[optimizerMultiStart.getStarts()];

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, new SimpleBounds(0.0, 1.0));
            expectedOptima[i] = optimizer.getResult();
            expectedOptimaValues[i] = optimizer.getFunctionValue();
        }

        Assert.assertArrayEquals("Optima should be the same as the ones obtained by the optimizer",
                                 expectedOptima, optima, 1e-6);
        Assert.assertArrayEquals("Optima values should be the same as the ones obtained by the optimizer",
                                 expectedOptimaValues, optimaValues, 1e-6);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerOptimize() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        double startPoint = 0.0;
        double min = 0.0;
        double max = 1.0;

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, startPoint, max);
            startPoint += 0.1;
        }

        optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, max);

        Assert.assertEquals("Last result should be the same as the one obtained by the optimizer",
                            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, max), optimizerMultiStart.getResult(), 1e-6);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, UnivariateRealBounds)} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerOptimizeWithBounds() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
        }

        optimizerMultiStart.optimize(function, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());

        Assert.assertEquals("Last result should be the same as the one obtained by the optimizer",
                            optimizer.optimize(function, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound()),
                            optimizerMultiStart.getResult(), 1e-6);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double)} .
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartUnivariateRealOptimizerOptimizeWithException() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer() {
            @Override
            public double optimize(UnivariateRealFunction function, GoalType goalType, double startValue, double endValue)
                    throws ConvergenceException, FunctionEvaluationException {
                if (Math.abs(startValue) < 1e-12) {
                    throw new FunctionEvaluationException("Function evaluation failed", null);
                }
                return super.optimize(function, goalType, startValue, endValue);
            }
        };
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0);
        }

        optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} with exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartUnivariateRealOptimizerOptimizeWithExceptionAndBounds() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer() {
            @Override
            public double optimize(UnivariateRealFunction function, GoalType goalType, double startValue, double endValue)
                    throws ConvergenceException, FunctionEvaluationException {
                if (Math.abs(startValue) < 1e-12) {
                    throw new FunctionEvaluationException("Function evaluation failed", null);
                }
                return super.optimize(function, goalType, startValue, endValue);
            }
        };
        MultiStartUnivariateRealOptimizer optimizerMultiStart = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        for (int i = 0; i < optimizerMultiStart.getStarts(); i++) {
            optimizerMultiStart.optimize(function, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
        }

        optimizerMultiStart.optimize(function, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
    }
}