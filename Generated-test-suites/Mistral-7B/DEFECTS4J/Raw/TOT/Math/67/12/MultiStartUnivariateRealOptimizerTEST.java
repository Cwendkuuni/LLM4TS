package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.gompertz.GompertzFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializers.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.maximization.SimpleBoundingMethod;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimize method with minimization goal type and bounds.
     * @throws Exception if optimization fails
     */
    @Test
    public void testMinimization() throws Exception {
        double min = 0.0;
        double max = 1.0;

        // define function to optimize
        UnivariateRealFunction function = new GompertzFunction(1.0, 0.5);

        // create optimizer
        UnivariateRealOptimizer optimizer = new SimpleBoundingMethod();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new AveragedStartingPointGenerator(10));

        // perform minimization
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);

        // check results
        double expected = 0.4154562193165882;
        assertEquals(expected, result, 1e-6);
        PointValuePair pvp = multiStartOptimizer.getOptima();
        // check that optima are correct
        double[] optima = pvp.getPoint();
        double[] expectedOptima = { 0.4282118282043622, 0.3973359703667131, 0.4125538124296861, 0.415456224373183, 0.4154562193165882, 0.4154562193165882, 0.4154562193165882, 0.4154562193165882, 0.4154562193165882, 0.4154562193165882 };
        assertArrayEquals(expectedOptima, optima, 1e-6);
        double[] optimaValues = pvp.getValue();
        double[] expectedOptimaValues = { 0.0559412783802967, 0.05756619913575733, 0.05692442586314635, 0.05679284886925166, 0.05679284886925166, 0.05679284886925166, 0.05679284886925166, 0.05679284886925166, 0.05679284886925166, 0.05679284886925166 };
        assertArrayEquals(expectedOptimaValues, optimaValues, 1e-6);
    }

    /**
     * Test optimize method with maximization goal type and bounds.
     * @throws Exception if optimization fails
     */
    @Test
    public void testMaximization() throws Exception {
        double min = 0.0;
        double max = 1.0;

        // define function to optimize
        UnivariateRealFunction function = new GompertzFunction(1.0, 1.0);

        // create optimizer
        UnivariateRealOptimizer optimizer = new MaxEval(1000);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new AveragedStartingPointGenerator(10));

        // perform maximization
        double result = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, min, max);

        // check results
        double expected = 0.8620752333863623;
        assertEquals(expected, result, 1e-6);
        PointValuePair pvp = multiStartOptimizer.getOptima();
        // check that optima are correct
        double[] optima = pvp.getPoint();
        double[] expectedOptima = { 0.8716869308666299, 0.8619920523146746, 0.8659482173204601, 0.8675734018548391, 0.8675734018548391, 0.8675734018548391, 0.8675734018548391, 0.8675734018548391, 0.8675734018548391, 0.8675734018548391 };
        assertArrayEquals(expectedOptima, optima, 1e-6);
        double[] optimaValues = pvp.getValue();
        double[] expectedOptimaValues = { 3.729581423656503, 3.733737273214247, 3.729994911282037, 3.733034535896627, 3.733034535896627, 3.733034535896627, 3.733034535896627, 3.733034535896627, 3.733034535896627, 3.733034535896627 };
        assertArrayEquals(expectedOptimaValues, optimaValues, 1e-6);
    }

    /**
     * Test optimize method with bounds out of order.
     * @throws Exception if optimization fails
     */
    @Test
    public void testBoundsOutOfOrder() throws Exception {
        double max = 0.0;
        double min = 1.0;

        // define function to optimize
        UnivariateRealFunction function = new GompertzFunction(1.0, 0.5);

        // create optimizer
        UnivariateRealOptimizer optimizer = new SimpleBoundingMethod();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new AveragedStartingPointGenerator(10));

        // perform optimization
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }
}