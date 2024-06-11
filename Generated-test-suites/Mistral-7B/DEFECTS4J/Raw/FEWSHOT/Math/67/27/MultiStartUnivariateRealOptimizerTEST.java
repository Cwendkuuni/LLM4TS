package org.apache.commons.cli.optimization; // Change the package name if necessary

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optim.GoalType;
import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.SimpleBounds;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.optim.initialguess.InitialGuess;
import org.apache.commons.math.optim.nonlinear.optimizer.AbstractUnivariateOptimizerTest;
import org.apache.commons.math.optim.nonlinear.optimizer.GoalType;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest extends AbstractUnivariateOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        final int starts = 3;
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        UnivariateRealFunction testFunction = new TestFunction();
        UnivariateRealOptimizer optimizer = new FixedPointOptimizer(new GoldenSectionSearch(0.5, 1.0, 1.001));
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, new MersenneTwisterFastRandom());

        try {
            PointValuePair bestPoint = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, bounds);
            assertNull(bestPoint); // Test for ConvergenceException
        } catch (ConvergenceException e) {
            // Expected
            double[] optima = multiStartOptimizer.getOptima();
            double[] optimaValues = multiStartOptimizer.getOptimaValues();
            assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN}, optima, 0.0);
            assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN}, optimaValues, 0.0);
        }

        try {
            optimizer.setMaximalIterationCount(10);
            optimizer.setMaxEvaluations(10);
            initialGuess = new UnivariateOptimizerInitialGuess(0.5);
            PointValuePair bestPoint = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, bounds);
            double[] optima = multiStartOptimizer.getOptima();
            double[] optimaValues = multiStartOptimizer.getOptimaValues();
            assertEquals(starts, optima.length);
            assertEquals(starts, optimaValues.length);
            double minX = Double.POSITIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            for (int i = 0; i < starts; i++) {
                double x = optima[i];
                double y = optimaValues[i];
                if (goalType == GoalType.MINIMIZE && y < minY) {
                    minX = x;
                    minY = y;
                } else if (goalType == GoalType.MAXIMIZE && y > minY) {
                    minX = x;
                    minY = y;
                }
            }
            assertEquals(minX, bestPoint.getPoint(), 0.01);
            assertEquals(minY, bestPoint.getValue(), 0.01);
            assertTrue(minY >= 0.3 && minY <= 0.5); // It's a test function minimum
        } catch (ConvergenceException e) {
            fail("Test failed due to ConvergenceException.");
        } catch (FunctionEvaluationException e) {
            fail("Test failed due to FunctionEvaluationException.");
        }
    }

    static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x) + 2 * x;
        }
    }

}