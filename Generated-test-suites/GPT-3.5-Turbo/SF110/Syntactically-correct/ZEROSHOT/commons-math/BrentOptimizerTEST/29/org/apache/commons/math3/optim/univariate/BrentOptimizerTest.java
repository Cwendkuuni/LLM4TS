package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(-0.1, 0.1, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(0.1, 0.0, null);
    }

    @Test
    public void testDoOptimize() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, checker);
        optimizer.withMinimize(true); // Set the goal type to minimize
        optimizer.withMin(0.0);
        optimizer.withStartValue(1.0);
        optimizer.withMax(10.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        double expectedValue = 0.0; // Expected minimum value
        Assert.assertEquals(expectedValue, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testBestWhenAIsNull() {
        double x = 1.0;
        double value = 2.0;
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(x, value);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(b, result);
    }

    @Test
    public void testBestWhenBIsNull() {
        double x = 1.0;
        double value = 2.0;
        UnivariatePointValuePair a = new UnivariatePointValuePair(x, value);
        UnivariatePointValuePair b = null;
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(a, result);
    }

    @Test
    public void testBestWhenAHasLowerValue() {
        double x1 = 1.0;
        double value1 = 1.0;
        UnivariatePointValuePair a = new UnivariatePointValuePair(x1, value1);
        double x2 = 2.0;
        double value2 = 2.0;
        UnivariatePointValuePair b = new UnivariatePointValuePair(x2, value2);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(a, result);
    }

    @Test
    public void testBestWhenBHasLowerValue() {
        double x1 = 2.0;
        double value1 = 2.0;
        UnivariatePointValuePair a = new UnivariatePointValuePair(x1, value1);
        double x2 = 1.0;
        double value2 = 1.0;
        UnivariatePointValuePair b = new UnivariatePointValuePair(x2, value2);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(b, result);
    }

    @Test
    public void testBestWhenAHasHigherValue() {
        double x1 = 1.0;
        double value1 = 2.0;
        UnivariatePointValuePair a = new UnivariatePointValuePair(x1, value1);
        double x2 = 2.0;
        double value2 = 1.0;
        UnivariatePointValuePair b = new UnivariatePointValuePair(x2, value2);
        boolean isMinim = false;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(a, result);
    }

    @Test
    public void testBestWhenBHasHigherValue() {
        double x1 = 2.0;
        double value1 = 1.0;
        UnivariatePointValuePair a = new UnivariatePointValuePair(x1, value1);
        double x2 = 1.0;
        double value2 = 2.0;
        UnivariatePointValuePair b = new UnivariatePointValuePair(x2, value2);
        boolean isMinim = false;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(b, result);
    }
}