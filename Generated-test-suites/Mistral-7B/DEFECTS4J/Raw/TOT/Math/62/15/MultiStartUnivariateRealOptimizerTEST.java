package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer<PolynomialFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());
        generator = multiStartOptimizer.getGenerator();
    }

    /**
     * Test optimization of a quadratic function with multiple starts.
     *
     * @throws Exception if optimization fails.
     */
    @Test
    public void testQuadraticFunction() throws Exception {
        Function f = new PolynomialFunction(-1.0, 2.0, 1.0);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalType.MINIMIZE;
        PointValuePair optima = multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
        assertEquals(0.0, optima.getPoint().doubleValue(), 1e-6);
        assertEquals(-1.0, optima.getValue(), 1e-6);

        // Check other optima
        for (int i = 1; i < multiStartOptimizer.getOptima().length; i++) {
            assertNull(multiStartOptimizer.getOptima()[i]);
        }
    }

    /**
     * Test optimization of a function with a single minimum that is hard to
     * find.
     *
     * @throws Exception if optimization fails.
     */
    @Test
    public void testSingleMinimumHardToFind() throws Exception {
        Function f = new PolynomialFunction(0.1, 0.1, -1000.0);
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        GoalType goal = GoalType.MINIMIZE;
        PointValuePair optima = multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
        assertEquals(-0.7815134521436724, optima.getPoint().doubleValue(), 1e-6);
        assertEquals(1.2676506224437521e-11, optima.getValue(), 1e-11);

        // Check other optima
        for (int i = 1; i < multiStartOptimizer.getOptima().length; i++) {
            assertNull(multiStartOptimizer.getOptima()[i]);
        }
    }

    /**
     * Test optimization of a function with multiple local minima.
     *
     * @throws Exception if optimization fails.
     */
    @Test
    public void testFunctionWithMultipleLocalMinima() throws Exception {
        Function f = new PolynomialFunction(0.0, 0.1, -Math.E);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalType.MINIMIZE;
        PointValuePair optima = multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
        assertEquals(0.0, optima.getPoint().doubleValue(), 1e-6);
        assertEquals(-Math.E, optima.getValue(), 1e-6);

        // Check other optima
        for (int i = 1; i < multiStartOptimizer.getOptima().length; i++) {
            assertNull(multiStartOptimizer.getOptima()[i]);
        }
    }

    /**
     * Test optimization of a function that has no solution.
     *
     * @throws Exception if optimization fails.
     */
    @Test(expected = ConvergenceException.class)
    public void testFunctionWithNoSolution() throws Exception {
        Function f = new PolynomialFunction(0.0, 0.1, 0.0);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
    }

    /**
     * Test optimization of a function with bounds that are too close.
     *
     * @throws Exception if optimization fails.
     */
    @Test(expected = ConvergenceException.class)
    public void testFunctionWithBoundsTooClose() throws Exception {
        Function f = new PolynomialFunction(0.0, 0.1, 0.0);
        SimpleBounds bounds = new SimpleBounds(1.0, 2.0);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
    }

    /**
     * Test optimization of a function with invalid bounds.
     *
     * @throws Exception if optimization fails.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithInvalidBounds() throws Exception {
        Function f = new PolynomialFunction(0.0, 0.1, 0.0);
        SimpleBounds bounds = new SimpleBounds(1.0, 0.0);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
    }

    /**
     * Test optimization of a function with an invalid initial guess.
     *
     * @throws Exception if optimization fails.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithInvalidInitialGuess() throws Exception {
        Function f = new PolynomialFunction(0.0, 0.1, 0.0);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 10, generator);
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(f, goal, bounds.getMin(), bounds.getMax(), InitialGuess.THE_MINIMUM);
    }

    /**
     * Test optimization of a function with a non-optimizable function.
     *
     * @throws Exception if optimization fails.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFunctionWithNonOptimizableFunction() throws Exception {
        Function f = new Function() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(f, goal, bounds.getMin(), bounds.getMax());
    }
}