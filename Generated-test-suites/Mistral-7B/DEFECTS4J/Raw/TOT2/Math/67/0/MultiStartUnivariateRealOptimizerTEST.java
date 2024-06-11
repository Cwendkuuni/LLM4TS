package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializers.RandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.points.RealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private RandomGenerator rg;
    private UnivariateRealFunction f1;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer optimizerMS;

    @Before
    public void setUp() {
        rg = new org.apache.commons.math3.util.DouglasFisherRandomGenerator();

        f1 = new UnivariateSinFunction();
        optimizer = new BrentOptimizer();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, rg);
    }

    /**
     * Test the basic functionality of MultiStartUnivariateRealOptimizer
     * by optimizing a function and checking the result.
     */
    @Test
    public void testBasicFunctionality() {
        PointValuePair pvp = optimizerMS.optimize(f1, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.0, pvp.getPoint(), 1e-6);
        assertEquals(-Math.sin(0.0), pvp.getValue(), 1e-6);
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * different starting points.
     */
    @Test
    public void testMultipleStartingPoints() {
        double[] expectedOptima = {-0.679436, 0.279436, 0.911622, 0.536561, 0.744714};
        double[] expectedOptimaValues = {-0.951057, -0.734376, -0.712373, -0.663319, -0.658880};

        PointValuePair[] pvps = new PointValuePair[optimizerMS.getStarts()];
        for (int i = 0; i < optimizerMS.getStarts(); ++i) {
            pvps[i] = optimizerMS.optimize(f1, GoalType.MINIMIZE, 0.0, 1.0);
        }

        for (int i = 0; i < optimizerMS.getStarts(); ++i) {
            assertEquals(expectedOptima[i], pvps[i].getPoint(), 1e-6);
            assertEquals(expectedOptimaValues[i], pvps[i].getValue(), 1e-6);
        }
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * a function that has multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        UnivariateRealFunction f2 = new UnivariateDoubleSineFunction();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, rg);

        PointValuePair[] pvps = new PointValuePair[optimizerMS.getStarts()];
        for (int i = 0; i < optimizerMS.getStarts(); ++i) {
            pvps[i] = optimizerMS.optimize(f2, GoalType.MINIMIZE, 0.0, 2 * Math.PI);
        }

        double[] expectedOptima = new double[] {0.25, 1.75, 2.25, 3.75, 4.25, 4.75, 5.25, 5.75, 6.25};
        double[] expectedOptimaValues = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        for (int i = 0; i < optimizerMS.getStarts(); ++i) {
            assertEquals(expectedOptima[i], pvps[i].getPoint(), 1e-6);
            assertEquals(expectedOptimaValues[i], pvps[i].getValue(), 1e-6);
        }
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * a function that has no solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoSolutionFunction() {
        UnivariateRealFunction f3 = new UnivariateFunctionWithNoSolution();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, rg);

        optimizerMS.optimize(f3, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * a function that throws an exception during optimization.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testExceptionFunction() {
        UnivariateRealFunction f4 = new UnivariateFunctionWithException();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, rg);

        optimizerMS.optimize(f4, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * a function that has a large search space.
     */
    @Test
    public void testLargeSearchSpaceFunction() {
        UnivariateRealFunction f5 = new UnivariateLargeSearchSpaceFunction();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, rg);

        PointValuePair pvp = optimizerMS.optimize(f5, GoalType.MINIMIZE, 0.0, 100.0);
        assertEquals(14.123456, pvp.getPoint(), 1e-6);
        assertEquals(-16.0, pvp.getValue(), 1e-6);
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * a function that has a large iteration limit.
     */
    @Test
    public void testLargeIterationLimitFunction() {
        UnivariateRealFunction f6 = new UnivariateLargeIterationLimitFunction();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, rg);

        PointValuePair pvp = optimizerMS.optimize(f6, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(1.2345678, pvp.getPoint(), 1e-6);
        assertEquals(-1.5678912, pvp.getValue(), 1e-6);
    }

    /**
     * Test the functionality of MultiStartUnivariateRealOptimizer with
     * a function that has a large maximum evaluation limit.
     */
    @Test
    public void testLargeEvaluationLimitFunction() {
        UnivariateRealFunction f7 = new UnivariateLargeEvaluationLimitFunction();
        optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 10, rg);

        PointValuePair pvp = optimizerMS.optimize(f7, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(3.456789, pvp.getPoint(), 1e-6);
        assertEquals(-4.567891, pvp.getValue(), 1e-6);
    }

}

class UnivariateSinFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return Math.sin(x);
    }

}

class UnivariateDoubleSineFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return Math.sin(x * 2.0);
    }

}

class UnivariateFunctionWithNoSolution implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        throw new FunctionEvaluationException("This function has no solution.");
    }

}

class UnivariateFunctionWithException implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        if (x > 0.5) {
            throw new FunctionEvaluationException("Exception thrown for x > 0.5.");
        }
        return Math.sin(x);
    }

}

class UnivariateLargeSearchSpaceFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return 10.0 * Math.sin(x / 10.0) + x;
    }

}

class UnivariateLargeIterationLimitFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        double result = 0.0;
        for (int i = 0; i < 1000; ++i) {
            result += Math.sin(x / 1000.0);
        }
        return result;
    }

}

class UnivariateLargeEvaluationLimitFunction implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        double result = 0.0;
        for (int i = 0; i < 100000; ++i) {
            result += Math.sin(x / 1000.0);
        }
        return result;
    }

}