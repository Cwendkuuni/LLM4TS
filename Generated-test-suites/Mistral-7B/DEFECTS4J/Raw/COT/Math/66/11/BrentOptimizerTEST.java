package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;

import org.junit.Test;

/**
 * JUnit 4 test file for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        // Check default values
        assertEquals(Double.MAX_VALUE, optimizer.getMaxEvaluations(), 0.0);
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testOptimize_Minimize() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(0.0, min, 1E-10);
    }

    @Test
    public void testOptimize_Minimize_Bounded() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(testFunction, GoalType.MINIMIZE, -2.0, 2.0, 0.0);
        assertEquals(0.0, min, 1E-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_NegativeAccuracy() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1.0e-10);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_NegativeTolerance() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-1.0e-10);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_MaxIterationsExceeded() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x > 0) {
                    throw new Exception();
                }
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(testFunction, GoalType.MINIMIZE, -1.0, 1.0, 0.0);
    }

    @Test
    public void testOptimize_Maximize() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double max = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(0.0, max, 1E-10);
    }

    @Test
    public void testOptimize_Maximize_Bounded() throws Exception {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double max = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -2.0, 2.0, 0.0);
        assertEquals(0.0, max, 1E-10);
    }
}