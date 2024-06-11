package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        assertNull(optimizer.getStartPoint());
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(MIN_RELATIVE_TOLERANCE, optimizer.getRelativeThreshold(), 0);
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getAbsoluteThreshold(), 0);
        assertEquals(SimpleUnivariateValueChecker.class, optimizer.getLineSearch().getConvergenceChecker().getClass());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(1.0, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        new PowellOptimizer(0.5, 1.0, null);
    }

    @Test
    public void testConstructorWithRelAndAbs() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 2.0, null);
        assertArrayEquals(new double[0], optimizer.getStartPoint());
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(1.0, optimizer.getRelativeThreshold(), 0);
        assertEquals(2.0, optimizer.getAbsoluteThreshold(), 0);
    }

    @Test
    public void testConstructorWithRelAbsAndChecker() {
        final ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 2.0, convergenceChecker);
        assertArrayEquals(new double[0], optimizer.getStartPoint());
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
        assertEquals(1.0, optimizer.getRelativeThreshold(), 0);
        assertEquals(2.0, optimizer.getAbsoluteThreshold(), 0);
    }

    @Test
    public void testConstructorWithRelAbsLineRelAbsAndChecker() {
        final double lineRelativeThreshold = 0.1;
        final double lineAbsoluteThreshold = 1.0;
        final ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        PowellOptimizer optimizer = new PowellOptimizer(1.0, 2.0, lineRelativeThreshold, lineAbsoluteThreshold, convergenceChecker);
        assertArrayEquals(new double[0], optimizer.getStartPoint());
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
        assertEquals(1.0, optimizer.getRelativeThreshold(), 0);
        assertEquals(2.0, optimizer.getAbsoluteThreshold(), 0);
        assertEquals(lineRelativeThreshold, optimizer.getLineSearch().getRelativeThreshold(), 0);
        assertEquals(lineAbsoluteThreshold, optimizer.getLineSearch().getAbsoluteThreshold(), 0);
    }

    @Test
    public void testOptimize_defaultGoalType() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        final double[] startPoint = {1.0, 1.0};
        final PointValuePair result = optimizer.optimize(startPoint, new TestFunction());
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint());
    }

    @Test
    public void testOptimize_minimizeGoalType() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, GoalType.MINIMIZE);
        final double[] startPoint = {1.0, 1.0};
        final PointValuePair result = optimizer.optimize(startPoint, new TestFunction());
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint());
    }

    @Test
    public void testOptimize_maximizeGoalType() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, GoalType.MAXIMIZE);
        final double[] startPoint = {1.0, 1.0};
        final PointValuePair result = optimizer.optimize(startPoint, new TestFunction());
        assertEquals(0.0, -result.getValue(), Precision.EPSILON);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint());
    }

    @Test
    public void testOptimize_customConvergenceChecker() {
        final ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, convergenceChecker);
        final double[] startPoint = {1.0, 1.0};
        final PointValuePair result = optimizer.optimize(startPoint, new TestFunction());
        verify(convergenceChecker).converged(anyInt(), any(PointValuePair.class), any(PointValuePair.class));
    }

    @Test
    public void testOptimize_customConvergenceCheckerAndLineSearch() {
        final double lineRelativeThreshold = 0.1;
        final double lineAbsoluteThreshold = 1.0;
        final ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, lineRelativeThreshold, lineAbsoluteThreshold, convergenceChecker);
        final double[] startPoint = {1.0, 1.0};
        final PointValuePair result = optimizer.optimize(startPoint, new TestFunction());
        verify(convergenceChecker).converged(anyInt(), any(PointValuePair.class), any(PointValuePair.class));
        assertEquals(lineRelativeThreshold, optimizer.getLineSearch().getRelativeThreshold(), 0);
        assertEquals(lineAbsoluteThreshold, optimizer.getLineSearch().getAbsoluteThreshold(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStartPoint() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        optimizer.optimize(null, new TestFunction());
    }

    @Test
    public void testOptimize_functionThrowsException() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        final double[] startPoint = {1.0, 1.0};
        when(new TestFunction().value(anyDouble())).thenThrow(new RuntimeException());
        try {
            optimizer.optimize(startPoint, new TestFunction());
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testOptimize_functionReturnsNaN() {
        final PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        final double[] startPoint = {1.0, 1.0};
        when(new TestFunction().value(anyDouble())).thenReturn(Double.NaN);
        try {
            optimizer.optimize(startPoint, new TestFunction());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double alpha) {
            return Math.pow(alpha, 2) - 4 * alpha + 4;
        }
    }

}