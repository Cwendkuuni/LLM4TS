package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;

public class BrentOptimizerTest {

    @org.junit.Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeThrowsException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @org.junit.Test
    public void testOptimizeMinimum() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0, 0.0);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(-8.0, result.getValue(), 0.001);
    }

    @org.junit.Test
    public void testOptimizeMaximum() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) * E.E;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Math.log(E.E), result.getPoint(), 0.001);
        assertEquals(E.E, result.getValue(), 0.001);
    }

    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionMinimum() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMinimalIterationCount(-1);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionMaximalIterationCount() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(-1);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionAbsoluteAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(0.0);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionRelativeAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(0.0);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveExceptionMaxEvaluations() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(-1);
    }
}