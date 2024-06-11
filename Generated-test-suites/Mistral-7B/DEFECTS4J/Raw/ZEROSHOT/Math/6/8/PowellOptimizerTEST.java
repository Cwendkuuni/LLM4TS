package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointsToTest;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointsToTest;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Tests the optimization of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0});
        PointValuePair result = optimizer.optimize(function, new PointValuePair(0.5, 0.25), GoalType.MINIMIZE);
        double expected = -1.0;
        assertEquals(expected, result.getPoint(), 1e-8);
        assertEquals(expected, result.getValue(), 1e-8);
    }

    /**
     * Tests the optimization of a simple function with custom bounds.
     */
    @Test
    public void testSimpleFunctionWithBounds() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0});
        PointsToTest pointsToTest = new PointsToTest(-5.0, 5.0);
        InitialGuess initialGuess = new InitialGuess(pointsToTest);
        PointValuePair result = optimizer.optimize(function, initialGuess, GoalType.MINIMIZE, new SimpleBounds(-5.0, 5.0));
        double expected = -1.0;
        assertEquals(expected, result.getPoint(), 1e-8);
        assertEquals(expected, result.getValue(), 1e-8);
    }

    /**
     * Tests the optimization of a function with many local minima.
     */
    @Test
    public void testFunctionWithManyLocalMinima() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new PolynomialFunction(2, new double[]{1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0});
        PointValuePair result = optimizer.optimize(function, new PointValuePair(0.5, 0.25), GoalType.MINIMIZE);
        double expected = -1.6;
        assertEquals(expected, result.getPoint(), 1e-8);
        assertEquals(expected, result.getValue(), 1e-8);
    }

    /**
     * Tests the optimization of a function with a large number of dimensions.
     */
    @Test
    public void testFunctionWithLargeNumberOfDimensions() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 2.0, 2.0);
                }
                return sum;
            }

            public int getNumberOfVariables() {
                return x.length;
            }
        };
        PointValuePair result = optimizer.optimize(function, new PointValuePair(new double[10], 0.0), GoalType.MINIMIZE);
        double expected = 0.0;
        assertEquals(expected, result.getPoint()[0], 1e-8);
        assertEquals(expected, result.getPoint()[1], 1e-8);
        assertEquals(expected, result.getPoint()[2], 1e-8);
        assertEquals(expected, result.getPoint()[3], 1e-8);
        assertEquals(expected, result.getPoint()[4], 1e-8);
        assertEquals(expected, result.getPoint()[5], 1e-8);
        assertEquals(expected, result.getPoint()[6], 1e-8);
        assertEquals(expected, result.getPoint()[7], 1e-8);
        assertEquals(expected, result.getPoint()[8], 1e-8);
        assertEquals(expected, result.getPoint()[9], 1e-8);
        assertEquals(expected, result.getValue(), 1e-8);
    }
}