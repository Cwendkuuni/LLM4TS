package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomValuesGenerator)}
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());

        // check constructor
        assertEquals(optimizer, multiStartOptimizer.getOptimizer());
        assertEquals(3, multiStartStarts(multiStartOptimizer));
        assertNull(multiStartOptimizer.getOptima());
        assertNull(multiStartOptimizer.getOptimaValues());
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxIterations());
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()}
     */
    @Test
    public void testGetIterationCount() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());
        multiStartOptimizer.optimize(new PolynomialFunction(0.1, 1), GoalType.MINIMIZE, -10.0, 10.0);

        assertEquals(3 * optimizer.getMaxIterations(), multiStartOptimizer.getIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}
     */
    @Test
    public void testGetEvaluations() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());
        multiStartOptimizer.optimize(new PolynomialFunction(0.1, 1), GoalType.MINIMIZE, -10.0, 10.0);

        assertEquals(3 * optimizer.getMaxIterations(), multiStartOptimizer.getEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     */
    @Test
    public void testOptimize() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());
        PolynomialFunction function = new PolynomialFunction(0.1, 1);
        double min = -10.0;
        double max = 10.0;

        PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        // test convergence
        assertTrue(result.isConverged());
        // test result
        assertEquals(result.getPoint(), optima[0], 0.001);
        assertEquals(result.getValue(), optimaValues[0], 0.001);
        // test all optima
        assertEquals(3, optima.length);
        assertArrayEquals(new double[] { result.getPoint(), optima[1], optima[2] }, optima, 0.001);
        assertArrayEquals(new double[] { result.getValue(), optimaValues[1], optimaValues[2] }, optimaValues, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     */
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());
        PolynomialFunction function = new PolynomialFunction(0.1, 1);
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;

        PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        // test convergence
        assertTrue(result.isConverged());
        // test result
        assertEquals(result.getPoint(), optima[0], 0.001);
        assertEquals(result.getValue(), optimaValues[0], 0.001);
        // test all optima
        assertEquals(3, optima.length);
        assertArrayEquals(new double[] { result.getPoint(), optima[1], optima[2] }, optima, 0.001);
        assertArrayEquals(new double[] { result.getValue(), optimaValues[1], optimaValues[2] }, optimaValues, 0.001);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}
     */
    @Test(expected = IllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());
        double[] optima = multiStartOptimizer.getOptima();
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()}
     */
    @Test(expected = IllegalStateException.class)
    public void testGetOptimaValuesBeforeOptimize() {
        UnivariateRealOptimizer optimizer = new FibonacciSearchRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new RandomValuesGenerator());
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#multiStartStarts(MultiStartUnivariateRealOptimizer)}
     */
    private int multiStartOptimizerStarts(MultiStartUnivariateRealOptimizer optimizer) {
        return optimizer.getStarts();
    }

}