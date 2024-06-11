
package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-4, 1e-6);
        optimizer.setMin(0);
        optimizer.setMax(10);
    }

    @Test
    public void testDoOptimize_minimize() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @Test
    public void testDoOptimize_maximize() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(10.0, result.getPoint(), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_relTooSmall() {
        new BrentOptimizer(1e-8, 1e-4);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absNotPositive() {
        new BrentOptimizer(1e-4, 0);
    }

}
