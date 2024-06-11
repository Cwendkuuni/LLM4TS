package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}
     * with valid arguments.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealOptimizer optimizer = new FibonacciMinimizer();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(optimizer, 5, new TauPrimeRandomGenerator());
        assertNotNull(optimizerWrapper);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with valid arguments and goal type as MINIMIZE.
     */
    @Test
    public void testOptimizeMinimize() {
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new GoldenSectionFinder(), 5, new TauPrimeRandomGenerator());
        PointValuePair result = optimizerWrapper.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        assertNotNull(result);
        assertEquals(-1.73205080757, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with valid arguments and goal type as MAXIMIZE.
     */
    @Test
    public void testOptimizeMaximize() {
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new GoldenSectionFinder(), 5, new TauPrimeRandomGenerator());
        PointValuePair result = optimizerWrapper.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0);
        assertNotNull(result);
        assertEquals(1.73205080757, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function that does not converge.
     */
    @Test(expected = OptimizationException.class)
    public void testNoConvergence() {
        UnivariateRealFunction testFunction = new NoConvergenceFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new BrentMinimizer(), 5, new TauPrimeRandomGenerator());
        optimizerWrapper.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function that throws an exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction testFunction = new ThrowFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new BrentMinimizer(), 5, new TauPrimeRandomGenerator());
        optimizerWrapper.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function that has multiple minimums.
     */
    @Test
    public void testMultipleMinima() {
        UnivariateRealFunction testFunction = new MultipleMinimaFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new BrentMinimizer(), 5, new TauPrimeRandomGenerator());
        PointValuePair result = optimizerWrapper.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        assertNotNull(result);
        assertEquals(0.25, result.getValue(), 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}
     * after calling {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with valid arguments.
     */
    @Test
    public void testGetOptima() {
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new GoldenSectionFinder(), 5, new TauPrimeRandomGenerator());
        optimizerWrapper.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        double[] optima = optimizerWrapper.getOptima();
        assertNotNull(optima);
        assertEquals(5, optima.length);
        for (double optimum : optima) {
            assertTrue(optimum >= -5.0 && optimum <= 5.0);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()}
     * after calling {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with valid arguments.
     */
    @Test
    public void testGetOptimaValues() {
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer optimizerWrapper = new MultiStartUnivariateRealOptimizer(new GoldenSectionFinder(), 5, new TauPrimeRandomGenerator());
        optimizerWrapper.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        double[] optimaValues = optimizerWrapper.getOptimaValues();
        assertNotNull(optimaValues);
        assertEquals(5, optimaValues.length);
        for (double value : optimaValues) {
            assertTrue(value >= -1.732 && value <= 1.732);
        }
    }
}

class TestFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2);
    }
}

class NoConvergenceFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        throw new RuntimeException("Function evaluation exception.");
    }

    @Override
    public double value(double x, double tolerance) {
        return 0.0;
    }
}

class ThrowFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Function evaluation exception.");
    }

    @Override
    public double value(double x, double tolerance) {
        return 0.0;
    }
}

class MultipleMinimaFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        if (x < 0) {
            return Math.pow(Math.sin(Math.PI * x), 2);
        } else if (x > 0) {
            return Math.pow(Math.cos(Math.PI * x), 2);
        } else {
            return 0.0;
        }
    }
}