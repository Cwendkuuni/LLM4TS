package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.linear.GoalTypeRelativeToMin;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciUnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimulatedAnnealing;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair;

/**
 * Test class for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, Random)}.
     * Test if the constructor call is successful.
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testConstructor() {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 5, new Random());
        assertNotNull(optimizerMS);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setConvergenceChecker(ConvergenceChecker)}.
     * Test if the setter method for the convergence checker works.
     */
    @org.junit.Test
    public void testSetConvergenceChecker() {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 5, new Random());
        SimulatedAnnealing convergenceChecker = new SimulatedAnnealing(1.0, 100);
        optimizerMS.setConvergenceChecker(convergenceChecker);
        assertTrue(optimizerMS.getConvergenceChecker() instanceof SimulatedAnnealing);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getConvergenceChecker()}.
     * Test if the getter method for the convergence checker works.
     */
    @org.junit.Test
    public void testGetConvergenceChecker() {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 5, new Random());
        SimulatedAnnealing convergenceChecker = new SimulatedAnnealing(1.0, 100);
        optimizerMS.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizerMS.getConvergenceChecker());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     * Test if the getter method for the maximum number of evaluations works.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 5, new Random());
        optimizerMS.setMaxEvaluations(10);
        assertEquals(10, optimizerMS.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     * Test if the getter method for the number of evaluations works.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 5, new Random());
        optimizerMS.optimize(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE, 0.0, 1.0);
        assertEquals(5 * optimizer.getMaxEvaluations(), optimizerMS.getEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     * Test if the setter method for the maximum number of evaluations works.
     */
    @org.junit.Test
    public void testSetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 5, new Random());
        optimizerMS.setMaxEvaluations(10);
        optimizer.setMaxEvaluations(20);
        optimizerMS.setMaxEvaluations(15);
        assertEquals(15, optimizerMS.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     * Test if the getOptima method returns the correct set of optima.
     */
    @org.junit.Test
    public void testGetOptima() throws Exception {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 3, new Random());
        PointValuePair optima[] = optimizerMS.optimize(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE, 0.0, 1.0).getOptima();
        MultiStartUnivariateRealOptimizerTest.UnivariateRealPointValuePair expectedOptima[] = new MultiStartUnivariateRealOptimizerTest.UnivariateRealPointValuePair[]{
            new MultiStartUnivariateRealOptimizerTest.UnivariateRealPointValuePair(0.0, -3.0),
            new MultiStartUnivariateRealOptimizerTest.UnivariateRealPointValuePair(1.0, -2.5),
            new MultiStartUnivariateRealOptimizerTest.UnivariateRealPointValuePair(1.0, -2.25)
        };
        assertArrayEquals(expectedOptima, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(FUNC, GoalType, double, double)}.
     * Test if the optimize method finds the optimum.
     */
    @org.junit.Test
    public void testOptimize() throws Exception {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 3, new Random());
        PointValuePair optima = optimizerMS.optimize(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE, 0.0, 1.0);
        assertEquals(-3.0, optima.getPoint(), 1e-7);
        assertEquals(-3.0, optima.getValue(), 1e-7);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(FUNC, GoalType, double, double, double)}.
     * Test if the optimize method finds the optimum with a given starting point.
     */
    @org.junit.Test
    public void testOptimizeWithStartingPoint() throws Exception {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 3, new Random());
        PointValuePair optima = optimizerMS.optimize(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE, 0.5, 1.5);
        assertEquals(-3.0, optima.getPoint(), 1e-7);
        assertEquals(-3.0, optima.getValue(), 1e-7);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(FUNC, GoalType, double, double, double, double)}.
     * Test if the optimize method throws an exception when the goal type is not supported.
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testOptimizeWithUnsupportedGoalType() throws Exception {
        UnivariateRealOptimizer optimizer = new FibonacciUnivariateOptimizer(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeValue.MINIMIZE);
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>((BaseUnivariateRealOptimizer<ObjectiveFunction>) optimizer, 3, new Random());
        optimizerMS.optimize(new PolynomialFunctions.Quadratic(1.0, 2.0, -3.0), GoalTypeRelativeToMin.RELATIVE_TO_MIN, 0.0, 1.0);
    }
}

class MultiStartUnivariateRealOptimizerTest {

    /**
     * Represents a UnivariateRealPointValuePair for testing.
     */
    public static class UnivariateRealPointValuePair implements PointValuePair {

        private double point;
        private double value;

        public UnivariateRealPointValuePair(double point, double value) {
            this.point = point;
            this.value = value;
        }

        @Override
        public double getPoint() {
            return point;
        }

        @Override
        public double getValue() {
            return value;
        }
    }
}