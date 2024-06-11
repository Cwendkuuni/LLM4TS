package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-6;
    private UnivariateOptimizer singleStartOptimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        singleStartOptimizer = new BrentOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer<>(singleStartOptimizer, 5, new MersenneTwister());
    }

    @Test
    public void testMinimization() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(2, 1, new double[]{-1, 1, 1, -1});
        GoalType goal = GoalType.MINIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);

        PointValuePair bestOptimum = optimizer.optimize(function, goal, bounds.getMin(), bounds.getMax());
        assertNull(bestOptimum);

        int maxEvaluations = 20;
        optimizer.setMaxEvaluations(maxEvaluations);

        optimaVerification(function, goal, bounds, maxEvaluations);
    }

    @Test
    public void testMaximization() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(2, 1, new double[]{-1, -1, 1, 1});
        GoalType goal = GoalType.MAXIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);

        PointValuePair bestOptimum = optimizer.optimize(function, goal, bounds.getMin(), bounds.getMax());
        assertNull(bestOptimum);

        int maxEvaluations = 20;
        optimizer.setMaxEvaluations(maxEvaluations);

        optimaVerification(function, goal, bounds, maxEvaluations);
    }

    private void optimaVerification(UnivariateRealFunction function, GoalType goal, SimpleBounds bounds, int maxEvaluations)
            throws Exception {
        PointValuePair bestOptimum = optimizer.optimize(function, goal, bounds.getMin(), bounds.getMax());
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        // Check best optimum
        assertEquals(bestOptimum.getX(), optima[0].getX(), EPSILON);
        assertEquals(bestOptimum.getFunctionValue(), optima[0].getValue(), EPSILON);

        // Check all optima
        for (int i = 1; i < optima.length; ++i) {
            assertNotNull(optima[i]);
            assertTrue(Precision.isClose(function.value(optima[i].getX()), optima[i].getValue(), EPSILON));
            assertTrue(Precision.isClose(bounds.getMin() + generator.nextDouble() * (bounds.getMax() - bounds.getMin()), optima[i].getX(), EPSILON));
        }

        assertEquals(maxEvaluations, optimizer.getEvaluations());
    }

    private void assertNull(PointValuePair optimum) {
        assertNotNull(optimum);
        assertNull(optimum.getX());
        assertNull(optimum.getValue());
    }

    private void assertNotNull(PointValuePair optimum) {
        assertNotNull(optimum);
        assertNotNull(optimum.getX());
        assertNotNull(optimum.getValue());
    }
}