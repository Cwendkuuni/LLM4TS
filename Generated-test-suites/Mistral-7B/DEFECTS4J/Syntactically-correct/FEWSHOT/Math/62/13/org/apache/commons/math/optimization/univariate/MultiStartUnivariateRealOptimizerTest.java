package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSelector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentOptions;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.commons.math.optimization.univariate.UnivariateRealPointValuePair;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1d, 1d, -1d}, 3);
        optimizer = new BrentOptimizer();
        BrentOptions options = new BrentOptions();
        options.setMaxIterations(100);
        options.setTolerance(1e-10);
        optimizer.setOptions(options);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new MersenneTwister());
        generator = multiStartOptimizer.getGenerator();
    }

    @Test
    public void testMinimize() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        PointValuePair optimalSolution = multiStartOptimizer.optimize(function, goal, min, max);
        assertNotNull(optimalSolution);
        assertEquals(0.0, Precision.abs(function.value(optimalSolution.getPoint()), 1e-8), 1e-8);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNotNull(pair);
            assertEquals(optimalSolution.getPoint(), Precision.isClose(pair.getPoint(), optimalSolution.getPoint(), 1e-8), 1e-8);
            assertEquals(optimalSolution.getValue(), pair.getValue(), 1e-8);
        }
    }

    @Test
    public void testMaximize() throws Exception {
        GoalType goal = GoalType.MAXIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        PointValuePair optimalSolution = multiStartOptimizer.optimize(function, goal, min, max);
        assertNotNull(optimalSolution);
        assertEquals(1.0, Precision.abs(function.value(optimalSolution.getPoint()), 1e-8), 1e-8);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNotNull(pair);
            assertEquals(optimalSolution.getPoint(), pair.getPoint(), 1e-8);
            assertEquals(-optimalSolution.getValue(), pair.getValue(), 1e-8);
        }
    }

    @Test
    public void testNoConvergence() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        try {
            multiStartOptimizer.optimize(function, goal, min, max);
            fail("Should have thrown ConvergenceException");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().startsWith("NO_CONVERGENCE_WITH_ANY_START_POINT"));
        }

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNull(pair);
        }
    }
}