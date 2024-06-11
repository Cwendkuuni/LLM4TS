package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optim.GoalType;
import org.apache.commons.math.optim.MaxEval;
import org.apache.commons.math.optim.NonConvergenceException;
import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.optim.linear.GoSe;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() {
        UnivariateRealOptimizer underlyingOptimizer = new GoSe();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 5, new GoSe.Default());

        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1d, 2d, -1d}, false);
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;

        double[] optima = null;
        double[] optimaValues = null;

        try {
            optima = multiStartOptimizer.optimize(function, goalType, min, max);
            optimaValues = multiStartOptimizer.getOptimaValues();
        } catch (ConvergenceException | FunctionEvaluationException | NonConvergenceException e) {
            assertNotNull("optimize should not throw an exception", e);
        }

        assertNotNull("Optima should not be null after optimization", optima);
        assertArrayEquals("Optima values should match the expected values", new double[]{1.0, 3.0, 5.0, 7.0, 9.0}, optima, 0.001);
        assertArrayEquals("Optima values should match the expected values", new double[]{0.25, 1.75, 3.75, 6.25, 8.75}, optimaValues, 0.001);
    }

    @Test
    public void testMultiStartMaximization() {
        UnivariateRealOptimizer underlyingOptimizer = new GoSe();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 5, new GoSe.Default());

        UnivariateRealFunction function = new PolynomialFunction(new double[]{1d, 2d, 1d}, true);
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MAXIMIZE;

        double[] optima = null;
        double[] optimaValues = null;

        try {
            optima = multiStartOptimizer.optimize(function, goalType, min, max);
            optimaValues = multiStartOptimizer.getOptimaValues();
        } catch (ConvergenceException | FunctionEvaluationException | NonConvergenceException e) {
            assertNotNull("optimize should not throw an exception", e);
        }

        assertNotNull("Optima should not be null after optimization", optima);
        assertArrayEquals("Optima values should match the expected values", new double[]{9.0, 7.0, 5.0, 3.0, 1.0}, optima, 0.001);
        assertArrayEquals("Optima values should match the expected values", new double[]{8.954639263357406, 7.386358258228112, 5.82842712474619, 4.361328114533925, 2.995237213140454}, optimaValues, 0.001);
    }

    @Test
    public void testOptimaEmptyAfterNonConvergence() {
        UnivariateRealOptimizer underlyingOptimizer = new GoSe();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 5, new GoSe.Default());

        UnivariateRealFunction function = new PolynomialFunction(new double[]{1d, 0d, 1d}, true);
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MAXIMIZE;

        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // expected
        }

        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        assertTrue("Optima should be empty after non-convergence", optima.length == 0);
        assertTrue("OptimaValues should be empty after non-convergence", optimaValues.length == 0);
    }
}