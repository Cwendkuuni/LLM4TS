package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizerConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerConstructorNegativeThreshold() {
        new PowellOptimizer(-0.1, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerConstructorTooSmallRelativeThreshold() {
        new PowellOptimizer(0.0, 0.0);
    }

    @Test
    public void testDoOptimizeWithDefaultParameters() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = f -> Math.pow(f[0], 2.0) + Math.pow(f[1], 2.0);

        PointValuePair result = optimizer.optimize(function, startPoint);
        double[] expectedResult = {-1.0, -1.0};
        assertEquals(expectedResult[0], result.getPoint()[0], 0.001);
        assertEquals(expectedResult[1], result.getPoint()[1], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, new MyConvergenceChecker());
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = f -> Math.pow(f[0], 2.0) + Math.pow(f[1], 2.0);

        PointValuePair result = optimizer.optimize(function, startPoint);
        double[] expectedResult = {-1.0, -1.0};
        assertEquals(expectedResult[0], result.getPoint()[0], 0.001);
        assertEquals(expectedResult[1], result.getPoint()[1], 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = {1.0, 2.0};
        double[] searchDirection = {-1.0, 1.0};

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double alpha) {
                double[] p = new double[]{1.0, 2.0};
                for (int i = 0; i < 2; i++) {
                    p[i] += alpha * searchDirection[i];
                }
                return Math.pow(p[0], 2.0) + Math.pow(p[1], 2.0);
            }
        };

        UnivariatePointValuePair result = optimizer.getLineSearch().search(startPoint, searchDirection);

        double expectedAlpha = 1.5;
        double expectedValue = Math.pow(expectedAlpha, 2.0) + Math.pow(1.0 + expectedAlpha, 2.0);

        assertEquals(expectedAlpha, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testLineSearchWithCustomBracketFinder() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double alpha) {
                return Math.sin(alpha);
            }
        };

        BracketFinder bracketFinder = new BracketFinder() {
            @Override
            public void search(UnivariateFunction f, GoalType goal, double lo, double hi, double[] result) {
                Arrays.fill(result, 0.0);
                result[0] = lo;
                result[1] = hi;
            }
        };

        optimizer.getLineSearch().setBracketFinder(bracketFinder);

        UnivariatePointValuePair result = optimizer.getLineSearch().search(new double[]{1.0}, new double[]{-1.0}, function);

        double expectedAlpha = 1.5707963267948966;
        double expectedValue = Math.sin(expectedAlpha);

        assertEquals(expectedAlpha, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testGetGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        GoalType expectedGoalType = GoalType.MINIMIZE;
        GoalType goalType = optimizer.getGoalType();

        assertEquals(expectedGoalType, goalType);
    }

    @Test
    public void testGetConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, new MyConvergenceChecker());
        ConvergenceChecker<PointValuePair> expectedConvergenceChecker = new MyConvergenceChecker();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();

        assertEquals(expectedConvergenceChecker, convergenceChecker);
    }

    @Test
    public void testGetRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double expectedRelativeThreshold = 0.01;
        double relativeThreshold = optimizer.getRelativeThreshold();

        assertEquals(expectedRelativeThreshold, relativeThreshold, 0.001);
    }

    @Test
    public void testGetAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double expectedAbsoluteThreshold = 0.001;
        double absoluteThreshold = optimizer.getAbsoluteThreshold();

        assertEquals(expectedAbsoluteThreshold, absoluteThreshold, 0.001);
    }

    @Test
    public void testGetLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        LineSearch expectedLineSearch = optimizer.getLineSearch();
        LineSearch lineSearch = optimizer.getLineSearch();

        assertEquals(expectedLineSearch, lineSearch);
    }

    @Test
    public void testGetLineSearchRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double expectedRelativeLineSearchThreshold = 0.01;
        double relativeLineSearchThreshold = optimizer.getLineSearch().getRelativeThreshold();

        assertEquals(expectedRelativeLineSearchThreshold, relativeLineSearchThreshold, 0.001);
    }

    @Test
    public void testGetLineSearchAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double expectedAbsoluteLineSearchThreshold = 0.001;
        double absoluteLineSearchThreshold = optimizer.getLineSearch().getAbsoluteThreshold();

        assertEquals(expectedAbsoluteLineSearchThreshold, absoluteLineSearchThreshold, 0.001);
    }

    /**
     * A custom convergence checker for testing purposes.
     */
    public static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return false;
        }
    }
}