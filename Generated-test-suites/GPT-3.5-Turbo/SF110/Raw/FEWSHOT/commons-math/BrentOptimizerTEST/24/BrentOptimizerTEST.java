
package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeToleranceTooSmall() {
        new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 1.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteToleranceNotPositive() {
        new BrentOptimizer(1.0, 0.0);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-12);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMax(100.0);
        optimizer.setMin(0.0);
        optimizer.setStartValue(50.0);
        optimizer.setConvergenceChecker((iter, previous, current) ->
                Precision.equals(current.getValue(), 0.0, 1e-6));
        UnivariatePointValuePair result = optimizer.doOptimize();

        double expected = 0.0;
        double actual = result.getValue();

        Assert.assertEquals(expected, actual, 1e-6);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-12);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker((iter, previous, current) ->
                Precision.equals(current.getValue(), 0.0, 1e-6));

        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertSame(b, result);
    }
}
