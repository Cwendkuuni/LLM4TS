package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer;
    private PolynomialFunction function;
    private MersenneTwister random;

    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1, 1, -1}, new double[]{0, 0, 1});
        optimizer = new FibonacciSearchUnivariateOptimizer();
        random = new MersenneTwister(12345);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, random);
    }

    /**
     * Test optimization with a simple polynomial function and minimal bounds.
     */
    @Test
    public void testMinimizeSimplePolynomial() {
        final double min = -5.0;
        final double max = 5.0;
        final double expectedOptimum = 1.0;

        try {
            final PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            assertEquals(expectedOptimum, result.getPoint(), Precision.EPSILON);
            assertEquals(expectedOptimum, result.getValue(), Precision.EPSILON);
        } catch (Exception e) {
            assertThrows(FunctionEvaluationException.class, () -> multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max));
        }

        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(expectedOptimum, optima[0].getValue(), Precision.EPSILON);
    }

    /**
     * Test optimization with a simple polynomial function and maximal bounds.
     */
    @Test
    public void testMaximizeSimplePolynomial() {
        final double min = -5.0;
        final double max = 5.0;
        final double expectedOptimum = -1.0;

        try {
            multiStartOptimizer.setMaxEvaluations(MaxEval.UNLIMITED);
            final PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, min, max);
            assertEquals(expectedOptimum, result.getValue(), Precision.EPSILON);
        } catch (Exception e) {
            assertThrows(FunctionEvaluationException.class, () -> multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, min, max));
        }

        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(expectedOptimum, optima[0].getValue(), Precision.EPSILON);
    }

    /**
     * Test optimization with a polynomial function with no real roots.
     */
    @Test
    public void testNoRealRoots() {
        final double min = -10.0;
        final double max = 10.0;
        final PolynomialFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 0, 1});

        try {
            multiStartOptimizer.setMaxEvaluations(MaxEval.UNLIMITED);
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            assertNull(multiStartOptimizer.getOptima());
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test optimization with a polynomial function with a non-converging starting point.
     */
    @Test
    public void testNonConvergingStartingPoint() {
        final double min = -10.0;
        final double max = 10.0;
        final PolynomialFunction function = new PolynomialFunction(new double[]{1, -1, 1}, new double[]{0, 0, 1});

        try {
            multiStartOptimizer.setMaxEvaluations(MaxEval.UNLIMITED);
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max, 10.0);
            assertNull(multiStartOptimizer.getOptima());
        } catch (Exception e) {
            // Expected
        }
    }
}