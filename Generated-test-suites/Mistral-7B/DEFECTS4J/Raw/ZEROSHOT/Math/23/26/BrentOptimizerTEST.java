package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Assert.assertNotNull;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} construction.
     */
    @Test
    public void testBrentOptimizerConstruction() {
        double rel = 1e-4;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)} construction.
     */
    @Test
    public void testBrentOptimizerConstructionWithChecker() {
        double rel = 1e-4;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, a, b) -> false;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructionWithInvalidRelativeThreshold() {
        double rel = 1e-6;
        double abs = 1e-6;
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructionWithInvalidAbsoluteThreshold() {
        double rel = 1e-6;
        double abs = -1e-6;
        new BrentOptimizer(rel, abs);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()} method.
     */
    @Test
    public void testBrentOptimizerDoOptimize() {
        Function testFunction = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        double rel = 1e-4;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 2 * Math.PI);
        assertNotNull(result);
        assertEquals(Math.PI, result.getPoint(), 1e-6);
        assertEquals(-1.112712816657983e-07, result.getValue(), 1e-8);
    }
}