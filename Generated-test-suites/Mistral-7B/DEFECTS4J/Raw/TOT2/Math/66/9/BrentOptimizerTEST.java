package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testMinimizeSimpleFunction() throws MaxIterationsExceededException, NonConvergentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double min = 0.0;
        double minValue = 0.0;
        double startValue = 1.0;
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, min, Double.POSITIVE_INFINITY, startValue);
        assertEquals(minValue, result.getPointValue(), 1E-9);
        assertEquals(min, result.getPoint(), 1E-9);
    }

    @Test
    public void testMaximizeSimpleFunction() throws MaxIterationsExceededException, NonConvergentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        double max = 0.0;
        double maxValue = 0.0;
        double startValue = -1.0;
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, startValue);
        assertEquals(maxValue, result.getPointValue(), 1E-9);
        assertEquals(max, result.getPoint(), 1E-9);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, NonConvergentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x);
            }
        };
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, NonConvergentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x > 0) {
                    throw new FunctionEvaluationException("Function evaluation error for positive x.");
                }
                return x * x;
            }
        };
        double min = 0.0;
        double minValue = 0.0;
        double startValue = -1.0;
        optimizer.optimize(f, GoalType.MINIMIZE, min, Double.POSITIVE_INFINITY, startValue);
    }

    @Test
    public void testNotStrictlyPositiveException() throws MaxIterationsExceededException, NonConvergentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(x);
            }
        };
        double min = 0.0;
        double minValue = Double.NEGATIVE_INFINITY;
        double startValue = 1.0;
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, min, Double.POSITIVE_INFINITY, startValue);
            fail("Expected NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}