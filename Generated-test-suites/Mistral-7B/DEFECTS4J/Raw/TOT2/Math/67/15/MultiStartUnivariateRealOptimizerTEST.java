package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateRealValuedFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiOptimizer;
    private UnivariateRealFunction function;
    private double min, max, optimum, result;

    @Before
    public void setUp() {
        optimizer = new NelderMeadSimplex();
        function = new MyFunction();
        multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        min = -10.0;
        max = 10.0;
        multiOptimizer.setMaxEvaluations(MaxEval.UNLIMITED);
        multiOptimizer.setMaxIterations(1000);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() {
        optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        assertEquals(min + 0.5, optimum, 0.01);

        result = multiOptimizer.getResult();
        assertEquals(optimum, result, 0.01);

        PointValuePair pvp = multiOptimizer.getOptima();
        assertEquals(5, pvp.getNumberOfPoints());
        for (int i = 0; i < pvp.getNumberOfPoints(); i++) {
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), pvp.getPoint(i), 0.01);
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), pvp.getValue(i), 0.01);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithStartValue() {
        optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max, 5.0);
        assertEquals(min + 0.5, optimum, 0.01);

        result = multiOptimizer.getResult();
        assertEquals(optimum, result, 0.01);

        PointValuePair pvp = multiOptimizer.getOptima();
        assertEquals(5, pvp.getNumberOfPoints());
        for (int i = 0; i < pvp.getNumberOfPoints(); i++) {
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), pvp.getPoint(i), 0.01);
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), pvp.getValue(i), 0.01);
        }
    }

    /**
     * Test method for error handling in {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        multiOptimizer.setMaxEvaluations(1);
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test method for error handling in {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        function = new ThrowingFunction();
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test method for error handling in {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = MathRuntimeException.class)
    public void testMathRuntimeException() {
        multiOptimizer.setMaxIterations(0);
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testGetOptima() {
        optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        PointValuePair pvp = multiOptimizer.getOptima();
        assertEquals(5, pvp.getNumberOfPoints());
        for (int i = 0; i < pvp.getNumberOfPoints(); i++) {
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), pvp.getPoint(i), 0.01);
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), pvp.getValue(i), 0.01);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()}.
     */
    @Test
    public void testGetOptimaValues() {
        optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double[] optimaValues = multiOptimizer.getOptimaValues();
        assertEquals(5, optimaValues.length);
        for (int i = 0; i < optimaValues.length; i++) {
            assertEquals(MyFunction.MINIMUM + 0.5 * (i + 1), optimaValues[i], 0.01);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()}.
     */
    @Test
    public void testGetIterationCount() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        int iterations = multiOptimizer.getIterationCount();
        assertTrue(iterations > 0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        int evaluations = multiOptimizer.getMaxEvaluations();
        assertTrue(evaluations > 0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxIterations()}.
     */
    @Test
    public void testGetMaxIterations() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        int maxIterations = multiOptimizer.getMaxIterations();
        assertTrue(maxIterations > 0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getResult()}.
     */
    @Test
    public void testGetResult() {
        optimum = multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        result = multiOptimizer.getResult();
        assertEquals(optimum, result, 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetAbsoluteAccuracy()}.
     */
    @Test
    public void testResetAbsoluteAccuracy() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.resetAbsoluteAccuracy();
        assertEquals(0.0, multiOptimizer.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetMaxIterations()}.
     */
    @Test
    public void testResetMaxIterations() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.resetMaxIterations();
        assertEquals(Integer.MAX_VALUE, multiOptimizer.getMaxIterations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#resetRelativeAccuracy()}.
     */
    @Test
    public void testResetRelativeAccuracy() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.resetRelativeAccuracy();
        assertEquals(0.0, multiOptimizer.getRelativeAccuracy(), 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setAbsoluteAccuracy(double)}.
     */
    @Test
    public void testSetAbsoluteAccuracy() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.setAbsoluteAccuracy(0.1);
        assertEquals(0.1, multiOptimizer.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxIterations(int)}.
     */
    @Test
    public void testSetMaxIterations() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.setMaxIterations(50);
        assertEquals(50, multiOptimizer.getMaxIterations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.setMaxEvaluations(50);
        assertEquals(50, multiOptimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setRelativeAccuracy(double)}.
     */
    @Test
    public void testSetRelativeAccuracy() {
        multiOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        multiOptimizer.setRelativeAccuracy(0.1);
        assertEquals(0.1, multiOptimizer.getRelativeAccuracy(), 0.0);
    }

    /**
     * A test function for testing the optimizer.
     */
    public static class MyFunction implements UnivariateRealValuedFunction {
        /** The minimum value of the function. */
        public static final double MINIMUM = -2.5;

        /**
         * Evaluate this function at given point.
         * @param x point
         * @return function value at x
         */
        public double value(double x) {
            return Math.pow(Math.sin(x), 2.0) + Math.pow(Math.cos(x), 2.0);
        }
    }

    /**
     * A test function for testing the optimizer.
     */
    public static class ThrowingFunction implements UnivariateFunctionAdapter {
        /**
         * Evaluate this function at given point.
         * @param x point
         * @return function value at x
         * @throws FunctionEvaluationException for testing
         */
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Test exception");
        }
    }

}