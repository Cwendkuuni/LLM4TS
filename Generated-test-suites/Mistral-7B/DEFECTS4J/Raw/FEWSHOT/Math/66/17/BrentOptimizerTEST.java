package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        final BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultDoOptimize() {
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeWithMinimizeGoalType() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils.pow(x, 2);
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 0.0;
        final double expectedResult = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, MINIMIZE, min, max, startValue);
        assertNotNull(result);
        assertEquals(MINIMIZE, result.getGoalType(), 0);
        assertEquals(expectedResult, result.getPoint(), 5e-5);
    }

    @Test
    public void testOptimizeWithMinimizeGoalTypeAndFixedBoundaries() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils.pow(x, 2);
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 0.0;
        final double expectedResult = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, MINIMIZE, new SimpleBounds(min, max), null);
        assertNotNull(result);
        assertEquals(MINIMIZE, result.getGoalType(), 0);
        assertEquals(expectedResult, result.getPoint(), 5e-5);
    }

    @Test
    public void testOptimizeWithMaximizeGoalType() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils.pow(x, 2);
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 0.0;
        final double expectedResult = 1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, MAXIMIZE, min, max, startValue);
        assertNotNull(result);
        assertEquals(MAXIMIZE, result.getGoalType(), 0);
        assertEquals(expectedResult, result.getPoint(), 5e-5);
    }

    @Test
    public void testOptimizeWithMaximizeGoalTypeAndFixedBoundaries() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils.pow(x, 2);
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 0.0;
        final double expectedResult = 1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, MAXIMIZE, new SimpleBounds(min, max), null);
        assertNotNull(result);
        assertEquals(MAXIMIZE, result.getGoalType(), 0);
        assertEquals(expectedResult, result.getPoint(), 5e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeMaxIterationCount() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 0.0;
        final int maxIterationsCount = -1;

        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(maxIterationsCount);
        optimizer.optimize(f, MINIMIZE, min, max, startValue);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithZeroAbsoluteAccuracy() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 0.0;
        final double absoluteAccuracy = 0.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(absoluteAccuracy);
        optimizer.optimize(f, MINIMIZE, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test");
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 0.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, MINIMIZE, min, max, startValue);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceededException() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 0.0;
        final int maxIterationsCount = 1;

        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(maxIterationsCount);
        optimizer.optimize(f, MINIMIZE, min, max, startValue);
    }

    @Test
    public void testOptimizeWithCustomFunction() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 0.0;
        final double expectedResult = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, MINIMIZE, min, max, startValue);
        assertNotNull(result);
        assertEquals(MINIMIZE, result.getGoalType(), 0);
        assertEquals(expectedResult, result.getPoint(), 5e-5);
    }

    @Test
    public void testOptimizeWithCustomFunctionAndCustomBoundaries() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 0.0;
        final double expectedResult = -1.0;

        final BrentOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, MINIMIZE, new SimpleBounds(min, max), null);
        assertNotNull(result);
        assertEquals(MINIMIZE, result.getGoalType(), 0);
        assertEquals(expectedResult, result.getPoint(), 5e-5);
    }

    @Test
    public void testOptimizeWithMaximumEvaluations() throws Exception {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 0.0;
        final int maxEvaluations = 1;

        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.optimize(f, MINIMIZE, min, max, startValue);
    }

}