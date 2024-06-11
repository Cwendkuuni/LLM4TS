package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbs() {
        optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroAbs() {
        optimizer = new BrentOptimizer(1e-6, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelTooSmall() {
        optimizer = new BrentOptimizer(1e-15, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(-5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        // Compare actual result with expected result

    }

    @Test
    public void testBestWithEqualValues() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.0);

        UnivariatePointValuePair best = optimizer.best(a, b, true);

        assertEquals(a, best);
    }

    @Test
    public void testBestWithDifferentValues() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);

        UnivariatePointValuePair best = optimizer.best(a, b, true);

        assertEquals(a, best);
    }

    @Test
    public void testBestWithBothNull() {
        UnivariatePointValuePair best = optimizer.best(null, null, true);

        assertNull(best);
    }

    @Test
    public void testBestWithNullAndValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair best = optimizer.best(null, a, true);

        assertEquals(a, best);
    }
}