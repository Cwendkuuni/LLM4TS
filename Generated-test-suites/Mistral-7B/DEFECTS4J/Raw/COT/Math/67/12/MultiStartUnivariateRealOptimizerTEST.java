package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private double[] optima;
    private double[] optimaValues;

    @Before
    public void setUp() {
        optimizer = new NewtonRaphsonOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MersenneTwister());
    }

    @After
    public void tearDown() {
        optima = null;
        optimaValues = null;
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getFunctionValue()}.
     */
    @Test
    public void testGetFunctionValue() {
        assertEquals("getFunctionValue() should return the value of the underlying optimizer",
                multiStartOptimizer.getFunctionValue(), optimizer.getFunctionValue(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getResult()}.
     */
    @Test
    public void testGetResult() {
        assertEquals("getResult() should return the result of the underlying optimizer",
                multiStartOptimizer.getResult(), optimizer.getResult(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getAbsoluteAccuracy()}.
     */
    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals("getAbsoluteAccuracy() should return the absolute accuracy of the underlying optimizer",
                multiStartOptimizer.getAbsoluteAccuracy(), optimizer.getAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()}.
     */
    @Test
    public void testGetIterationCount() {
        assertEquals("getIterationCount() should return the iteration count of the underlying optimizer",
                multiStartOptimizer.getIterationCount(), optimizer.getIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals("getMaxEvaluations() should return the maximum evaluation count of the underlying optimizer",
                multiStartOptimizer.getMaxEvaluations(), optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        assertEquals("getEvaluations() should return the evaluation count of the underlying optimizer",
                multiStartOptimizer.getEvaluations(), optimizer.getEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getRelativeAccuracy()}.
     */
    @Test
    public void testGetRelativeAccuracy() {
        assertEquals("getRelativeAccuracy() should return the relative accuracy of the underlying optimizer",
                multiStartOptimizer.getRelativeAccuracy(), optimizer.getRelativeAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetAbsoluteAccuracy()}.
     */
    @Test
    public void testResetAbsoluteAccuracy() {
        optimizer.resetAbsoluteAccuracy();
        multiStartOptimizer.resetAbsoluteAccuracy();
        assertEquals("resetAbsoluteAccuracy() should reset the absolute accuracy of the underlying optimizer",
                multiStartOptimizer.getAbsoluteAccuracy(), optimizer.getAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetMaxIterations()}.
     */
    @Test
    public void testResetMaxIterations() {
        optimizer.setMaxIterations(5);
        multiStartOptimizer.setMaxIterations(5);
        optimizer.setMaxIterations(10);
        multiStartOptimizer.setMaxIterations(10);
        assertEquals("resetMaxIterations() should set the maximum iteration count of the underlying optimizer",
                multiStartOptimizer.getMaxIterations(), optimizer.getMaxIterations(), 1);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetMaxEvaluations()}.
     */
    @Test
    public void testResetMaxEvaluations() {
        optimizer.setMaxEvaluations(5);
        multiStartOptimizer.setMaxEvaluations(5);
        optimizer.setMaxEvaluations(10);
        multiStartOptimizer.setMaxEvaluations(10);
        assertEquals("resetMaxEvaluations() should set the maximum evaluation count of the underlying optimizer",
                multiStartOptimizer.getMaxEvaluations(), optimizer.getMaxEvaluations(), 1);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetRelativeAccuracy()}.
     */
    @Test
    public void testResetRelativeAccuracy() {
        optimizer.resetRelativeAccuracy();
        multiStartOptimizer.resetRelativeAccuracy();
        assertEquals("resetRelativeAccuracy() should reset the relative accuracy of the underlying optimizer",
                multiStartOptimizer.getRelativeAccuracy(), optimizer.getRelativeAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setAbsoluteAccuracy(double)}.
     */
    @Test
    public void testSetAbsoluteAccuracy() {
        double accuracy = 1e-6;
        optimizer.setAbsoluteAccuracy(accuracy);
        multiStartOptimizer.setAbsoluteAccuracy(accuracy);
        assertEquals("setAbsoluteAccuracy() should set the absolute accuracy of the underlying optimizer",
                multiStartOptimizer.getAbsoluteAccuracy(), optimizer.getAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxIterations(int)}.
     */
    @Test
    public void testSetMaxIterations() {
        int maxIterations = 10;
        optimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxIterations(maxIterations);
        assertEquals("setMaxIterations() should set the maximum iteration count of the underlying optimizer",
                multiStartOptimizer.getMaxIterations(), optimizer.getMaxIterations(), 1);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 10;
        optimizer.setMaxEvaluations(maxEvaluations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        assertEquals("setMaxEvaluations() should set the maximum evaluation count of the underlying optimizer",
                multiStartOptimizer.getMaxEvaluations(), optimizer.getMaxEvaluations(), 1);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setRelativeAccuracy(double)}.
     */
    @Test
    public void testSetRelativeAccuracy() {
        double accuracy = 1e-6;
        optimizer.setRelativeAccuracy(accuracy);
        multiStartOptimizer.setRelativeAccuracy(accuracy);
        assertEquals("setRelativeAccuracy() should set the relative accuracy of the underlying optimizer",
                multiStartOptimizer.getRelativeAccuracy(), optimizer.getRelativeAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, new double[]{0.0, 1.0, 1.0});
        double min = -2.0;
        double max = 2.0;
        double goal = -1.0;
        double optimum = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, min, max);
        assertFalse("optimize() should not return NaN for a convergent run",
                Double.isNaN(optimum));
        double expectedOptimum = Precision.round(Math.pow(2.0, 0.5), 5);
        assertEquals("optimize() should return the expected optimum",
                optimum, expectedOptimum, 1e-6);
        optima = multiStartOptimizer.getOptima();
        optimaValues = multiStartOptimizer.getOptimaValues();
        assertEquals("optimize() should find the same optimum for each start",
                optima[0], expectedOptimum, 1e-6);
        assertEquals("optimize() should find the same function value for each start",
                optimaValues[0], f.value(expectedOptimum), 1e-6);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function that has multiple local extrema.
     */
    @Test
    public void testOptimizeFunctionMultipleLocalExtrema() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, -1.0, 1.0},
                new double[]{0.0, 1.0, 2.0, 3.0, 4.0});
        double min = -5.0;
        double max = 5.0;
        double goal = GoalType.MAXIMIZE.equals(GoalType.MAXIMIZE) ? Double.MIN_VALUE : Double.MAX_VALUE;
        double optimum = multiStartOptimizer.optimize(f, GoalType.MAXIMIZE, min, max);
        assertFalse("optimize() should return NaN for a non-convergent run",
                Double.isNaN(optimum));
        double expectedOptimum = Precision.round(2.0, 5);
        assertEquals("optimize() should return the expected optimum",
                optimum, expectedOptimum, 1e-6);
        optima = multiStartOptimizer.getOptima();
        optimaValues = multiStartOptimizer.getOptimaValues();
        for (int i = 0; i < optima.length; ++i) {
            assertEquals("optimize() should find the same optimum for each start",
                    optima[i], expectedOptimum, 1e-6);
            assertEquals("optimize() should find the same function value for each start",
                    optimaValues[i], f.value(expectedOptimum), 1e-6);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function that has a global extrema outside the bounds.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionOutOfBounds() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, new double[]{0.0, 1.0, 1.0});
        double min = -2.0;
        double max = 1.0;
        double goal = -1.0;
        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function that has a local extrema outside the bounds.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionLocalExtremaOutOfBounds() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, -1.0, 1.0},
                new double[]{0.0, 1.0, 2.0, 3.0, 4.0});
        double min = -5.0;
        double max = 2.0;
        double goal = GoalType.MAXIMIZE.equals(GoalType.MAXIMIZE) ? Double.MIN_VALUE : Double.MAX_VALUE;
        multiStartOptimizer.optimize(f, GoalType.MAXIMIZE, min, max);
    }

}