package org.apache.commons.math.optimization.test;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethod;
import org.apache.commons.math3.optim.point.PointValuePair;

/**
 * Test cases for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization of a simple polynomial function with multiple starts.
     * @throws Exception if an error occurs during the optimization process
     */
    @org.junit.jupiter.api.Test
    public void testSimplePolynomial() throws Exception {
        final UnivariateRealFunction function = new PolynomialFunction(3, -1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 3, new RandomInitializer(5));
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;
        double optimum = optimizer.optimize(function, goalType, min, max);
        assertArrayEquals(new double[] {-2.0, -1.5, -0.5, optimum}, optimizer.getOptima(), 0.001);
        assertArrayEquals(new double[] {function.value(-2.0), function.value(-1.5),
                function.value(-0.5), function.value(optimum)}, optimizer.getOptimaValues(), 0.001);
    }

    /**
     * Test optimization of a simple polynomial function with multiple starts and
     * a specific starting point.
     * @throws Exception if an error occurs during the optimization process
     */
    @org.junit.jupiter.api.Test
    public void testSimplePolynomialWithStartValue() throws Exception {
        final UnivariateRealFunction function = new PolynomialFunction(3, -1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 3, new AroundMinimumInitializer(2.0));
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;
        double optimum = optimizer.optimize(function, goalType, min, max);
        assertArrayEquals(new double[] {-2.0, -1.5, -0.5, optimum}, optimizer.getOptima(), 0.001);
        assertArrayEquals(new double[] {function.value(-2.0), function.value(-1.5),
                function.value(-0.5), function.value(optimum)}, optimizer.getOptimaValues(), 0.001);
    }

    /**
     * Test optimization of a simple polynomial function with a small number of starts
     * and a strict maximum number of iterations and evaluations.
     * @throws Exception if an error occurs during the optimization process
     */
    @org.junit.jupiter.api.Test
    public void testSimplePolynomialWithLimitedIterationsAndEvaluations() throws Exception {
        final UnivariateRealFunction function = new PolynomialFunction(3, -1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 1, new RandomInitializer(5));
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;
        try {
            optimizer.optimize(function, goalType, min, max, 1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
        assertArrayEquals(new double[] {Double.NaN}, optimizer.getOptima(), 0.0);
        assertArrayEquals(new double[] {Double.NaN}, optimizer.getOptimaValues(), 0.0);
    }

    /**
     * Test optimization of a non-convergent function with multiple starts.
     * @throws Exception if an error occurs during the optimization process
     */
    @org.junit.jupiter.api.Test
    public void testNonConvergentFunction() throws Exception {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 3, new RandomInitializer(5));
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;
        try {
            optimizer.optimize(function, goalType, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
        assertArrayEquals(new double[] {Double.NaN, Double.NaN, Double.NaN}, optimizer.getOptima(), 0.0);
        assertArrayEquals(new double[] {Double.NaN, Double.NaN, Double.NaN}, optimizer.getOptimaValues(), 0.0);
    }

    /**
     * Test optimization of a convergent function with a large number of starts.
     * @throws Exception if an error occurs during the optimization process
     */
    @org.junit.jupiter.api.Test
    public void testConvergentFunctionWithManyStarts() throws Exception {
        final UnivariateRealFunction function = new PolynomialFunction(3, -1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 100, new RandomInitializer(5));
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;
        double optimum = optimizer.optimize(function, goalType, min, max);
        assertArrayEquals(new double[] {-2.0, -1.5, -0.5, optimum}, optimizer.getOptima(), 0.001);
        assertArrayEquals(new double[] {function.value(-2.0), function.value(-1.5),
                function.value(-0.5), function.value(optimum)}, optimizer.getOptimaValues(), 0.001);
    }

    /**
     * Test optimization of a convergent function with a large number of starts and
     * a specific starting point.
     * @throws Exception if an error occurs during the optimization process
     */
    @org.junit.jupiter.api.Test
    public void testConvergentFunctionWithManyStartsAndStartValue() throws Exception {
        final UnivariateRealFunction function = new PolynomialFunction(3, -1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 100, new AroundMinimumInitializer(2.0));
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0, max = 5.0;
        double optimum = optimizer.optimize(function, goalType, min, max);
        assertArrayEquals(new double[] {-2.0, -1.5, -0.5, optimum}, optimizer.getOptima(), 0.001);
        assertArrayEquals(new double[] {function.value(-2.0), function.value(-1.5),
                function.value(-0.5), function.value(optimum)}, optimizer.getOptimaValues(), 0.001);
    }

}