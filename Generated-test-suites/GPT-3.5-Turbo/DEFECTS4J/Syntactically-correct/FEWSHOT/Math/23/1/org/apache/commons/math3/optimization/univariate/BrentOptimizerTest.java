package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    private final double relativeThreshold = 1E-6;
    private final double absoluteThreshold = 1E-6;
    private final ConvergenceChecker<UnivariatePointValuePair> checker = null;

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double invalidRelativeThreshold = 1E-20;
        BrentOptimizer optimizer = new BrentOptimizer(invalidRelativeThreshold, absoluteThreshold, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double invalidAbsoluteThreshold = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, invalidAbsoluteThreshold, checker);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(4.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(4.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 100;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(4.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNull(result);
    }

    @Test
    public void testBest() {
        boolean isMinim = true;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 20.0);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(10.0, result.getValue(), 0.0);

        isMinim = false;
        result = optimizer.best(a, b, isMinim);
        assertEquals(2.0, result.getPoint(), 0.0);
        assertEquals(20.0, result.getValue(), 0.0);
    }
}