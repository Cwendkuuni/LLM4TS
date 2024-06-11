package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = x -> Math.pow(x, 2) - 2 * x + 1;
        optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     */
    @Test
    public void testBrentOptimizer() {
        PointValuePair min = optimizer.optimize(function, new InitialGuess(0.5), new MaxEval(100));
        assertEquals(0.0, min.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     */
    @Test
    public void testBrentOptimizerWithDefaultChecker() {
        PointValuePair min = optimizer.optimize(function, new InitialGuess(0.5), null);
        assertEquals(0.0, min.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, InitialGuess, MaxEval)}
     * with GoalType.MINIMIZE
     */
    @Test
    public void testMinimize() {
        PointValuePair min = optimizer.optimize(function, new InitialGuess(2.0), new MaxEval(100));
        assertEquals(0.0, min.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, InitialGuess, MaxEval)}
     * with GoalType.MAXIMIZE
     */
    @Test
    public void testMaximize() {
        BrentOptimizer maximizer = new BrentOptimizer(1.0e-5, 1.0e-5, GoalType.MAXIMIZE);
        PointValuePair max = maximizer.optimize(function, new InitialGuess(2.0), new MaxEval(100));
        assertEquals(-0.0, max.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, InitialGuess, MaxEval)}
     * with custom convergence checker
     */
    @Test
    public void testCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, a, b) -> a.getValue() - b.getValue() < 1e-6;
        PointValuePair min = optimizer.optimize(function, new InitialGuess(1.0), new MaxEval(100), customChecker);
        assertEquals(0.0, min.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, InitialGuess, MaxEval)}
     * with an unbounded function
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnboundedFunction() {
        UnivariateFunction unboundedFunction = x -> 1.0 / x;
        optimizer.optimize(unboundedFunction, new InitialGuess(1.0), new MaxEval(100));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, InitialGuess, MaxEval)}
     * with an incomplete function
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncompleteFunction() {
        UnivariateFunction incompleteFunction = x -> {};
        optimizer.optimize(incompleteFunction, new InitialGuess(1.0), new MaxEval(100));
    }
}