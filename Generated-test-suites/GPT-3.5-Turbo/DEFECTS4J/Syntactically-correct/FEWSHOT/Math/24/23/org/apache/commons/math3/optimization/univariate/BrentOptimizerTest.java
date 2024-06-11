package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        // Test optimization
        double result = optimizer.doOptimize();

        // Check if result is within expected range
        assertTrue(result > -1 && result < 1);
    }

    @Test
    public void testRelativeThreshold() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        // Get relative threshold
        double relativeThreshold = optimizer.getRelativeThreshold();

        // Check if relative threshold is positive
        assertTrue(relativeThreshold > 0);
    }

    @Test
    public void testAbsoluteThreshold() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        // Get absolute threshold
        double absoluteThreshold = optimizer.getAbsoluteThreshold();

        // Check if absolute threshold is positive
        assertTrue(absoluteThreshold > 0);
    }

    @Test
    public void testConvergenceChecker() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        // Get convergence checker
        ConvergenceChecker<UnivariatePointValuePair> checker = optimizer.getConvergenceChecker();

        // Check if convergence checker is not null
        assertNotNull(checker);
    }

    @Test
    public void testBest() {
        // Create two UnivariatePointValuePair objects
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 2);

        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        // Call best method
        UnivariatePointValuePair best = optimizer.best(a, b, true);

        // Check if best is equal to b
        assertEquals(best, b);
    }

}