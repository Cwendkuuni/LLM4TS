package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Polynomials;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizer() throws Exception {
        // Create test function.
        UnivariateRealFunction testFunction = new PolynomialFunction(new double[] { 1.0, -2.0, 3.0 }, 3);

        // Create optimizer to wrap.
        UnivariateRealOptimizer optimizer = new BrentOptimizer();

        // Create multi-start optimizer.
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.5);
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 10, randomGenerator);

        // Test with 5 evaluations per start.
        multiStartOptimizer.setMaxEvaluations(50);
        GoalType goal = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        // Test optimization.
        PointValuePair p = multiStartOptimizer.optimize(testFunction, goal, min, max);
        assertEquals(-2.0, p.getPoint().doubleValue(), 1e-6);
        assertEquals(0.0, p.getValue(), 1e-6);

        // Test all optima.
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(13, optima.length);
        assertArrayEquals(new Object[] { new Double[] { -2.0 }, new Double[] { -0.445745174461454 } },
                Arrays.stream(optima).filter(op -> op != null).map(op -> op.getPoint().toArray()).toArray());
    }

    @Test
    public void testMultiStartOptimizerNoConvergence() throws Exception {
        // Create test function.
        UnivariateRealFunction testFunction = new PolynomialFunction(new double[] { 1.0, -2.0, 3.0 }, 3);

        // Create optimizer to wrap.
        UnivariateRealOptimizer optimizer = new BrentOptimizer();

        // Create multi-start optimizer.
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.5);
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 10, randomGenerator);

        // Test with 5 evaluations per start.
        multiStartOptimizer.setMaxEvaluations(5);
        GoalType goal = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        // Test optimization.
        try {
            multiStartOptimizer.optimize(testFunction, goal, min, max);
            assert(false);
        } catch (ConvergenceException e) {
            UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
            assertEquals(10, optima.length);
            for (int i = 0; i < optima.length; i++) {
                assertNull(optima[i]);
            }
        }
    }
}