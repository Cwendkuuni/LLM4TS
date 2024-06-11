package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-6;

    private static class TestFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.pow(x, 2) - 2;
        }
    }

    @Test
    public void testDoOptimize() {
        // create optimizer
        BrentOptimizer optimizer = new BrentOptimizer(EPSILON, EPSILON);
        // set function
        optimizer.setObjective(new TestFunction());
        // set search interval
        optimizer.setMin(-100);
        optimizer.setMax(100);
        // set initial guess
        optimizer.setStartValue(0);
        // set goal type
        optimizer.setGoalType(GoalType.MINIMIZE);

        // optimize
        UnivariatePointValuePair result = optimizer.optimize();

        // check result
        assertEquals(FastMath.sqrt(2), result.getValue(), EPSILON);
        assertEquals(FastMath.sqrt(2), result.getPoint(), EPSILON);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        new BrentOptimizer(1e-10, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        new BrentOptimizer(0.1, 0);
    }

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 10);
        assertEquals(0.1, optimizer.getRelativeThreshold(), 0);
        assertEquals(10, optimizer.getAbsoluteThreshold(), 0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 10, checker);
        assertEquals(0.1, optimizer.getRelativeThreshold(), 0);
        assertEquals(10, optimizer.getAbsoluteThreshold(), 0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

}