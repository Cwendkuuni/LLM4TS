package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Test class for the MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with a simple polynomial function.
     *
     * @throws Exception if an error occurs during the tests.
     */
    @ org.junit.Test
    public void testOptimizePolynomial() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        // single start test
        UnivariateRealOptimizer singleStartOptimizer = new FibonacciSearchUnivariateRealOptimizer();
        double resultSingleStart = singleStartOptimizer.optimize(f, goalType, min, max);
        double expectedSingleStart = -1.618055259296863;
        assertEquals(expectedSingleStart, resultSingleStart, 1e-8);

        // multi start test with 5 starts
        UnivariateRealOptimizer multiStartOptimizer = new FibonacciSearchUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStart = new MultiStartUnivariateRealOptimizer(multiStartOptimizer, 5, new MersenneTwister());
        double resultMultiStart = multiStart.optimize(f, goalType, min, max);
        double[] optima = multiStart.getOptima();
        double[] optimaValues = multiStart.getOptimaValues();
        double expectedOptima = -1.618055259296863;
        double expectedOptimaValue = 1.61803398875;
        assertEquals(expectedOptima, optima[0], 1e-8);
        assertEquals(expectedOptimaValue, optimaValues[0], 1e-8);
        for (int i = 1; i < optima.length; i++) {
            assertEquals(Double.NaN, optima[i], 1e-8);
            assertEquals(Double.NaN, optimaValues[i], 1e-8);
        }
        assertEquals(expectedSingleStart, resultMultiStart, 1e-8);

        // multi start test with 1 start
        multiStart = new MultiStartUnivariateRealOptimizer(multiStartOptimizer, 1, new MersenneTwister());
        resultMultiStart = multiStart.optimize(f, goalType, min, max);
        assertEquals(expectedSingleStart, resultMultiStart, 1e-8);
    }

    /**
     * Test optimization with a complex function.
     *
     * @throws Exception if an error occurs during the tests.
     */
    @ org.junit.Test
    public void testOptimizeComplexFunction() throws Exception {
        UnivariateRealFunction f = new RastriginFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        // single start test
        UnivariateRealOptimizer singleStartOptimizer = new NelderMeadSimplexSearch();
        double resultSingleStart = singleStartOptimizer.optimize(f, goalType, min, max);
        double expectedSingleStart = -6.0;
        assertEquals(expectedSingleStart, resultSingleStart, 1e-8);

        // multi start test with 5 starts
        UnivariateRealOptimizer multiStartOptimizer = new NelderMeadSimplexSearch();
        MultiStartUnivariateRealOptimizer multiStart = new MultiStartUnivariateRealOptimizer(multiStartOptimizer, 5, new MersenneTwister());
        double resultMultiStart = multiStart.optimize(f, goalType, min, max);
        double[] optima = multiStart.getOptima();
        double[] optimaValues = multiStart.getOptimaValues();
        double expectedOptima = -6.0;
        double expectedOptimaValue = 6.0;
        assertEquals(expectedOptima, optima[0], 1e-8);
        assertEquals(expectedOptimaValue, optimaValues[0], 1e-8);
        for (int i = 1; i < optima.length; i++) {
            assertEquals(Double.NaN, optima[i], 1e-8);
            assertEquals(Double.NaN, optimaValues[i], 1e-8);
        }
        assertEquals(expectedSingleStart, resultMultiStart, 1e-8);

        // multi start test with 1 start
        multiStart = new MultiStartUnivariateRealOptimizer(multiStartOptimizer, 1, new MersenneTwister());
        resultMultiStart = multiStart.optimize(f, goalType, min, max);
        assertEquals(expectedSingleStart, resultMultiStart, 1e-8);
    }
}

/**
 * Custom optimization function for testing purposes.
 */
class RastriginFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        double sum = 0;
        for (int i = 0; i < 10; ++i) {
            double xi = x + (i + 1.0) * Math.PI / 2.0;
            sum += (xi * xi - 10.0 * Math.cos(2.0 * Math.PI * xi) + 10.0);
        }
        return sum;
    }
}