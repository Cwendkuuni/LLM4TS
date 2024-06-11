package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairList;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Simplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexInit;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIterationException;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexTermination;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleValueChecker.ConvergenceChecker;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {

    /** Multivariate function to be optimized. */
    private MultivariateFunction f;

    /** Optimizer under test. */
    private SimplexOptimizer optimizer;

    /** Optimization data. */
    private OptimizationData data;

    /** Simplex used for optimization. */
    private AbstractSimplex simplex;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        f = new PolynomialFunction(new double[][]{{-1.0, -1.0}, {0.0, -1.0},
                                                  {1.0, -1.0}, {-1.0, 0.0},
                                                  {1.0, 0.0}, {-1.0, 1.0},
                                                  {1.0, 1.0}, {0.0, 0.0}},
                                          new double[]{-1.0, -1.0});
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        data = new OptimizationData() {
            @Override
            public Object getParameter() {
                return new Object[] {"optimizer"};
            }

            @Override
            public Object getInitialGuess() {
                RandomInitializer random = new RandomInitializer(new MersenneTwister());
                return new Object[]{random.getInitialPoint(f, 2)};
            }

            @Override
            public Object getSimplex() {
                simplex = new Simplex(new SimplexInit(f, new GoalType[] {GoalType.MINIMIZE}));
                return simplex;
            }
        };
    }

    /**
     * Basic test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])} method.
     */
    @Test
    public void testOptimize() {
        optimizer.optimize(100, f, GoalType.MINIMIZE, (Object[]) data.getInitialGuess());
        PointValuePair result = optimizer.getOptimum();
        assertEquals(-1.0, result.getPoint()[0], 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    /**
     * Test for exception when {@link #setSimplex(AbstractSimplex)} is not called.
     */
    @Test
    public void testSetSimplex() {
        assertThrows(NullPointerException.class, () -> {
            optimizer.optimize(100, f, GoalType.MINIMIZE,
                              (Object[]) data.getInitialGuess());
        });
    }

    /**
     * Test for exception when {@link AbstractSimplex} is null.
     */
    @Test
    public void testSetSimplexNull() {
        optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> {
            optimizer.setSimplex(null);
        });
    }

    /**
     * Test for convergence when optimization goal is reached.
     */
    @Test
    public void testConvergence() {
        optimizer.optimize(100, f, GoalType.MINIMIZE, (Object[]) data.getInitialGuess());
        simplex.setCheckForConvergence(new SimplexTermination(new ConvergenceChecker(0.001)));
        optimizer.optimize(100, f, GoalType.MINIMIZE, (Object[]) data.getInitialGuess());
        PointValuePair[] worst = simplex.getPoints();
        assertEquals(-1.0, worst[0].getValue(), 0.001);
        assertEquals(-1.0, worst[simplex.getSize() - 1].getValue(), 0.001);
        assertArrayEquals(FunctionUtils.pointsToArray(new PointValuePairList(worst)),
                          FunctionUtils.pointsToArray(simplex.getPoints()), 0.001);
    }

    /**
     * Test for exception when optimization goal is not reached.
     */
    @Test(expected = SimplexIterationException.class)
    public void testNoConvergence() {
        optimizer.optimize(100, f, GoalType.MINIMIZE, (Object[]) data.getInitialGuess());
        simplex.setCheckForConvergence(new SimplexTermination(new ConvergenceChecker(0.01)));
        optimizer.optimize(100, f, GoalType.MINIMIZE, (Object[]) data.getInitialGuess());
    }
}