package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private double absTolerance = 1e-6;
    private double relativeTolerance = 1e-6;

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_RelativeToleranceSmallerThanMin() {
        new BrentOptimizer(relativeTolerance/2, absTolerance);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_AbsoluteToleranceNegative() {
        new BrentOptimizer(relativeTolerance, -absTolerance);
    }

    @Test
    public void testBrentOptimizer_RelativeToleranceAndAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeTolerance, absTolerance);
        Assert.assertEquals(relativeTolerance, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(absTolerance, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizer_RelativeToleranceAndAbsoluteToleranceWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(relativeTolerance, absTolerance, checker);
        Assert.assertEquals(relativeTolerance, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(absTolerance, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }

    // More test cases for other methods of the class

    // ...
   /**
     * Method for testing {@link BrentOptimizer#doOptimize()}.
     */
    @Test
    public void testBrentOptimizer_TestDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(relativeTolerance, absTolerance);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double result = optimizer.getOptimum();

        //Assert expected result
        Assert.assertEquals(0.0, result, absTolerance);
    }
}