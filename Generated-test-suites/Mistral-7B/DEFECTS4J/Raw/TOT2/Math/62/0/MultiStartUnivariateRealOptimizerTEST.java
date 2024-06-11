package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {

    private static final UnivariateFunction FUNCTION_XSQUARE = new PolynomialFunction(new double[]{-1d, 2d});

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizerMS;
    private int starts;
    private int maxIterations;

    @Before
    public void setUp() {
        optimizer = new MaxEval(new SimpleBounds(-1.0, 1.0), 100);
        optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        starts = 3;
        maxIterations = 100;
    }

    @Parameterized.Parameters(name = "{index}: [{0}, {1}]")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new SimpleBounds(-10.0, 10.0), GoalType.MINIMIZE, Double.NEGATIVE_INFINITY},
                {new SimpleBounds(0.0, 10.0), GoalType.MAXIMIZE, Double.POSITIVE_INFINITY},
                {new SimpleBounds(-1.0, 1.0), GoalType.MINIMIZE, -2.0},
                {new SimpleBounds(-1.0, 1.0), GoalType.MAXIMIZE, 2.0}
        });
    }

    @Parameterized.Parameter
    public SimpleBounds bounds;
    @Parameterized.Parameter
    public GoalType goal;
    @Parameterized.Parameter
    public double expectedValue;

    @Test
    public void testBasicFunctionality() throws MaxEvalException {
        optimizerMS.setMaxEvaluations(maxIterations);
        PointValuePair result = optimizerMS.optimize(FUNCTION_XSQUARE, goal, bounds);
        double value = result.getValue();
        if (goal == GoalType.MINIMIZE) {
            assertEquals(expectedValue, value, 1e-13);
        } else {
            assertEquals(expectedValue, value, 1e-13);
            fail("Expected the function to not converge.");
        }
    }

    @Test
    public void testEdgeCases() throws MaxEvalException {
        optimizerMS.setMaxEvaluations(maxIterations);

        // Test bounds with negative limits
        SimpleBounds boundsNegative = new SimpleBounds(-10.0, -5.0);
        PointValuePair resultNegative = optimizerMS.optimize(FUNCTION_XSQUARE, goal, boundsNegative);
        assertNull(resultNegative);

        // Test bounds with zero limits
        SimpleBounds boundsZero = new SimpleBounds(0.0, 0.0);
        PointValuePair resultZero = optimizerMS.optimize(FUNCTION_XSQUARE, goal, boundsZero);
        assertNull(resultZero);

        // Test bounds with infinite limits
        SimpleBounds boundsInfinity = new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        PointValuePair resultInfinity = optimizerMS.optimize(FUNCTION_XSQUARE, goal, boundsInfinity);
        assertNull(resultInfinity);

        // Test bounds containing the minimum or maximum
        SimpleBounds boundsMin = new SimpleBounds(-1.0, 1.0);
        PointValuePair resultMin = optimizerMS.optimize(FUNCTION_XSQUARE, goal, boundsMin);
        if (goal == GoalType.MINIMIZE) {
            assertEquals(-1.0, resultMin.getPoint().getValue(), 1e-13);
        } else {
            assertEquals(1.0, resultMin.getPoint().getValue(), 1e-13);
            fail("Expected the function to not converge.");
        }

        SimpleBounds boundsMax = new SimpleBounds(1.0, 3.0);
        PointValuePair resultMax = optimizerMS.optimize(FUNCTION_XSQUARE, goal, boundsMax);
        if (goal == GoalType.MINIMIZE) {
            fail("Expected the function to not converge.");
        } else {
            assertEquals(9.0, resultMax.getPoint().getValue(), 1e-13);
        }
    }

    @Test
    public void testExceptionHandling() throws MaxEvalException, ConvergenceException {
        optimizerMS.setMaxEvaluations(maxIterations);

        // Test function evaluation exception
        optimizerMS.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation exception.");
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException("Not supported.");
            }
        }, goal, new SimpleBounds(-1.0, 1.0));
        assertNull(optimizerMS.getOptima());

        // Test convergence exception
        optimizerMS.setMaxIterations(1);
        optimizerMS.optimize(FUNCTION_XSQUARE, goal, new SimpleBounds(-1.0, 1.0));
        UnivariateRealPointValuePair[] optima = optimizerMS.getOptima();
        assertNull(optima[0]);
    }
}