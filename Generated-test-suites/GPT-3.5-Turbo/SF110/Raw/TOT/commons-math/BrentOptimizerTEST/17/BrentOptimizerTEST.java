package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer brentOptimizer;

    @Before
    public void setUp() {
        brentOptimizer = new BrentOptimizer(0.01, 0.1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeThresholdLessThanMinRelativeTolerance() {
        new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 0.01, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsoluteThresholdNotPositive() {
        new BrentOptimizer(0.01, -0.1);
    }

    @Test
    public void testDoOptimizeWithMinimizationGoalType() {
        brentOptimizer.setGoalType(GoalType.MINIMIZE);
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> x * x), 0, 3);
        Assert.assertEquals(0, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithMaximizationGoalType() {
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> x * x), 0, 3);
        Assert.assertEquals(3, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithNonStrictlyIncreasingObjectiveFunction() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> -1), 0, 3);
        Assert.assertEquals(0, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithNonStrictlyDecreasingObjectiveFunction() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> 1), 0, 3);
        Assert.assertEquals(3, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithObjectiveFunctionWithMultipleLocalMinima() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> x * x * x), -3, 3);
        Assert.assertEquals(0, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithObjectiveFunctionWithMultipleLocalMaxima() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> -x * x * x), -3, 3);
        Assert.assertEquals(0, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithObjectiveFunctionReturningNaN() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> Double.NaN), 0, 3);
        Assert.assertTrue(Double.isNaN(brentOptimizer.getResult()));
    }

    @Test
    public void testDoOptimizeWithZeroInterval() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> x * x), 0, 0);
        Assert.assertEquals(0, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithEqualEndpoints() {
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> x * x), 3, 3);
        Assert.assertEquals(3, brentOptimizer.getResult(), 0.01);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        brentOptimizer.setConvergenceChecker((iteration, previous, current) ->
                Precision.equals(previous.getPoint(), current.getPoint(), 0.01));
        brentOptimizer.minimize(new UnivariateObjectiveFunction(x -> x * x), 0, 3);
        Assert.assertEquals(0, brentOptimizer.getResult(), 0.01);
    }

}