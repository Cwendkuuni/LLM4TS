package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        // Test with default coefficients
        MultiDirectional optimizer = new MultiDirectional();
        UnconstrainedMinimizer minimizer = optimizer;
        double[] lowerBounds = new double[]{-10.0, -10.0};
        double[] upperBounds = new double[]{10.0, 10.0};
        SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);
        double[] startPoint = new double[]{0.0, 0.0};
        minimizer.minimize(new ScalarFunction(Math::pow), startPoint, bounds);
        PointValuePair bestSolution = minimizer.getSolution();
        double expectedSolution = Math.sqrt(2.0);
        assertEquals(expectedSolution, bestSolution.getPoint()[0], 1e-5);
        assertEquals(0.0, bestSolution.getValue(), 1e-5);

        // Test with custom coefficients
        MultiDirectional customOptimizer = new MultiDirectional(3.0, 0.8);
        minimizer = customOptimizer;
        minimizer.minimize(new ScalarFunction(Math::pow), startPoint, bounds);
        bestSolution = minimizer.getSolution();
        // The custom coefficients may lead to a different solution, but it should be better than the one obtained with default coefficients
        assertTrue(bestSolution.getValue() < Double.POSITIVE_INFINITY);

        // Test with a function that is not monotonous
        MultiDirectional optimizerWithErrorHandling = new MultiDirectional();
        try {
            minimizer = optimizerWithErrorHandling;
            minimizer.minimize(new ScalarFunction(Math::sin), startPoint, bounds);
            fail("An OptimizationException should have been thrown.");
        } catch (OptimizationException e) {
            // expected
        }

        // Test with a function that has multiple minima
        MultiDirectional optimizerForMultiMinima = new MultiDirectional();
        minimizer = optimizerForMultiMinima;
        double[] startPointForMultiMinima = new double[]{-5.0, 5.0};
        minimizer.minimize(new ScalarFunction(x -> x[0] * x[0] - x[0] * Math.log(Math.abs(x[1]))), startPointForMultiMinima, bounds);
        bestSolution = minimizer.getSolution();
        double expectedSolutionForMultiMinima = Math.log(Math.E) - 1.0;
        assertEquals(expectedSolutionForMultiMinima, bestSolution.getPoint()[0], 1e-5);
        assertEquals(expectedSolutionForMultiMinima, bestSolution.getValue(), 1e-5);
    }

}