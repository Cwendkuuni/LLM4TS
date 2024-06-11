package org.apache.commons.cli.optimization;

import static org.junit.Assert.*;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optim.GoalType;
import org.apache.commons.math.optim.MaxEval;
import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.optim.nonlinear.guess.GuessType;
import org.apache.commons.math.optim.nonlinear.simple.CMAES;
import org.apache.commons.math.optim.nonlinear.simple.CMAESOptimizer;
import org.junit.Test;

/**
 * Test cases for MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private static UnivariateRealFunction f(double a, double b) {
        return new PolynomialFunction(new double[]{-b, 4.0 * a, 6.0 * a * a, -12.0 * a * a * a}, new double[]{0.0, 0.0, 1.0, 1.0});
    }

    @Test
    public void testOptimize() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new CMAESOptimizer(GoalType.MINIMIZE, GuessType.RANDOM, new MaxEval(1000)),
                5, new CMAES(GoalType.MINIMIZE, new MaxEval(1000)));
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(5, optima.length);
        assertEquals(5, optimaValues.length);

        // check optima values
        for (int i = 0; i < 5; ++i) {
            assertEquals(result.getValue(), optimaValues[i], 1e-6);
        }

        // check optima
        for (int i = 0; i < 5; ++i) {
            assertNotEquals(result.getPoint().getX(), optima[i], 1e-6);
        }

        // check optima order
        for (int i = 1; i < 5; ++i) {
            for (int j = 0; j < i; ++j) {
                if (result.getValue() > optimaValues[j]) {
                    double temp = optima[i];
                    optima[i] = optima[j];
                    optima[j] = temp;
                    double tempVal = optimaValues[i];
                    optimaValues[i] = optimaValues[j];
                    optimaValues[j] = tempVal;
                }
            }
        }
    }
}