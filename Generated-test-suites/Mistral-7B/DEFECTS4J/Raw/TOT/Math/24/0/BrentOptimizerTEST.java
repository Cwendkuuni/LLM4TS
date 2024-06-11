package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function for {@link BrentOptimizer}.
     */
    static class TestFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            return Math.exp(x) - E.E;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Test {@link BrentOptimizer} with simple value checker and optimal initializer.
     */
    @Test
    public void testBrentOptimizerSimpleValueChecker() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateOptimizer result = optimizer.optimize(testFunction, new AroundMinimumInitializer(testFunction, 0.5));
        PointValuePair expected = new PointValuePair(0.5, -E.E);
        assertEquals(expected.getValue(), result.getValue(), 1e-8);
        assertEquals(expected.getPoint(), result.getPoint(), 1e-8);
    }

    /**
     * Test {@link BrentOptimizer} with user-defined convergence checker.
     */
    @Test
    public void testBrentOptimizerCustomChecker() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new SimpleValueChecker(1e-7));
        UnivariateOptimizer result = optimizer.optimize(testFunction, new AroundMinimumInitializer(testFunction, 0.5));
        PointValuePair expected = new PointValuePair(0.45, -1.2212344739492812e-12);
        assertEquals(expected.getValue(), result.getValue(), 1e-12);
        assertEquals(expected.getPoint(), result.getPoint(), 1e-12);
    }

    /**
     * Test {@link BrentOptimizer} with not strictly positive absolute threshold.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-5);
    }

    /**
     * Test {@link BrentOptimizer} with not strictly positive relative threshold.
     */
    @Test(expected = org.apache.commons.math3.exception.NumberIsTooSmallException.class)
    public void testBrentOptimizerSmallRelativeThreshold() {
        new BrentOptimizer(1e-8, 1e-5);
    }

    /**
     * Test {@link BrentOptimizer} with not strictly positive initial bounds.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testBrentOptimizerBounds() {
        new BrentOptimizer(1e-5, 1e-5, new AroundMinimumInitializer(-1.0, 0.5));
    }
}