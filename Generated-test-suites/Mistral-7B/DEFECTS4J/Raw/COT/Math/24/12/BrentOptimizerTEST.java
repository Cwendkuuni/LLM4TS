package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for class BrentOptimizer.
 */
public class BrentOptimizerTest {
    // 

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getMin());
        assertNull(optimizer.getMax());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with user-defined tolerance and no convergence checker.
     */
    @Test
    public void testConstructorWithTolerance() {
        double rel = 1e-4;
        double abs = 1e-5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
        assertNull(optimizer.getMin());
        assertNull(optimizer.getMax());
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with user-defined tolerance and convergence checker.
     */
    @Test
    public void testConstructorWithToleranceAndChecker() {
        double rel = 1e-4;
        double abs = 1e-5;
        ConvergenceChecker<UnivariatePointValuePair> checker
            = (iter, previous, current) -> !previous.getValue().isNaN()
                         && Math.abs(previous.getValue() - current.getValue()) < 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
        assertNull(optimizer.getMin());
        assertNull(optimizer.getMax());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with simple function f(x) = x^2.
     */
    @Test
    public void testOptimizeFunctionXSquared() {
        UnivariateFunction function = x -> x * x;
        double min = 2.5;
        double max = 3.5;
        BrentOptimizer optimizer = new BrentOptimizer(min, min / 100.0, null);
        UnivariatePointValuePair result = optimizer.optimize(function, min, max);
        assertEquals(min, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization with user-defined convergence checker.
     */
    @Test
    public void testOptimizeFunctionXSquaredWithCustomChecker() {
        UnivariateFunction function = x -> x * x;
        double min = 2.5;
        double max = 3.5;
        double rel = 1e-5;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker
            = (iter, previous, current) -> Math.abs(previous.getValue() - current.getValue()) < 1e-7;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariatePointValuePair result = optimizer.optimize(function, min, max);
        assertEquals(min, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test optimization with function f(x) = -E^x.
     */
    @Test
    public void testOptimizeFunctionExponentialNegative() {
        UnivariateFunction function = x -> -Math.exp(x);
        double min = -10.0;
        double max = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(min, min / 100.0, null);
        UnivariatePointValuePair result = optimizer.optimize(function, min, max);
        assertTrue(result.getValue() > -1000.0);
        assertEquals(-Math.log(E.E), result.getValue(), 1e-8);
    }

    /**
     * Test optimization with function f(x) = sin(x) / x.
     */
    @Test
    public void testOptimizeFunctionSinOverX() {
        UnivariateFunction function = x -> Math.sin(x) / x;
        double min = -Math.PI;
        double max = Math.PI;
        BrentOptimizer optimizer = new BrentOptimizer(min, min / 100.0, null);
        UnivariatePointValuePair result = optimizer.optimize(function, min, max);
        assertEquals(min, result.getPoint(), 1e-8);
        assertEquals(-1.0 / Math.sin(Math.PI), result.getValue(), 1e-8);
    }

    // 
}