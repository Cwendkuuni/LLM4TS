package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;

/**
 * Test for BrentOptimizer class.
 */
public class BrentOptimizerTest {
    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BrentOptimizer instance = new BrentOptimizer();
        assertEquals(Double.MAX_VALUE, instance.getMaxEvaluations(), 0.0);
        assertEquals(100, instance.getMaximalIterationCount(), 0.0);
        assertEquals(1.0e-14, instance.getRelativeAccuracy(), 0.0);
        assertEquals(1.0e-10, instance.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test optimize method with a function that has a minimum value.
     */
    @Test
    public void testOptimizeMinimumValue() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 5.0;
        double startValue = 3.0;
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(2.0, result.getPoint(), 0.0);
        assertEquals(-3.0, result.getValue(), 0.0);
    }

    /**
     * Test optimize method with a function that has a maximum value.
     */
    @Test
    public void testOptimizeMaximumValue() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 1.0;
        double max = 5.0;
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(5.0, result.getPoint(), 0.0);
        assertEquals(3.0, result.getValue(), 0.0);
    }

    /**
     * Test optimize method with a function that has a minimum value inside the bounds.
     */
    @Test
    public void testOptimizeMinimumValueInsideBounds() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 5.0;
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(-3.0, result.getValue(), 0.0);
    }

    /**
     * Test optimize method with a function that has a minimum value outside the bounds.
     */
    @Test
    public void testOptimizeMinimumValueOutsideBounds() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 5.0;
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }

    /**
     * Test optimize method with a function that has a maximum value outside the bounds.
     */
    @Test
    public void testOptimizeMaximumValueOutsideBounds() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 1.0;
        double max = 5.0;
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }

    /**
     * Test optimize method with a function that has a minimum value at the boundary.
     */
    @Test
    public void testOptimizeMinimumValueAtBoundary() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 2.0;
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test optimize method with a function that has a maximum value at the boundary.
     */
    @Test
    public void testOptimizeMaximumValueAtBoundary() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 1.0;
        double max = 2.0;
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(2.0, result.getPoint(), 0.0);
        assertEquals(2.0, result.getValue(), 0.0);
    }

    /**
     * Test optimize method with a function that is not defined in the interval.
     */
    @Test
    public void testOptimizeFunctionNotDefinedInInterval() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }

    /**
     * Test optimize method with a function that has a non-finite minimum value.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeNonFiniteMinimumValue() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 5.0;
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test optimize method with a function that has a non-finite maximum value.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeNonFiniteMaximumValue() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 3.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 1.0;
        double max = 5.0;
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test optimize method with a function that has no root.
     */
    @Test
    public void testOptimizeFunctionNoRoot() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 2.0}, 3);
        BrentOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 5.0;
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }
}